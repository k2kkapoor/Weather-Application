package com.example.howework03;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.example.howework03.CityWeather.positionDate;

public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.ViewHolder> {

    //static String TRACK_KEY = "TRACK";
    int selectedPosition=-1;

    ArrayList<CityWeatherData> tdata;

    public recyclerAdapter(ArrayList<CityWeatherData> tdata) {
        this.tdata = tdata;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_five_day_weather, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


//        Log.d("demo",tdata.get(position).getCurrent().toString());
//        if (tdata.get(position).getCurrent().equals(Boolean.FALSE)){
//            CityWeatherData cityWeather = tdata.get(position);
//            final int currentPosition = position;
//            Log.d("demochecked",tdata.toString());
//            Picasso.get().load(cityWeather.getDayIconURL()).into(holder.imageViewIcon);
//            holder.date.setText(cityWeather.getEffectiveDateRecycler());
//
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    positionDate=currentPosition;
//                    Log.d("clicked", String.valueOf(positionDate));
//                    new CityWeather.refreshWeather(tdata,currentPosition);
//                    notifyDataSetChanged();
//                }
//            });
//            holder.cityWeather = cityWeather;
//        }

        CityWeatherData cityWeather = tdata.get(position);
        final int currentPosition = position;

        Picasso.get().load(cityWeather.getDayIconURL()).into(holder.imageViewIcon);
        holder.date.setText(cityWeather.getEffectiveDateRecycler());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                positionDate=currentPosition;
                Log.d("clicked", String.valueOf(positionDate));
                new CityWeather.refreshWeather(tdata,currentPosition);
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

        ImageView imageViewIcon;
        TextView  date;
        CityWeatherData cityWeather;
        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            itemView.setClickable(true);

            imageViewIcon =itemView.findViewById(R.id.imageViewIcon);
            date = itemView.findViewById(R.id.date);


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
