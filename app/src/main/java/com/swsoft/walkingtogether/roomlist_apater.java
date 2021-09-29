package com.swsoft.walkingtogether;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class roomlist_apater extends RecyclerView.Adapter {

    Context context;
    ArrayList<roomlist_item> items;

    public roomlist_apater(Context context, ArrayList<roomlist_item> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.roomlist_item, parent, false);
        VH holder = new VH(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        VH vh = (VH)holder;
       roomlist_item item = items.get(position);

       vh.title_view.setText(item.title);
       vh.time_view.setText(item.time);
       vh.goal_view.setText(item.goal);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class VH extends RecyclerView.ViewHolder{
        Button title_view;
        Button time_view;
        Button goal_view;

        public VH(@NonNull View itemView) {
            super(itemView);
            title_view = itemView.findViewById(R.id.roomtitle);
            time_view = itemView.findViewById(R.id.roomtime);
            goal_view = itemView.findViewById(R.id.roomgoal);

        }
    }


}
