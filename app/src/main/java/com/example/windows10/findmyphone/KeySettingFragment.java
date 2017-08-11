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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by WINDOWS7 on 2017-08-11.
 */

public class KeySettingFragment extends Fragment {

    private String inputKey="";
    private String oneMoreCheck="";

    final private TextView keyStatus[]=new TextView[6];
    private ImageButton keyNumberBtn[]=new ImageButton[9];
    private ImageButton zeroKeyNumberBtn=null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.key_setting_page, container, false);

        keyStatus[0]=(TextView)rootView.findViewById(R.id.firstKey);
        keyStatus[1]=(TextView)rootView.findViewById(R.id.secondKey);
        keyStatus[2]=(TextView)rootView.findViewById(R.id.thirdKey);
        keyStatus[3]=(TextView)rootView.findViewById(R.id.forthKey);
        keyStatus[4]=(TextView)rootView.findViewById(R.id.fifthKey);
        keyStatus[5]=(TextView)rootView.findViewById(R.id.sixthKey);

        keyNumberBtn[0]=(ImageButton)rootView.findViewById(R.id.oneK);
        keyNumberBtn[1]=(ImageButton)rootView.findViewById(R.id.twoK);
        keyNumberBtn[2]=(ImageButton)rootView.findViewById(R.id.threeK);
        keyNumberBtn[3]=(ImageButton)rootView.findViewById(R.id.fourK);
        keyNumberBtn[4]=(ImageButton)rootView.findViewById(R.id.fiveK);
        keyNumberBtn[5]=(ImageButton)rootView.findViewById(R.id.sixK);
        keyNumberBtn[6]=(ImageButton)rootView.findViewById(R.id.sevenK);
        keyNumberBtn[7]=(ImageButton)rootView.findViewById(R.id.eightK);
        keyNumberBtn[8]=(ImageButton)rootView.findViewById(R.id.nineK);

        zeroKeyNumberBtn=(ImageButton)rootView.findViewById(R.id.zeroK);
        initListener();

        return rootView;
    }

    private void initListener(){
        zeroKeyNumberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputKey=inputKey.concat(String.valueOf(0));
                checkRightKey();
            }
        });

        for(int i=0; i<9; i++){
            final int PIN=i;
            keyNumberBtn[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    inputKey=inputKey.concat(String.valueOf(PIN+1));
                    checkRightKey();
                }
            });
        }
    }


    private void checkRightKey() {
        if(inputKey.length()==6){
            //check whether inserted key is available
            boolean isAvailableKey=true;
            if(inputKey.equals("000000")){
                isAvailableKey=false;
            }else if(inputKey.equals("123456")){
                isAvailableKey=false;
            }

            for(int i=0; i<6; i++){
                keyStatus[i].setText("_");
            }

            if(isAvailableKey){
                checkKeyOneMore();
            }else{
                Toast.makeText(getActivity().getApplicationContext(), "사용할 수 없는 키 값입니다. 다른 값으로 시도하세요.", Toast.LENGTH_SHORT).show();
            }

        }else{
            keyStatus[inputKey.length()-1].setText("*");
        }
    }

    private void checkKeyOneMore(){

        zeroKeyNumberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oneMoreCheck=oneMoreCheck.concat(String.valueOf(0));
                check();
            }
        });

        for(int i=0; i<9; i++){
            final int PIN=i;
            keyNumberBtn[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    inputKey=inputKey.concat(String.valueOf(PIN+1));
                    check();
                }
            });
        }
    }

    private void check(){

        if(oneMoreCheck.length()==6){
            if(oneMoreCheck.equals(inputKey)){
                //Sync FireBase

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("message");
                myRef.setValue("Hello World");

                Toast.makeText(getActivity().getApplicationContext(), "키 설정이 완료되었습니다.", Toast.LENGTH_SHORT).show();
            }else{
                initListener();
                Toast.makeText(getActivity().getApplicationContext(), "이전에 입력했던 값과 다릅니다. 키 설정을 다시 시작합니다.", Toast.LENGTH_SHORT).show();
            }

            for(int i=0; i<6; i++){
                keyStatus[i].setText("_");
            }

        }else{
            keyStatus[oneMoreCheck.length()-1].setText("*");
        }
    }

}
