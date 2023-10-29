package com.example.dopinpan.ViewHolder;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.dopinpan.Common.Common;
import com.example.dopinpan.Interface.ItemClickListener;
import com.example.dopinpan.Model.Order;
import com.example.dopinpan.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener{

    public TextView txtcartName,txtcartPrice;
    public ImageView imageView;

    private ItemClickListener itemClickListener;



    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);

        txtcartName=itemView.findViewById(R.id.cart_item_name);
        txtcartPrice=itemView.findViewById(R.id.cart_item_price);
        imageView=itemView.findViewById(R.id.cart_item_count);

        itemView.setOnCreateContextMenuListener(this);


    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        contextMenu.setHeaderTitle("Select Action");
        contextMenu.add(0,0,getAdapterPosition(), Common.DELETE);
    }
}
public class CartAdapter extends RecyclerView.Adapter<CartViewHolder>{

    private List<Order> list=new ArrayList<>();
    private Context context;

    public CartAdapter(List<Order> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View itemView=inflater.inflate(R.layout.cart_layout,parent,false);
        return new CartViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        TextDrawable drawable=TextDrawable.builder().buildRound(""+list.get(position).getQuantity(), Color.RED);
        holder.imageView.setImageDrawable(drawable);

        Locale locale=new Locale("en","US");
        NumberFormat format=NumberFormat.getCurrencyInstance(locale);
        int price=(Integer.parseInt(list.get(position).getPrice()))*(Integer.parseInt(list.get(position).getQuantity()));
        holder.txtcartPrice.setText(format.format(price));
        holder.txtcartName.setText(list.get(position).getProductName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
