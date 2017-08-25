package com.example.windows10.findmyphone;

/**
 * Created by WINDOWS7 on 2017-08-25.
 */

public class PhoneLockStatus {
    private static boolean activityStatus=false;

    public static void setStatus(boolean b){
        activityStatus=b;
    }

    public static boolean getStatus(){
        return activityStatus;
    }

}
