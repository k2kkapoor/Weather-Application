package com.example.howework03;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.howework03.MainActivity.currentCityName;
import static com.example.howework03.MainActivity.currentForecast;
import static com.example.howework03.MainActivity.currentImage;
import static com.example.howework03.MainActivity.currentTemp;
import static com.example.howework03.MainActivity.savedCity;
import static com.example.howework03.MainActivity.savedCity;

import android.util.Log;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class RecyclerAdapterSaved extends RecyclerView.Adapter<RecyclerAdapterSaved.ViewHolder> {

    ArrayList<City> tdata;

    public RecyclerAdapterSaved(ArrayList<City> tdata) {
        this.tdata = tdata;
        Log.d("recyclerAdapter","tdata: "+tdata.toString());
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_recycler_saved, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {


        final City cityWeather = tdata.get(position);

        holder.savedCityName.setText(cityWeather.getCityname() +", "+cityWeather.getCountry());
        holder.tempSaved.setText("Temperature: "+cityWeather.getTemperature() + " " + cityWeather.getUnitOfTemp());
        holder.upadated.setText("Last updated: "+tdata.get(position).getLastUpdated()+" min ago");
        if(tdata.get(position).getFavorite()){
            holder.imageStar.setImageResource(android.R.drawable.btn_star_big_on);
        }

       holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
           @Override
           public boolean onLongClick(View v) {
               savedCity.remove(position);
               notifyDataSetChanged();
               return true;
           }
       });

       holder.imageStar.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(tdata.get(position).getFavorite()){
                   holder.imageStar.setImageResource(android.R.drawable.btn_star_big_off);
                   savedCity.get(position).setFavorite(Boolean.FALSE);
               }else{
                   holder.imageStar.setImageResource(android.R.drawable.btn_star_big_on);
                   savedCity.get(position).setFavorite(Boolean.TRUE);
               }
           }
       });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentCityName.setText(cityWeather.getCityname() + ", "+ cityWeather.getCountry());
                currentForecast.setText(cityWeather.getWeatherText());
                currentTemp.setText("Temperature: "+cityWeather.getTemperature()+" "+cityWeather.getUnitOfTemp());
                Picasso.get().load(cityWeather.getImageURL()).into(currentImage);
                notifyDataSetChanged();
            }
        });

        holder.cityWeather = cityWeather;
    }

    @Override
    public int getItemCount() {
        return tdata.size();
    }

    public final class ViewHolder extends RecyclerView.ViewHolder {

        ImageButton imageStar;
        TextView  savedCityName;
        TextView  tempSaved;
        TextView  upadated;
        City cityWeather;
        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            itemView.setClickable(true);

            imageStar =itemView.findViewById(R.id.imageStar);
            savedCityName = itemView.findViewById(R.id.savedCityName);
            tempSaved = itemView.findViewById(R.id.tempSaved);
            upadated = itemView.findViewById(R.id.upadated);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Log.d("demoClicked",cityWeather.toString());
//
//                }
//            });


        }

    }
}
