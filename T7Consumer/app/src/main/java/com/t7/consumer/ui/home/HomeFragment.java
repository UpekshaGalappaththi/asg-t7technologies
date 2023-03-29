package com.t7.consumer.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.t7.consumer.CardAdapter;
import com.t7.consumer.CardItem;
import com.t7.consumer.HomeActivity;
import com.t7.consumer.R;
import com.t7.consumer.databinding.FragmentHomeBinding;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private RecyclerView recyclerView;
    private CardAdapter adapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        final TextView textView = binding.;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);



        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));


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

//        Button cartButton = findViewById(R.id.viewCartButton);
//
//        cartButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
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
//                ArrayList<String> cartNames = CardAdapter.getCartNames();
//                ArrayList<String> cartPrices = CardAdapter.getCartPrices();
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
//                StringBuilder stringBuilder = new StringBuilder();
//                stringBuilder.append("Name\t\tPrice\n");
//
//                for (int i = 0; i < cartNames.size(); i++) {
//                    stringBuilder.append(cartNames.get(i) + "\t\t" + cartPrices.get(i) + "\n");
//                }
//                builder.setMessage(stringBuilder.toString())
//                        .setCancelable(false);
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
////                            });
//                builder.setPositiveButton("Purchase", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        // do something when the OK button is clicked
//                        CardAdapter.clearNameArray();
//                        CardAdapter.clearPriceArray();
//                        try {
//                            purchaseOnClick(v);
//                        } catch (JSONException | IOException e) {
//                            throw new RuntimeException(e);
//                        }

//                                                    ArrayList<String> cartNames = CardAdapter.getCartNames();
//                ArrayList<String> cartPrices = CardAdapter.getCartPrices();
//
//                System.out.println(cartNames);
//                System.out.println(cartPrices);
//                        Snackbar.make(v, "Thank you for your purchase", Snackbar.LENGTH_INDEFINITE)
//                                .setAction("Action", null).show();
//
//                    }
//
//                });
//
//                builder.setNegativeButton("Clear Cart", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        // do something when the OK button is clicked
//                        CardAdapter.clearNameArray();
//                        CardAdapter.clearPriceArray();

//                                                    ArrayList<String> cartNames = CardAdapter.getCartNames();
//                ArrayList<String> cartPrices = CardAdapter.getCartPrices();
//
//                System.out.println(cartNames);
//                System.out.println(cartPrices);
//                        Snackbar.make(v, "Cleared Cart", Snackbar.LENGTH_INDEFINITE)
//                                .setAction("Action", null).show();
//                    }
//
//                });
//
//                builder.setNeutralButton("Back", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        // do something when the OK button is clicked


//                                                    ArrayList<String> cartNames = CardAdapter.getCartNames();
//                ArrayList<String> cartPrices = CardAdapter.getCartPrices();
//
//                System.out.println(cartNames);
//                System.out.println(cartPrices);
//                        Snackbar.make(v, "Cleared Cart", Snackbar.LENGTH_INDEFINITE)
//                                .setAction("Action", null).show();
//                    }
//
//                });
//
//
//                AlertDialog alert = builder.create();
//                alert.show();
//            }
//        });
        return root;
//
//
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}