package com.example.windows10.findmyphone;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by WINDOWS7 on 2017-08-19.
 */

public class AppLockerManageActivity extends AppCompatActivity{
    private Button changePinBtn=null;
    private Button deletePinBtn=null;
    private View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.changePIN:
                    Toast.makeText(getApplicationContext(), "비밀번호를 재설정합니다.", Toast.LENGTH_SHORT).show();
                    getSupportFragmentManager().beginTransaction().replace(R.id.appLockManageWrapper, new AppLockerSettingFragment()).commit();
                    break;
                case R.id.deletePIN:
                    final DialogMaker deleteDialog=new DialogMaker();
                    deleteDialog.setValue("앱 잠금을 해제합니다.", "예", "아니오",
                            new DialogMaker.Callback() {
                                @Override
                                public void callbackMethod() {
                                    Settings.getInstance().setAppPassWord("");
                                    Settings.getInstance().setIsLockThisApp(false);
                                    deleteDialog.dismiss();
                                    finish();
                                }
                            }, new DialogMaker.Callback() {
                                @Override
                                public void callbackMethod() {
                                    deleteDialog.dismiss();
                                }
                            });
                    deleteDialog.show(getSupportFragmentManager(), "");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_lock_manage_activity);

        //Hide Label Bar
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().hide();

        changePinBtn=(Button)findViewById(R.id.changePIN);
        changePinBtn.setOnClickListener(onClickListener);

        deletePinBtn=(Button)findViewById(R.id.deletePIN);
        deletePinBtn.setOnClickListener(onClickListener);
    }
}
