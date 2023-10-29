package com.example.dopinpan.ViewHolder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dopinpan.Model.Order;
import com.example.dopinpan.R;

import java.util.List;

class MyViewHolder extends RecyclerView.ViewHolder {

    public TextView name, quantity, price;


    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.product_name);
        quantity = itemView.findViewById(R.id.product_quantity);
        price = itemView.findViewById(R.id.product_price);



    }
}

public class OrderDeatilAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private List<Order> myOrders;

    public OrderDeatilAdapter(List<Order> myOrders) {
        this.myOrders = myOrders;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_detail_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Order order = myOrders.get(position);
        holder.name.setText(String.format("%s",order.getProductName()));
        holder.quantity.setText(String.format("%s",order.getQuantity()));
        holder.price.setText(String.format("$%s",order.getPrice()));


    }

    @Override
    public int getItemCount() {
        return myOrders.size();
    }
}
