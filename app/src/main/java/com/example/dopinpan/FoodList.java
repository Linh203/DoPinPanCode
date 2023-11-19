package com.example.dopinpan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.dopinpan.Common.Common;
import com.example.dopinpan.Database.Database;
import com.example.dopinpan.Interface.ItemClickListener;
import com.example.dopinpan.Model.Favorites;
import com.example.dopinpan.Model.Food;
import com.example.dopinpan.ViewHolder.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FoodList extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseDatabase database;
    private DatabaseReference foodList,favorites;
    private String categoryId = "";

    private LinearLayout back;
    private Database localDB;

    private FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter;
    private FirebaseRecyclerAdapter<Food, FoodViewHolder> searchadapter;
    private List<String> suggetsList = new ArrayList<>();
    private SearchView searchView;

    private ImageView btnBack;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        recyclerView = findViewById(R.id.recycler_foodlist);

        database = FirebaseDatabase.getInstance();
        foodList = database.getReference("Foods");
        favorites = database.getReference("Favorites");
        btnBack=findViewById(R.id.btn_back3);



        localDB=new Database(this);


        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        if (getIntent() != null)
            categoryId = getIntent().getStringExtra("CategoryId");
        if (!categoryId.isEmpty() && categoryId != null) {
            if (Common.isConectedInternet(getBaseContext())) {
                loadListFood();
            } else {
                Toast.makeText(this, "Vui Lòng Kết Nối Mạng !!", Toast.LENGTH_SHORT).show();
                return;
            }

        }
    }




    private void loadListFood() {

        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(Food.class, R.layout.food_item, FoodViewHolder.class, foodList.orderByChild("menuId").equalTo(categoryId)) {
            @Override
            protected void populateViewHolder(FoodViewHolder foodViewHolder, Food food, int i) {
                foodViewHolder.txtFoodname.setText(food.getName());
                foodViewHolder.txtpricefood.setText(food.getPrice());
                Picasso.with(getBaseContext()).load(food.getImage()).into(foodViewHolder.imageViewfood);

                if (localDB.isFavorites(adapter.getRef(i).getKey()))
                    foodViewHolder.fav_img.setImageResource(R.drawable.baseline_favorite_24);

                foodViewHolder.fav_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!localDB.isFavorites(adapter.getRef(i).getKey())){
                            localDB.addToFavorites(adapter.getRef(i).getKey());

                            foodViewHolder.fav_img.setImageResource(R.drawable.baseline_favorite_24);
                            Toast.makeText(FoodList.this, ""+food.getName()+" đã thêm vào yêu thích", Toast.LENGTH_SHORT).show();
                        }else {
                            localDB.removeToFavorites(adapter.getRef(i).getKey());

                            foodViewHolder.fav_img.setImageResource(R.drawable.baseline_favorite_border_24);
                            Toast.makeText(FoodList.this, ""+food.getName()+" đã xóa khỏi yêu thích", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
                Food click = food;
                foodViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent intent = new Intent(FoodList.this, FoodDetail.class);
                        intent.putExtra("FoodId", adapter.getRef(position).getKey());
                        startActivity(intent);
                    }
                });
            }
        };
        recyclerView.setAdapter(adapter);
    }
}