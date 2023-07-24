package com.shahareinisim.tzachiapp;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolderTPart extends RecyclerView.ViewHolder {

    TextView text;
    ImageView indicator;
    public ViewHolderTPart(@NonNull View itemView) {
        super(itemView);

        text = itemView.findViewById(R.id.text);
        indicator = itemView.findViewById(R.id.indicator);
    }
}
