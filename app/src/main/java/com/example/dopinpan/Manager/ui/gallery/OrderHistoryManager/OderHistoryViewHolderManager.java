package com.example.dopinpan.Manager.ui.gallery.OrderHistoryManager;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dopinpan.R;

public class OderHistoryViewHolderManager extends RecyclerView.ViewHolder  {

    public TextView txtOderId,txtOderStatus,txtOderPhone,txtOderAddress,txtOderTotal,txtDateTime;

    public Button btnEdit,btnRemove,btnDetail;


    public OderHistoryViewHolderManager(@NonNull View itemView) {
        super(itemView);

        txtOderId=itemView.findViewById(R.id.oder_id4);
        txtOderAddress=itemView.findViewById(R.id.oder_address4);
        txtOderPhone=itemView.findViewById(R.id.oder_phone4);
        txtOderStatus=itemView.findViewById(R.id.oder_status4);
        txtOderTotal=itemView.findViewById(R.id.oder_total4);
        txtDateTime=itemView.findViewById(R.id.oder_datetime4);

        btnDetail=itemView.findViewById(R.id.btn_detailorder4);
        btnEdit=itemView.findViewById(R.id.btn_editorder4);
        btnRemove=itemView.findViewById(R.id.btn_removeorder4);


    }
}
