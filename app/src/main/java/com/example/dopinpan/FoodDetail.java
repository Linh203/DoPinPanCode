package com.example.dopinpan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.dopinpan.Common.Common;
import com.example.dopinpan.Database.Database;
import com.example.dopinpan.Model.Carts;
import com.example.dopinpan.Model.Food;
import com.example.dopinpan.Model.Order;
import com.example.dopinpan.Model.Rating;
import com.example.dopinpan.Model.Request;
import com.example.dopinpan.Model.Soluong;
import com.example.dopinpan.Model.User;
import com.example.dopinpan.ViewHolder.CartsViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class FoodDetail extends AppCompatActivity implements RatingDialogListener {
    private TextView foodname, foodprice, fooddescription;

    private ImageView foodimage;

    private Button btncong, btntru;
    private List<Order> cart = new ArrayList<>();

    private TextView number;
    private int numberCount = 1;

    private Soluong soluong;

    private CollapsingToolbarLayout collapsingToolbarLayout;
    private FloatingActionButton btncart, btnrating;
    private RatingBar ratingBar;
    private String foodId = "";

    private FirebaseDatabase database;


    private Food curentFood;
    private FirebaseRecyclerAdapter<Carts, CartsViewHolder> adapters;



    private DatabaseReference foods, ratingTbl, carts;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        database = FirebaseDatabase.getInstance();
        foods = database.getReference("Foods");
        ratingTbl = database.getReference("Rating");
        carts = database.getReference("Carts");


        btncart = findViewById(R.id.btn_cart);
        fooddescription = findViewById(R.id.food_description);
        foodname = findViewById(R.id.food_name);
        foodprice = findViewById(R.id.food_price);
        foodimage = findViewById(R.id.img_food);
        btncong = findViewById(R.id.cong);
        btntru = findViewById(R.id.tru);
        number = findViewById(R.id.number);
        btnrating = findViewById(R.id.btn_rating);
        ratingBar = findViewById(R.id.ratingBar);


        collapsingToolbarLayout = findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);


        if (getIntent() != null)
            foodId = getIntent().getStringExtra("FoodId");
        if (!foodId.isEmpty()) {
            if (Common.isConectedInternet(getBaseContext())) {
                getDetailFood(foodId);
                getRatinglFood(foodId);

            } else {
                Toast.makeText(this, "Vui Lòng Kết Nối Mạng !!", Toast.LENGTH_SHORT).show();
                return;
            }

        }
        soluong = new Soluong(String.valueOf(numberCount));
        number.setText(soluong.getSoluong());
        btncong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numberCount++;
                soluong = new Soluong(String.valueOf(numberCount));
                number.setText(soluong.getSoluong());
            }
        });
        btntru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (numberCount > 0) {
                    numberCount--;
                    soluong = new Soluong(String.valueOf(numberCount));
                    number.setText(soluong.getSoluong());
                } else {
                    numberCount = 0;
                    soluong = new Soluong(String.valueOf(numberCount));
                    number.setText(soluong.getSoluong());
                }

            }
        });


        btncart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Carts cart1 = new Carts(foodId, curentFood.getName(), soluong.getSoluong(), curentFood.getPrice(), curentFood.getDiscount(), Common.currentUser.getPhone());
                carts.child(String.valueOf(System.currentTimeMillis())).setValue(cart1);


                Toast.makeText(FoodDetail.this, "Đã Thêm Vào Giỏ Hàng", Toast.LENGTH_SHORT).show();
            }
        });

        btnrating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRatingDialog();
            }
        });
    }

    private void getRatinglFood(String foodId) {
        Query foodRating = ratingTbl.orderByChild("foodId").equalTo(foodId);
        foodRating.addValueEventListener(new ValueEventListener() {
            int count = 0, sum = 0;

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot post : snapshot.getChildren()) {
                    Rating item = post.getValue(Rating.class);
                    sum += Integer.parseInt(item.getRateValue());
                    count++;
                }
                if (count != 0) {
                    float average = sum / count;
                    ratingBar.setRating(average);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showRatingDialog() {
        new AppRatingDialog.Builder()
                .setPositiveButtonText("Submit")
                .setNegativeButtonText("Cancel")
                .setNoteDescriptions(Arrays.asList("Very Bad", "Not Good", "Ok", "Very Good", "Excellent"))
                .setDefaultRating(1)
                .setTitle("Rate This Food")
                .setDescription("Please Select Some Stars And Give Your Feedback")
                .setTitleTextColor(R.color.colorPrimary)
                .setDescriptionTextColor(R.color.colorPrimary)
                .setHint("Please write your comment here ...")
                .setHintTextColor(R.color.colorAccent)
                .setCommentTextColor(R.color.white)
                .setCommentBackgroundColor(R.color.colorPrimaryDark)
                .setWindowAnimation(R.style.RatingDialogFadeAnim)
                .create(FoodDetail.this)
                .show();
    }

    private void getDetailFood(String foodId) {
        foods.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                curentFood = snapshot.getValue(Food.class);
                Picasso.with(getBaseContext()).load(curentFood.getImage()).into(foodimage);
                collapsingToolbarLayout.setTitle(curentFood.getName());
                foodprice.setText(curentFood.getPrice());
                foodname.setText(curentFood.getName());
                fooddescription.setText(curentFood.getDescriptions());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onNegativeButtonClicked() {

    }

    @Override
    public void onPositiveButtonClicked(int value, @NonNull String comments) {
        Rating rating = new Rating(Common.currentUser.getPhone(), foodId, String.valueOf(value), comments);
//        ratingTbl.orderByChild("userPhone").equalTo(Common.currentUser.getPhone()).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.child(Common.currentUser.getPhone()).exists()){
//                    ratingTbl.child(Common.currentUser.getPhone()).removeValue();
//                    ratingTbl.child(Common.currentUser.getPhone()).setValue(rating);
//
//                }else {
//                    ratingTbl.child(Common.currentUser.getPhone()).setValue(rating);
//
//                }
//                Toast.makeText(FoodDetail.this, "Thanks For Submit Rating !!", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        ratingTbl.child(Common.currentUser.getPhone()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(Common.currentUser.getPhone()).exists()) {
//                    ratingTbl.child(Common.currentUser.getPhone()).removeValue();
//                    ratingTbl.child(Common.currentUser.getPhone()).setValue(rating);
                    ratingTbl.child(foodId).removeValue();
                    ratingTbl.child(foodId).setValue(rating);


                } else {
                    //               ratingTbl.child(Common.currentUser.getPhone()).setValue(rating);
                    ratingTbl.child(foodId).setValue(rating);

                }
                Toast.makeText(FoodDetail.this, "Cảm Ơn Bài Đáng Giá ", Toast.LENGTH_SHORT).show();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}