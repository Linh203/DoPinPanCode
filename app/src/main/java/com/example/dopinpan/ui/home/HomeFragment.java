package com.example.dopinpan.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dopinpan.Common.Common;
import com.example.dopinpan.Database.Database;
import com.example.dopinpan.FoodDetail;
import com.example.dopinpan.FoodList;
import com.example.dopinpan.FoodListAll;
import com.example.dopinpan.Interface.ItemClickListener;
import com.example.dopinpan.Model.Category;
import com.example.dopinpan.Model.Food;
import com.example.dopinpan.R;
import com.example.dopinpan.ViewHolder.FoodViewHolder;
import com.example.dopinpan.ViewHolder.MenuViewHolder;
import com.example.dopinpan.databinding.FragmentHomeBinding;
import com.example.dopinpan.ui.gallery.GalleryFragment;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

public class HomeFragment extends Fragment {
    private FirebaseDatabase database;
    private DatabaseReference category, foodList;
    private FragmentHomeBinding binding;
    private Database localDB;

    private FirebaseRecyclerAdapter<Category, MenuViewHolder> adapter;
    private FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter1;
    private String categoryId = "1";


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        database = FirebaseDatabase.getInstance();
        category = database.getReference("Category");
        foodList = database.getReference("Foods");
        localDB=new Database(getContext());



        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        binding.recyclerviewhome.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        binding.recyclerviewfood.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(getContext(), 2);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);

        binding.recyclerviewfood.setLayoutManager(layoutManager1);
        binding.recyclerviewhome.setLayoutManager(gridLayoutManager);


        if (Common.isConectedInternet(getContext()))
            load();
        else {
            Toast.makeText(getContext(), "Vui Lòng Kết Nối Mạng !!", Toast.LENGTH_SHORT).show();
        }

        ActionViewFlipper();
        binding.tvAllfood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), FoodListAll.class);
                startActivity(intent);
            }
        });
        return root;
    }

    public void load() {
        adapter = new FirebaseRecyclerAdapter<Category, MenuViewHolder>(Category.class, R.layout.menu_item, MenuViewHolder.class, category) {
            @Override
            protected void populateViewHolder(MenuViewHolder viewHolder, Category model, int position) {
                viewHolder.txtMenuname.setText(model.getName());
                Picasso.with(getContext()).load(model.getImage()).into(viewHolder.imageView);
                Category click = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent intent = new Intent(getContext(), FoodList.class);
                        intent.putExtra("CategoryId", adapter.getRef(position).getKey());
                        startActivity(intent);
                    }
                });
            }
        };
        binding.recyclerviewhome.setAdapter(new ScaleInAnimationAdapter(adapter));

        adapter1 = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(Food.class, R.layout.food_item, FoodViewHolder.class, foodList.orderByChild("menuId").equalTo(categoryId)) {
            @Override
            protected void populateViewHolder(FoodViewHolder foodViewHolder, Food food, int i) {
                foodViewHolder.txtFoodname.setText(food.getName());
                foodViewHolder.txtpricefood.setText(food.getPrice());
                Picasso.with(getContext()).load(food.getImage()).into(foodViewHolder.imageViewfood);
                if (localDB.isFavorites(adapter.getRef(i).getKey()))
                    foodViewHolder.fav_img.setImageResource(R.drawable.baseline_favorite_24);

                foodViewHolder.fav_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!localDB.isFavorites(adapter.getRef(i).getKey())){
                            localDB.addToFavorites(adapter.getRef(i).getKey());
                            foodViewHolder.fav_img.setImageResource(R.drawable.baseline_favorite_24);
                            Toast.makeText(getContext(), ""+food.getName()+" đã thêm vào yêu thích", Toast.LENGTH_SHORT).show();
                        }else {
                            localDB.removeToFavorites(adapter.getRef(i).getKey());
                            foodViewHolder.fav_img.setImageResource(R.drawable.baseline_favorite_border_24);
                            Toast.makeText(getContext(), ""+food.getName()+" đã xóa khỏi yêu thích", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
                Food click = food;
                foodViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent intent = new Intent(getContext(), FoodDetail.class);
                        intent.putExtra("FoodId", adapter1.getRef(position).getKey());
                        startActivity(intent);
                    }
                });
            }
        };
        binding.recyclerviewfood.setAdapter(new ScaleInAnimationAdapter(adapter1));

    }

    private void ActionViewFlipper() {
        List<String> mangquangcao = new ArrayList<>();
        mangquangcao.add("https://vuakhuyenmai.vn/wp-content/uploads/kfc-sale.png");
        mangquangcao.add("https://10az.net/wp-content/uploads/2023/08/word-image-12282-1.jpeg");
        mangquangcao.add("https://static.kfcvietnam.com.vn/free.jpg");
        mangquangcao.add("https://static.kfcvietnam.com.vn/pepsi%20zero.jpg");
        for (int i = 0; i < mangquangcao.size(); i++) {
            ImageView imageView = new ImageView(getActivity().getApplicationContext());
            Glide.with(this).load(mangquangcao.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            binding.viewflipperhome.addView(imageView);
        }
        binding.viewflipperhome.setFlipInterval(4000);
        binding.viewflipperhome.setAutoStart(true);
        Animation slide_in = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.slide_out);
        Animation slide_out = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.slide_in);
        binding.viewflipperhome.setInAnimation(slide_in);
        binding.viewflipperhome.setOutAnimation(slide_out);


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;

    }
}