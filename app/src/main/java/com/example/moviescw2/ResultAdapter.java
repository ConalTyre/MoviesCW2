package com.example.moviescw2;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.parse.ParseObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ResultAdapter extends RecyclerView.Adapter<ResultsHolder> {

    Context context;
    List<ParseObject> list;
    boolean isChecked;

    // Array Lists
    public ArrayList<String> arrayListChecked = new ArrayList<String>();
    public ArrayList<String> arrayListUnChecked = new ArrayList<String>();

    public ResultAdapter(Context context, List<ParseObject> list, boolean isChecked) {
        this.list = list;
        this.context = context;
        this.isChecked = isChecked;
    }


    @NonNull
    @Override
    public ResultsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.result_cell, parent, false);
        return new ResultsHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultsHolder holder, int position) {
        ParseObject object = list.get(position);
        if (object.getString("title") != null)
        {
            holder.cb.setChecked(isChecked);
        holder.name.setText(object.getString("title"));
    }
        else {
                holder.name.setText("null");
        }

            // Handle Checkbox
            holder.cb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.cb.isChecked())
                    {
                        System.out.println(list.get(position).getString("title"));
                        arrayListChecked.add(list.get(position).getString("title"));
                    }
                    else {
                        arrayListUnChecked.remove(list.get(position).getString("title"));
                        arrayListUnChecked.add(list.get(position).getString("title"));
                    }
                }
            });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditMovies.class);
                intent.putExtra("title", object.getString("title"));
                intent.putExtra("year", object.getString("year"));
                intent.putExtra("director", object.getString("director"));
                intent.putExtra("actors", object.getString("actors"));
                intent.putExtra("rating", object.getString("rating"));
                intent.putExtra("review", object.getString("review"));
                intent.putExtra("fav", object.getString("fav"));
                context.startActivity(intent);
            }
        });

        }
    @Override
    public int getItemCount() {
        return list.size();
    }

    public ArrayList<String> getListChecked()
    {
        return arrayListChecked;
    }

    public ArrayList<String> getArrayListUnChecked()
    {
        return arrayListUnChecked;
    }

    public void clearList()
    {
        list = new ArrayList<>();
        notifyDataSetChanged();
    }
}
