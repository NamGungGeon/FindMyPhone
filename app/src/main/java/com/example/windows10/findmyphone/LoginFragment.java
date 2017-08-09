package com.example.windows10.findmyphone;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

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
                    ((MainActivity)getActivity()).login_google();
                    break;
                case R.id.facebookLogin:
                    //Developing...
                    Toast.makeText(getActivity().getApplicationContext(), "현재는 구글 계정으로만 이용하실 수 있습니다.", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.login_page, container, false);

        Button googleLoginBtn=(Button)rootView.findViewById(R.id.googleLogin);
        googleLoginBtn.setOnClickListener(onClickListener);
        Button facebookLoginBtn=(Button)rootView.findViewById(R.id.facebookLogin);
        facebookLoginBtn.setOnClickListener(onClickListener);

        return rootView;
    }

}
