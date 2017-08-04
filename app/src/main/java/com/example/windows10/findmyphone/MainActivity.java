package com.example.windows10.findmyphone;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private final int PERMISSION_REQUEST_CODE=1234;

    FragmentTransaction transaction=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Hide Label Bar
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().hide();

        permissionCheck();

        if(Settings.isFirst){
            transaction=getSupportFragmentManager().beginTransaction();
            FragmentHolder.setFirstStartFragment(new FirstStartFragment());
            transaction.replace(R.id.mainContainer, FragmentHolder.getFirstStartFragment());
            transaction.commit();
        }else{

        }
    }

    private boolean permissionCheck(){
        //Permission Check
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED){
            //All Permission is granted
            return true;
        }else{
            //All Permission is not granted
            DialogMaker whyNeedPermissionExplain=new DialogMaker();
            final Activity thisActivty=this;
            DialogMaker.Callback ok=new DialogMaker.Callback() {
                @Override
                public void callbackMethod() {
                    String requestPermissionList[]=new String[]
                            {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA};
                    ActivityCompat.requestPermissions(thisActivty, requestPermissionList, PERMISSION_REQUEST_CODE);
                }
            };
            DialogMaker.Callback appClose=new DialogMaker.Callback() {
                @Override
                public void callbackMethod() {
                    finish();
                }
            };
            whyNeedPermissionExplain.setValue("다음 권한이 필요합니다.", "알겠습니다", "앱 종료", ok, appClose);
            whyNeedPermissionExplain.show(getSupportFragmentManager(), "");
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_REQUEST_CODE:
                for(int i=0; i<permissions.length; i++){
                    if(grantResults[i]==PackageManager.PERMISSION_DENIED){
                        Toast.makeText(getApplicationContext(), "모든 권한을 취득하지 않으면 앱 사용이 불가능합니다.", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
                break;
        }
    }
}
