package com.example.windows10.findmyphone;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Windows10 on 2017-08-17.
 */

public class AppLockerSettingFragment extends Fragment {

    private final int SUCCESS=2033;
    private final int FAIL=2034;
    private int result=FAIL;

    private String inputPIN="";
    private String oneMoreCheck="";

    final private TextView keyStatus[]=new TextView[4];
    private ImageButton keyNumberBtn[]=new ImageButton[9];
    private ImageButton zeroKeyNumberBtn=null;
    private ImageButton deleteKeyBtn=null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.app_lock_setting_page, container, false);
        init(rootView);
        initListener();
        getActivity().setResult(result);

        return rootView;
    }

    private void init(ViewGroup rootView){
        keyStatus[0]=(TextView)rootView.findViewById(R.id.firstPINS);
        keyStatus[1]=(TextView)rootView.findViewById(R.id.secondPINS);
        keyStatus[2]=(TextView)rootView.findViewById(R.id.thirdPINS);
        keyStatus[3]=(TextView)rootView.findViewById(R.id.forthPINS);

        keyNumberBtn[0]=(ImageButton)rootView.findViewById(R.id.oneS);
        keyNumberBtn[1]=(ImageButton)rootView.findViewById(R.id.twoS);
        keyNumberBtn[2]=(ImageButton)rootView.findViewById(R.id.threeS);
        keyNumberBtn[3]=(ImageButton)rootView.findViewById(R.id.fourS);
        keyNumberBtn[4]=(ImageButton)rootView.findViewById(R.id.fiveS);
        keyNumberBtn[5]=(ImageButton)rootView.findViewById(R.id.sixS);
        keyNumberBtn[6]=(ImageButton)rootView.findViewById(R.id.sevenS);
        keyNumberBtn[7]=(ImageButton)rootView.findViewById(R.id.eightS);
        keyNumberBtn[8]=(ImageButton)rootView.findViewById(R.id.nineS);

        zeroKeyNumberBtn=(ImageButton)rootView.findViewById(R.id.zeroS);
        deleteKeyBtn=(ImageButton)rootView.findViewById(R.id.deleteS);
    }


    private void initListener(){
        zeroKeyNumberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputPIN=inputPIN.concat(String.valueOf(0));
                checkRightKey();
            }
        });

        for(int i=0; i<9; i++){
            final int PIN=i;
            keyNumberBtn[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    inputPIN=inputPIN.concat(String.valueOf(PIN+1));
                    checkRightKey();
                }
            });
        }
        deleteKeyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(inputPIN.length()>=1){
                    keyStatus[inputPIN.length()-1].setText("_");
                    if(inputPIN.length()==1){
                        inputPIN="";
                    }else{
                        inputPIN=inputPIN.substring(0, inputPIN.length()-1);
                    }
                }
            }
        });
    }


    private void checkRightKey() {
        if(inputPIN.length()==4){

            for(int i=0; i<4; i++){
                keyStatus[i].setText("_");
            }

            checkKeyOneMore();
            Toast.makeText(getActivity().getApplicationContext(), "비밀번호 확인을 위해 다시 한번 입력해 주세요.", Toast.LENGTH_SHORT).show();

        }
        for(int i=0; i<inputPIN.length(); i++){
            keyStatus[i].setText("*");
        }
    }

    private void checkKeyOneMore(){
        zeroKeyNumberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oneMoreCheck=oneMoreCheck.concat(String.valueOf(0));
                recheck();
            }
        });

        for(int i=0; i<9; i++){
            final int PIN=i;
            keyNumberBtn[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    oneMoreCheck=oneMoreCheck.concat(String.valueOf(PIN+1));
                    recheck();
                }
            });
        }

        deleteKeyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(oneMoreCheck.length()-1>=0){
                    keyStatus[oneMoreCheck.length()-1].setText("_");
                    if(oneMoreCheck.length()==1){
                        oneMoreCheck="";
                    }else{
                        oneMoreCheck=oneMoreCheck.substring(0, inputPIN.length()-1);
                    }
                }
            }
        });
    }

    private void recheck(){
        if(oneMoreCheck.length()==4){
            if(oneMoreCheck.equals(inputPIN)){
                Settings.getInstance().setIsLockThisApp(true);
                Settings.getInstance().setAppPassWord(oneMoreCheck);
                Toast.makeText(getActivity().getApplicationContext(), "비밀번호 설정이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                result=SUCCESS;
                getActivity().finish();
            }else{
                initListener();
                oneMoreCheck="";
                inputPIN="";
                Toast.makeText(getActivity().getApplicationContext(), "이전에 입력했던 값과 다릅니다. 비밀번호 설정을 다시 시작합니다.", Toast.LENGTH_SHORT).show();

                for(int i=0; i<4; i++){
                    keyStatus[i].setText("_");
                }
            }


        }
        for(int i=0; i<oneMoreCheck.length(); i++){
            keyStatus[i].setText("*");
        }
    }
}
