package com.example.dopinpan.ui.gallery.OrderHistory;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dopinpan.Common.Common;
import com.example.dopinpan.Model.Request;
import com.example.dopinpan.OrderDetail;
import com.example.dopinpan.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

public class OrderHistory extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference requests,requests1;

    private RecyclerView recyclerView;
    private EditText edtreason;


    private RecyclerView.LayoutManager layoutManager;

    private FirebaseRecyclerAdapter<Request, OderHistoryViewHolder> adapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_placed);

        database = FirebaseDatabase.getInstance();
        requests1 = database.getReference("Requests Removed");
        requests = database.getReference("Requests");


        recyclerView = findViewById(R.id.listodersplaced);
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
        adapter = new FirebaseRecyclerAdapter<Request, OderHistoryViewHolder>(Request.class, R.layout.oder_history_layout, OderHistoryViewHolder.class, requests.orderByChild("phone").equalTo(phone)) {
            @Override
            protected void populateViewHolder(OderHistoryViewHolder oderViewHolder, Request request, int i) {
                oderViewHolder.txtOderId.setText(adapter.getRef(i).getKey());
                oderViewHolder.txtOderStatus.setText(Common.convertCodeToStatus(request.getStatus()));
                oderViewHolder.txtOderPhone.setText(request.getPhone());
                oderViewHolder.txtOderAddress.setText(request.getAddress());
                oderViewHolder.txtOderTotal.setText("$"+request.getTotal());
                oderViewHolder.txtDateTime.setText(request.getStartAt());

                oderViewHolder.btnDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(OrderHistory.this, OrderDetail.class);
                        Common.currentRequest = request;
                        intent.putExtra("OrderId", adapter.getRef(i).getKey());
                        startActivity(intent);
                    }
                });
                oderViewHolder.btnOrderRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Common.currentRequest = request;
                        removeOrder(adapter.getRef(i).getKey(), adapter.getItem(i));
                    }
                });
            }
        };

        recyclerView.setAdapter(new ScaleInAnimationAdapter(adapter));
    }


    @SuppressLint("MissingInflatedId")
    private void removeOrder(String key, Request item) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        LayoutInflater layoutInflater = this.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.remove_order_layout, null);

        edtreason = view.findViewById(R.id.edt_reasonremove);

        alertDialog.setView(view);
        alertDialog.setIcon(R.drawable.baseline_cancel_presentation_24);

        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Handler handler = new Handler();
                ProgressDialog dialog = new ProgressDialog(OrderHistory.this);
                dialog.setMessage("Vui Lòng Đợi ...");
                dialog.show();
                final String localKey = key;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String formattedDate = df.format(c.getTime());
                        Request request = new Request(Common.currentUser.getPhone().toString(), Common.currentUser.getName().toString(), Common.currentUser.getAddress(), item.getTotal(), "2", formattedDate,edtreason.getText().toString(), item.getFoods());
                        requests1.child(String.valueOf(System.currentTimeMillis())).setValue(request);
                        requests.child(localKey).removeValue();
                        dialog.dismiss();
                        adapter.notifyDataSetChanged();
                    }
                }, 2000);
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }

}