package com.example.windows10.findmyphone;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Windows10 on 2017-08-05.
 */

public class CheckAppStatusInBackground extends Service implements Runnable{
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void run() {

    }
}
