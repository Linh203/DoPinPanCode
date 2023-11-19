package com.example.dopinpan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.dopinpan.Common.Common;
import com.example.dopinpan.Database.Database;
import com.example.dopinpan.Interface.ItemClickListener;
import com.example.dopinpan.Model.Food;
import com.example.dopinpan.ViewHolder.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

public class FoodListAll extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseDatabase database;
    private DatabaseReference foodList,favorites;
    private String categoryId = "";

    private LinearLayout back;
    private FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter;
    private FirebaseRecyclerAdapter<Food, FoodViewHolder> searchadapter;
    private List<String> suggestList = new ArrayList<>();
    private MaterialSearchBar materialSearchBar;
    private Database localDB;
    private ImageView btnBack;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list_all);

        recyclerView = findViewById(R.id.recycler_foodlistall);
        database = FirebaseDatabase.getInstance();
        foodList = database.getReference("Foods");
        localDB = new Database(this);
        btnBack=findViewById(R.id.btn_back4);


        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        if (Common.isConectedInternet(getBaseContext())) {
            loadListFood();
        } else {
            Toast.makeText(this, "Vui Lòng Kết Nối Mạng !!", Toast.LENGTH_SHORT).show();
            return;
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        materialSearchBar = findViewById(R.id.searchBar);
        materialSearchBar.setHint("Nhập Đồ Ăn Cần Tìm");
        //materialSearchBar.setSpeechMode(false);
        loadSuggest();

        materialSearchBar.setLastSuggestions(suggestList);
        materialSearchBar.setCardViewElevation(10);

        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                List<String> suggest = new ArrayList<String>();
                for (String search : suggestList) {
                    if (search.toLowerCase().contains(materialSearchBar.getText().toLowerCase()))
                        suggest.add(search);

                }
                materialSearchBar.setLastSuggestions(suggest);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                if (!enabled)
                    recyclerView.setAdapter(new ScaleInAnimationAdapter(adapter));
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                startSearch(text);

            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });

    }

    private void startSearch(CharSequence text) {
        searchadapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(Food.class, R.layout.food_item, FoodViewHolder.class, foodList.orderByChild("name").equalTo(text.toString())) {
            @Override
            protected void populateViewHolder(FoodViewHolder foodViewHolder, Food food, int i) {
                foodViewHolder.txtFoodname.setText(food.getName());
                foodViewHolder.txtpricefood.setText(food.getPrice());
                Picasso.with(getBaseContext()).load(food.getImage()).into(foodViewHolder.imageViewfood);

                if (localDB.isFavorites(searchadapter.getRef(i).getKey()))
                    foodViewHolder.fav_img.setImageResource(R.drawable.baseline_favorite_24);

                foodViewHolder.fav_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!localDB.isFavorites(searchadapter.getRef(i).getKey())) {
                            localDB.addToFavorites(searchadapter.getRef(i).getKey());
                            foodViewHolder.fav_img.setImageResource(R.drawable.baseline_favorite_24);
                            Toast.makeText(FoodListAll.this, "" + food.getName() + " đã thêm vào yêu thích", Toast.LENGTH_SHORT).show();
                        } else {
                            localDB.removeToFavorites(searchadapter.getRef(i).getKey());
                            foodViewHolder.fav_img.setImageResource(R.drawable.baseline_favorite_border_24);
                            Toast.makeText(FoodListAll.this, "" + food.getName() + " đã xóa khỏi yêu thích", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                Food click = food;
                foodViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent intent = new Intent(FoodListAll.this, FoodDetail.class);
                        intent.putExtra("FoodId", searchadapter.getRef(position).getKey());
                        startActivity(intent);
                    }
                });
            }
        };
        recyclerView.setAdapter(searchadapter);
    }

    private void loadSuggest() {
        foodList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Food item = postSnapshot.getValue(Food.class);
                    suggestList.add(item.getName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadListFood() {
        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(Food.class, R.layout.food_item, FoodViewHolder.class, foodList) {
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
                        if (!localDB.isFavorites(adapter.getRef(i).getKey())) {
                            localDB.addToFavorites(adapter.getRef(i).getKey());
                            foodViewHolder.fav_img.setImageResource(R.drawable.baseline_favorite_24);
                            Toast.makeText(FoodListAll.this, "" + food.getName() + " đã thêm vào yêu thích", Toast.LENGTH_SHORT).show();
                        } else {
                            localDB.removeToFavorites(adapter.getRef(i).getKey());
                            foodViewHolder.fav_img.setImageResource(R.drawable.baseline_favorite_border_24);
                            Toast.makeText(FoodListAll.this, "" + food.getName() + " đã xóa khỏi yêu thích", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
                foodViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent intent = new Intent(FoodListAll.this, FoodDetail.class);
                        intent.putExtra("FoodId", adapter.getRef(position).getKey());
                        startActivity(intent);
                    }
                });
            }
        };
        recyclerView.setAdapter(new ScaleInAnimationAdapter(adapter));
    }


}

