package com.example.dopinpan.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dopinpan.Interface.ItemClickListener;
import com.example.dopinpan.R;

public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtFoodname,txtpricefood;
    public ImageView imageViewfood,fav_img;
    private ItemClickListener itemClickListener;

    public FoodViewHolder(@NonNull View itemView) {
        super(itemView);
        txtFoodname=itemView.findViewById(R.id.food_name);
        imageViewfood=itemView.findViewById(R.id.food_image);
        txtpricefood=itemView.findViewById(R.id.price_name);
        fav_img=itemView.findViewById(R.id.fav);

        itemView.setOnClickListener(this);
    }


    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);

    }
}
