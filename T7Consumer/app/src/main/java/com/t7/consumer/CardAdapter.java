package com.t7.consumer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    private List<CardItem> cardList;

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
        // Bind data to the UI elements in the ViewHolder
        // For example:
        // holder.imageView.setImageURI(item.getImageUrl());

        String cardDescription = card.getDescription();
        String cardSummary = cardDescription.substring(0, 20);
        holder.name.setText("Name : " + card.getName());
        holder.price.setText("Price : " + card.getPrice());
        holder.description.setText("Description : " + cardSummary + "...");
        holder.manufacturer.setText("Manufacturer : " + card.getManufacturer());
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


        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            description = itemView.findViewById(R.id.description);
            manufacturer = itemView.findViewById(R.id.manufacturer);
        }
    }
}
