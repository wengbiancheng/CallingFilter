package com.flamingo.filterdemo.tool;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * 来电广播
 */
public class BroadcastTrigger extends BroadcastReceiver {

    private TelephonyManager phoneManger;

    @Override
    public void onReceive(Context context, Intent intent) {
        phoneManger = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
        switch (phoneManger.getCallState()) {
            case TelephonyManager.CALL_STATE_RINGING:
                String phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                Log.i("PhoneService", "监测到来电");
                Intent intent1 = new Intent(context, ServiceMain.class);
                intent1.putExtra("phone", phoneNumber);
                context.startService(intent1);
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                break;
            case TelephonyManager.CALL_STATE_IDLE:
                break;
        }
    }
}