package com.example.dopinpan.Manager.ui.gallery.OrderPlacedManager;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dopinpan.R;

public class OderPlacedViewHolderManager extends RecyclerView.ViewHolder  {

    public TextView txtOderId,txtOderStatus,txtOderPhone,txtOderAddress,txtOderTotal,txtDateTime;

    public Button btnDetail;


    public OderPlacedViewHolderManager(@NonNull View itemView) {
        super(itemView);

        txtOderId=itemView.findViewById(R.id.oder_id5);
        txtOderAddress=itemView.findViewById(R.id.oder_address5);
        txtOderPhone=itemView.findViewById(R.id.oder_phone5);
        txtOderStatus=itemView.findViewById(R.id.oder_status5);
        txtOderTotal=itemView.findViewById(R.id.oder_total5);
        txtDateTime=itemView.findViewById(R.id.oder_datetime5);

        btnDetail=itemView.findViewById(R.id.btn_detailorder5);



    }
}
