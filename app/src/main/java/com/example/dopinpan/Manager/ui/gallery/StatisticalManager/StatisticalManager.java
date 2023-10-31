package com.example.dopinpan.Manager.ui.gallery.StatisticalManager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dopinpan.Common.Common;
import com.example.dopinpan.Database.Database;
import com.example.dopinpan.Manager.ui.gallery.OrderHistoryManager.OrderHistoryManager;
import com.example.dopinpan.Manager.ui.gallery.OrderShippedManager.OderShippedViewHolderManager;
import com.example.dopinpan.Model.Request;
import com.example.dopinpan.Model.Statictical;
import com.example.dopinpan.OrderDetail;
import com.example.dopinpan.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

public class StatisticalManager extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference statis, requests;
    private MaterialSpinner spinner;

    private FirebaseRecyclerAdapter<Request, OderShippedViewHolderManager> adapter;
 //   private FirebaseRecyclerAdapter<Statictical, OderShippedViewHolderManager> adapter1;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Statictical> staticticals = new ArrayList<>();

   Statictical statictical;

   LinearLayout btnLoc;
    String month;


    private TextView txtTotal;




    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistical_manager);

        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests Shipped");
        statis = database.getReference("Statistical");



        txtTotal = findViewById(R.id.txtTotal);
        btnLoc=findViewById(R.id.btn_loc);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);


        recyclerView = findViewById(R.id.listStatic);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        new Database(getBaseContext()).cleanStatis();
        loadOders();

        btnLoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Database(getBaseContext()).cleanStatis();
                showDialogLoc();
            }
        });


    }

    private void showDialogLoc() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(StatisticalManager.this);
        alertDialog.setTitle("Bộ Lọc");
        alertDialog.setMessage("Xác Nhận Lọc Theo Tháng");
        Handler handler = new Handler();

        LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.update_order_layout, null);

        spinner = view.findViewById(R.id.statusSpiner);
        spinner.setItems("","1","2","3","4","5","6","7","8","9","10","11","12");

        alertDialog.setView(view);


        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                txtTotal.setText("");
                ProgressDialog dialog = new ProgressDialog(StatisticalManager.this);
                dialog.setMessage("Vui Lòng Đợi ...");
                dialog.show();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialogInterface.dismiss();
                        month=String.valueOf(spinner.getSelectedIndex());
                        adapter = new FirebaseRecyclerAdapter<Request, OderShippedViewHolderManager>(Request.class, R.layout.oder_shipped_layout_manager, OderShippedViewHolderManager.class, statis.orderByChild("month").equalTo(month)) {
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
                        dialog.dismiss();
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

    private void loadOders() {
        adapter = new FirebaseRecyclerAdapter<Request, OderShippedViewHolderManager>(Request.class, R.layout.oder_shipped_layout_manager, OderShippedViewHolderManager.class, statis) {
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