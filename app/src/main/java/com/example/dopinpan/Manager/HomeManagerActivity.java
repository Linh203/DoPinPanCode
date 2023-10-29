package com.example.dopinpan.Manager;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dopinpan.Common.Common;
import com.example.dopinpan.Model.Category;
import com.example.dopinpan.Model.User;
import com.example.dopinpan.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dopinpan.databinding.ActivityHomeManagerBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

public class HomeManagerActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private MaterialEditText edtName;
    private Button btnUpload, btnSelect;
    private Uri saveUri;

    private ActivityHomeManagerBinding binding;
    private Category newCategory;
    private FirebaseDatabase database;
    private DatabaseReference category,users;
    private DrawerLayout drawer;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private TextView txtname;
    private ImageView img;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeManagerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance();
        category = database.getReference("Category");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        users = database.getReference("User");


        setSupportActionBar(binding.appBarHomeManager.toolbar1);
        binding.appBarHomeManager.fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDiaLog();

            }
        });
        drawer = binding.drawerLayout1;
        NavigationView navigationView = binding.navView1;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home1, R.id.nav_gallery1, R.id.nav_slideshow1)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home_manager);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        View headerView = binding.navView1.getHeaderView(0);
        txtname = headerView.findViewById(R.id.admin_name);
        img = headerView.findViewById(R.id.imageView2);

        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.child(Common.currentUser.getPhone()).getValue(User.class);
                txtname.setText(user.getName());
                Picasso.with(getBaseContext()).load(user.getAvatarUser()).into(img);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_manager, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home_manager);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    @SuppressLint("MissingInflatedId")
    private void showDiaLog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomeManagerActivity.this);
        alertDialog.setTitle("Add New Category");
        alertDialog.setMessage("Please Fill Full Information !");
        Handler handler=new Handler();

        LayoutInflater layoutInflater = this.getLayoutInflater();
        View add_menu_layout = layoutInflater.inflate(R.layout.add_new_menu_layout, null);

        edtName = add_menu_layout.findViewById(R.id.edtName);
        btnUpload = add_menu_layout.findViewById(R.id.btn_upload1);
        btnSelect = add_menu_layout.findViewById(R.id.btn_select1);




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

        alertDialog.setView(add_menu_layout);
        alertDialog.setIcon(R.drawable.baseline_shopping_cart_24);

        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ProgressDialog dialog = new ProgressDialog(HomeManagerActivity.this);
                dialog.setMessage("Please Waiting ...");
                dialog.show();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (newCategory != null) {
                            category.push().setValue(newCategory);
                            Snackbar.make(drawer, "New Category " + newCategory.getName() + "was added", Snackbar.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                        dialogInterface.dismiss();
                    }
                },2000);


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
    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), Common.PICK_IMAGE_REQUEST);
    }
    private void uploadImage() {
        if (saveUri != null) {
            ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage("Uploading ...");
            dialog.show();

            String imageName = UUID.randomUUID().toString();
            StorageReference imageFoder = storageReference.child("image/" + imageName);
            imageFoder.putFile(saveUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    dialog.dismiss();
                    Toast.makeText(HomeManagerActivity.this, "Uploaded !!", Toast.LENGTH_SHORT).show();
                    imageFoder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            newCategory = new Category(edtName.getText().toString(), uri.toString());
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                    Toast.makeText(HomeManagerActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                    dialog.setMessage("Uploaded" + progress + "%");
                }
            });
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Common.PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            saveUri = data.getData();
            btnSelect.setText("Image Selected !");
        }
    }




}