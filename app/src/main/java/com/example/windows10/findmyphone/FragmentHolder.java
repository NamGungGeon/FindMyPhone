package com.example.windows10.findmyphone;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

/**
 * Created by Windows10 on 2017-08-04.
 */

public class FragmentHolder {
    //This class is never instantiated
    private FragmentHolder(){}

    private static FirstStartFragment firstStartFragment=null;
    private static LoginFragment loginFragment=null;

    @NonNull
    public static FirstStartFragment getFirstStartFragment() {
        return firstStartFragment;
    }

    public static void setFirstStartFragment(FirstStartFragment firstStartFragment) {
        FragmentHolder.firstStartFragment = firstStartFragment;
    }

    @NonNull
    public static LoginFragment getLoginFragment() {
        return loginFragment;
    }

    public static void setLoginFragment(LoginFragment loginFragment) {
        FragmentHolder.loginFragment = loginFragment;
    }


}
