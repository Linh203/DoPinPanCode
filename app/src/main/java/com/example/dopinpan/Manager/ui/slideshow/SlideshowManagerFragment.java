package com.example.dopinpan.Manager.ui.slideshow;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.dopinpan.Common.Common;
import com.example.dopinpan.Database.Database;
import com.example.dopinpan.MainActivity;
import com.example.dopinpan.Manager.HomeManagerActivity;
import com.example.dopinpan.R;
import com.example.dopinpan.SplashActivity;
import com.example.dopinpan.databinding.FragmentSlideshowBinding;
import com.example.dopinpan.databinding.FragmentSlideshowManagerBinding;

import io.paperdb.Paper;

public class SlideshowManagerFragment extends Fragment {

    private FragmentSlideshowManagerBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SlideshowManagerViewModel slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowManagerViewModel.class);

        binding = FragmentSlideshowManagerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.btnTabuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Handler handler = new Handler();
                ProgressDialog dialog = new ProgressDialog(getContext());
                dialog.setMessage("Vui Lòng Đợi ...");
                dialog.show();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "Welcome Back With User !", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getContext(), SplashActivity.class);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                }, 2000);
            }
        });

        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogLogout();

            }
        });


        return root;
    }

    private void showDialogLogout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Thông Báo");
        builder.setIcon(R.drawable.baseline_warning_24);
        builder.setMessage("Vui Lòng Xác Nhận Đăng Xuất");

        builder.setPositiveButton("Đăng Xuất", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Handler handler = new Handler();
                ProgressDialog dialog = new ProgressDialog(getContext());
                dialog.setMessage("Vui Lòng Đợi ...");
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
                }, 2000);
            }
        });
        builder.setNegativeButton("Thoát", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
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