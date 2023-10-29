package com.example.dopinpan.Manager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.dopinpan.Common.Common;
import com.example.dopinpan.Interface.ItemClickListener;
import com.example.dopinpan.Manager.ViewHolderManager.FoodViewHolderManager;
import com.example.dopinpan.Model.Category;
import com.example.dopinpan.Model.Food;
import com.example.dopinpan.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.util.UUID;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

public class FoodListManager extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private FloatingActionButton fab;
    private ConstraintLayout rootLayout;
    private FirebaseDatabase database;
    private DatabaseReference foodlist;
    private MaterialEditText edtName, edtDescription, edtPrice, edtDiscount;

    private Food newFood;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Button btnUpload, btnSelect;
    private String categoryId = "";
    private Uri saveUri;


    private FirebaseRecyclerAdapter<Food, FoodViewHolderManager> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list_manager);
        database = FirebaseDatabase.getInstance();
        foodlist = database.getReference("Foods");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        recyclerView = findViewById(R.id.recycler_foodlistmanager);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        rootLayout = findViewById(R.id.rootLayout);

        fab = findViewById(R.id.fab1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddFoodDialog();
            }
        });

        if (getIntent() != null)
            categoryId = getIntent().getStringExtra("CategoryId");
        if (!categoryId.isEmpty())
            loadFoodList(categoryId);
    }

    private void showAddFoodDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(FoodListManager.this);
        alertDialog.setTitle("Thêm Đồ Ăn Mới");
        alertDialog.setMessage("Vui Lòng Điền Đủ Thông Tin !");

        LayoutInflater layoutInflater = this.getLayoutInflater();
        View add_menu_layout = layoutInflater.inflate(R.layout.add_new_food_layout, null);

        edtName = add_menu_layout.findViewById(R.id.edtName1);
        edtDescription = add_menu_layout.findViewById(R.id.edtDescription);
        edtPrice = add_menu_layout.findViewById(R.id.edtPrice);
        edtDiscount = add_menu_layout.findViewById(R.id.edtDiscount);

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
                Handler handler = new Handler();
                ProgressDialog dialog = new ProgressDialog(FoodListManager.this);
                dialog.setMessage("Vui Lòng Đợi ...");
                dialog.show();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (newFood != null) {
                            foodlist.push().setValue(newFood);
                            Snackbar.make(rootLayout, "New Food " + newFood.getName() + " thêm thành công", Snackbar.LENGTH_SHORT).show();
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

    private void uploadImage() {
        if (saveUri != null) {
            ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage("Đang Tải Lên ...");
            dialog.show();

            String imageName = UUID.randomUUID().toString();
            StorageReference imageFoder = storageReference.child("image/" + imageName);
            imageFoder.putFile(saveUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    dialog.dismiss();
                    Toast.makeText(FoodListManager.this, "Đã Tải Lên", Toast.LENGTH_SHORT).show();
                    imageFoder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            newFood = new Food();
                            newFood.setName(edtName.getText().toString());
                            newFood.setDescriptions(edtDescription.getText().toString());
                            newFood.setPrice(edtPrice.getText().toString());
                            newFood.setDiscount(edtDiscount.getText().toString());
                            newFood.setMenuId(categoryId);
                            newFood.setImage(uri.toString());
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                    Toast.makeText(FoodListManager.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                    dialog.setMessage("Tải Lên" + progress + "%");
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Common.PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            saveUri = data.getData();
            btnSelect.setText("Đã Chọn");
        }
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Chọn Ảnh"), Common.PICK_IMAGE_REQUEST);
    }

    private void loadFoodList(String categoryId) {
        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolderManager>(Food.class, R.layout.food_item_manager, FoodViewHolderManager.class, foodlist.orderByChild("menuId").equalTo(categoryId)) {
            @Override
            protected void populateViewHolder(FoodViewHolderManager foodViewHolder, Food food, int i) {

                foodViewHolder.txtFoodname.setText(food.getName());
                foodViewHolder.txtpricefood.setText(food.getPrice());
                Picasso.with(getBaseContext()).load(food.getImage()).into(foodViewHolder.imageViewfood);

                foodViewHolder.btnUpdateFood.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showUpdateFoodDialog(adapter.getRef(i).getKey(), adapter.getItem(i));
                    }
                });

                foodViewHolder.btnRemoveFood.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteFoods(adapter.getRef(i).getKey());
                    }
                });


            }
        };
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(new ScaleInAnimationAdapter(adapter));
    }


    private void deleteFoods(String key) {
        Handler handler = new Handler();
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Thông Báo");
        alertDialog.setIcon(R.drawable.baseline_warning_24);
        alertDialog.setMessage("Vui Lòng Xác Nhận Xóa");

        alertDialog.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ProgressDialog dialog = new ProgressDialog(FoodListManager.this);
                dialog.setMessage("Vui Lòng Đợi ...");
                dialog.show();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        foodlist.child(key).removeValue();
                        dialog.dismiss();
                        Toast.makeText(FoodListManager.this, "Xóa Thành Công", Toast.LENGTH_SHORT).show();
                    }
                },2000);
            }
        });
        alertDialog.setNegativeButton("Thoát", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        alertDialog.show();
    }

    private void showUpdateFoodDialog(String key, Food item) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(FoodListManager.this);
        alertDialog.setTitle("Chỉnh Sửa");
        alertDialog.setMessage("Vui Lòng Điền Đủ Thông Tin");
        Handler handler = new Handler();

        LayoutInflater layoutInflater = this.getLayoutInflater();
        View add_menu_layout = layoutInflater.inflate(R.layout.add_new_food_layout, null);

        edtName = add_menu_layout.findViewById(R.id.edtName1);
        edtDescription = add_menu_layout.findViewById(R.id.edtDescription);
        edtPrice = add_menu_layout.findViewById(R.id.edtPrice);
        edtDiscount = add_menu_layout.findViewById(R.id.edtDiscount);

        edtName.setText(item.getName());
        edtDescription.setText(item.getDescriptions());
        edtDiscount.setText(item.getDiscount());
        edtPrice.setText(item.getPrice());

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
                changeImage(item);
            }
        });

        alertDialog.setView(add_menu_layout);
        alertDialog.setIcon(R.drawable.baseline_shopping_cart_24);

        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ProgressDialog dialog = new ProgressDialog(FoodListManager.this);
                dialog.setMessage("Vui Lòng Đợi ...");
                dialog.show();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        item.setName(edtName.getText().toString());
                        item.setPrice(edtPrice.getText().toString());
                        item.setDescriptions(edtDescription.getText().toString());
                        item.setDiscount(edtDiscount.getText().toString());

                        foodlist.child(key).setValue(item);
                        Snackbar.make(rootLayout, item.getName() + " sửa thành công ", Snackbar.LENGTH_SHORT).show();

                        dialog.dismiss();
                        dialogInterface.dismiss();
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

    private void changeImage(Food item) {
        if (saveUri != null) {
            ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage("Đang Tải Lên ...");
            dialog.show();

            String imageName = UUID.randomUUID().toString();
            StorageReference imageFoder = storageReference.child("image/" + imageName);
            imageFoder.putFile(saveUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    dialog.dismiss();
                    Toast.makeText(FoodListManager.this, "Đã Tải Lên", Toast.LENGTH_SHORT).show();
                    imageFoder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            item.setImage(uri.toString());
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.dismiss();
                    Toast.makeText(FoodListManager.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                    dialog.setMessage("Tải Lên" + progress + "%");
                }
            });
        }
    }


}