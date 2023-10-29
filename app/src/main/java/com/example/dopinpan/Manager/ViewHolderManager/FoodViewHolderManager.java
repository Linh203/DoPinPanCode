package com.example.dopinpan.Manager.ViewHolderManager;

import android.view.ContextMenu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dopinpan.Common.Common;
import com.example.dopinpan.Interface.ItemClickListener;
import com.example.dopinpan.R;

public class FoodViewHolderManager extends RecyclerView.ViewHolder{
    public TextView txtFoodname,txtpricefood;
    public ImageView imageViewfood;

    public Button btnUpdateFood,btnRemoveFood;


    public FoodViewHolderManager(@NonNull View itemView) {
        super(itemView);
        txtFoodname=itemView.findViewById(R.id.food_name);
        imageViewfood=itemView.findViewById(R.id.food_image);
        txtpricefood=itemView.findViewById(R.id.price_name);

        btnRemoveFood=itemView.findViewById(R.id.btn_removeFood);
        btnUpdateFood=itemView.findViewById(R.id.btn_updateFood);


    }




}
