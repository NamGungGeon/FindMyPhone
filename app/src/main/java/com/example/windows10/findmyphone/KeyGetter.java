package com.example.windows10.findmyphone;

import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Windows10 on 2017-08-22.
 */

public class KeyGetter implements Runnable{
    //This class is designed as singleton pattern.
    private KeyGetter(){
    }
    private static KeyGetter inst=null;
    public static KeyGetter getInstance(){
        if(inst==null){
            inst=new KeyGetter();
        }
        return inst;
    }


    //KeyReader class is only used for getting key from database
    private class KeyReader{
        String key=LOADING;
    }

    private final String TAG="KEYGETTER";
    private final String LOADING="LOADING";
    private final KeyReader reader=new KeyReader();

    private final DialogMaker loadingDialog=new DialogMaker();

    boolean b=true;
    @Override
    public void run() {
        String userId= FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference saved= FirebaseDatabase.getInstance().getReference().child("/users").child("/"+userId).child("/key");
        saved.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    reader.key=((String)dataSnapshot.getValue());
                    b=false;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public String getKey(FragmentManager fragmentManager, LayoutInflater inflater){
        Thread keyGetterWithThread=new Thread(this);
        keyGetterWithThread.start();

        loadingDialog.setValue("키 값을 로딩하는 중입니다.", null, null, null, null, getProgreeBarLayout(inflater));
        loadingDialog.setCancelable(false);
        loadingDialog.show(fragmentManager, "");

        try{
            looper();
        }catch (Exception e){
            Log.i(TAG, e.toString());
        }
        return reader.key;
    }

    private void looper() throws InterruptedException{
        int loopNumber=0;
        while(b) {
            Log.i(TAG, reader.key);
            Log.i(TAG, String.valueOf(++loopNumber));
            Thread.sleep(3000L);
        }
    }

    private View getProgreeBarLayout(LayoutInflater inflater){
        return inflater.inflate(R.layout.progress_bar, null);
    }
}
