package com.android.shagogo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Date;

public class EveryHourHandler extends BroadcastReceiver {

    private static int NOTIFY_ID = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        PedometerSharedPreferences.WriteCurrentStepsAndTime(context);
    }
}
