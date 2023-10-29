package com.example.dopinpan.ui.gallery.OrderRemoved;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dopinpan.R;

public class OderRemovedViewHolder extends RecyclerView.ViewHolder {

    public TextView txtOderId,txtOderStatus,txtOderPhone,txtOderAddress,txtOderTotal,txtDateTime,txtReasonRemove;

    public Button btnDetail;



    public OderRemovedViewHolder(@NonNull View itemView) {
        super(itemView);

        txtOderId=itemView.findViewById(R.id.oder_id7);
        txtOderAddress=itemView.findViewById(R.id.oder_address7);
        txtOderPhone=itemView.findViewById(R.id.oder_phone7);
        txtOderStatus=itemView.findViewById(R.id.oder_status7);
        txtOderTotal=itemView.findViewById(R.id.oder_total7);
        txtDateTime=itemView.findViewById(R.id.oder_datetime7);
        txtReasonRemove=itemView.findViewById(R.id.oder_reasonremove7);

        btnDetail=itemView.findViewById(R.id.btn_detail7);



    }

}
