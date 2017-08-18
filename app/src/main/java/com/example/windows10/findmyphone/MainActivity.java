package com.example.windows10.findmyphone;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private final int PERMISSION_REQUEST_CODE=1234;
    private final int LOGIN_GOOGLE=1235;
    private final int LOGIN_FACEBOOK=1236;


    private Settings settings=null;

    private GoogleSignInOptions gso=null;
    private GoogleApiClient mGoogleApiClient=null;
    private FirebaseAuth firebaseAuth=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settings=Settings.getInstance(getApplicationContext());

        //Hide Label Bar
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().hide();

        setLanguage();
        permissionCheck();
        startAppProtect();
    }

    private boolean permissionCheck(){
        //Permission Check
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)==PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECEIVE_SMS)==PackageManager.PERMISSION_GRANTED){
            //All Permission is granted
            return true;
        }else{
            //All Permission is not granted
            final DialogMaker explainWhyNeedPermission=new DialogMaker();
            final Activity thisActivty=this;
            DialogMaker.Callback ok=new DialogMaker.Callback() {
                @Override
                public void callbackMethod() {
                    String requestPermissionList[]=new String[]
                            {Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
                    //getSettingWritePermission();
                    ActivityCompat.requestPermissions(thisActivty, requestPermissionList, PERMISSION_REQUEST_CODE);
                    explainWhyNeedPermission.dismiss();
                }
            };
            DialogMaker.Callback appClose=new DialogMaker.Callback() {
                @Override
                public void callbackMethod() {
                    finish();
                }
            };
            explainWhyNeedPermission.setValue("다음 권한이 필요합니다.", "알겠습니다", "앱 종료", ok, appClose, getLayoutInflater().inflate(R.layout.permission_explain, null));
            explainWhyNeedPermission.setCancelable(false);
            explainWhyNeedPermission.show(getSupportFragmentManager(), "");
            return false;
        }
    }
    private void setLanguage(){
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
                                    Toast.makeText(getApplicationContext(), "Not yet suporrted language", Toast.LENGTH_SHORT);
                                    break;
                            }
                        }
                    });
            //setLang.setCancelable(false);
            setLang.show(getSupportFragmentManager(), "");
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

    public void startAppProtect(){
        if(settings.getIsLockThisApp()){
            getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, new AppLockerFragment()).commit();
        }else{
            if(settings.getIsFirst()){
                FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.mainContainer, new FirstStartFragment());
                transaction.commit();
            }else{
                FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.mainContainer, new MainPageFragment()).commit();
            }
        }
    }

    private long time=0;
    @Override
    public void onBackPressed() {
        if(System.currentTimeMillis()-time>3000L){
            time=System.currentTimeMillis();
            Toast.makeText(getApplicationContext(), "'뒤로'버튼을 한번 더 누르면 종료합니다.", Toast.LENGTH_SHORT).show();
        }else{
            super.onBackPressed();
        }
    }

    public void login_google(){
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(getApplicationContext(), "로그인 실패. 다시 시도하세요.", Toast.LENGTH_SHORT).show();
                    }
                }).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, LOGIN_GOOGLE);
    }

    public void login_facebook(){
        //Developing...
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case LOGIN_GOOGLE:
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                if(result.isSuccess()){
                    firebaseAuthWithGoogle(result.getSignInAccount());
                    settings.setLoginType(settings.loginType_google);
                    if(!isExistKeyValue(result.getSignInAccount().getId())){
                        registerKeyValue();
                    }else{
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, new MainPageFragment()).commit();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"로그인 실패. 다시 시도하세요.", Toast.LENGTH_SHORT).show();

                    if(mGoogleApiClient!=null && mGoogleApiClient.isConnected()){
                        mGoogleApiClient.stopAutoManage(this);
                        mGoogleApiClient.disconnect();
                    }

                  }
                break;

            case LOGIN_FACEBOOK:
                //Dev...
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if(mGoogleApiClient!=null && mGoogleApiClient.isConnected()){
            mGoogleApiClient.stopAutoManage(this);
            mGoogleApiClient.disconnect();
        }
        super.onDestroy();
    }

    private void registerKeyValue(){
        final DialogMaker maker=new DialogMaker();
        maker.setValue("Key값을 등록합니다.", "등록", "", new DialogMaker.Callback() {
            @Override
            public void callbackMethod() {
                getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, new KeySettingFragment()).commit();
                maker.dismiss();
            }
        }, null, getLayoutInflater().inflate(R.layout.key_setting_explain, null));
        maker.setCancelable(false);
        maker.show(getSupportFragmentManager(), "");
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("Firebase Auth", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!=null){
                    //User is login
                    settings.setIsFirst(false);
                }else{
                    //User is logout
                    settings.setIsFirst(true);
                }
            }
        });
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("Firebase Auth", "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("Firebase Auth", "signInWithCredential", task.getException());
                            finish();
                            Toast.makeText(getApplicationContext(), "인증 실패. 앱을 종료합니다.",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mGoogleApiClient!=null){
            mGoogleApiClient.connect();
        }
    }

    private DatabaseReference getDatabaseReference(){
        return FirebaseDatabase.getInstance().getReference();
    }

    public boolean isExistKeyValue(String id){
        DatabaseReference saved=getDatabaseReference().child("/user/"+ id+"/key");
        final Checker checker=new Checker();
        saved.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    checker.check=true;
                }else{
                    checker.check=false;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return checker.check;
    }
    //This class is only used in isExistKeyValue().
    private class Checker{
        boolean check=false;
    }
}
