package com.example.dopinpan.Manager.ui.gallery.StatisticalManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.dopinpan.Common.Common;
import com.example.dopinpan.Database.Database;
import com.example.dopinpan.Manager.ui.gallery.OrderShippedManager.OderShippedViewHolderManager;
import com.example.dopinpan.Model.Request;
import com.example.dopinpan.Model.Statictical;
import com.example.dopinpan.OrderDetail;
import com.example.dopinpan.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

public class StatisticalManager extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference statis, requests;
    private FirebaseRecyclerAdapter<Request, OderShippedViewHolderManager> adapter;
 //   private FirebaseRecyclerAdapter<Statictical, OderShippedViewHolderManager> adapter1;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Statictical> staticticals = new ArrayList<>();

   Statictical statictical;


    private TextView txtTotal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistical_manager);

        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests Shipped");
        statis = database.getReference("Statistical");

        txtTotal = findViewById(R.id.txtTotal);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);


        recyclerView = findViewById(R.id.listStatic);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        new Database(getBaseContext()).cleanStatis();
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
                        Intent intent = new Intent(StatisticalManager.this, OrderDetail.class);
                        Common.currentRequest = request;
                        intent.putExtra("OrderId", adapter.getRef(i).getKey());
                        startActivity(intent);
                    }
                });
                new Database(StatisticalManager.this).addToStatis(new Statictical(
                        request.getDay(),
                        request.getMonth(),
                        request.getYear(),
                        request.getTotal()));

               staticticals = new Database(StatisticalManager.this).getStatis();
                int total = 0;

                for (Statictical statis1 : staticticals) {
                    total += (Integer.parseInt(statis1.getTotal()));
                }
                Locale locale = new Locale("en", "US");
                NumberFormat format = NumberFormat.getCurrencyInstance(locale);

                txtTotal.setText(format.format(total));

            }
        };
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

}