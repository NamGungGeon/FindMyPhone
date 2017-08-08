package com.example.windows10.findmyphone;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;

/**
 * Created by Windows10 on 2017-08-05.
 */

public class CheckAppStatusInBackground extends Service implements Runnable{

    private boolean isRunningCheck=true;
    private Thread thisThread=null;

    private Serializable statusInfo=null;
    private ImageView appStatusIcon=null;
    private TextView appStatusMessage=null;

    private final int NOT_ERROR=1000;
    private final int NOT_LOCK_ERROR=1001;
    private final int NOT_DOING_GPS_ERROR=1002;
    private final int NOT_GET_PERMISSION=1003;

    private Settings settings=Settings.getInstance(getApplicationContext());

    @Override
    public void onCreate() {
        super.onCreate();

        thisThread=new Thread(this);
        thisThread.start();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        statusInfo=intent.getSerializableExtra("Status");
        appStatusIcon=((MainPageFragment)statusInfo).appStatusIcon;
        appStatusMessage=((MainPageFragment)statusInfo).appStatus;
        return null;
    }

    //문제가 발생하면 알림창도 띄워줄 것(미구현)
    @Override
    public void run() {
        while(isRunningCheck){
            Log.i("", "Thread");
            try {
                thisThread.sleep(5000L);
            } catch (InterruptedException e) {
                Log.i("Check App Status", "InterruptedException is occur at this Thread.");
            }

            //Check App is locked
            if(settings.getIsLockThisApp()==false){
                appStatusReport(true, NOT_LOCK_ERROR);
                continue;
            }

            //Check GPS is doing

            //Check Permission
            if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECEIVE_SMS)!=PackageManager.PERMISSION_GRANTED){
                //Any Permission is not granted
                appStatusReport(true, NOT_GET_PERMISSION);
                continue;
            }

            appStatusReport(false, NOT_ERROR);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        isRunningCheck=false;
    }

    private void appStatusReport(boolean detectedError, int errorCode){
        String message="";

        if(detectedError){
            switch(errorCode){
                case NOT_DOING_GPS_ERROR:
                    message="GPS가 꺼져 있습니다.";
                    break;
                case NOT_LOCK_ERROR:
                    message="앱 잠금이 설정되어 있지 않습니다.";
                    break;
                case NOT_GET_PERMISSION:
                    message="일부 권한이 해제되었습니다. 앱을 재시작하세요.";
                    break;
            }
        }else{
            message="앱이 정상 작동 중입니다";
        }
        appStatusMessage.setText(message);
        //appStatusIcon.setBackground();
    }

}
