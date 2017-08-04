package com.example.windows10.findmyphone;

/**
 * Created by Windows10 on 2017-08-04.
 */

public class Settings {
    private static class SettingsValue{
        private boolean isFirst=true;
    }
    //This class is never instantiated
    private Settings(){}

    public static boolean isFirst=true;
}
