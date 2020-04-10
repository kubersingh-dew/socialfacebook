package com.omni.dew.socialfacebook;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.internal.CallbackManagerImpl;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.gson.Gson;
import com.omni.dew.basemodule.BaseLogin;
import com.omni.dew.basemodule.LoginResponse;

import java.util.Arrays;

public class FaceBookLogin implements BaseLogin {

    Activity activity;
    LoginResponse loginResponse;
    CallbackManager callbackManager;
    int code;
    private static final String EMAIL = "email";
    private static final String PROFILE = "public_profile";

    @Override
    public void init(Activity activity, LoginResponse loginResponse){
        callbackManager = CallbackManager.Factory.create();
        this.activity = activity;
        this.loginResponse = loginResponse;
    }

    @Override
    public int login(){
        LoginManager.getInstance().logInWithReadPermissions(activity, Arrays.asList(EMAIL, PROFILE));
        code = CallbackManagerImpl.RequestCodeOffset.Login.toRequestCode();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                loginResponse.sendResponse(new Gson().toJson(loginResult));
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException exception) {
            }
        });
        return code;
    }

    @Override
    public void onResponse(Intent data) {
        callbackManager.onActivityResult(code, Activity.RESULT_OK, data);
    }

    @Override
    public boolean isLogin() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return (accessToken != null && !accessToken.isExpired());
    }
}
