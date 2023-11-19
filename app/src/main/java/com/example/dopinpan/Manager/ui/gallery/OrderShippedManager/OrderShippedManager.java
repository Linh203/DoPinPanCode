package com.example.dopinpan.Manager.ui.gallery.OrderShippedManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.dopinpan.Common.Common;
import com.example.dopinpan.Manager.ui.gallery.OrderHistoryManager.OderHistoryViewHolderManager;
import com.example.dopinpan.Manager.ui.gallery.OrderPlacedManager.OrderPlacedManager;
import com.example.dopinpan.Model.Request;
import com.example.dopinpan.OrderDetail;
import com.example.dopinpan.R;
import com.example.dopinpan.ui.gallery.OrderShipped.OderShippedViewHolder;
import com.example.dopinpan.ui.gallery.OrderShipped.OrderShipped;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

public class OrderShippedManager extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference requests;
    private FirebaseRecyclerAdapter<Request, OderShippedViewHolderManager> adapter;

    private RecyclerView recyclerView;

    private ImageView btnBack;
    private RecyclerView.LayoutManager layoutManager;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_shipped_manager);

        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests Shipped");

        btnBack=findViewById(R.id.btn_back13);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        recyclerView=findViewById(R.id.listodershippedmanager);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        loadOders();
    }
    private void loadOders() {

        String phone = Common.currentUser.getPhone().toString();
        adapter = new FirebaseRecyclerAdapter<Request, OderShippedViewHolderManager>(Request.class, R.layout.oder_shipped_layout_manager, OderShippedViewHolderManager.class, requests) {
            @Override
            protected void populateViewHolder(OderShippedViewHolderManager oderViewHolder, Request request, int i) {
                oderViewHolder.txtOderId.setText(adapter.getRef(i).getKey());
                oderViewHolder.txtOderStatus.setText(Common.convertCodeToStatus(request.getStatus()));
                oderViewHolder.txtOderPhone.setText(request.getPhone());
                oderViewHolder.txtOderAddress.setText(request.getAddress());
                oderViewHolder.txtOderTotal.setText("$"+request.getTotal());
                oderViewHolder.txtDateTime.setText(request.getStartAt());

                oderViewHolder.btnDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(OrderShippedManager.this, OrderDetail.class);
                        Common.currentRequest = request;
                        intent.putExtra("OrderId", adapter.getRef(i).getKey());
                        startActivity(intent);
                    }
                });
            }
        };
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(new ScaleInAnimationAdapter(adapter));
    }

}