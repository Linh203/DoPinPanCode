package com.example.dopinpan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.dopinpan.Common.Common;
import com.example.dopinpan.Database.Database;
import com.example.dopinpan.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {
    private Button btnregister, btnlogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnlogin = findViewById(R.id.btn_login);
        btnregister = findViewById(R.id.btn_register);

        Handler handler = new Handler();

        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Vui Lòng Đợi ...");

        Paper.init(this);


        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new Database(getBaseContext()).cleanCart();
                        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                }, 2000);
            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.show();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(MainActivity.this, LogInActivity.class);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                }, 2000);
            }
        });

        String user = Paper.book().read(Common.USER_KEY);
        String pwd = Paper.book().read(Common.PWD_KEY);

        if (Common.isConectedInternet(getBaseContext())) {
            if (user != null && pwd != null) {
                if (!user.isEmpty() && !pwd.isEmpty()) {
                    login(user, pwd);
                }
            }
        } else {
            Toast.makeText(this, "Vui Lòng Kết Nối Mạng !!", Toast.LENGTH_SHORT).show();
            return;
        }


    }

    private void login(String phone, String pwd) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference table_user = database.getReference("User");
        Handler handler = new Handler();
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Vui Lòng Đợi ...");
        dialog.show();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.child(phone).exists()) {
                            User user = snapshot.child(phone).getValue(User.class);
                            user.setPhone(phone);
                            if (user.getPassWord().equals(pwd)) {
                                Common.currentUser = user;
                                Toast.makeText(MainActivity.this, "Chào Mừng Quay Trở Lại ", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, Home2Activity.class);
                                dialog.dismiss();
                                startActivity(intent);
                                table_user.removeEventListener(this);
                                finishAffinity();
                            } else {
                                Toast.makeText(MainActivity.this, "Tài Khoản Hoặc Mật Khẩu Không Chính Xác ", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            dialog.dismiss();
                            Toast.makeText(MainActivity.this, "Tài Khoản Không Tồn Tại", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }, 2000);
    }
}