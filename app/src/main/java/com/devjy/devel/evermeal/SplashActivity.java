package com.devjy.devel.evermeal;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.devjy.devel.evermeal.Auth.Gender;
import com.devjy.devel.evermeal.Auth.UserInfo;
import com.devjy.devel.evermeal.Auth.UserManager;
import com.devjy.devel.evermeal.Extend.AppExActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class SplashActivity extends AppExActivity
{
    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_splash);

        callbackManager = CallbackManager.Factory.create();

        final LoginButton loginButton = (LoginButton)findViewById(R.id.login_button);

        assert loginButton != null;

        loginButton.setVisibility(View.GONE);
        loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email", "user_birthday"));

        loginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult)
                    {
                        LoadUserProfile(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel()
                    {
                        // not called
                    }

                    @Override
                    public void onError(FacebookException e)
                    {
                        // not called
                    }
                });
            }
        });

        Delay(1500, new Runnable() {
            @Override
            public void run() {
                // TODO: 계정 연동 체크 및 데이터 로드

                if (!UserManager.HasUserData())
                {
                    ObjectAnimator animation = ObjectAnimator.ofFloat(loginButton, LoginButton.TRANSLATION_Y, -100, 0);
                    ObjectAnimator animation2 = ObjectAnimator.ofFloat(loginButton, LoginButton.ALPHA, 0.0f, 1.0f);

                    animation.setDuration(300);
                    animation2.setDuration(300);

                    animation.setInterpolator(new AccelerateDecelerateInterpolator());
                    animation2.setInterpolator(new AccelerateDecelerateInterpolator());

                    animation.start();
                    animation2.start();

                    loginButton.setVisibility(View.VISIBLE);
                }
                else
                {
                    finish();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void LoadUserProfile(AccessToken token)
    {
        GraphRequest request = GraphRequest.newMeRequest(
                token,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try
                        {
                            JSONObject json = response.getJSONObject();

                            UserManager.SetUserInfo(
                                    new UserInfo(
                                            json.getString("id"),
                                            json.getString("name"),
                                            json.getString("email"),
                                            Gender.fromString(json.getString("gender"))
                                    ));

                            finish();
                        }
                        catch (JSONException ex)
                        {
                            ex.printStackTrace();
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender,birthday");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    public void finish()
    {
        setResult(200, getIntent());

        super.finish();
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
    }

    private boolean isLoggedIn()
    {
        return AccessToken.getCurrentAccessToken() != null;
    }
}
