package com.example.windows10.findmyphone;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by WINDOWS7 on 2017-08-25.
 */

public class PhoneLockActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_lock_activity);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PhoneLockStatus.setStatus(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        PhoneLockStatus.setStatus(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PhoneLockStatus.setStatus(false);
    }
}
