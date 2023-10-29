package com.example.dopinpan.Manager.ui.home;

import static android.app.Activity.RESULT_OK;
import static com.example.dopinpan.Common.Common.PICK_IMAGE_REQUEST;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dopinpan.Common.Common;
import com.example.dopinpan.Interface.ItemClickListener;
import com.example.dopinpan.Manager.FoodListManager;
import com.example.dopinpan.Manager.ViewHolderManager.MenuViewHolderManager;
import com.example.dopinpan.Model.Category;
import com.example.dopinpan.R;
import com.example.dopinpan.ViewHolder.MenuViewHolder;
import com.example.dopinpan.databinding.FragmentHomeManagerBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class HomeManagerFragment extends Fragment {
    private DatabaseReference category;
    private FirebaseDatabase database;
    private FirebaseRecyclerAdapter<Category, MenuViewHolderManager> adapter;
    private MaterialEditText edtName;
    private Button btnUpload, btnSelect;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Uri saveUri;

    private RecyclerView.LayoutManager layoutManager;


    private FragmentHomeManagerBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeManagerViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeManagerViewModel.class);

        binding = FragmentHomeManagerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        database = FirebaseDatabase.getInstance();
        category = database.getReference("Category");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        binding.recyclerviewhomemanager.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        binding.recyclerviewhomemanager.setLayoutManager(layoutManager);
        // GridLayoutManager gridLayoutManager=new GridLayoutManager(getContext(),2);
        //  gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        //  binding.recyclerviewhomemanager.setLayoutManager(gridLayoutManager);

        load();


        return root;
    }

    private void load() {
        adapter = new FirebaseRecyclerAdapter<Category, MenuViewHolderManager>(Category.class, R.layout.menu_item_manager, MenuViewHolderManager.class, category) {
            @Override
            protected void populateViewHolder(MenuViewHolderManager viewHolder, Category model, int position) {
                viewHolder.txtMenuname.setText(model.getName());
                Picasso.with(getContext()).load(model.getImage()).into(viewHolder.imageView);

                viewHolder.btnUpdateCat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showUpdateDialog(adapter.getRef(position).getKey(), adapter.getItem(position));
                    }
                });
                viewHolder.btnRemoveCat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteCategory(adapter.getRef(position).getKey());
                    }
                });
                Category click = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent intent = new Intent(getContext(), FoodListManager.class);
                        intent.putExtra("CategoryId", adapter.getRef(position).getKey());
                        startActivity(intent);
                    }
                });
            }
        };
        adapter.notifyDataSetChanged();
        binding.recyclerviewhomemanager.setAdapter(new ScaleInAnimationAdapter(adapter));
    }

    private void deleteCategory(String key) {
        Handler handler = new Handler();
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle("Thông Báo");
        alertDialog.setIcon(R.drawable.baseline_warning_24);
        alertDialog.setMessage("Vui Lòng Xác Nhận Xóa");

        alertDialog.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ProgressDialog dialog = new ProgressDialog(getContext());
                dialog.setMessage("Vui Lòng Đợi ...");
                dialog.show();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        DatabaseReference foods = database.getReference("Foods");
                        Query foodInCat = foods.orderByChild("menuId").equalTo(key);
                        foodInCat.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot post : snapshot.getChildren()) {
                                    post.getRef().removeValue();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        category.child(key).removeValue();
                        dialog.dismiss();
                        Toast.makeText(getContext(), "Xóa Thành Công", Toast.LENGTH_SHORT).show();

                    }
                }, 2000);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            saveUri = data.getData();
            btnSelect.setText("Image Selected !");
        }

    }

    private void showUpdateDialog(String key, Category item) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle("Chỉnh Sửa");
        alertDialog.setMessage("Vui Lòng Điền Đủ Thông Tin");
        Handler handler = new Handler();

        LayoutInflater layoutInflater = this.getLayoutInflater();
        View add_menu_layout = layoutInflater.inflate(R.layout.add_new_menu_layout, null);

        edtName = add_menu_layout.findViewById(R.id.edtName);
        btnUpload = add_menu_layout.findViewById(R.id.btn_upload1);
        btnSelect = add_menu_layout.findViewById(R.id.btn_select1);

        edtName.setText(item.getName());


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
                ProgressDialog dialog = new ProgressDialog(getContext());
                dialog.setMessage("Vui Lòng Đợi ...");
                dialog.show();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialogInterface.dismiss();
                        item.setName(edtName.getText().toString());
                        category.child(key).setValue(item);
                        dialog.dismiss();
                        Toast.makeText(getContext(), "" + item.getName() + " sửa thành công", Toast.LENGTH_SHORT).show();
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

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Chọn Ảnh"), PICK_IMAGE_REQUEST);
    }

    private void changeImage(Category item) {
        if (saveUri != null) {
            ProgressDialog dialog = new ProgressDialog(getContext());
            dialog.setMessage("Đang Tải Lên ...");
            dialog.show();

            String imageName = UUID.randomUUID().toString();
            StorageReference imageFoder = storageReference.child("image/" + imageName);
            imageFoder.putFile(saveUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    dialog.dismiss();
                    Toast.makeText(getContext(), "Đã Tải Lên !!", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}