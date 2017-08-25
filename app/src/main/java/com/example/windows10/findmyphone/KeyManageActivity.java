package com.example.windows10.findmyphone;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

/**
 * Created by WINDOWS7 on 2017-08-20.
 */

public class KeyManageActivity extends AppCompatActivity{

    View.OnClickListener onClickListener= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.changeKey:
                    final DialogMaker changeKeyDialog=new DialogMaker();
                    changeKeyDialog.setValue("키 값을 변경합니다.", "예", null,
                            new DialogMaker.Callback() {
                                @Override
                                public void callbackMethod() {
                                    getSupportFragmentManager().beginTransaction().replace(R.id.keyManageWrapper, new KeyResettingFragment()).commit();
                                    changeKeyDialog.dismiss();
                                }
                            },null);
                    changeKeyDialog.show(getSupportFragmentManager(), "");
                    break;
                case R.id.deleteKey:
                    final DialogMaker deleteKeyDialog=new DialogMaker();
                    deleteKeyDialog.setValue("키 값을 삭제합니다.", "확인", "취소",
                            new DialogMaker.Callback() {
                                @Override
                                public void callbackMethod() {
                                    Settings.getInstance().setKeyValue(Settings.getInstance().keyNotExist);
                                    writeKeyValue(Settings.getInstance().keyNotExist);
                                    Toast.makeText(getApplicationContext(), "키 값을 성공적으로 삭제하였습니다.", Toast.LENGTH_SHORT).show();
                                    deleteKeyDialog.dismiss();
                                }
                            }, new DialogMaker.Callback() {
                                @Override
                                public void callbackMethod() {
                                    deleteKeyDialog.dismiss();
                                }
                            }, getLayoutInflater().inflate(R.layout.key_delete_explain, null));
                    deleteKeyDialog.show(getSupportFragmentManager(), "");
                    break;
            }
        }
    };

    private Button changeKeyBtn=null;
    private Button deleteKeyBtn=null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.key_manage_activity);

        //Hide Label Bar
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().hide();

        changeKeyBtn=(Button)findViewById(R.id.changeKey);
        changeKeyBtn.setOnClickListener(onClickListener);
        deleteKeyBtn=(Button)findViewById(R.id.deleteKey);
        deleteKeyBtn.setOnClickListener(onClickListener);
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
