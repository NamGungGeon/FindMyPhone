package com.example.windows10.findmyphone;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 * Created by Windows10 on 2017-08-04.
 */

public class LoginFragment extends Fragment {
    @NonNull
    private View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.googleLogin:
                    //Developing...
                    //Test Code
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, new MainPageFragment()).commit();
                    break;
                case R.id.facebookLogin:
                    //Developing...
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.login_page, container, false);

        ImageButton googleLoginBtn=(ImageButton)rootView.findViewById(R.id.googleLogin);
        googleLoginBtn.setOnClickListener(onClickListener);
        ImageButton facebookLoginBtn=(ImageButton)rootView.findViewById(R.id.facebookLogin);
        facebookLoginBtn.setOnClickListener(onClickListener);

        return rootView;
    }
}
