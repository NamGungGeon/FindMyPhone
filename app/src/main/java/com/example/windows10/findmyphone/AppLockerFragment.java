package com.example.windows10.findmyphone;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by WINDOWS7 on 2017-08-06.
 */

public class AppLockerFragment extends Fragment {

    private Settings settings=Settings.getInstance(getActivity().getApplicationContext());

    private String inputPIN="";

    final private TextView pinStatus[]=new TextView[4];
    private ImageButton pinNumberBtn[]=new ImageButton[9];
    private ImageButton zeroPinNumberBtn=null;

    private int PIN_errorStack=0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.app_lock_page, container, false);

        pinStatus[0]=(TextView)rootView.findViewById(R.id.firstPIN);
        pinStatus[1]=(TextView)rootView.findViewById(R.id.secondPIN);
        pinStatus[2]=(TextView)rootView.findViewById(R.id.thirdPIN);
        pinStatus[3]=(TextView)rootView.findViewById(R.id.forthPIN);

        pinNumberBtn[0]=(ImageButton)rootView.findViewById(R.id.one);
        pinNumberBtn[1]=(ImageButton)rootView.findViewById(R.id.two);
        pinNumberBtn[2]=(ImageButton)rootView.findViewById(R.id.three);
        pinNumberBtn[3]=(ImageButton)rootView.findViewById(R.id.four);
        pinNumberBtn[4]=(ImageButton)rootView.findViewById(R.id.five);
        pinNumberBtn[5]=(ImageButton)rootView.findViewById(R.id.six);
        pinNumberBtn[6]=(ImageButton)rootView.findViewById(R.id.seven);
        pinNumberBtn[7]=(ImageButton)rootView.findViewById(R.id.eight);
        pinNumberBtn[8]=(ImageButton)rootView.findViewById(R.id.nine);

        zeroPinNumberBtn=(ImageButton)rootView.findViewById(R.id.zero);
        zeroPinNumberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputPIN=inputPIN.concat(String.valueOf(0));
                checkRightPIN();
            }
        });

        for(int i=0; i<9; i++){
            final int PIN=i;
            pinNumberBtn[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    inputPIN=inputPIN.concat(String.valueOf(PIN+1));
                    checkRightPIN();
                }
            });
        }
        return rootView;
    }

    public void checkRightPIN() {
        if(inputPIN.equals(settings.getAppPassWord())){
            FragmentTransaction transaction=getActivity().getSupportFragmentManager().beginTransaction();
            if(settings.getIsFirst()){
                transaction.replace(R.id.mainContainer, new FirstStartFragment());
            }else{
                transaction.replace(R.id.mainContainer, new MainPageFragment());
            }
            transaction.commit();
        }else if(inputPIN.length()==settings.getAppPassWord().length()){
            Toast.makeText(getActivity().getApplicationContext(), "PIN번호가 틀립니다", Toast.LENGTH_SHORT).show();
            inputPIN="";
            for(int i=0; i<4; i++){
                pinStatus[i].setText("_");
            }

            PIN_errorStack++;
            if(PIN_errorStack==3){
                Toast.makeText(getActivity().getApplicationContext(), "3회 이상 틀렸습니다. 앱을 종료합니다.", Toast.LENGTH_LONG).show();
                getActivity().finish();
            }
        }else{
            pinStatus[inputPIN.length()-1].setText("*");
        }
    }
}
