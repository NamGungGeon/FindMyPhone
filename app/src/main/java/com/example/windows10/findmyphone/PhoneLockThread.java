package com.example.windows10.findmyphone;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by WINDOWS7 on 2017-08-25.
 */

public class PhoneLockThread implements Runnable{
    private Context context=null;
    private Intent locker=null;


    private PhoneLockThread(Context context){
        this.context=context;
        locker=new Intent(context, PhoneLockActivity.class);
    }

    private static PhoneLockThread inst=null;
    public static PhoneLockThread getInstance(Context context){
        if(inst==null){
            inst=new PhoneLockThread(context);
        }
        return inst;
    }
    @Override
    public void run() {
        while(true){
            if(PhoneLockStatus.getStatus()==false){
                context.startActivity(locker);
            }
            try {
                wait(700L);
            } catch (InterruptedException e) {
                Log.i("PhoneLockThread", e.toString());
            }
        }
    }
}
