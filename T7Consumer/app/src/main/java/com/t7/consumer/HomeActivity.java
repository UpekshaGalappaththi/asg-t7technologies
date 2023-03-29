package com.t7.consumer;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.Nullable;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.t7.consumer.databinding.ActivityHomeBinding;

import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationResponse;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceConfiguration;
import net.openid.appauth.ResponseTypeValues;
import net.openid.appauth.TokenResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HomeActivity extends AppCompatActivity {

    private AuthorizationService authorizationService;
    private AuthorizationRequest authorizationRequest;
    private AuthorizationServiceConfiguration authorizationServiceConfiguration;
    private String authStateJson;
    private AuthState authState;

    private static final String CLIENT_ID = "rGdUjiBMxSaLJpPGefj96ULJxdEa";
    private static final String REDIRECT_URI = "com.t7.consumer://callback";
    private static final String SCOPES = "openid profile internal_login loyalty";
    private static final String SHARED_PREFERENCES_NAME = "t7_my_prefs";
    private static final String AUTH_STATE = "auth_state";
    private static final String TAG = "HomeActivity";
    private static final String TOKEN_ENDPOINT = "https://api.asgardeo.io/t/t7technologies/oauth2/token";
    private static final String AUTHORIZATION_ENDPOINT = "https://api.asgardeo.io/t/t7technologies/oauth2/authorize";
    private static final String LOGOUT_ENDPOINT = "https://api.asgardeo.io/t/t7technologies/oidc/logout";
    private static final String MYACCOUNT_ENDPOINT = "https://myaccount.asgardeo.io/t/t7technologies";
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomeBinding binding;
    private RecyclerView recyclerView;
    private CardAdapter adapter;

    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadAuthState();


        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarHome.toolbar);
        binding.appBarHome.fab.setOnClickListener(view -> {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

            counter++;
            binding.appBarHome.fabCounter.setText(String.valueOf(counter));
        });

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);



        navigationView.setNavigationItemSelectedListener(menuItem -> setNavigationItemSelection(menuItem.getItemId()));


        updateUI();
        initAuth();
        handleAuthCallback(getIntent());
