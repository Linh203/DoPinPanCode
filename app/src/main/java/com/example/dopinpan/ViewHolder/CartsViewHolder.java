package com.example.dopinpan.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dopinpan.R;

public class CartsViewHolder extends RecyclerView.ViewHolder {

    public TextView txtcartName,txtcartPrice;
    public ImageView imageView,btnDeleteCart;
    public CartsViewHolder(@NonNull View itemView) {
        super(itemView);
        txtcartName=itemView.findViewById(R.id.cart_item_name);
        txtcartPrice=itemView.findViewById(R.id.cart_item_price);
        imageView=itemView.findViewById(R.id.cart_item_count);
        btnDeleteCart=itemView.findViewById(R.id.btn_deleteCart);
    }

}
