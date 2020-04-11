package com.bignerdranch.android.myapplication.WAPfunctions.list;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bignerdranch.android.myapplication.R;

/**
 * Created by mythmayor on 2017/6/2.
 */

public class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView tvMy;

    public MyViewHolder(View itemView) {
        super(itemView);
        tvMy = (TextView) itemView.findViewById(R.id.tv_my);
    }
}
