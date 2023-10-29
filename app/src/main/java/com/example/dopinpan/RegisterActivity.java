package com.example.dopinpan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.dopinpan.Common.Common;
import com.example.dopinpan.Database.Database;
import com.example.dopinpan.Model.Order;
import com.example.dopinpan.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {
    private EditText edtphone, edtname, edtpass, edtcfpass,edtsecureCode;
    private Button btnregister;
    private LinearLayout tvbacktologin;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtphone = findViewById(R.id.edt_phone);
        edtname = findViewById(R.id.edt_name);
        edtpass = findViewById(R.id.edt_pass);
        edtcfpass = findViewById(R.id.edt_cfpass);
        btnregister = findViewById(R.id.btn_resgister);
        tvbacktologin = findViewById(R.id.tv_backtologin);
        edtsecureCode=findViewById(R.id.edt_secureCode);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table_user = database.getReference("User");

        final ProgressDialog dialog = new ProgressDialog(RegisterActivity.this);
        dialog.setMessage("Vui Lòng Đợi ...");

        Handler handler = new Handler();

        tvbacktologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(RegisterActivity.this, LogInActivity.class);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                }, 2000);
            }
        });

        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog dialog = new ProgressDialog(RegisterActivity.this);
                dialog.setMessage("Vui Lòng Đợi ...");
                dialog.show();
                if (edtpass.getText().toString().equals(edtcfpass.getText().toString())) {
                    if (Common.isConectedInternet(getBaseContext())) {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                table_user.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        Calendar c = Calendar.getInstance();
                                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        String formattedDate = df.format(c.getTime());
                                        User user = new User(edtname.getText().toString(), edtpass.getText().toString(), edtphone.getText().toString(), "null", "null","false",edtsecureCode.getText().toString(),"https://media.dolenglish.vn/PUBLIC/MEDIA/9590ffca-47b8-43ef-98a7-742ca207ca23.jpg",formattedDate);
                                        table_user.child(edtphone.getText().toString()).setValue(user);
                                        Toast.makeText(RegisterActivity.this, "Đăng Ký Thành Công !", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(RegisterActivity.this, LogInActivity.class);
                                        startActivity(intent);
                                        finishAffinity();
                                        dialog.dismiss();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }, 2000);
                    }else {
                        Toast.makeText(RegisterActivity.this, "Vui Lòng Kết Nối Mạng !", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "Mật Khẩu Xác Nhận Không Trùng Khớp !", Toast.LENGTH_SHORT).show();
                    edtcfpass.setText("");
                }

            }
        });
    }
}