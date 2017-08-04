package com.example.windows10.findmyphone;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Windows10 on 2017-08-05.
 */

//백그라운드에서 쓰레드를 이용해 계속해서 앱 상태를 점검해야 함(미구현).
public class MainPageFragment extends Fragment {
    @NonNull
    private View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.settingBtn:
                    //SettingActivity Open
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.main_page, container, false);
        appStatusCheck(rootView);

        Intent checkAppStatusThread=new Intent(getContext(), CheckAppStatusInBackground.class);
        getActivity().startService(checkAppStatusThread);

        return rootView;
    }

    private void appStatusCheck(ViewGroup rootView){
        ImageView appStatusIcon=(ImageView)rootView.findViewById(R.id.appStatusIcon);
        TextView appStatus=(TextView)rootView.findViewById(R.id.appStatus);
        if(true){
            //When App status is fine
            appStatusIcon.setImageResource(R.drawable.shield);
            appStatus.setText("앱이 정상 작동 중입니다");
        }else{
            //When App can't be running (because something is wrong)
            appStatusIcon.setImageResource(R.drawable.cracked_shield);
            appStatus.setText("앱이 정상적으로 작동하지 않고 있습니다");
        }
    }
}
