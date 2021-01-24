package com.example.howework03;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import static com.example.howework03.MainActivity.alreadySaved;
import static com.example.howework03.MainActivity.currentCity;
import static com.example.howework03.MainActivity.currentCityName;
import static com.example.howework03.MainActivity.currentCityNotText;
import static com.example.howework03.MainActivity.currentForecast;
import static com.example.howework03.MainActivity.currentImage;
import static com.example.howework03.MainActivity.currentTemp;
import static com.example.howework03.MainActivity.lastUpadted;
import static com.example.howework03.MainActivity.setCurrentCity;
import static com.example.howework03.MainActivity.msg1;
import static com.example.howework03.MainActivity.msg2;

import static com.example.howework03.MainActivity.savedCity;

import static com.example.howework03.MainActivity.RecyclerAdapterSaved;
import static com.example.howework03.MainActivity.recycleViewAdapterSave;

public class CityWeather extends AppCompatActivity {
    int isSaveCity =0;

    String currentBaseURL = "https://dataservice.accuweather.com/currentconditions/v1/";

    String iconImageURL =  "https://developer.accuweather.com/sites/default/files/";

        static int positionDate = 0;
        ArrayList<CityWeatherData> cityWeather;
        String TAG ="demo";

        static TextView cityCountry;
        static TextView headline;
        static TextView forecastOn;
        static TextView temprature;
        static TextView dayForecast;
        static TextView nightForecast;
        static TextView moreInfo;

        static ImageView imageViewDay;
        static ImageView imageViewNight;
        static Button setCurrentButton;
        static Button saveCity;

