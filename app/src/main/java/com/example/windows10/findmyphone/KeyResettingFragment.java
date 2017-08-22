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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

/**
 * Created by WINDOWS7 on 2017-08-16.
 */

public class KeyResettingFragment extends Fragment {

    private final int SUCCESS=2033;
    private final int FAIL=2034;

    private String inputKey="";
    private String oneMoreCheck="";

    final private TextView keyStatus[]=new TextView[6];
    private ImageButton keyNumberBtn[]=new ImageButton[9];
    private ImageButton zeroKeyNumberBtn=null;
    private ImageButton deleteKeyBtn=null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.key_setting_page, container, false);
        init(rootView);
        initListener();
        getActivity().setResult(FAIL);

        return rootView;
    }

    private void init(ViewGroup rootView){
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
        deleteKeyBtn=(ImageButton)rootView.findViewById(R.id.deleteK);
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
        deleteKeyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(inputKey.length()-1>=0){
                    keyStatus[inputKey.length()-1].setText("_");
                    if(inputKey.length()==1){
                        inputKey="";
                    }else{
                        inputKey=inputKey.substring(0, inputKey.length()-1);
                    }
                }
            }
        });
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
                Toast.makeText(getActivity().getApplicationContext(), "키 값 확인을 위해 다시 한번 입력해 주세요.", Toast.LENGTH_SHORT).show();
            }else{
                inputKey="";
                Toast.makeText(getActivity().getApplicationContext(), "사용할 수 없는 키 값입니다. 다른 값으로 시도하세요.", Toast.LENGTH_SHORT).show();
            }

        }else{
            for(int i=0; i<inputKey.length(); i++){
                keyStatus[i].setText("*");
            }
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
                        oneMoreCheck=oneMoreCheck.substring(0, oneMoreCheck.length()-1);
                    }
                }
            }
        });
    }

    private void recheck(){
        if(oneMoreCheck.length()==6){
            if(oneMoreCheck.equals(inputKey)){
                writeKeyValue(oneMoreCheck);
                Settings.getInstance().setKeyValue(oneMoreCheck);
                getActivity().setResult(SUCCESS);
                getActivity().finish();
            }else{
                initListener();
                oneMoreCheck="";
                inputKey="";
                Toast.makeText(getActivity().getApplicationContext(), "이전에 입력했던 값과 다릅니다. 키 설정을 다시 시작합니다.", Toast.LENGTH_SHORT).show();

                for(int i=0; i<6; i++){
                    keyStatus[i].setText("_");
                }
            }
        }else{
            for(int i=0; i<oneMoreCheck.length(); i++){
                keyStatus[i].setText("*");
            }
        }
    }

    private DatabaseReference getDatabaseReference(){
        return FirebaseDatabase.getInstance().getReference();
    }

    private boolean writeKeyValue(String key){
        try{
            //Checking input key can be converted int type
            //If can't do that, occur Exception and return false
            int keyValue=Integer.parseInt(key);
            HashMap<String, Object> val=new HashMap<String, Object>();

            val.put("/users/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()+"/key", key);
            getDatabaseReference().updateChildren(val);
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }

}


