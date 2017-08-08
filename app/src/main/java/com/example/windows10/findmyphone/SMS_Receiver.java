package com.example.windows10.findmyphone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.SmsMessage;
import android.widget.Toast;

import java.io.File;
import java.util.StringTokenizer;

/**
 * Created by Windows10 on 2017-08-07.
 */

/*
* 데이터베이스에 저장된 Key값을 꺼내오는 기능을 구현해야 함(미구현)
* */


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

            String command=spliter.nextToken();
            String key=spliter.nextToken();

            if(command!=null && key!=null){
                //key값을 if문을 이용하여 저장된 key값과 비교해야 함
                if(command.equals("removeAllFile")){
                    //removeAllFiles();
                }else if(command.equals("encryptAllFile")){
                    //encryptAllFiles();
                }
            }

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




}
