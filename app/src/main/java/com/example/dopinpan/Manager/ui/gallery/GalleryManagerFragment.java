package com.example.dopinpan.Manager.ui.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.dopinpan.Manager.ui.gallery.OrderHistoryManager.OrderHistoryManager;
import com.example.dopinpan.Manager.ui.gallery.OrderPlacedManager.OrderPlacedManager;
import com.example.dopinpan.Manager.ui.gallery.OrderRemovedManager.OrderRemovedManager;
import com.example.dopinpan.Manager.ui.gallery.OrderShippedManager.OrderShippedManager;
import com.example.dopinpan.Manager.ui.gallery.StatisticalManager.StatisticalManager;
import com.example.dopinpan.databinding.FragmentGalleryManagerBinding;

public class GalleryManagerFragment extends Fragment {

    private FragmentGalleryManagerBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GalleryManagerViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryManagerViewModel.class);

        binding = FragmentGalleryManagerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.btnOrderhistory2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), OrderHistoryManager.class);
                startActivity(intent);
            }
        });

        binding.btnOrderplaced2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), OrderPlacedManager.class);
                startActivity(intent);
            }
        });
        binding.btnOrderremoved2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), OrderRemovedManager.class);
                startActivity(intent);
            }
        });
        binding.btnOrdershipped2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), OrderShippedManager.class);
                startActivity(intent);
            }
        });
        binding.btnStatistical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), StatisticalManager.class);
                startActivity(intent);
            }
        });


        return root;
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}