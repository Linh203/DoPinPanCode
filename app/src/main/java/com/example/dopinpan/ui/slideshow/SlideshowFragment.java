package com.example.dopinpan.ui.slideshow;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.dopinpan.Common.Common;
import com.example.dopinpan.Database.Database;
import com.example.dopinpan.Home2Activity;
import com.example.dopinpan.MainActivity;
import com.example.dopinpan.Manager.HomeManagerActivity;
import com.example.dopinpan.Manager.ui.home.HomeManagerFragment;
import com.example.dopinpan.R;
import com.example.dopinpan.SplashManager;
import com.example.dopinpan.UserProfile;
import com.example.dopinpan.databinding.FragmentSlideshowBinding;

import io.paperdb.Paper;

public class SlideshowFragment extends Fragment {


    private FragmentSlideshowBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SlideshowViewModel slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogLogout();
            }
        });

        binding.btnUserprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Handler handler=new Handler();
                ProgressDialog dialog = new ProgressDialog(getContext());
                dialog.setMessage("Vui Lòng Đợi ...");
                dialog.show();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent=new Intent(getContext(), UserProfile.class);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                },2000);
            }
        });

        binding.btnTabmanager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Handler handler=new Handler();
                ProgressDialog dialog = new ProgressDialog(getContext());
                dialog.setMessage("Vui Lòng Đợi ...");
                dialog.show();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (Common.currentUser.getIsStaff().equals("false")){
                            dialog.dismiss();
                            showDialogIsStaff();
                        }
                        else {
                            Toast.makeText(getContext(), "Welcome Back With Manager !", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(getContext(), SplashManager.class);
                            startActivity(intent);
                            dialog.dismiss();
                        }

                    }
                },2000);
            }
        });

        return root;
    }

    private void showDialogIsStaff() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Notification ");
        builder.setIcon(R.drawable.baseline_notification_important_24);
        builder.setMessage("Your account has not been granted administrator rights !!");
        builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.show();

    }

    private void showDialogLogout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Warning !!");
        builder.setIcon(R.drawable.baseline_warning_24);
        builder.setMessage("Do You Want Logout ?");

        builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Handler handler = new Handler();
                ProgressDialog dialog = new ProgressDialog(getContext());
                dialog.setMessage("Please Waiting ...");
                dialog.show();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        new Database(getContext()).cleanCart();
                        new Database(getContext()).clearFavorites();
                        Paper.book().destroy();
                        startActivity(intent);
                        dialog.dismiss();
                    }
                },2000);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}