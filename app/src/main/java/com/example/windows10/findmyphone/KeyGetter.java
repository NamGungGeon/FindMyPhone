package com.example.windows10.findmyphone;

import android.content.Context;
import android.os.Looper;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Windows10 on 2017-08-22.
 */

//Not Used Class

public class KeyGetter implements Runnable{
    private Context context=null;
    //This class is designed as singleton pattern.
    private KeyGetter(Context context){
        this.context=context;
    }
    private static KeyGetter inst=null;
    public static KeyGetter getInstance(Context context){
        if(inst==null){
            inst=new KeyGetter(context);
        }
        return inst;
    }

    private final String TAG="KEYGETTER";
    private final String LOADING="LOADING";
    private final String EMPTY="EMPTY";
    public String keyReader=new String(LOADING);

    private final DialogMaker loadingDialog=new DialogMaker();

    @Override
    public void run() {
        Looper.prepare();

        String userId= FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference saved= FirebaseDatabase.getInstance().getReference().child("/users").child("/"+userId).child("/key");
        Log.i("CALL!!", "THREAD");
        saved.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("CALL!!", "SNAPSHOT");
                if(dataSnapshot.getValue()!=null){
                    keyReader=(String)dataSnapshot.getValue();
                }else{
                    keyReader=new String(EMPTY);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    public String getKey(FragmentManager fragmentManager, LayoutInflater inflater){
        loadingDialog.setValue("키 값을 로딩하는 중입니다.", null, null, null, null, getProgressBarLayout(inflater));
        loadingDialog.setCancelable(false);
        loadingDialog.show(fragmentManager, "");

        Thread keyGetterWithThread=new Thread(this);
        keyGetterWithThread.start();
        try {
            keyGetterWithThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        loadingDialog.dismiss();
        /*
        try {
            waiter();
        } catch (InterruptedException e) {
            Log.i(TAG, e.toString());
        }
        */
        Toast.makeText(context, keyReader, Toast.LENGTH_LONG).show();
        return keyReader;
    }

    //Not Perfect Method
    private void waiter() throws InterruptedException{
        int loopNumber=0;
        while(true) {
            Log.i(TAG, keyReader);
            Log.i(TAG, String.valueOf(++loopNumber));
            break;
        }

        Toast.makeText(context, keyReader, Toast.LENGTH_SHORT).show();
        loadingDialog.dismiss();
    }

    private View getProgressBarLayout(LayoutInflater inflater){
        return inflater.inflate(R.layout.progress_bar, null);
    }
}
