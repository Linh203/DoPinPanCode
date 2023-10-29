package com.example.dopinpan.ui.gallery.OrderHistory;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dopinpan.R;

public class OderHistoryViewHolder extends RecyclerView.ViewHolder {

    public TextView txtOderId,txtOderStatus,txtOderPhone,txtOderAddress,txtOderTotal,txtDateTime;

    public Button btnDetail,btnOrderRemove;



    public OderHistoryViewHolder(@NonNull View itemView) {
        super(itemView);

        txtOderId=itemView.findViewById(R.id.oder_id1);
        txtOderAddress=itemView.findViewById(R.id.oder_address1);
        txtOderPhone=itemView.findViewById(R.id.oder_phone1);
        txtOderStatus=itemView.findViewById(R.id.oder_status1);
        txtOderTotal=itemView.findViewById(R.id.oder_total1);
        txtDateTime=itemView.findViewById(R.id.oder_datetime1);

        btnDetail=itemView.findViewById(R.id.btn_detail1);
        btnOrderRemove=itemView.findViewById(R.id.btn_orderremove1);



    }

}
