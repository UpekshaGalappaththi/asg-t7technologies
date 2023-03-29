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



//    public CardAdapter(List<Cart> cartItems) {
//        this.cartItems = cartItems;
//    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CardItem card = cardList.get(position);
        // Bind data to the UI elements in the ViewHolder
        // For example:
        // holder.imageView.setImageURI(item.getImageUrl());

        String cardDescription = card.getDescription();
        String cardSummary = cardDescription.substring(0, 20);
        holder.name.setText("Name : " + card.getName());
        holder.price.setText("Price : " + card.getPrice());
        holder.description.setText("Description : " + cardSummary + "...");
        holder.manufacturer.setText("Manufacturer : " + card.getManufacturer());
        // Inflate the layout containing the CardView
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                // Handle button click event here
                // Handle the "Add to Cart" button click event
                // For example, you can add the item to a shopping cart or show a message
                // You can get the data from the CardView's text views, e.g.:
//                String name = ((TextView) view.findViewById(R.id.name)).getText().toString();
//                String price = ((TextView) view.findViewById(R.id.price)).getText().toString();
                String name = card.getName();
                String price = card.getPrice();

                nameArray.add(name);
                priceArray.add(price);
                System.out.println("Name : " + nameArray); // Print the current cartItems list
                System.out.println("Price : " + priceArray); // Print the current cartItems list

//                ArrayList <String> cartNames = getCartNames();
//                System.out.println(cartNames);

//                for (int i = 0; i < nameArray.size() && i < priceArray.size(); i++) {
//                    System.out.println("Name" + nameArray.get(i) + "Price" + priceArray.get(i));
//                }

//
//                System.out.println("Hash code of list1: " + cartItems.hashCode());
//                System.out.println("Memory ID of list: " + System.identityHashCode(cartItems));
//
//
//                for (Cart pair : cartItems) {
//                    System.out.println("Name: " + pair.getName() + ", Price: " + pair.getPrice());

//                }


//                // Add elements to the list
//                list.add(card);
//                System.out.println(list);
                // Print the contents of the list
//                System.out.println(name);
//                TextView textView = findViewById(R.id.textView3);
//                textView.setText(list.toString());
                Toast.makeText(view.getContext(), "Added  to cart for ", Toast.LENGTH_SHORT).show();
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

