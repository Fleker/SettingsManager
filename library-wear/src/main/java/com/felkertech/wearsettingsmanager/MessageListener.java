package com.felkertech.wearsettingsmanager;

import com.felkertech.settingsmanager.SettingsManager;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.WearableListenerService;

/**
 * Created by guest1 on 1/5/2016.
 */
public class MessageListener extends WearableListenerService {
    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        WearSettingsManager sm = new WearSettingsManager(getApplicationContext());
        sm.pullData(dataEvents);
    }
}
