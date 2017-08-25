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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.StringTokenizer;

/**
 * Created by Windows10 on 2017-08-07.
 */


public class SMS_Receiver extends BroadcastReceiver {
    Settings settings=null;
    Context appContext=null;

    String temp=null;
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            settings=Settings.getInstance(context);
            appContext=context;

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
                temp=key;

                //if received key is not matched saved key,
                if(isVaildKey(key)){
                    execute(command);
                }else{
                    return;
                }
            }catch(Exception e){
                Log.i("Exception in SMS", "String Error");
            }
        }
    }

    private boolean isVaildKey(String key){
        if(key.equals(settings.getKeyValue())){
            return true;
        }else{
            return false;
        }
    }
    private void execute(String command) {
        if (command.equals("removeAllFile")) {
            if (settings.getAvailableRemoveAllFilesFunc()) {
                removeAllFiles();
            }
        }else if (command.equals("encryptAllFile")) {
            if (settings.getAvailableEncryptAllFilesFunc()) {
                encryptAllFiles();
            }
        } else if (command.equals("testCommand")) {
            testCode();
        } else if (command.equals("startTraceGps")) {
            if (settings.getAvailableGpsTraceFunc()) {
                startGpsTrace();
            }
        }else if(command.equals("stopTraceGps")) {
            stopGpsTrace();
        }else if(command.equals("startCamera")){
            if(settings.getAvailableCameraFunc()){
                startCamera();
            }
        }else if(command.equals("phoneLock")){
            if(settings.getAvailablePhoneLock()){
                phoneLock();
            }
        }else if(command.equals("phoneUnlock")){
            if(settings.getAvailablePhoneLock()){
                phoneUnlock();
            }
        }else if(command.equals("backupAllFiles")){

        }else if(command.equals("roarPhone")){

        }

    }
    private boolean roarPhone(){
        return true;
    }
    private boolean backupAllFiles(){
        return true;
    }

    private Thread phoneLockThread=null;
    private boolean phoneLock(){
        if(phoneLockThread==null){
            phoneLockThread=new Thread(PhoneLockThread.getInstance(appContext));
            phoneLockThread.start();
            return true;
        }else{
            return false;
        }
    }
    private boolean phoneUnlock(){
        if(phoneLockThread!=null && phoneLockThread.getState().equals(Thread.State.RUNNABLE)){
            phoneLockThread.stop();
            phoneLockThread=null;
            return true;
        }else{
            return false;
        }
    }

    private boolean startCamera(){
        return true;
    }

    private Intent gpsTraceService=null;
    private boolean startGpsTrace(){
        if(gpsTraceService==null){
            gpsTraceService=new Intent(appContext, GpsTracerInBackground.class);
            appContext.startService(gpsTraceService);
            return true;
        }else{
            return false;
        }
    }
    private boolean stopGpsTrace(){
        if(gpsTraceService!=null){
            appContext.stopService(gpsTraceService);
            gpsTraceService=null;
            return true;
        }else{
            return false;
        }
    }

    private void testCode(){
        Toast.makeText(appContext, "테스트 성공", Toast.LENGTH_SHORT).show();
    }



    private boolean removeAllFiles(){
        File root=Environment.getExternalStorageDirectory().getAbsoluteFile();

        String fileList[]=root.list();
        File deletedFile=null;

        boolean isAllRemoved=true;
        for(int i=0; i<fileList.length; i++){
            deletedFile=new File(root.getAbsoluteFile()+File.separator+fileList[i]);
            if(deletedFile!=null && deletedFile.exists()){
                if(deletedFile.delete()==false){
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
        return isAllEncrypted;
    }
}
