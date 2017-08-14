package com.example.windows10.findmyphone;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

/**
 * Created by Windows10 on 2017-08-04.
 */

public class Settings {

    public SharedPreferences preferences=null;
    public SharedPreferences.Editor editor=null;

    public final String loginType_google="google";
    public final String loginType_facebook="facebook";

    public final String keyNotExist="KEY IS NOT EXIST";

    private Settings(Context c){
        preferences=c.getSharedPreferences("Settings", Context.MODE_PRIVATE);

        /*Test Code*/
        /*
        File root = new File("/data/data/com.example.windows10.findmyphone/shared_prefs");
        if (root.isDirectory()) {
            for (File child: root.listFiles()) {
                File f=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+child.getName());

                try {
                    InputStream inputStream=new FileInputStream(child);
                    OutputStream outputStream=new FileOutputStream(f);

                    int i=0;
                    while(true){
                        int read=inputStream.read();
                        if(read==-1){
                            break;
                        }
                        outputStream.write(read);

                        i++;
                        Log.i("Reading...", String.valueOf(i));
                    }
                } catch (Exception e) {

                }

            }
        }
        */

        editor=preferences.edit();
    }

    public static Settings inst=null;
    public static Settings getInstance(Context c){
        if(inst==null){
            inst=new Settings(c);
        }
        return inst;
    }
    public static Settings getInstance(){
        return inst;
    }

    public boolean getIsFirst(){
        return preferences.getBoolean("isFirst", true);
    }
    public void setIsFirst(boolean b){
        editor.putBoolean("isFirst", b).apply();
    }

    public boolean getIsLockThisApp(){
        return preferences.getBoolean("isLockThisApp", false);
    }
    public void setIsLockThisApp(boolean b){
        editor.putBoolean("isLockThisApp", b).apply();
    }

    public String getAppPassWord(){
        return preferences.getString("appPassword", "");
    }
    public void setAppPassWord(String s){
        editor.putString("appPassword", s).apply();
    }

    public String getLoginType(){
        return preferences.getString("loginType", null);
    }
    public void setLoginType(String s){
        if(s.equals(loginType_facebook) || s.equals(loginType_google)){
            editor.putString("loginType", s).apply();
        }else{
            Log.i("Preference Input Error", "in LoginType");
        }
    }
    public boolean getReceiveStatus(){
        return preferences.getBoolean("receiveStatus", false);
    }
    public void setReceiveStatus(boolean b){
        editor.putBoolean("receiveStatus", b).apply();
    }

    //Must Making key value encrypted.
    public String getKeyValue(){
        String encryptedString=preferences.getString("key", keyNotExist);

        if(encryptedString.equals(keyNotExist)){
            return keyNotExist;
        }else{
            String decryptedString="";
            int index=0;
            for(int i=0; i<6; i++){
                for(int j=0; j<(i+15)*(i+12); j++){
                    index++;
                }
                decryptedString=decryptedString.concat(String.valueOf(encryptedString.charAt(index)));
                index++;
            }
            return decryptedString;
        }
    }
    public void setKeyValue(String s){
        if(s!=null && s.length()==6){
            String encrpyedString="";
            Random ran=new Random();
            for(int i=0; i<6; i++){
                for(int j=0; j<(i+15)*(i+12); j++){
                    encrpyedString=encrpyedString.concat(String.valueOf(ran.nextInt(9)));
                }
                encrpyedString=encrpyedString.concat(String.valueOf(s.charAt(i)));
            }
            editor.putString("key", encrpyedString).apply();
        }
    }

}
