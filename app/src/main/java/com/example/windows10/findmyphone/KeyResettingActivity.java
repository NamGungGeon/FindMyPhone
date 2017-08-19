package com.example.windows10.findmyphone;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by WINDOWS7 on 2017-08-20.
 */

public class KeyResettingActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.key_resetting_activity);

        //Hide Label Bar
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().hide();

        getSupportFragmentManager().beginTransaction().replace(R.id.keyResettingWrapper, new KeyResettingFragment()).commit();
    }
}
