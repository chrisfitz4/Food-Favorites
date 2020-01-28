package com.illicitintelligence.android.foodfinder.adapter;

import android.content.ClipData;
import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.illicitintelligence.android.foodfinder.R;
import com.illicitintelligence.android.foodfinder.database.FavoriteEntity;

import java.util.List;

public class RVAdapterFaves extends RecyclerView.Adapter<RVAdapterFaves.ViewHolder> {

    private final String TAG = "TAG_X";
    List<FavoriteEntity> entities;
    Clicker clicker;
    Context context;

    public interface Clicker{
        void click(FavoriteEntity entity);
    }

    public RVAdapterFaves(List<FavoriteEntity> entities, Clicker clicker, Context context) {
        this.entities = entities;
        this.clicker = clicker;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorites_rv,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(entities.get(position).getRestaurantName());
        Glide.with(context)
                .load(entities.get(position).getImageURL())
                .into(holder.restaurantPic);
        Log.d(TAG, "onBindViewHolder: "+entities.get(position).getImageURL());
        holder.itemView.setOnClickListener(view->{
            clicker.click(entities.get(position));
        });

    }


    @Override
    public int getItemCount() {
        return entities.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView restaurantPic;
        TextView title;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            restaurantPic = itemView.findViewById(R.id.rest_pic);
            title = itemView.findViewById(R.id.titleRest);
        }
    }
}
