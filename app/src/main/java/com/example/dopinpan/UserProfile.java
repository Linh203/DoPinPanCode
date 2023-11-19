package com.example.dopinpan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dopinpan.Common.Common;
import com.example.dopinpan.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;

public class UserProfile extends AppCompatActivity {
    private RoundedImageView userImg;
    private TextView userName, userPhone, userAddress, userEmail;
    private Uri saveUri;
    private StorageReference storageReference;
    private FirebaseStorage storage;
    private ImageView btnBack;


    private User newUser;


    private EditText edtname, edtphone, edtaddress, edtemail;
    private DatabaseReference users;

    private Button btnEditUser, btnSelect, btnUpload, btnChangePass;
    private FirebaseDatabase database;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        database = FirebaseDatabase.getInstance();
        users = database.getReference("User");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        btnBack=findViewById(R.id.btn_back15);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        userName = findViewById(R.id.user_name);
        userPhone = findViewById(R.id.user_phone);
        userAddress = findViewById(R.id.user_address);
        userImg = findViewById(R.id.user_img);
        userEmail = findViewById(R.id.user_email);
        btnEditUser = findViewById(R.id.btn_edituser);
        btnChangePass = findViewById(R.id.btn_changepass);
        Paper.init(this);


        loadUser();

        btnEditUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogEditUser();
            }
        });
        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChangePassDialog();

            }
        });
    }

    private void showChangePassDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Đổi Mật Khẩu");
        builder.setMessage("Vui Lòng Điền Đủ Thông Tin !");

        LayoutInflater inflater = LayoutInflater.from(this);
        View layout_pwd = inflater.inflate(R.layout.change_pass_layout, null);
        EditText edtpass = layout_pwd.findViewById(R.id.edt_passwrd);
        EditText edtnewpass = layout_pwd.findViewById(R.id.edt_newpasswrd);
        EditText edtrenewpass = layout_pwd.findViewById(R.id.edt_renewpasswrd);

        builder.setView(layout_pwd);

        builder.setPositiveButton("Đổi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                android.app.AlertDialog waitingDialog = new SpotsDialog(UserProfile.this);
                waitingDialog.show();

                if (edtpass.getText().toString().equals(Common.currentUser.getPassWord())) {
                    if (edtnewpass.getText().toString().equals(edtrenewpass.getText().toString())) {
                        Map<String, Object> passUpdate = new HashMap<>();
                        passUpdate.put("passWord", edtnewpass.getText().toString());
                        DatabaseReference user = FirebaseDatabase.getInstance().getReference("User");
                        user.child(Common.currentUser.getPhone()).updateChildren(passUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                waitingDialog.dismiss();
                                Paper.book().destroy();
                                Paper.book().write(Common.USER_KEY, Common.currentUser.getPhone());
                                Paper.book().write(Common.PWD_KEY, edtnewpass.getText().toString());
                                Intent intent = new Intent(UserProfile.this, MainActivity.class);
                                startActivity(intent);
                                finishAffinity();
                                Toast.makeText(UserProfile.this, "Đổi Mật Khẩu Thành Công !", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(UserProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        waitingDialog.dismiss();
                        Toast.makeText(UserProfile.this, "Xác Nhận Mật Khẩu Không Trùng Khớp !", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    waitingDialog.dismiss();
                    Toast.makeText(UserProfile.this, "Mật Khẩu Hiện Tại Sai !", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Thoát", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }


    private void showDialogEditUser() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        LayoutInflater layoutInflater = this.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.edit_user_layout, null);

        btnSelect = view.findViewById(R.id.btn_select);
        btnUpload = view.findViewById(R.id.btn_upload);
        edtname = view.findViewById(R.id.edt_username3);
        edtphone = view.findViewById(R.id.edt_userphone3);
        edtaddress = view.findViewById(R.id.edt_useraddress3);
        edtemail = view.findViewById(R.id.edt_useremail3);

        edtname.setText(Common.currentUser.getName().toString());
        edtphone.setText(Common.currentUser.getPhone().toString());
        if (Common.currentUser.getEmail().equals("null"))
            edtemail.setText("");
        else
            edtemail.setText(Common.currentUser.getEmail().toString());
        if (Common.currentUser.getAddress().equals("null"))
            edtaddress.setText("");
        else
            edtaddress.setText(Common.currentUser.getAddress().toString());


        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();

            }
        });

        alertDialog.setView(view);
        alertDialog.setIcon(R.drawable.baseline_edit_24);

        alertDialog.setPositiveButton("Đổi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Handler handler = new Handler();
                ProgressDialog dialog = new ProgressDialog(UserProfile.this);
                dialog.setMessage("Vui Lòng Đợi ...");
                dialog.show();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (newUser != null) {
                            Map<String, Object> keyUpdate = new HashMap<>();
                            keyUpdate.put(Common.currentUser.getPhone(), newUser.getPhone());
                            users.updateChildren(keyUpdate);
                            users.child(newUser.getPhone()).setValue(newUser);
                            if (newUser.getPhone().equals(Common.currentUser.getPhone()))
                                Toast.makeText(UserProfile.this, "Đăng Nhập Lại !", Toast.LENGTH_SHORT).show();
                            else
                                users.child(Common.currentUser.getPhone()).removeValue();
                            Common.currentUser = newUser;
                            Paper.book().destroy();
                            Paper.book().write(Common.USER_KEY, newUser.getPhone());
                            Paper.book().write(Common.PWD_KEY, newUser.getPassWord());
                            Toast.makeText(UserProfile.this, "Thay Đổi Thành Công !", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(UserProfile.this, MainActivity.class);
                            startActivity(intent);
                            dialog.dismiss();
                            finishAffinity();

                        } else {
                            String isStaff="false";
                            if (isStaff.equals(Common.currentUser.getIsStaff()))
                                newUser = new User(edtname.getText().toString(), Common.currentUser.getPassWord(), edtphone.getText().toString(), edtaddress.getText().toString(), edtemail.getText().toString(),isStaff, Common.currentUser.getSecureCode(),Common.currentUser.getAvatarUser(),Common.currentUser.getStartAt());
                            else
                                newUser = new User(edtname.getText().toString(), Common.currentUser.getPassWord(), edtphone.getText().toString(), edtaddress.getText().toString(), edtemail.getText().toString(),"true", Common.currentUser.getSecureCode(), Common.currentUser.getAvatarUser(),Common.currentUser.getStartAt());
                            Map<String, Object> keyUpdate = new HashMap<>();
                            keyUpdate.put(Common.currentUser.getPhone(), newUser.getPhone());
                            users.updateChildren(keyUpdate);
                            users.child(newUser.getPhone()).setValue(newUser);
                            if (newUser.getPhone().equals(Common.currentUser.getPhone()))
                                Toast.makeText(UserProfile.this, "Đăng Nhập Lại !", Toast.LENGTH_SHORT).show();
                            else
                                users.child(Common.currentUser.getPhone()).removeValue();
                            Common.currentUser = newUser;
                            Paper.book().destroy();
                            Paper.book().write(Common.USER_KEY, newUser.getPhone());
                            Paper.book().write(Common.PWD_KEY, newUser.getPassWord());
                            Toast.makeText(UserProfile.this, "Thay Đổi Thành Công !", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(UserProfile.this, MainActivity.class);
                            startActivity(intent);
                            dialog.dismiss();
                            finishAffinity();
                        }
                    }
                }, 2000);

                dialogInterface.dismiss();
            }
        });

        alertDialog.setNegativeButton("Thoát", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });


        alertDialog.show();

    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Chọn Ảnh"), Common.PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Common.PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            saveUri = data.getData();
            btnSelect.setText("Đã Chọn");
        }
    }

    private void uploadImage() {
        if (saveUri != null) {
            ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage(" Đang Tải Lên ...");
            dialog.show();

            String imageName = UUID.randomUUID().toString();
            StorageReference imageFoder = storageReference.child("image/" + imageName);
            imageFoder.putFile(saveUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    dialog.dismiss();
                    Toast.makeText(UserProfile.this, "Đã Tải Lên", Toast.LENGTH_SHORT).show();
                    imageFoder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String isStaff="false";
                            if (isStaff.equals(Common.currentUser.getIsStaff()))
                                newUser = new User(edtname.getText().toString(), Common.currentUser.getPassWord(), edtphone.getText().toString(), edtaddress.getText().toString(), edtemail.getText().toString(),isStaff, Common.currentUser.getSecureCode(), uri.toString(),Common.currentUser.getStartAt());
                            else
                                newUser = new User(edtname.getText().toString(), Common.currentUser.getPassWord(), edtphone.getText().toString(), edtaddress.getText().toString(), edtemail.getText().toString(),"true", Common.currentUser.getSecureCode(), uri.toString(),Common.currentUser.getStartAt());
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                    Toast.makeText(UserProfile.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                    dialog.setMessage("Tải Lên " + progress + "%");
                }
            });
        }
    }


    private void loadUser() {
        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User user = snapshot.child(Common.currentUser.getPhone()).getValue(User.class);
                userName.setText(user.getName());
                userPhone.setText(user.getPhone());

                if (user.getAddress().equals("null"))
                    userAddress.setText("");
                else
                    userAddress.setText(user.getAddress());
                if (user.getEmail().equals("null"))
                    userEmail.setText("");
                else
                    userEmail.setText(user.getEmail());

                Picasso.with(getBaseContext()).load(user.getAvatarUser()).into(userImg);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}