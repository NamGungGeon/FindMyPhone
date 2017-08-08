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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;

/**
 * Created by Windows10 on 2017-08-05.
 */

//백그라운드에서 쓰레드를 이용해 계속해서 앱 상태를 점검해야 함(미구현).
public class MainPageFragment extends Fragment implements Serializable{
    public ImageView appStatusIcon;
    public TextView appStatus;

    private Button settingBtn=null;
    private Button helpBtn=null;

    @NonNull
    private View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.settingBtn:
                    Intent intent=new Intent(getActivity().getApplicationContext(), SettingActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.main_page, container, false);

        appStatusIcon=(ImageView)rootView.findViewById(R.id.appStatusIcon);
        appStatus=(TextView)rootView.findViewById(R.id.appStatus);

        settingBtn=(Button)rootView.findViewById(R.id.settingBtn);
        settingBtn.setOnClickListener(onClickListener);
        helpBtn=(Button)rootView.findViewById(R.id.helpBtn);
        helpBtn.setOnClickListener(onClickListener);

        appStatusCheck(rootView);

        Intent checkAppStatusThread=new Intent(getActivity().getApplicationContext(), CheckAppStatusInBackground.class);
        //checkAppStatusThread.putExtras((new Intent()).putExtra("Status", this));
        //getActivity().startService(checkAppStatusThread);

        return rootView;
    }

    private void appStatusCheck(ViewGroup rootView){
        if(true){
            //When App status is fine
            appStatusIcon.setImageResource(R.drawable.main_icon);
            appStatus.setText("앱이 정상 작동 중입니다...");
        }else{
            //When App can't be running (because something is wrong)
            appStatusIcon.setImageResource(R.drawable.blocking);
            appStatus.setText("앱이 정상적으로 작동하지 않고 있습니다");
        }
    }

}
