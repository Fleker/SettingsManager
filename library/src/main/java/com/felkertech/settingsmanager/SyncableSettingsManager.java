package com.felkertech.settingsmanager;

import android.app.Activity;
import android.content.Context;

/**
 * The SyncableSettingsManager is a standard abstract class that can be implemented by both
 * the Wear and Wearable modules in order to have a similar syncing interface while having
 * slightly different implementations.
 *
 * Created by Nick on 4/22/2016.
 */
public abstract class SyncableSettingsManager extends SettingsManager {
    private boolean syncEnabled = false;

    public SyncableSettingsManager(Activity activity) {
        super(activity);
    }

    public SyncableSettingsManager(Context context) {
        super(context);
    }

    public abstract void pushData();
    public void enableSync() {
        syncEnabled = true;
    }
    public void disableSync() {
        syncEnabled = false;
    }
    public boolean isSyncEnabled() {
        return syncEnabled;
    }

    public String setString(int resId, String val) {
        String out = super.setString(resId, val);
        pushData();
        return out;
    }
    public String setString(String key, String val) {
        String out = super.setString(key, val);
        pushData();
        return out;
    }
    public boolean setBoolean(int resId, boolean val) {
        boolean out = super.setBoolean(mContext.getString(resId), val);
        pushData();
        return out;
    }
    public boolean setBoolean(String key, boolean val) {
        boolean out = super.setBoolean(key, val);
        pushData();
        return out;
    }
    public int setInt(int resId, int val) {
        int out = super.setInt(mContext.getString(resId), val);
        pushData();
        return out;
    }
    public int setInt(String key, int val) {
        int out = super.setInt(key, val);
        pushData();
        return out;
    }
    public long setLong(int resId, long val) {
        long out = super.setLong(mContext.getString(resId), val);
        pushData();
        return out;
    }
    public long setLong(String key, long val) {
        long out = super.setLong(key, val);
        pushData();
        return out;
    }
}
