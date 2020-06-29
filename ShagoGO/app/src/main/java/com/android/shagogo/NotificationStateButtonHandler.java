package com.android.shagogo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationStateButtonHandler extends BroadcastReceiver {

    private Intent mPedometerService;

    @Override
    public void onReceive(Context context, Intent intent) {
        // Изменить состояние
        if (PedometerService.isListenAccelerometer() == false) {
            SendIntentToService(context, PedometerService.ACCELEROMETER_ON);
        } else {
            SendIntentToService(context, PedometerService.ACCELEROMETER_OFF);
        }
    }

    // Создать новый intent and отправить его  service для передачи в эксплуатацию нового значения
    // слушатель акселерометра (включен или выключен)
    private void SendIntentToService(Context context, int lister_state) {
        mPedometerService = new Intent(context, PedometerService.class);
        mPedometerService.putExtra(PedometerService.ACCELEROMETER_KEY, lister_state);
        context.startService(mPedometerService);
    }
}
