package com.example.windows10.findmyphone;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by WINDOWS7 on 2017-08-19.
 */

public class AppLockerSettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_lock_setting_wrapper);

        //Hide Label Bar
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().hide();

        getSupportFragmentManager().beginTransaction().replace(R.id.appLockerSettingWrapper, new AppLockerSettingFragment()).commit();
    }
}
