package com.example.dopinpan;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dopinpan.Common.Common;
import com.example.dopinpan.ViewHolder.OrderDeatilAdapter;

public class OrderDetail extends AppCompatActivity {

    TextView order_id, order_phone, order_address, order_total, order_datetime;
    String order_id_value = "";
    RecyclerView lstFoods;
    RecyclerView.LayoutManager layoutManager;

    private ImageView btnBack;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        order_id = findViewById(R.id.oder_id1);
        order_phone = findViewById(R.id.oder_phone1);
        order_total = findViewById(R.id.oder_total1);
        order_address = findViewById(R.id.oder_address1);
        order_datetime = findViewById(R.id.oder_datetime2);
        btnBack=findViewById(R.id.btn_back5);

        lstFoods = findViewById(R.id.lstFoods);
        lstFoods.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        lstFoods.setLayoutManager(layoutManager);

        if (getIntent() != null)
            order_id_value = getIntent().getStringExtra("OrderId");
        order_id.setText(order_id_value);
        order_phone.setText(Common.currentRequest.getPhone());
        order_total.setText("$"+Common.currentRequest.getTotal());
        order_address.setText(Common.currentRequest.getAddress());
        order_datetime.setText(Common.currentRequest.getStartAt());

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        OrderDeatilAdapter adapter = new OrderDeatilAdapter(Common.currentRequest.getFoods());
        adapter.notifyDataSetChanged();
        lstFoods.setAdapter(adapter);
    }
}