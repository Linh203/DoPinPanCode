package com.example.dopinpan.ui.home;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dopinpan.Interface.ItemClickListener;
import com.example.dopinpan.R;

public class HomeViewModel extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtMenuname;
    public ImageView imageView;



    public HomeViewModel(@NonNull View itemView) {
        super(itemView);
        txtMenuname=itemView.findViewById(R.id.menu_name);
        imageView=itemView.findViewById(R.id.menu_image);

        itemView.setOnClickListener(this);
    }




    @Override
    public void onClick(View view) {

    }
}