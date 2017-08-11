package com.example.windows10.findmyphone;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by WINDOWS7 on 2017-08-09.
 */

public class MainSettingFragment extends Fragment{
    private final int GPS_SETTING_ACTIVITY_CODE=1009;

    private Button appLockStatusBtn=null;
    private Button receiveStatusBtn=null;
    private Button gpsStatusBtn=null;
    private Button keyChangeBtn=null;
    private Button inputCodeBtn=null;
    private Button devInfoBtn=null;

    private Settings settings=Settings.getInstance();

    private View.OnClickListener onClickListener=new View.OnClickListener(){

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.appLock:
                    if(settings.getIsLockThisApp()){
                        //Make app unlocked
                    }else{
                        //Make app locked\
                    }
                    break;
                case R.id.receiveInfo:
                    if(settings.getReceiveStatus()){
                        settings.setReceiveStatus(false);
                        receiveStatusBtn.setBackground(getResources().getDrawable(R.drawable.receive_no));
                    }else{
                        settings.setReceiveStatus(true);
                        receiveStatusBtn.setBackground(getResources().getDrawable(R.drawable.receive_ok));
                    }
                    break;
                case R.id.statusGPS:
                    startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), GPS_SETTING_ACTIVITY_CODE);
                    break;
                case R.id.changeKey:
                    Toast.makeText(getActivity().getApplicationContext(), "미구현 기능입니다.", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.inputCode:
                    Toast.makeText(getActivity().getApplicationContext(), "미구현 기능입니다.", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.devInfo:
                    Toast.makeText(getActivity().getApplicationContext(), "미구현 기능입니다.", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.setting_page, container, false);

        init(rootView);
        updateStatus();

        return rootView;
    }

    private void init(ViewGroup rootView){
        appLockStatusBtn=(Button)rootView.findViewById(R.id.appLock);
        appLockStatusBtn.setOnClickListener(onClickListener);
        receiveStatusBtn=(Button)rootView.findViewById(R.id.receiveInfo);
        receiveStatusBtn.setOnClickListener(onClickListener);
        gpsStatusBtn=(Button)rootView.findViewById(R.id.statusGPS);
        gpsStatusBtn.setOnClickListener(onClickListener);
        keyChangeBtn=(Button)rootView.findViewById(R.id.changeKey);
        keyChangeBtn.setOnClickListener(onClickListener);
        inputCodeBtn=(Button)rootView.findViewById(R.id.inputCode);
        inputCodeBtn.setOnClickListener(onClickListener);
        devInfoBtn=(Button)rootView.findViewById(R.id.devInfo);
        devInfoBtn.setOnClickListener(onClickListener);
    }
    private void updateStatus(){
        if(settings.getIsLockThisApp()){
            appLockStatusBtn.setBackground(getResources().getDrawable(R.drawable.lock));
        }else{
            appLockStatusBtn.setBackground(getResources().getDrawable(R.drawable.unlock));
        }

        if(settings.getReceiveStatus()){
            receiveStatusBtn.setBackground(getResources().getDrawable(R.drawable.receive_ok));
        }else{
            receiveStatusBtn.setBackground(getResources().getDrawable(R.drawable.receive_no));
        }

        if(getGpsStatus()){
            gpsStatusBtn.setBackground(getResources().getDrawable(R.drawable.doing_gps));
        }else{
            gpsStatusBtn.setBackground(getResources().getDrawable(R.drawable.undoing_gps));
        }
    }

    private boolean getGpsStatus(){
        String provider = android.provider.Settings.Secure
                .getString(getActivity().getContentResolver(), android.provider.Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        return provider.contains("gps");
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case GPS_SETTING_ACTIVITY_CODE:
                updateStatus();
                break;
        }
    }
}
