package com.example.windows10.findmyphone;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * Created by WINDOWS7 on 2017-08-09.
 */

public class MainSettingFragment extends Fragment{
    private final int GPS_SETTING_ACTIVITY_CODE=1009;

    final int APP_LOCK_SETTING=6044;
    final int KEY_SETTING=6045;
    final int PIN_MANAGE=6046;

    final int SUCCESS=2033;
    final int FAIL=2034;

    private Button appLockStatusBtn=null;
    private Button receiveStatusBtn=null;
    private Button gpsStatusBtn=null;
    private Button keyChangeBtn=null;
    private Button inputCodeBtn=null;
    private Button devInfoBtn=null;
    private Button manageFunctionBtn=null;
    private Button donationBtn=null;

    private Settings settings=Settings.getInstance();

    private View.OnClickListener onClickListener=new View.OnClickListener(){

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.appLock:
                    if(settings.getIsLockThisApp()){
                        getActivity().startActivityForResult(new Intent(getActivity().getApplicationContext(), AppLockerManageActivity.class), PIN_MANAGE);
                    }else{
                        final DialogMaker appLock=new DialogMaker();
                        appLock.setValue("앱 잠금을 설정합니다.", "확인", "취소",
                                new DialogMaker.Callback() {
                                    @Override
                                    public void callbackMethod() {
                                        getActivity().startActivityForResult(new Intent(getActivity().getApplicationContext(), AppLockerSettingActivity.class), KEY_SETTING);
                                        appLock.dismiss();
                                    }
                                }
                                , new DialogMaker.Callback() {
                                    @Override
                                    public void callbackMethod() {
                                        appLock.dismiss();
                                    }
                                });
                        appLock.show(getActivity().getSupportFragmentManager(), "");
                    }
                    break;
                case R.id.receiveInfo:
                    if(settings.getReceiveStatus()){
                        settings.setReceiveStatus(false);
                        receiveStatusBtn.setBackground(getResources().getDrawable(R.drawable.receive_no));
                    }else{
                        settings.setReceiveStatus(true);
                        receiveStatusBtn.setBackground(getResources().getDrawable(R.drawable.receive_ok));
                    }
                    break;
                case R.id.statusGPS:
                    startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), GPS_SETTING_ACTIVITY_CODE);
                    break;
                case R.id.changeKey:
                    final DialogMaker changeKey=new DialogMaker();
                    changeKey.setValue("키 값을 재설정합니다", "확인", "취소", new DialogMaker.Callback() {
                        @Override
                        public void callbackMethod() {
                            changeKey.dismiss();
                            getActivity().startActivityForResult(new Intent(getActivity().getApplicationContext(), KeyResettingActivity.class), KEY_SETTING);
                        }
                    }, new DialogMaker.Callback() {
                        @Override
                        public void callbackMethod() {
                            changeKey.dismiss();
                        }
                    });
                    changeKey.show(getActivity().getSupportFragmentManager(), "");
                    break;
                case R.id.functionManage:
                    final DialogMaker functionMange=new DialogMaker();
                    functionMange.setValue("기능 관리", "닫기", "",
                            new DialogMaker.Callback() {
                                @Override
                                public void callbackMethod() {
                                    functionMange.dismiss();

                                }
                            }, null, getFunctionManageView());
                    functionMange.show(getActivity().getSupportFragmentManager(), "");
                    break;
                case R.id.donation:
                    Toast.makeText(getActivity().getApplicationContext(), "미구현 기능입니다.", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.inputCode:
                    Toast.makeText(getActivity().getApplicationContext(), "미구현 기능입니다.", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.devInfo:
                    final DialogMaker devInfo=new DialogMaker();
                    devInfo.setValue("개발자 정보", "", "", null, null, getActivity().getLayoutInflater().inflate(R.layout.dev_info_page, null));
                    devInfo.show(getActivity().getSupportFragmentManager(), "");
                    break;
                case R.id.languageSet:
                    /*
                    if(settings.getLanguage()==null){
                        final DialogMaker setLang= new DialogMaker();
                        setLang.setValue("언어 설정", "", "", null, null,
                                new String[]{"한국어", "English (not yet supported)"},
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which){
                                            case 0:
                                                settings.setLanguage(settings.KOR);
                                                setLang.dismiss();
                                                break;
                                            case 1:
                                                Toast.makeText(getActivity().getApplicationContext(), "Not yet suporrted language", Toast.LENGTH_SHORT);
                                                break;
                                        }
                                    }
                                });
                        //setLang.setCancelable(false);
                        setLang.show(getActivity().getSupportFragmentManager(), "");

                    }
                    */
                    Toast.makeText(getActivity().getApplicationContext(), "미구현", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.setting_page, container, false);

        init(rootView);
        updateStatus();

        return rootView;
    }

    private void init(ViewGroup rootView){
        appLockStatusBtn=(Button)rootView.findViewById(R.id.appLock);
        appLockStatusBtn.setOnClickListener(onClickListener);
        receiveStatusBtn=(Button)rootView.findViewById(R.id.receiveInfo);
        receiveStatusBtn.setOnClickListener(onClickListener);
        gpsStatusBtn=(Button)rootView.findViewById(R.id.statusGPS);
        gpsStatusBtn.setOnClickListener(onClickListener);
        keyChangeBtn=(Button)rootView.findViewById(R.id.changeKey);
        keyChangeBtn.setOnClickListener(onClickListener);
        inputCodeBtn=(Button)rootView.findViewById(R.id.inputCode);
        inputCodeBtn.setOnClickListener(onClickListener);
        devInfoBtn=(Button)rootView.findViewById(R.id.devInfo);
        devInfoBtn.setOnClickListener(onClickListener);
        donationBtn=(Button)rootView.findViewById(R.id.donation);
        donationBtn.setOnClickListener(onClickListener);
        manageFunctionBtn=(Button)rootView.findViewById(R.id.functionManage);
        manageFunctionBtn.setOnClickListener(onClickListener);
    }
    private void updateStatus(){
        if(settings.getIsLockThisApp()){
            appLockStatusBtn.setBackground(getResources().getDrawable(R.drawable.lock));
        }else{
            appLockStatusBtn.setBackground(getResources().getDrawable(R.drawable.unlock));
        }

        if(settings.getReceiveStatus()){
            receiveStatusBtn.setBackground(getResources().getDrawable(R.drawable.receive_ok));
        }else{
            receiveStatusBtn.setBackground(getResources().getDrawable(R.drawable.receive_no));
        }

        if(getGpsStatus()){
            gpsStatusBtn.setBackground(getResources().getDrawable(R.drawable.doing_gps));
        }else{
            gpsStatusBtn.setBackground(getResources().getDrawable(R.drawable.undoing_gps));
        }
    }

    private boolean getGpsStatus(){
        String provider = android.provider.Settings.Secure
                .getString(getActivity().getContentResolver(), android.provider.Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        return provider.contains("gps");
    }


    private View getFunctionManageView(){
        View view=getActivity().getLayoutInflater().inflate(R.layout.function_manage, null);


        RelativeLayout removeAllWrapper= (RelativeLayout) view.findViewById(R.id.removeAllWrapper);
        final CheckBox enableRemoveAll=(CheckBox)view.findViewById(R.id.useRemoveAllFilesFunctionBtn);
        enableRemoveAll.setChecked(settings.getAvailableRemoveAllFilesFunc());

        RelativeLayout encryptWrapper=(RelativeLayout)view.findViewById(R.id.encryptAllWrapper);
        final CheckBox enableEncryptAll=(CheckBox)view.findViewById(R.id.useEncryptAllFilesFunctionBtn);
        enableEncryptAll.setChecked(settings.getAvailableEncryptAllFilesFunc());

        RelativeLayout gpsTraceWrapper=(RelativeLayout)view.findViewById(R.id.gpsTraceWrapper);
        final CheckBox enableGpsTrace=(CheckBox)view.findViewById(R.id.useGpsTraceFunction);
        enableGpsTrace.setChecked(settings.getAvailableGpsTraceFunc());

        RelativeLayout cameraWrapper=(RelativeLayout)view.findViewById(R.id.cameraWrapper);
        final CheckBox enableCamera=(CheckBox)view.findViewById(R.id.useCameraFunction);
        enableCamera.setChecked(settings.getAvailableCameraFunc());

        RelativeLayout phoneLockWrapper=(RelativeLayout)view.findViewById(R.id.phoneLockWrapper);
        final CheckBox enablePhoneLock=(CheckBox)view.findViewById(R.id.usePhoneLockFunction);
        enablePhoneLock.setChecked(settings.getAvailablePhoneLock());

        RelativeLayout uploadAllWrapper=(RelativeLayout)view.findViewById(R.id.uploadAllWrapper);
        final CheckBox enableUploadAll=(CheckBox)view.findViewById(R.id.useUploadAllFileFunction);
        enableUploadAll.setChecked(settings.getAvailableUploadAll());

        RelativeLayout loudSoundWrapper=(RelativeLayout)view.findViewById(R.id.loudSoundWrapper);
        final CheckBox enableLoudSound=(CheckBox)view.findViewById(R.id.useLoudSoundFunction);
        enableUploadAll.setChecked(settings.getAvailableLoudSound());

        View.OnClickListener listener=new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.useRemoveAllFilesFunctionBtn:
                    case R.id.removeAllWrapper:
                        if(settings.getAvailableRemoveAllFilesFunc()){
                            settings.setAvailableRemoveAllFilesFunc(false);
                            enableRemoveAll.setChecked(false);
                        }else{
                            settings.setAvailableRemoveAllFilesFunc(true);
                            enableRemoveAll.setChecked(true);
                        }
                        break;
                    case R.id.useEncryptAllFilesFunctionBtn:
                    case R.id.encryptAllWrapper:
                        if(settings.getAvailableEncryptAllFilesFunc()){
                            settings.setAvailableEncryptAllFilesFunc(false);
                            enableEncryptAll.setChecked(false);
                        }else{
                            settings.setAvailableEncryptAllFilesFunc(true);
                            enableEncryptAll.setChecked(true);
                        }
                        break;
                    case R.id.useGpsTraceFunction:
                    case R.id.gpsTraceWrapper:
                        if(settings.getAvailableGpsTraceFunc()){
                            settings.setAvailableGpsTraceFunc(false);
                            enableGpsTrace.setChecked(false);
                        }else{
                            settings.setAvailableGpsTraceFunc(true);
                            enableGpsTrace.setChecked(true);
                        }

                        break;
                    case R.id.useCameraFunction:
                    case R.id.cameraWrapper:
                        if(settings.getAvailableCameraFunc()){
                            settings.setAvailableCameraFunc(false);
                            enableCamera.setChecked(false);
                        }else{
                            settings.setAvailableCameraFunc(true);
                            enableCamera.setChecked(true);
                        }
                        break;
                    case R.id.usePhoneLockFunction:
                    case R.id.phoneLockWrapper:
                        if(settings.getAvailablePhoneLock()){
                            settings.setAvailablePhoneLock(false);
                            enablePhoneLock.setChecked(false);
                        }else{
                            settings.setAvailablePhoneLock(true);
                            enablePhoneLock.setChecked(true);
                        }
                        break;
                    case R.id.uploadAllWrapper:
                        if(settings.getAvailableUploadAll()){
                            settings.setAvailableUploadAll(false);
                            enableUploadAll.setChecked(false);
                        }else{
                            settings.setAvailableUploadAll(true);
                            enableCamera.setChecked(true);
                        }
                        break;
                    case R.id.loudSoundWrapper:
                        if(settings.getAvailableLoudSound()){
                            settings.setAvailableLoudSound(false);
                            enableLoudSound.setChecked(false);
                        }else{
                            settings.setAvailableLoudSound(true);
                            enableLoudSound.setChecked(true);
                        }
                        break;
                }
            }
        };

        removeAllWrapper.setOnClickListener(listener);
        enableRemoveAll.setOnClickListener(listener);

        encryptWrapper.setOnClickListener(listener);
        enableEncryptAll.setOnClickListener(listener);

        gpsTraceWrapper.setOnClickListener(listener);
        enableGpsTrace.setOnClickListener(listener);

        cameraWrapper.setOnClickListener(listener);
        enableCamera.setOnClickListener(listener);

        phoneLockWrapper.setOnClickListener(listener);
        enablePhoneLock.setOnClickListener(listener);

        uploadAllWrapper.setOnClickListener(listener);
        enableUploadAll.setOnClickListener(listener);

        loudSoundWrapper.setOnClickListener(listener);
        enableLoudSound.setOnClickListener(listener);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case APP_LOCK_SETTING:
                if(resultCode==SUCCESS){
                    Toast.makeText(getActivity().getApplicationContext(), "앱 잠금 설정이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                }else if(resultCode==FAIL){
                    Toast.makeText(getActivity().getApplicationContext(), "앱 잠금 설정이 취소되었습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
            case KEY_SETTING:
                if(resultCode==SUCCESS){
                    Toast.makeText(getActivity().getApplicationContext(), "키 설정이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                }else if(resultCode==FAIL){
                    Toast.makeText(getActivity().getApplicationContext(), "키 설정이 취소되었습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
            case GPS_SETTING_ACTIVITY_CODE:
                break;
            case PIN_MANAGE:
                break;
        }
        updateStatus();
    }

}
