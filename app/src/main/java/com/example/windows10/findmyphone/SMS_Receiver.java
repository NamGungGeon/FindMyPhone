package com.example.windows10.findmyphone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Windows10 on 2017-08-07.
 */

public class SMS_Receiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            Toast.makeText(context, "문자 수신", Toast.LENGTH_LONG).show();
        }
    }
}
