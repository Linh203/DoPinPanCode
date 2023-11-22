package com.example.dopinpan;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dopinpan.Common.Common;
import com.example.dopinpan.Interface.ItemClickListener;
import com.example.dopinpan.Model.Category;
import com.example.dopinpan.Model.Food;
import com.example.dopinpan.Model.User;
import com.example.dopinpan.Service.ListenOrder;
import com.example.dopinpan.ViewHolder.FoodViewHolder;
import com.example.dopinpan.ViewHolder.MenuViewHolder;
import com.example.dopinpan.databinding.FragmentHomeBinding;
import com.example.dopinpan.ui.gallery.GalleryFragment;
import com.example.dopinpan.ui.home.HomeFragment;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dopinpan.databinding.ActivityHome2Binding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

public class Home2Activity extends AppCompatActivity {

    private TextView txtname;
    private ImageView img;

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHome2Binding binding;
    private FirebaseDatabase database;

    private List<User> user = new ArrayList<>();


    private DatabaseReference category, foodList, users;

    private String categoryId = "1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHome2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        database = FirebaseDatabase.getInstance();

        database = FirebaseDatabase.getInstance();
        category = database.getReference("Category");
        foodList = database.getReference("Foods");
        users = database.getReference("User");


        setSupportActionBar(binding.appBarHome2.toolbar);
        binding.appBarHome2.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home2Activity.this, Cart.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_setting)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home2);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        View headerView = binding.navView.getHeaderView(0);
        txtname = headerView.findViewById(R.id.username);
        img = headerView.findViewById(R.id.imageView1);

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


        Intent intent = new Intent(this, ListenOrder.class);
        startService(intent);



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home2, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.search) {
            Intent intent = new Intent(Home2Activity.this, FoodListAll.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home2);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}