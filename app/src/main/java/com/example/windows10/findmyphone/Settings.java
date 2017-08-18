package com.example.windows10.findmyphone;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Random;

/**
 * Created by Windows10 on 2017-08-04.
 */

public class Settings {

    public SharedPreferences preferences=null;
    public SharedPreferences.Editor editor=null;

    public final String loginType_google="google";
    public final String loginType_facebook="facebook";

    public final String KOR="kor";
    public final String ENG="eng";

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

    public String getLanguage(){
        return preferences.getString("langage", null);
    }
    public void setLanguage(String s){
        editor.putString("language", s).apply();
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
        return preferences.getBoolean("receiveStatus", true);
    }
    public void setReceiveStatus(boolean b){
        editor.putBoolean("receiveStatus", b).apply();
    }

    //key value is must encrypted.
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

    public boolean getAvailableRemoveAllFilesFunc(){
        return preferences.getBoolean("removeAllFiles", true);
    }
    public void setAvailableRemoveAllFilesFunc(boolean b){
        editor.putBoolean("removeAllFiles", b).apply();
    }

    public boolean getAvailableEncryptAllFilesFunc(){
        return preferences.getBoolean("encryptAllFiles", true);
    }
    public void setAvailableEncryptAllFilesFunc(boolean b){
        editor.putBoolean("encryptAllFiles", b).apply();
    }

    public boolean getAvailableGpsTraceFunc(){
        return preferences.getBoolean("traceGps", true);
    }
    public void setAvailableGpsTraceFunc(boolean b){
        editor.putBoolean("traceGps", b).apply();
    }

    public boolean getAvailableCameraFunc(){
        return preferences.getBoolean("camera", true);
    }
    public void setAvailableCameraFunc(boolean b){
        editor.putBoolean("camera", b).apply();
    }
    public boolean getAvailablePhoneLock(){
        return preferences.getBoolean("phoneLock", true);
    }
    public void setAvailablePhoneLock(boolean b){
        editor.putBoolean("phoneLock", b).apply();
    }

}
