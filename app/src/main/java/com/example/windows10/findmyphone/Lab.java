package com.example.windows10.findmyphone;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by WINDOWS7 on 2017-08-25.
 */

public class Lab extends AppCompatActivity {
    private Button leftBtn=null;
    private Button rightBtn=null;

    private Intent gpsTraceService=null;

    private View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.labBtn1:
                    startGpsTrace(getApplicationContext());
                    break;
                case R.id.labBtn2:
                    stopGpsTrace(getApplicationContext());
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lab_activity);

        //Hide Action Bar
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().hide();

        leftBtn=(Button)findViewById(R.id.labBtn1);
        leftBtn.setOnClickListener(onClickListener);
        rightBtn=(Button)findViewById(R.id.labBtn2);
        rightBtn.setOnClickListener(onClickListener);
    }

    private boolean startGpsTrace(Context appContext){
        if(gpsTraceService==null){
            gpsTraceService=new Intent(appContext, GpsTracerInBackground.class);
            appContext.startService(gpsTraceService);
            return true;
        }else{
            return false;
        }
    }
    private boolean stopGpsTrace(Context appContext){
        if(gpsTraceService!=null){
            appContext.stopService(gpsTraceService);
            gpsTraceService=null;
            return true;
        }else{
            return false;
        }
    }

}
