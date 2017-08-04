package com.example.windows10.findmyphone;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import static android.R.attr.fragment;

/**
 * Created by Windows10 on 2017-08-04.
 */

public class FirstStartFragment extends Fragment {
    @NonNull
    private View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.startBtn:
                    FragmentHolder.setLoginFragment(new LoginFragment());
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, FragmentHolder.getLoginFragment()).commit();
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.fragment_first_start_page, container, false);

        ImageButton startBtn=(ImageButton)rootView.findViewById(R.id.startBtn);
        startBtn.setOnClickListener(onClickListener);

        return rootView;
    }



}
