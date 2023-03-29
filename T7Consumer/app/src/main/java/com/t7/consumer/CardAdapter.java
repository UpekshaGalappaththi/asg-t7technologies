package com.t7.consumer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    private List<CardItem> cardList;
//    private ArrayList<Cart> cartItems = new ArrayList<>();
    public static ArrayList<String> nameArray = new ArrayList<>();
    public static ArrayList<String> priceArray = new ArrayList<>();



    public CardAdapter(List<CardItem> cardList) {
        this.cardList = cardList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CardItem card = cardList.get(position);

        String cardDescription = card.getDescription();
        String cardSummary = cardDescription.substring(0, 40);
        holder.name.setText("Name : " + card.getName());
        holder.price.setText("Price USD: " + card.getPrice());
        holder.description.setText("Description : " + cardSummary + "...");
        holder.manufacturer.setText("Manufacturer : " + card.getManufacturer());
        // Inflate the layout containing the CardView
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String name = card.getName();
                String price = card.getPrice();

                nameArray.add(name);
                priceArray.add(price);
                System.out.println("Name : " + nameArray); // Print the current cartItems list
                System.out.println("Price : " + priceArray); // Print the current cartItems list


                Toast.makeText(view.getContext(), "Added to cart: " + name, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView price;
        public TextView description;
        public TextView manufacturer;

        public Button button;


        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            description = itemView.findViewById(R.id.description);
            manufacturer = itemView.findViewById(R.id.manufacturer);
            button = itemView.findViewById(R.id.addToCartButton);
        }
    }
    public  static ArrayList<String> getCartNames() {

        return nameArray;
    }

    public static ArrayList<String> getCartPrices() {

        return priceArray;
    }



    public static void clearNameArray() {
        nameArray.clear(); // create a new empty array
    }
    public static void clearPriceArray() {
        priceArray.clear(); // create a new empty array
    }
}