//        Button btnProfile = findViewById(R.id.btnProfile);
//        btnProfile.setOnClickListener(v -> openProfile(v.getContext()));

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        String json = null;
        try {
            json = getJsonResponse();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Gson gson = new Gson();
        CardItem[] products = gson.fromJson(json, CardItem[].class);
        List<CardItem> cardList = Arrays.asList(products);
        adapter = new CardAdapter(cardList);
        recyclerView.setAdapter(adapter);

        // in the activity's Java file

        Button cartButton = findViewById(R.id.viewCartButton);

        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // actions to take when the button is clicked
//                ArrayList<String> cartNames = CardAdapter.getCartNames();
//                ArrayList<String> cartPrices = CardAdapter.getCartPrices();
//
//                System.out.println(cartNames);
//                System.out.println(cartPrices);

//                Intent intent = new Intent(HomeActivity.this, CartView.class);
//                startActivity(intent);
//
//            }
                ArrayList<String> cartNames = CardAdapter.getCartNames();
                ArrayList<String> cartPrices = CardAdapter.getCartPrices();

                    AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Name\t\tPrice\n");

                for (int i = 0; i < cartNames.size(); i++) {
                    stringBuilder.append(cartNames.get(i) + "\t\t" + cartPrices.get(i) + "\n");
                }
                builder.setMessage(stringBuilder.toString())
                            .setCancelable(false);
//                            .setPositiveButton("Purchase", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    // do something when the OK button is clicked
//                                    CardAdapter.clearNameArray();
//                                    CardAdapter.clearPriceArray();
//
////                                                    ArrayList<String> cartNames = CardAdapter.getCartNames();
////                ArrayList<String> cartPrices = CardAdapter.getCartPrices();
////
////                System.out.println(cartNames);
////                System.out.println(cartPrices);
//                                    Snackbar.make(v, "Replace with your own action", Snackbar.LENGTH_INDEFINITE)
//                                            .setAction("Action", null).show();
//                                }
//
//                            });
                builder.setPositiveButton("Purchase", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // do something when the OK button is clicked
                        CardAdapter.clearNameArray();
                        CardAdapter.clearPriceArray();
                        try {
                            purchaseOnClick(v);
                        } catch (JSONException | IOException e) {
                            throw new RuntimeException(e);
                        }

//                                                    ArrayList<String> cartNames = CardAdapter.getCartNames();
//                ArrayList<String> cartPrices = CardAdapter.getCartPrices();
//
//                System.out.println(cartNames);
//                System.out.println(cartPrices);
                        Snackbar.make(v, "Thank you for your purchase", Snackbar.LENGTH_INDEFINITE)
                                .setAction("Action", null).show();

                    }

                });

                builder.setNegativeButton("Clear Cart", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // do something when the OK button is clicked
                        CardAdapter.clearNameArray();
                        CardAdapter.clearPriceArray();

//                                                    ArrayList<String> cartNames = CardAdapter.getCartNames();
//                ArrayList<String> cartPrices = CardAdapter.getCartPrices();
//
//                System.out.println(cartNames);
//                System.out.println(cartPrices);
                        Snackbar.make(v, "Cleared Cart", Snackbar.LENGTH_INDEFINITE)
                                .setAction("Action", null).show();
                    }

                });

                builder.setNeutralButton("Back", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // do something when the OK button is clicked


//                                                    ArrayList<String> cartNames = CardAdapter.getCartNames();
//                ArrayList<String> cartPrices = CardAdapter.getCartPrices();
//
//                System.out.println(cartNames);
//                System.out.println(cartPrices);
//                        Snackbar.make(v, "Cleared Cart", Snackbar.LENGTH_INDEFINITE)
//                                .setAction("Action", null).show();
                    }

                });


                    AlertDialog alert = builder.create();
                    alert.show();
                }
        });

    }


    private void purchaseOnClick(View v) throws JSONException, IOException {

        if (authState == null || !authState.isAuthorized()) {
//            initAuthzRequest();
                            Snackbar.make(v, "Please login to purchase", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
        } else {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        JSONObject json = new JSONObject();

        int totalPrice = 0;
            for (int i = 0; i < CardAdapter.priceArray.size(); i++) {
                totalPrice += Integer.parseInt(CardAdapter.priceArray.get(i));
            }
        json.put("total", totalPrice);
        String body = json.toString();
//        TextView textView = findViewById(R.id.textView3);
//        textView.setText(body);
        String url = "https://331gmgq1ba.execute-api.eu-north-1.amazonaws.com/purchase";
        postPurchase(url,body);
//        System.out.println(response);
//
//        TextView textView2 = findViewById(R.id.textView2);
//        textView2.setText(response);


        }
    }


    Void postPurchase(String url, String json) throws IOException {

        authState.performActionWithFreshTokens(authorizationService, new AuthState.AuthStateAction() {
            @Override
            public void execute(@Nullable String accessToken, @Nullable String idToken, @Nullable AuthorizationException exception) {
                if (exception != null) {
                    // handle error
                    throw new RuntimeException(exception);
                } else {
                    // use access token
                    // call url with access token handle exception
                    OkHttpClient client = new OkHttpClient();
                    RequestBody body = RequestBody.create(json, MediaType.get("application/json; charset=utf-8"));
                    Request request = new Request.Builder()
                            .url(url)
                            .addHeader("Authorization", "Bearer " + accessToken)
                            .post(body)
                            .build();
                    try (Response response = client.newCall(request).execute()) {
                        System.out.println(response.body().string());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

        });
        return null;
    }

    private String getJsonResponse() throws IOException {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        OkHttpClient client = new OkHttpClient();

        String url = "https://asg-t7technologies.fly.dev/devices";
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }

    }


    private boolean setNavigationItemSelection(int id) {


        if (id == R.id.btnLogin) {
            loginOnClick();
            //add toast
            Toast.makeText(this, "Login", Toast.LENGTH_SHORT).show();


        } else if (id == R.id.btnProfile) {
            openProfile(this);
        }
        DrawerLayout drawer1 = findViewById(R.id.drawer_layout);
        drawer1.closeDrawer(GravityCompat.START);
        return true;
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);


        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void loginOnClick() {

        if (authState == null || !authState.isAuthorized()) {
            initAuthzRequest();
        } else {
            handleLogout();
        }
    }

    private void initAuthzRequest() {

        authorizationService.performAuthorizationRequest(
                authorizationRequest,
                PendingIntent.getActivity(this, 0, new Intent(this.getIntent()), PendingIntent.FLAG_MUTABLE),
                PendingIntent.getActivity(this, 0, new Intent(this.getIntent()), PendingIntent.FLAG_MUTABLE)
        );
    }

    private void saveAuthState() {

        authStateJson = authState == null ? null : authState.jsonSerializeString();
        SharedPreferences preferences = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        preferences.edit().putString(AUTH_STATE, authStateJson).apply();
    }

    private void loadAuthState() {

        SharedPreferences preferences = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        authStateJson = preferences.getString(AUTH_STATE, null);
        if (authStateJson != null) {
            try {
                authState = AuthState.jsonDeserialize(authStateJson);
            } catch (JSONException e) {
                Log.d(TAG, "Error loading auth state. " + e.getMessage());
                Toast.makeText(this, "Error loading auth state. " + e.getMessage(), Toast.LENGTH_SHORT).show();
                throw new RuntimeException(e);
            }
        }
    }

    private void updateLoginButton() {

        NavigationView navigationView = binding.navView;
        Menu menu = navigationView.getMenu();
        MenuItem loginMenuItem = menu.findItem(R.id.btnLogin);

        if (authState == null || !authState.isAuthorized()) {
            loginMenuItem.setTitle("Login/Signup");
        } else {
            loginMenuItem.setTitle("Logout");
        }
    }

    private void updateProfileButton() {

        NavigationView navigationView = binding.navView;
        Menu menu = navigationView.getMenu();
        MenuItem profileMenuItem = menu.findItem(R.id.btnProfile);

        if (authState == null || !authState.isAuthorized()) {
            profileMenuItem.setVisible(false);
        } else {
            profileMenuItem.setVisible(true);
        }
    }

    private void handleCodeExchangeResponse(TokenResponse tokenResponse,
                                            AuthorizationException authException) {

        authState.update(tokenResponse, authException);
        saveAuthState();
        updateUI();
    }

    private void updateUI() {
        updateLoginButton();
        updateProfileButton();
        updateBasicUserInfoInHeader();
    }

    private void updateBasicUserInfoInHeader() {
        NavigationView navigationView = binding.navView;
        View headerView = navigationView.getHeaderView(0);
        TextView textViewUsername = headerView.findViewById(R.id.tvUsername);
        TextView textViewFullName = headerView.findViewById(R.id.tvFullName);
        TextView textViewTierData = headerView.findViewById(R.id.tvTierData);
        if (authState != null && authState.isAuthorized()) {

            textViewUsername.setText(authState.getParsedIdToken().additionalClaims.get("username").toString());

            //set given_name and family_name from idtoken to tvFullName
            textViewFullName.setText(authState.getParsedIdToken().additionalClaims.get("given_name").toString()
                    + " " + authState.getParsedIdToken().additionalClaims.get("family_name").toString());

            ImageView imageView =  headerView.findViewById(R.id.ivProfilePic);
            Glide.with(this)
                    .load(authState.getParsedIdToken().additionalClaims.get("profile").toString())
                    .into(imageView);

            textViewTierData.setText("[ Tier:" + authState.getParsedIdToken().additionalClaims.get("tier").toString()
                    + " (Points:" + authState.getParsedIdToken().additionalClaims.get("points").toString() + ") ]");

            // make visible TextViews textViewUsername, textViewFullName, textViewTierData
            textViewUsername.setVisibility(View.VISIBLE);
            textViewFullName.setVisibility(View.VISIBLE);
            textViewTierData.setVisibility(View.VISIBLE);

        } else {
            // hide TextViews textViewUsername, textViewFullName, textViewTierData
            textViewUsername.setVisibility(View.GONE);
            textViewFullName.setVisibility(View.GONE);
            textViewTierData.setVisibility(View.GONE);
        }
    }

    private void promptLogin() {

        Toast.makeText(this, "Please login to continue", Toast.LENGTH_SHORT).show();
    }

    private void initAuth() {

        authorizationServiceConfiguration = new AuthorizationServiceConfiguration(
                Uri.parse(AUTHORIZATION_ENDPOINT),
                Uri.parse(TOKEN_ENDPOINT)
        );
        authorizationService = new AuthorizationService(this);
        authorizationRequest = new AuthorizationRequest.Builder(
                authorizationServiceConfiguration,
                CLIENT_ID,
                ResponseTypeValues.CODE,
                Uri.parse(REDIRECT_URI))
                .setScope(SCOPES)
                .build();
    }

    private void handleAuthCallback(Intent intent) {

        AuthorizationResponse response = AuthorizationResponse.fromIntent(intent);
        AuthorizationException error = AuthorizationException.fromIntent(intent);
        if (response != null) {
            authState = new AuthState(response, error);
            authorizationService.performTokenRequest(response.createTokenExchangeRequest(), this::handleCodeExchangeResponse);

        } else if (error != null){
            // Authorization failed
            Log.d(TAG, "Authorization failed. " + error.error);
            Toast.makeText(this, "Authorization failed: " + error.toString(), Toast.LENGTH_SHORT).show();
            throw new RuntimeException(error);
        }
    }

    private void handleLogout() {

        String url = LOGOUT_ENDPOINT + "?id_token_hint=" + authState.getIdToken() + "&post_logout_redirect_uri=" + REDIRECT_URI;
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        customTabsIntent.launchUrl(getApplicationContext(), Uri.parse(url));

        authState = null;
        saveAuthState();
        updateUI();
    }

    private void openProfile(Context context) {

        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        customTabsIntent.launchUrl(context, Uri.parse(MYACCOUNT_ENDPOINT));
    }
}