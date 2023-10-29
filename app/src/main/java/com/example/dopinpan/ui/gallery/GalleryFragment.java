package com.example.dopinpan.ui.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.dopinpan.databinding.FragmentGalleryBinding;
import com.example.dopinpan.ui.gallery.OrderHistory.OrderHistory;
import com.example.dopinpan.ui.gallery.OrderPlaced.OrderPlaced;
import com.example.dopinpan.ui.gallery.OrderRemoved.OrderRemoved;
import com.example.dopinpan.ui.gallery.OrderShipped.OrderShipped;

public class GalleryFragment extends Fragment {


    private FragmentGalleryBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.btnOrderhistory1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), OrderHistory.class);
                startActivity(intent);
            }
        });


        binding.btnOrderplaced1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), OrderPlaced.class);
                startActivity(intent);

            }
        });
        binding.btnOrdershipped1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), OrderShipped.class);
                startActivity(intent);
            }
        });
        binding.btnOrderremoved1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), OrderRemoved.class);
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