package com.example.dopinpan.ui.gallery.OrderRemoved;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.dopinpan.Common.Common;
import com.example.dopinpan.Model.Request;
import com.example.dopinpan.OrderDetail;
import com.example.dopinpan.R;
import com.example.dopinpan.ui.gallery.OrderShipped.OderShippedViewHolder;
import com.example.dopinpan.ui.gallery.OrderShipped.OrderShipped;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

public class OrderRemoved extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference requests;

    private RecyclerView recyclerView;
    private ImageView btnBack;

    private RecyclerView.LayoutManager layoutManager;

    private FirebaseRecyclerAdapter<Request, OderRemovedViewHolder> adapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_removed);

        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests Removed");

        btnBack = findViewById(R.id.btn_back10);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.listoderremoved);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        if (Common.isConectedInternet(this)) {
            loadOders();
        } else {
            Toast.makeText(this, "Vui Lòng Kết Nối Mạng !!", Toast.LENGTH_SHORT).show();
        }


    }

    private void loadOders() {

        String phone = Common.currentUser.getPhone().toString();
        adapter = new FirebaseRecyclerAdapter<Request, OderRemovedViewHolder>(Request.class, R.layout.oder_removed_layout, OderRemovedViewHolder.class, requests.orderByChild("phone").equalTo(phone)) {
            @Override
            protected void populateViewHolder(OderRemovedViewHolder oderViewHolder, Request request, int i) {
                oderViewHolder.txtOderId.setText(adapter.getRef(i).getKey());
                oderViewHolder.txtOderStatus.setText(Common.convertCodeToStatus(request.getStatus()));
                oderViewHolder.txtOderPhone.setText(request.getPhone());
                oderViewHolder.txtOderAddress.setText(request.getAddress());
                oderViewHolder.txtOderTotal.setText("$" + request.getTotal());
                oderViewHolder.txtDateTime.setText(request.getStartAt());
                oderViewHolder.txtReasonRemove.setText(request.getMoment());

                oderViewHolder.btnDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(OrderRemoved.this, OrderDetail.class);
                        Common.currentRequest = request;
                        intent.putExtra("OrderId", adapter.getRef(i).getKey());
                        startActivity(intent);
                    }
                });
            }
        };

        recyclerView.setAdapter(new ScaleInAnimationAdapter(adapter));
    }
}