        static private RecyclerView recyclerView;
        static RecyclerView.Adapter recycleViewAdapter;
        static RecyclerView.LayoutManager layoutManager;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_weather);

        cityCountry = findViewById(R.id.cityCountry);
        headline = findViewById(R.id.headline);
        forecastOn = findViewById(R.id.forecastOn);
        temprature = findViewById(R.id.temprature);
        dayForecast = findViewById(R.id.dayForecast);
        nightForecast = findViewById(R.id.nightForecast);
        moreInfo = findViewById(R.id.moreInfo);
        recyclerView = findViewById(R.id.recyclerView);

        imageViewDay = findViewById(R.id.imageViewDay);
        imageViewNight = findViewById(R.id.imageViewNight);

        setCurrentButton = findViewById(R.id.setCurrentButton);
        saveCity = findViewById(R.id.saveCity);



        if(getIntent()!=null && getIntent().getExtras()!=null) {

            cityWeather = (ArrayList<CityWeatherData>) getIntent().getExtras().getSerializable(MainActivity.CITYKEY);

            setCurrentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    isSaveCity = 0;
                    new GetCurrentWeather().execute(cityWeather.get(0).getCityCode(),cityWeather.get(0).getCountry(),cityWeather.get(0).getCity());
                }
            });
            saveCity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isSaveCity = 1;
                    //savedCity.add(cityWeather.get(0));
                    new GetCurrentWeather().execute(cityWeather.get(0).getCityCode(),cityWeather.get(0).getCountry(),cityWeather.get(0).getCity());
                }
            });

           // final CityWeatherData cityWeather = (CityWeatherData) getIntent().getExtras().getSerializable(MainActivity.CITYKEY);
            new refreshWeather(cityWeather,positionDate);

        }

    }


     static class refreshWeather {
        public refreshWeather(ArrayList<CityWeatherData> cityWeather, int positionDate) {

            cityCountry.setText(cityWeather.get(positionDate).getCity()+", "+cityWeather.get(0).getCountry());
            headline.setText(cityWeather.get(positionDate).getHeadLine());
            forecastOn.setText("Forecast on "+cityWeather.get(positionDate).getEffectiveDate());
            temprature.setText("Temperature: " + cityWeather.get(0).getMinTemp()+"/"+cityWeather.get(positionDate).getMaxTemp()+" "+cityWeather.get(positionDate).getUnitOfTemp());
            dayForecast.setText(cityWeather.get(positionDate).getDayText());
            nightForecast.setText(cityWeather.get(positionDate).getNightText());
            Picasso.get().load(cityWeather.get(positionDate).getDayIconURL().toString()).into(imageViewDay);
            Picasso.get().load(cityWeather.get(positionDate).getNightIconURL()).into(imageViewNight);

            String moreIfoLink = "<a href="+ cityWeather.get(positionDate).getMobileLink()+" > Click here for more details </a>";
            moreInfo.setClickable(true);
            moreInfo.setMovementMethod(LinkMovementMethod.getInstance());
            moreInfo.setText(Html.fromHtml(moreIfoLink, Html.FROM_HTML_MODE_LEGACY));

            recycleViewAdapter = new recyclerAdapter(cityWeather);
            recyclerView.setAdapter(recycleViewAdapter);
        }
    }



    class GetCurrentWeather extends AsyncTask<String,Void,ArrayList<City>>{

        @Override
        protected ArrayList<City> doInBackground(String... strings) {

            HttpURLConnection connection = null;
            //String city_key = "";
            String cityCode = strings[0];
            String CountryName = strings[1];
            String cityName = strings[2];
            Log.d(TAG,"code: "+cityCode+" countryNmae: "+CountryName +" cityName: "+ cityName);

            try {
                String url = currentBaseURL + cityCode + "?"
                        + "apikey=" + URLEncoder.encode(getResources().getString(R.string.api_key), "UTF-8");
                Log.d("link", "URL:" + url);
                URL urlB = new URL(url);

                connection = (HttpURLConnection) urlB.openConnection();

                connection.connect();

                currentCity.clear();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String json = IOUtils.toString(connection.getInputStream(), "UTF8");
                    JSONArray root = new JSONArray(json);

                    City currCity = new City();
                    currCity.setCityname(cityName);
                    currCity.setCountry(CountryName);

                    currCity.setWeatherText(root.getJSONObject(0).getString("WeatherText"));
                    currCity.setTemperature(root.getJSONObject(0).getJSONObject("Temperature").getJSONObject("Imperial").getString("Value"));
                    currCity.setUnitOfTemp(root.getJSONObject(0).getJSONObject("Temperature").getJSONObject("Imperial").getString("Unit"));

                    int imageIcon = root.getJSONObject(0).getInt("WeatherIcon");
                    String ImageIcon2 = String.format("%02d", imageIcon);
                    currCity.setImageURL(iconImageURL + ImageIcon2 + "-s.png");

                    currCity.setLastUpdated(root.getJSONObject(0).getString("EpochTime"));

                    String lastUpdated = currCity.getLastUpdated();
                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(Long.parseLong(lastUpdated)* 1000L);
                    cal.setTimeZone(TimeZone.getTimeZone("UTC"));
                    lastUpdated = String.valueOf(cal.get(Calendar.MINUTE));
                    currCity.setLastUpdated(lastUpdated);
                    currCity.setFavorite(Boolean.FALSE);
                    currentCity.add(currCity);
                    if(!alreadySaved.containsKey(cityCode)){
                        alreadySaved.put(cityCode,cityCode);
                        savedCity.add(currCity);
                    }
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Log.d(TAG,currentCity.toString());
            return currentCity;
        }

        @Override
        protected void onPostExecute(ArrayList<City> cities) {
            super.onPostExecute(cities);
            if (cities.isEmpty()){
                Toast.makeText(CityWeather.this, "Please check if city name is valid and Please Check api key is valid", Toast.LENGTH_SHORT).show();
            }else{
                if(isSaveCity==0){
                    msg1.setVisibility(View.INVISIBLE);
                    msg2.setVisibility(View.INVISIBLE);

                    RecyclerAdapterSaved.setVisibility(View.VISIBLE);
                    RecyclerAdapterSaved.setHasFixedSize(true);
                    recycleViewAdapterSave = new RecyclerAdapterSaved(savedCity);
                    RecyclerAdapterSaved.setAdapter(recycleViewAdapterSave);

                    currentCityNotText.setVisibility(View.INVISIBLE);
                    setCurrentCity.setVisibility(View.INVISIBLE);
                    currentCityName.setVisibility(View.VISIBLE);
                    currentForecast.setVisibility(View.VISIBLE);
                    currentTemp.setVisibility(View.VISIBLE);
                    currentImage.setVisibility(View.VISIBLE);
                    msg1.setVisibility(View.INVISIBLE);
                    msg2.setVisibility(View.INVISIBLE);

                    currentCityName.setText(cities.get(0).getCityname()+ ", "+ cities.get(0).getCountry());
                    currentForecast.setText(cities.get(0).getWeatherText());
                    currentTemp.setText("Temperature: "+cities.get(0).getTemperature()+" "+ cities.get(0).getUnitOfTemp());
                    lastUpadted.setText("Last updated: "+cities.get(0).getLastUpdated()+ " min ago");
                    Picasso.get().load(cities.get(0).getImageURL()).into(currentImage);
                    Toast.makeText(CityWeather.this, "City Updated", Toast.LENGTH_SHORT).show();
                }else{

                    msg1.setVisibility(View.INVISIBLE);
                    msg2.setVisibility(View.INVISIBLE);
                    RecyclerAdapterSaved.setVisibility(View.VISIBLE);
                    RecyclerAdapterSaved.setHasFixedSize(true);
                    recycleViewAdapterSave = new RecyclerAdapterSaved(savedCity);
                    RecyclerAdapterSaved.setAdapter(recycleViewAdapterSave);
                    Toast.makeText(CityWeather.this, "City Saved", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }
}
