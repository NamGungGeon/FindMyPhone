package com.example.windows10.findmyphone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

/**
 * Created by Windows10 on 2017-08-07.
 */


public class SMS_Receiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            Bundle bundle=intent.getExtras();
            Object objs[]=(Object[])bundle.get("pdus");
            SmsMessage messages[]=new SmsMessage[objs.length];

            int smsCount=objs.length;
            for(int i=0; i<smsCount; i++){
                if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
                    String format=bundle.getString("format");
                    messages[i]=SmsMessage.createFromPdu((byte[])objs[i], format);
                }else{
                    messages[i]=SmsMessage.createFromPdu((byte[])objs[i]);
                }
            }

            String content=messages[0].getMessageBody().toString();
            StringTokenizer spliter=new StringTokenizer(content, "#", false);

            try{

                String command=spliter.nextToken();
                String key=spliter.nextToken();
                //if received key is not matched saved key,
                if(!isVaildKey(key)){
                    return;
                }

                execute(command);

            }catch(Exception e){
                Log.i("Exception in SMS", "String Error");
            }
        }
    }

    private boolean isVaildKey(String key){
        if(key.equals(Settings.getInstance().getKeyValue())){
            return true;
        }else{
            return false;
        }
    }
    private void execute(String command){
        if(command.equals("removeAllFile")){
            //removeAllFiles();
        }else if(command.equals("encryptAllFile")) {
            //encryptAllFiles();
        }
    }



    private boolean removeAllFiles(){
        File root=Environment.getExternalStorageDirectory().getAbsoluteFile();

        String fileList[]=root.list();
        File deletedFile=null;

        boolean isAllRemoved=true;
        for(int i=0; i<fileList.length; i++){
            deletedFile=new File(root.getAbsoluteFile()+File.separator+fileList[i]);
            if(deletedFile!=null && deletedFile.exists()){
                if(!deletedFile.delete()){
                    isAllRemoved=false;
                }
            }else{
                isAllRemoved=false;
            }
        }

        return isAllRemoved;
    }

    private boolean encryptAllFiles(){
        File root=Environment.getExternalStorageDirectory().getAbsoluteFile();

        String fileList[]=root.list();
        File encryptedFile=null;

        boolean isAllEncrypted=true;
        for(int i=0; i<fileList.length; i++){
            encryptedFile=new File(root.getAbsoluteFile()+File.separator+fileList[i]);
            if(encryptedFile!=null && encryptedFile.exists()){
                //암호화
            }else{
                isAllEncrypted=false;
            }
        }
        return false;

    }
    /*
    //This class is only using in getKey()
    private class KeyGetter{
        public String key=null;
    }
    public String getKey(String id) {
        DatabaseReference saved = getDatabaseReference().child("/user/" + id + "/key");
        final KeyGetter keyGetter=new KeyGetter();
        saved.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Object obj=dataSnapshot.getValue();
                if (obj != null) {
                    keyGetter.key=(String)obj;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return keyGetter.key;
    }
    */

}
