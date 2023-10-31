package com.example.dopinpan.Manager.ui.gallery.OrderShippedManager;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dopinpan.R;

public class OderShippedViewHolderManager extends RecyclerView.ViewHolder  {

    public TextView txtOderId,txtOderStatus,txtOderPhone,txtOderAddress,txtOderTotal,txtDateTime;

    public Button btnDetail;




    public OderShippedViewHolderManager(@NonNull View itemView) {
        super(itemView);

        txtOderId=itemView.findViewById(R.id.oder_id6);
        txtOderAddress=itemView.findViewById(R.id.oder_address6);
        txtOderPhone=itemView.findViewById(R.id.oder_phone6);
        txtOderStatus=itemView.findViewById(R.id.oder_status6);
        txtOderTotal=itemView.findViewById(R.id.oder_total6);
        txtDateTime=itemView.findViewById(R.id.oder_datetime6);

        btnDetail=itemView.findViewById(R.id.btn_detailorder6);




    }
}
