package com.example.dopinpan.ui.gallery.OrderShipped;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dopinpan.R;

public class OderShippedViewHolder extends RecyclerView.ViewHolder {

    public TextView txtOderId,txtOderStatus,txtOderPhone,txtOderAddress,txtOderTotal,txtDateTime;

    public Button btnDetail;



    public OderShippedViewHolder(@NonNull View itemView) {
        super(itemView);

        txtOderId=itemView.findViewById(R.id.oder_id3);
        txtOderAddress=itemView.findViewById(R.id.oder_address3);
        txtOderPhone=itemView.findViewById(R.id.oder_phone3);
        txtOderStatus=itemView.findViewById(R.id.oder_status3);
        txtOderTotal=itemView.findViewById(R.id.oder_total3);
        txtDateTime=itemView.findViewById(R.id.oder_datetime3);

        btnDetail=itemView.findViewById(R.id.btn_detail3);



    }

}
