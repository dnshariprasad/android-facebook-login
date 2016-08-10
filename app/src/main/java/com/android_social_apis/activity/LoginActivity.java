package com.android_social_apis.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android_social_apis.R;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {
    private FacebookCallback facebookCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            Log.d(TAG, "onSuccess: User ID:  " + loginResult.getAccessToken().getUserId() + "\n" + "Auth Token: " + loginResult.getAccessToken().getToken());
            GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), graphRequest);
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender,birthday");
            request.setParameters(parameters);
            request.executeAsync();
        }

        @Override
        public void onCancel() {
            Log.d(TAG, "onCancel: Login attempt cancelled.");
        }

        @Override
        public void onError(FacebookException e) {
            Log.d(TAG, "onError: Login attempt failed.");
        }
    };
    private GraphRequest.GraphJSONObjectCallback graphRequest = new GraphRequest.GraphJSONObjectCallback() {
        @Override
        public void onCompleted(JSONObject object, GraphResponse response) {
            Log.v("LoginActivity", response.toString());
            // Application code
            try {
                String email = object.getString("email");
                String birthday = object.getString("birthday"); // 01/31/1980 format
                MainActivity.start(LoginActivity.this);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    };
    private AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {

        }
    };

    private ProfileTracker profileTracker = new ProfileTracker() {
        @Override
        protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
            displayMessage(newProfile);
        }
    };
    private static final String TAG = "LoginActivity";
    private CallbackManager callbackManager;
    private LoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        //Facebook
        loginButton = (LoginButton) findViewById(R.id.login_button);
        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday", "user_friends"));
        loginButton.registerCallback(callbackManager, facebookCallback);


    }

    @Override
    public void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
        displayMessage(profile);
    }

    @Override
    public void onStop() {
        super.onStop();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private void displayMessage(Profile profile) {
        if (profile != null) {
//            textView.setText(profile.getName());
        }
    }
}
