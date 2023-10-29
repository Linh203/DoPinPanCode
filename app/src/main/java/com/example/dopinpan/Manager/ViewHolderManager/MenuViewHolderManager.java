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

public class MenuViewHolderManager extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtMenuname;
    public ImageView imageView;
    public Button btnUpdateCat,btnRemoveCat;

    public ItemClickListener itemClickListener;

    public MenuViewHolderManager(@NonNull View itemView) {
        super(itemView);
        txtMenuname=itemView.findViewById(R.id.menu_name1);
        imageView=itemView.findViewById(R.id.menu_image1);
        btnRemoveCat=itemView.findViewById(R.id.btn_removeCat);
        btnUpdateCat=itemView.findViewById(R.id.btn_updateCat);

        imageView.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
