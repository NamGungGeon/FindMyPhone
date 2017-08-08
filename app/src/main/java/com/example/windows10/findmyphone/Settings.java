package com.example.windows10.findmyphone;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Windows10 on 2017-08-04.
 */

public class Settings {

    public SharedPreferences preferences=null;
    public SharedPreferences.Editor editor=null;
    private Settings(Context c){
        preferences=c.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        editor=preferences.edit();
    }

    public static Settings inst=null;
    public static Settings getInstance(Context c){
        if(inst==null){
            inst=new Settings(c);
        }
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
        editor.putString("appPassword", s);
    }

}
