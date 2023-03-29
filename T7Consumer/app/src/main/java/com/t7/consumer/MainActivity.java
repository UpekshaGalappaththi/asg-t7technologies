package com.t7.consumer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationResponse;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceConfiguration;
import net.openid.appauth.ResponseTypeValues;
import net.openid.appauth.TokenRequest;
import net.openid.appauth.TokenResponse;

import org.json.JSONException;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

//    private AuthorizationService authorizationService;
//    private AuthorizationRequest authorizationRequest;
//    private AuthorizationServiceConfiguration authorizationServiceConfiguration;
//    private String authStateJson;
//    private AuthState authState;
//
//    private static final String CLIENT_ID = "4GhNFXK1xtHEVPwRhvujZyMI5kYa";
//    private static final String REDIRECT_URI = "com.t7.consumer://callback";
//    private static final String SCOPES = "openid profile internal_login";
//    private static final String SHARED_PREFERENCES_NAME = "t7_my_prefs";
//    private static final String AUTH_STATE = "auth_state";
//    private static final String TAG = "MainActivity";
//    private static final String TOKEN_ENDPOINT = "https://api.asgardeo.io/t/t7technologies/oauth2/token";
//    private static final String AUTHORIZATION_ENDPOINT = "https://api.asgardeo.io/t/t7technologies/oauth2/authorize";
//    private static final String LOGOUT_ENDPOINT = "https://api.asgardeo.io/t/t7technologies/oidc/logout";
//    private static final String MYACCOUNT_ENDPOINT = "https://myaccount.asgardeo.io/t/t7technologies";

    private OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        loadAuthState();
//        updateUI();
//        initAuth();
//        handleAuthCallback(getIntent());
//
//
//        Button btnLogin = findViewById(R.id.btnlogin);
//        btnLogin.setOnClickListener(v -> loginOnClick(v.getContext()));
//
//        Button btnProfile = findViewById(R.id.btnProfile);
//        btnProfile.setOnClickListener(v -> openProfile(v.getContext()));

    }

//    private void loginOnClick(Context context) {
//
//        if (authState == null || !authState.isAuthorized()) {
//            initAuthzRequest();
//        } else {
//            handleLogout(context);
////            authState = null;
////            saveAuthState();
////            updateUI();
//        }
//    }
//
//    private void initAuthzRequest() {
//
//        authorizationService.performAuthorizationRequest(
//                authorizationRequest,
//                PendingIntent.getActivity(this, 0, new Intent(this.getIntent()), PendingIntent.FLAG_MUTABLE),
//                PendingIntent.getActivity(this, 0, new Intent(this.getIntent()), PendingIntent.FLAG_MUTABLE)
//        );
//    }
//
//    private void saveAuthState() {
//
//        authStateJson = authState == null ? null : authState.jsonSerializeString();
//        SharedPreferences preferences = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
//        preferences.edit().putString(AUTH_STATE, authStateJson).apply();
//    }
//
//    private void loadAuthState() {
//
//        SharedPreferences preferences = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
//        authStateJson = preferences.getString(AUTH_STATE, null);
//        if (authStateJson != null) {
//            try {
//                authState = AuthState.jsonDeserialize(authStateJson);
//            } catch (JSONException e) {
//                Log.d(TAG, "Error loading auth state. " + e.getMessage());
//                Toast.makeText(this, "Error loading auth state. " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                throw new RuntimeException(e);
//            }
//        }
//        updateLoginButton();
//    }
//
//    //Update the button btnLogin text to reflect the current auth state
//    private void updateLoginButton() {
//        // get btnLogin button
//        Button btnLogin = findViewById(R.id.btnlogin);
//        if (authState == null || !authState.isAuthorized()) {
//            btnLogin.setText("Login/Signup");
//        } else {
//            btnLogin.setText("Logout");
//        }
//    }
//
//    private void handleCodeExchangeResponse(TokenResponse tokenResponse,
//                                            AuthorizationException authException) {
//
//        authState.update(tokenResponse, authException);
//        saveAuthState();
//        updateUI();
//    }
//
//    private void updateUI() {
//       updateLoginButton();
//    }
//
//    private void promptLogin() {
//
//        Toast.makeText(this, "Please login to continue", Toast.LENGTH_SHORT).show();
//    }
//
//    private void initAuth() {
//
//        authorizationServiceConfiguration = new AuthorizationServiceConfiguration(
//                Uri.parse(AUTHORIZATION_ENDPOINT),
//                Uri.parse(TOKEN_ENDPOINT)
//        );
//        authorizationService = new AuthorizationService(this);
//        authorizationRequest = new AuthorizationRequest.Builder(
//                authorizationServiceConfiguration,
//                CLIENT_ID,
//                ResponseTypeValues.CODE,
//                Uri.parse(REDIRECT_URI))
//                .setScope(SCOPES)
//                .build();
//    }
//
//    private void handleAuthCallback(Intent intent) {
//
//        AuthorizationResponse response = AuthorizationResponse.fromIntent(intent);
//        AuthorizationException error = AuthorizationException.fromIntent(intent);
//        if (response != null) {
//            authState = new AuthState(response, error);
//            authorizationService.performTokenRequest(response.createTokenExchangeRequest(), this::handleCodeExchangeResponse);
//
//        } else if (error != null){
//            // Authorization failed
//            Log.d(TAG, "Authorization failed. " + error.error);
//            Toast.makeText(this, "Authorization failed: " + error.toString(), Toast.LENGTH_SHORT).show();
//            throw new RuntimeException(error);
//        }
//    }
//
//    //handle logout
//    private void handleLogout(Context context) {
//
//        String url = LOGOUT_ENDPOINT + "?id_token_hint=" + authState.getIdToken() + "&post_logout_redirect_uri=" + REDIRECT_URI;
//        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
//        CustomTabsIntent customTabsIntent = builder.build();
//        customTabsIntent.intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
//                Intent.FLAG_ACTIVITY_NEW_TASK
//                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        customTabsIntent.launchUrl(context, Uri.parse(url));
////        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
////                .permitAll().build();
////        StrictMode.setThreadPolicy(policy);
////        Request request = new Request.Builder()
////                .url(url)
////                .build();
////
////        try (Response response = client.newCall(request).execute()) {
////            Log.d(TAG, response.body().string());
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
//        authState = null;
//        saveAuthState();
//        updateUI();
//    }
//
//    private void openProfile(Context context) {
//
//        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
//        CustomTabsIntent customTabsIntent = builder.build();
//        customTabsIntent.intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
//                Intent.FLAG_ACTIVITY_NEW_TASK
//                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        customTabsIntent.launchUrl(context, Uri.parse(MYACCOUNT_ENDPOINT));
//    }
}