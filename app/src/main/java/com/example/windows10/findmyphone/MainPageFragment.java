package com.example.windows10.findmyphone;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.io.Serializable;

/**
 * Created by Windows10 on 2017-08-05.
 */

//백그라운드에서 쓰레드를 이용해 계속해서 앱 상태를 점검해야 함(미구현).
public class MainPageFragment extends Fragment implements Serializable{
    public ImageView appStatusIcon;
    public TextView appStatus;
    public TextView userName;

    private Button settingBtn=null;
    private Button helpBtn=null;

    private Settings settings=Settings.getInstance();
    private final int OPEN_SETTING_ACTIVITY=10003;

    @NonNull
    private View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.settingBtn:
                    Intent intent=new Intent(getActivity().getApplicationContext(), SettingActivity.class);
                    startActivityForResult(intent, OPEN_SETTING_ACTIVITY);
                    break;
                case R.id.helpBtn:
                    Toast.makeText(getActivity().getApplicationContext(), "Test Code", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case OPEN_SETTING_ACTIVITY:
                appStatusCheck();
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.main_page, container, false);

        init(rootView);
        appStatusCheck();

        //Intent checkAppStatusThread=new Intent(getActivity().getApplicationContext(), CheckAppStatusInBackground.class);
        //checkAppStatusThread.putExtras((new Intent()).putExtra("Status", this));
        //getActivity().startService(checkAppStatusThread);

        return rootView;
    }

    private void init(ViewGroup rootView){
        appStatusIcon=(ImageView)rootView.findViewById(R.id.appStatusIcon);
        appStatus=(TextView)rootView.findViewById(R.id.appStatus);
        userName=(TextView)rootView.findViewById(R.id.userName);
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            userName.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName()+"님 안녕하세요");
        }

        settingBtn=(Button)rootView.findViewById(R.id.settingBtn);
        settingBtn.setOnClickListener(onClickListener);
        helpBtn=(Button)rootView.findViewById(R.id.helpBtn);
        helpBtn.setOnClickListener(onClickListener);

    }


    private void appStatusCheck(){
        boolean isFine=true;
        String message="";

        if(!settings.getIsLockThisApp()){
            isFine=false;
            message=message.concat("앱 잠금이 설정되어 있지 않습니다.\n");
        }
        if(settings.getKeyValue().equals(settings.keyNotExist)){
            isFine=false;
            message=message.concat("키 값이 설정되지 않았습니다.\n");
        }
        if(settings.getReceiveStatus()==false){
            isFine=false;
            message=message.concat("앱 알림이 꺼져 있습니다.\n");
        }
        if(getGpsStatus()==false){
            isFine=false;
            message=message.concat("GPS기능이 꺼져 있습니다.\n");
        }

        if(isFine){
            //When App status is fine
            appStatusIcon.setImageResource(R.drawable.main_icon);
            appStatus.setText("앱이 정상 작동 중입니다");
        }else{
            //When App can't be running (because something is wrong)
            appStatusIcon.setImageResource(R.drawable.blocking);
            appStatus.setText(message);
        }
    }

    private boolean getGpsStatus(){
        String provider = android.provider.Settings.Secure
                .getString(getActivity().getContentResolver(), android.provider.Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        return provider.contains("gps");
    }
}
