package com.example.dopinpan.Manager.ui.gallery.OrderHistoryManager;

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

import com.example.dopinpan.Common.Common;
import com.example.dopinpan.Model.Request;
import com.example.dopinpan.OrderDetail;
import com.example.dopinpan.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

public class OrderHistoryManager extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference requests,requests1,requests2;
    private FirebaseRecyclerAdapter<Request, OderHistoryViewHolderManager> adapter;

    private EditText edtreason;

    private MaterialSpinner spinner;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history_manager);

        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");
        requests1 = database.getReference("Requests Placed");
        requests2 = database.getReference("Requests Removed");



        recyclerView=findViewById(R.id.listoderhistorymanager);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        loadOders();

    }
    private void loadOders() {

        String phone = Common.currentUser.getPhone().toString();
        adapter = new FirebaseRecyclerAdapter<Request, OderHistoryViewHolderManager>(Request.class, R.layout.oder_history_layout_manager, OderHistoryViewHolderManager.class, requests) {
            @Override
            protected void populateViewHolder(OderHistoryViewHolderManager oderViewHolder, Request request, int i) {
                oderViewHolder.txtOderId.setText(adapter.getRef(i).getKey());
                oderViewHolder.txtOderStatus.setText(Common.convertCodeToStatus(request.getStatus()));
                oderViewHolder.txtOderPhone.setText(request.getPhone());
                oderViewHolder.txtOderAddress.setText(request.getAddress());
                oderViewHolder.txtOderTotal.setText("$"+request.getTotal());
                oderViewHolder.txtDateTime.setText(request.getStartAt());

                oderViewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Common.currentRequest = request;
                        showUpdateDialog(adapter.getRef(i).getKey(), adapter.getItem(i));
                    }
                });
                oderViewHolder.btnRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteOrder(adapter.getRef(i).getKey(),adapter.getItem(i));
                    }
                });
                oderViewHolder.btnDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(OrderHistoryManager.this, OrderDetail.class);
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

    private void showUpdateDialog(String key, Request item) {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(OrderHistoryManager.this);
        alertDialog.setTitle("Giao Hàng");
        alertDialog.setMessage("Xác Nhận Giao Hàng");
        Handler handler = new Handler();

        LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.update_order_layout, null);

        spinner = view.findViewById(R.id.statusSpiner);
        spinner.setItems("Shipping >>");

        alertDialog.setView(view);

        final String localKey = key;

        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ProgressDialog dialog = new ProgressDialog(OrderHistoryManager.this);
                dialog.setMessage("Vui Lòng Đợi ...");
                dialog.show();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialogInterface.dismiss();
                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String formattedDate = df.format(c.getTime());
                        Request request = new Request(Common.currentUser.getPhone().toString(), Common.currentUser.getName().toString(), Common.currentUser.getAddress(), item.getTotal(), item.getFoods(),"1", formattedDate);
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

    private void deleteOrder(String key,Request item) {
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
                ProgressDialog dialog = new ProgressDialog(OrderHistoryManager.this);
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