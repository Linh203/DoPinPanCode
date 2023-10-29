package com.example.dopinpan.ui.gallery.OrderPlaced;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dopinpan.R;

public class OderPlacedViewHolder extends RecyclerView.ViewHolder {

    public TextView txtOderId,txtOderStatus,txtOderPhone,txtOderAddress,txtOderTotal,txtDateTime;

    public Button btnDetail,btnOrderReceive,btnOrderRemove;



    public OderPlacedViewHolder(@NonNull View itemView) {
        super(itemView);

        txtOderId=itemView.findViewById(R.id.oder_id2);
        txtOderAddress=itemView.findViewById(R.id.oder_address2);
        txtOderPhone=itemView.findViewById(R.id.oder_phone2);
        txtOderStatus=itemView.findViewById(R.id.oder_status2);
        txtOderTotal=itemView.findViewById(R.id.oder_total2);
        txtDateTime=itemView.findViewById(R.id.oder_datetime2);

        btnDetail=itemView.findViewById(R.id.btn_detail2);
        btnOrderReceive=itemView.findViewById(R.id.btn_orderreceive2);
        btnOrderRemove=itemView.findViewById(R.id.btn_orderremove2);


    }

}
