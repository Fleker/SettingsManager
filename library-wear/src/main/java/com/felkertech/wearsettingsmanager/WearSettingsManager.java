package com.felkertech.wearsettingsmanager;

/**
 * Created by guest1 on 1/2/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.felkertech.settingsmanager.SettingsManager;
import com.felkertech.settingsmanager.SyncableSettingsManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Set;

/**
 * Version 1.1
 * Created by N on 14/9/2014.
 * Last Edited 13/5/2015
 *   * Support for syncing data to wearables
 */
public class WearSettingsManager extends SyncableSettingsManager {
    public WearSettingsManager(Activity activity) {
        super(activity);
    }
    public WearSettingsManager(Context context) {
        super(context);
    }

    /* SYNCABLE SETTINGS MANAGER */
    private GoogleApiClient syncClient;

    public boolean setSyncableSettingsManager(GoogleApiClient gapi) {
        syncClient = gapi;
        if(gapi != null && gapi.isConnected()) {
            ConnectionResult wearable = gapi.getConnectionResult(Wearable.API);
            if (wearable.isSuccess()) {
                //Sync enabled
                enableSync();
                return true;
            } else {
                Log.e(TAG, "Wear API is disabled");
            }
        }
        return false;
    }

    /**
     * Pushes all settings to other devices
     */
    public void pushData() {
        if(syncClient == null || !syncClient.isConnected()) {
            Log.e(TAG, "Can't sync");
            throw new NullPointerException("GoogleApiClient is null or not connected");
        }
        Iterator<String> keys = sharedPreferences.getAll().keySet().iterator();
        PutDataMapRequest dataMap = PutDataMapRequest.create("/prefs");
        while(keys.hasNext()) {
            String key = keys.next();
            Object v = sharedPreferences.getAll().get(key);
            Log.d(TAG, "Push "+key+" = "+v.getClass().getSimpleName()+", "+v);
            if(v.getClass().toString().contains("Boolean")) {
                dataMap.getDataMap().putBoolean(key, (Boolean) v);
                //Log.d(TAG, "Sending boolean "+key+" = "+v);
            } else if(v.getClass().toString().contains("String")) {
                dataMap.getDataMap().putString(key, (String) v);
                //Log.d(TAG, "Sending string "+key+" = "+v);
            } else if (v.getClass().toString().contains("Integer")) {
                dataMap.getDataMap().putInt(key, (int) v);
                //Log.d(TAG, "Sending int "+key+" = "+v);
            } else if (v.getClass().toString().contains("Long")) {
                dataMap.getDataMap().putLong(key, (long) v);
                // Log.d(TAG, "Sending long "+key+" = "+v);
            }
        }
        PutDataRequest request = dataMap.asPutDataRequest();
        dataMap.setUrgent();
        //Log.d(TAG, "Pending intent");
        PendingResult<DataApi.DataItemResult> pendingResult =
                Wearable.DataApi.putDataItem(syncClient, request);
    }

    /**
     * Retrieves any changes in settings and applies them to the correct key
     * @param dataEvents DataEventBuffer from the service callback
     */
    public void pullData(DataEventBuffer dataEvents) {
        for (DataEvent event : dataEvents) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                Log.d(TAG, "DataItem changed: " + event.getDataItem().getUri());
                DataMap dataMap = DataMap.fromByteArray(event.getDataItem().getData());
                for (String key : dataMap.keySet()) {
                    Object v = dataMap.get(key);
                    if (v.getClass().toString().contains("Boolean")) {
                        setLocalBoolean(key, (Boolean) v);
                    } else if (v.getClass().toString().contains("String")) {
                        setLocalString(key, (String) v);
                    } else if (v.getClass().toString().contains("Integer")) {
                        setLocalInt(key, (int) v);
                    } else if (v.getClass().toString().contains("Long")) {
                        setLocalLong(key, (long) v);
                    }
                }
            }
        }
    }
}

