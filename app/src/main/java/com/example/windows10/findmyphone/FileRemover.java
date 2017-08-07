package com.example.windows10.findmyphone;

import java.io.File;

/**
 * Created by Windows10 on 2017-08-07.
 */

public class FileRemover {
    //This class is never instantiated
    private FileRemover(){}

    public static boolean removeFile(File file){
        if(file.exists()){
            if(file.delete()){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }
}
