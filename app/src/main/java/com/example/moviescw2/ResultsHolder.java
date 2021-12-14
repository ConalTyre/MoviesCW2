package com.example.moviescw2;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ResultsHolder extends RecyclerView.ViewHolder {

    TextView name;
    CheckBox cb;
    public ResultsHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.textViewName);
        cb = itemView.findViewById(R.id.chkBox);
    }
}
