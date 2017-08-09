package com.example.windows10.findmyphone;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

public class MainActivity extends AppCompatActivity {
    private final int PERMISSION_REQUEST_CODE=1234;
    private final int LOGIN_GOOGLE=1235;
    private final int LOGIN_FACEBOOK=1236;


    private Settings settings=null;

    private GoogleSignInOptions gso=null;
    private GoogleApiClient mGoogleApiClient=null;
    public FirebaseAuth firebaseAuth=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settings=Settings.getInstance(getApplicationContext());

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

        //Hide Label Bar
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().hide();

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
            final DialogMaker whyNeedPermissionExplain=new DialogMaker();
            final Activity thisActivty=this;
            DialogMaker.Callback ok=new DialogMaker.Callback() {
                @Override
                public void callbackMethod() {
                    String requestPermissionList[]=new String[]
                            {Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
                    ActivityCompat.requestPermissions(thisActivty, requestPermissionList, PERMISSION_REQUEST_CODE);
                    whyNeedPermissionExplain.dismiss();
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
        if(System.currentTimeMillis()-time<3000L){
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
                    getSupportFragmentManager().beginTransaction().replace(R.id.mainContainer, new MainPageFragment()).commit();
                }else{
                    final DialogMaker failToLogin=new DialogMaker();
                    DialogMaker.Callback ok=new DialogMaker.Callback() {
                        @Override
                        public void callbackMethod() {
                            failToLogin.dismiss();
                        }
                    };
                    failToLogin.setValue("로그인에 실패하였습니다. 다시 시도하세요.", "확인", "", ok, null);
                    failToLogin.show(getSupportFragmentManager(), "Fail to Login");
                }
                break;

            case LOGIN_FACEBOOK:

                break;
        }
    }

    @Override
    protected void onDestroy() {
        if(mGoogleApiClient!=null && mGoogleApiClient.isConnected()){
            mGoogleApiClient.stopAutoManage((FragmentActivity) getApplicationContext());
            mGoogleApiClient.disconnect();
        }
        super.onDestroy();
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("Firebase Auth", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
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
                            Toast.makeText(getApplicationContext(), "인증 실패. 다시 시도하세요.",Toast.LENGTH_SHORT).show();
                        }
                        // ...
                    }
                });
    }
}
