package com.example.dopinpan.Manager.ui.gallery.OrderPlacedManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.dopinpan.Common.Common;
import com.example.dopinpan.Manager.ui.gallery.OrderHistoryManager.OderHistoryViewHolderManager;
import com.example.dopinpan.Manager.ui.gallery.OrderHistoryManager.OrderHistoryManager;
import com.example.dopinpan.Model.Request;
import com.example.dopinpan.OrderDetail;
import com.example.dopinpan.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jaredrummler.materialspinner.MaterialSpinner;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

public class OrderPlacedManager extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference requests;
    private FirebaseRecyclerAdapter<Request, OderPlacedViewHolderManager> adapter;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_placed_manager2);

        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests Placed");

        recyclerView=findViewById(R.id.listoderplacedmanager);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        loadOders();

    }
    private void loadOders() {

        String phone = Common.currentUser.getPhone().toString();
        adapter = new FirebaseRecyclerAdapter<Request, OderPlacedViewHolderManager>(Request.class, R.layout.oder_placed_layout_manager, OderPlacedViewHolderManager.class, requests) {
            @Override
            protected void populateViewHolder(OderPlacedViewHolderManager oderViewHolder, Request request, int i) {
                oderViewHolder.txtOderId.setText(adapter.getRef(i).getKey());
                oderViewHolder.txtOderStatus.setText(Common.convertCodeToStatus(request.getStatus()));
                oderViewHolder.txtOderPhone.setText(request.getPhone());
                oderViewHolder.txtOderAddress.setText(request.getAddress());
                oderViewHolder.txtOderTotal.setText("$"+request.getTotal());
                oderViewHolder.txtDateTime.setText(request.getStartAt());

                oderViewHolder.btnDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(OrderPlacedManager.this, OrderDetail.class);
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