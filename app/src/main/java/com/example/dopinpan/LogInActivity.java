package com.example.dopinpan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.rey.material.widget.CheckBox;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class LogInActivity extends AppCompatActivity {

    private EditText edtphone, edtpass;
    private TextView tvforgotpass;
    private Button btnlogin;
    private LinearLayout tvcreateacc;
    FirebaseDatabase database;
    DatabaseReference table_user;


    private CheckBox checkBox;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        edtphone = findViewById(R.id.edt_phone1);
        edtpass = findViewById(R.id.edt_pass1);
        tvforgotpass = findViewById(R.id.tv_forgotpass);
        tvcreateacc = findViewById(R.id.tv_createacc);
        btnlogin = findViewById(R.id.btn_login);

        checkBox = findViewById(R.id.ckbremember);

        Paper.init(this);

        database = FirebaseDatabase.getInstance();
        table_user = database.getReference("User");

        final ProgressDialog dialog = new ProgressDialog(LogInActivity.this);
        dialog.setMessage("Vui Lòng Đợi ...");

        Handler handler = new Handler();

        tvforgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showForgotPass();
            }
        });

        tvcreateacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(LogInActivity.this, RegisterActivity.class);
                        startActivity(intent);
                        finishAffinity();
                        dialog.dismiss();
                    }
                }, 2000);
            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();

                if (Common.isConectedInternet(getBaseContext())) {
                    if (checkBox.isChecked()) {
                        Paper.book().write(Common.USER_KEY, edtphone.getText().toString());
                        Paper.book().write(Common.PWD_KEY, edtpass.getText().toString());
                    }
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            table_user.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.child(edtphone.getText().toString()).exists()) {
                                        User user = snapshot.child(edtphone.getText().toString()).getValue(User.class);
                                        user.setPhone(edtphone.getText().toString());
                                        if (user.getPassWord().equals(edtpass.getText().toString())) {
                                            Common.currentUser = user;
                                            Toast.makeText(LogInActivity.this, "Đăng Nhập Thành Công", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(LogInActivity.this, Home2Activity.class);
                                            dialog.dismiss();
                                            startActivity(intent);
                                            finish();

                                            table_user.removeEventListener(this);


                                        } else {
                                            dialog.dismiss();
                                            Toast.makeText(LogInActivity.this, "Tài Khoản Hoặc Mật Khẩu Sai", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        dialog.dismiss();
                                        Toast.makeText(LogInActivity.this, "Tài Khoản Không Tồn Tại", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }, 2000);
                } else {
                    Toast.makeText(LogInActivity.this, "Vui Lòng Kết Nối Mạng", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    private void showForgotPass() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Quên Mật Khẩu");
        builder.setMessage("Vui Lòng Nhập Mã Bảo Mật");

        LayoutInflater inflater = this.getLayoutInflater();
        View forgot_pass = inflater.inflate(R.layout.forgotpass_layout, null);

        builder.setView(forgot_pass);
        builder.setIcon(R.drawable.baseline_security_24);

        EditText edtPhone = forgot_pass.findViewById(R.id.edt_phone2);
        EditText edtsecureCode = forgot_pass.findViewById(R.id.edt_secureCode1);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user=snapshot.child(edtPhone.getText().toString()).getValue(User.class);
                            if (user.getSecureCode().equals(edtsecureCode.getText().toString()))
                                Toast.makeText(LogInActivity.this, "Mật Khẩu Của Bạn Là : "+user.getPassWord(), Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(LogInActivity.this, "Sai Mã Bảo Mật", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }
}