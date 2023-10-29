package com.example.dopinpan.Manager.ui.gallery.OrderRemovedManager;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dopinpan.R;

public class OderRemovedManagerViewHolder extends RecyclerView.ViewHolder {

    public TextView txtOderId,txtOderStatus,txtOderPhone,txtOderAddress,txtOderTotal,txtDateTime,txtReasonRemove;

    public Button btnDetail;



    public OderRemovedManagerViewHolder(@NonNull View itemView) {
        super(itemView);

        txtOderId=itemView.findViewById(R.id.oder_id8);
        txtOderAddress=itemView.findViewById(R.id.oder_address8);
        txtOderPhone=itemView.findViewById(R.id.oder_phone8);
        txtOderStatus=itemView.findViewById(R.id.oder_status8);
        txtOderTotal=itemView.findViewById(R.id.oder_total8);
        txtDateTime=itemView.findViewById(R.id.oder_datetime8);
        txtReasonRemove=itemView.findViewById(R.id.oder_reasonremove8);

        btnDetail=itemView.findViewById(R.id.btn_detail8);



    }

}
