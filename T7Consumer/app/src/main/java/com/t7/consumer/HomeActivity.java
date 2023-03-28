package com.t7.consumer;

import android.app.PendingIntent;
import android.content.Context;
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

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
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


        // Inflate the layout containing the CardView
        View cardViewLayout = getLayoutInflater().inflate(R.layout.card_view, null);

        // Find the CardView within the inflated layout
        CardView cardView = cardViewLayout.findViewById(R.id.cardView);

        Button addToCartButton = cardViewLayout.findViewById(R.id.addToCartButton);
        System.out.println("check");

        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the "Add to Cart" button click event
                // For example, you can add the item to a shopping cart or show a message
                // You can get the data from the CardView's text views, e.g.:
//                String name = ((TextView) cardView.findViewById(R.id.name)).getText().toString();
//                String price = ((TextView) cardView.findViewById(R.id.price)).getText().toString();
//                // Add elements to the list
//                list.add(name);
                System.out.println("clicked");
                // Print the contents of the list
//                System.out.println(list);
//                TextView textView = findViewById(R.id.textView3);
//                textView.setText(list.toString());
//                Toast.makeText(MainActivity.this, "Added " + name + " to cart for " + price, Toast.LENGTH_SHORT).show();
            }
        });
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