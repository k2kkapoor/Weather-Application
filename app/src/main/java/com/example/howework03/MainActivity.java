package com.example.howework03;

/**
 a. Assignment Homework03  (Weather app)
 b. File Name: MainActivity.java
 c. Submitted by: Karan Kapoor & Surabhi Gurav
 **/

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    static ArrayList<City> currentCity = new ArrayList<>();
    static ArrayList<City> savedCity = new ArrayList<>();
    static HashMap<String,String> alreadySaved = new HashMap<>();

    static Button setCurrentCity;
    static Button search_city;

    static ProgressBar progressBar;

    EditText city_name;
    EditText country_name;

    static String citiesBaseURL = "https://dataservice.accuweather.com/locations/v1/cities/";

    String currentBaseURL = "https://dataservice.accuweather.com/currentconditions/v1/";

    String fiveDayBaseURL = "https://dataservice.accuweather.com/forecasts/v1/daily/5day/";

    String iconImageURL =  "https://developer.accuweather.com/sites/default/files/";

    String TAG = "demo";
    static String typeSearchCurrent = "";

    static String CITYKEY = "CITYKEY";

    static TextView currentCityName;
    static TextView currentForecast;
    static TextView currentTemp;
    static TextView lastUpadted;
    static TextView currentCityNotText;
    static TextView msg1;
    static TextView msg2;
    static ImageView currentImage;

    static RecyclerView RecyclerAdapterSaved;
    static RecyclerView.Adapter recycleViewAdapterSave;
    static RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        RecyclerAdapterSaved =findViewById(R.id.recyclerSaved);
        RecyclerAdapterSaved.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(MainActivity.this);
        RecyclerAdapterSaved.setLayoutManager(layoutManager);

        currentCityName = findViewById(R.id.currentCityName);
        currentForecast = findViewById(R.id.currentForecast);
        currentTemp = findViewById(R.id.currentTemp);
        lastUpadted = findViewById(R.id.lastUpadted);
        currentImage = findViewById(R.id.currentImage);

        msg1 = findViewById(R.id.msg1);
        msg2 = findViewById(R.id.msg2);

        progressBar = findViewById(R.id.progressBar);


        setCurrentCity = findViewById(R.id.setCurrentCity);
        search_city = findViewById(R.id.search_city);

        city_name = findViewById(R.id.city_name);
        country_name = findViewById(R.id.country_name);

        currentCityNotText = findViewById(R.id.currentCityNotText);



        if(isConnected()){

            search_city.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(city_name.getText().toString().equals("")){
                        city_name.setError("Please enter a city");
                    }else if(country_name.getText().toString().equals("")){
                        country_name.setError("Please enter the Country");
                    }else{
                        typeSearchCurrent = "search";
                        new GetCurrentCondition().execute(country_name.getText().toString(),city_name.getText().toString());
                    }
                }
            });

            setCurrentCity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(v.getContext());
                    dialog.setContentView(R.layout.activity_alert_dialog);
                    final TextView cityNameAlert =  dialog.findViewById(R.id.cityNameAlert);
                    final TextView countryNameAlert = dialog.findViewById(R.id.countryNameAlert);
                    Button cancelBtnAlertcs= dialog.findViewById(R.id.cancelBtnAlert);
                    Button okBtnAlert = dialog.findViewById(R.id.saveBtnAlert);
                    okBtnAlert.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(cityNameAlert.getText().toString().equals("")){
                                cityNameAlert.setError("Please Enter City");
                            }else if (countryNameAlert.getText().toString().equals("")){
                                countryNameAlert.setError("Please Enter Country");
                            }
                            else{
                                typeSearchCurrent = "current";
                                dialog.dismiss();
                                progressBar.setVisibility(View.VISIBLE);
                                new GetCurrentCondition().execute(countryNameAlert.getText().toString(),cityNameAlert.getText().toString());
                            }
                        }
                    });

                    cancelBtnAlertcs.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.cancel();
                        }
                    });
                    dialog.show();
                }
            });
        }else{
            Toast.makeText(this, "Not Connected to internet", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected() ||
                (networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                        && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)) {
            return false;
        }
        return true;
    }


    class GetCurrentCondition  extends AsyncTask<String, Void, ArrayList<SearchCity>> implements com.example.howework03.GetCurrentCondition{

        @Override
        protected ArrayList<SearchCity> doInBackground(String... strings) {

            ArrayList<SearchCity> citiesSearchedSaved = new ArrayList<>();

            HttpURLConnection connection = null;
            //String city_key = "";
            String countryName = strings[0];
            String cityName = strings[1];

            try {
                String url = citiesBaseURL + countryName + "/search" + "?"
                        + "apikey=" + URLEncoder.encode(getResources().getString(R.string.api_key), "UTF-8") +
                        "&" + "q=" + URLEncoder.encode(cityName, "UTF-8");
                URL urlB = new URL(url);

                connection = (HttpURLConnection) urlB.openConnection();

                connection.connect();

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String json = IOUtils.toString(connection.getInputStream(), "UTF8");
                    JSONArray root = new JSONArray(json);

                    //city_keys = new String[root.length()];
                    for (int i = 0; i < root.length(); i++) {
                        JSONObject cityInfoJSON = root.getJSONObject(i);
                        SearchCity searchCity1 = new SearchCity();
                        searchCity1.setCityKey(cityInfoJSON.getInt("Key"));
                        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                        String cityReadLine = br.readLine();
                        String s1 = cityName.substring(0, 1).toUpperCase();
                        String finalcityName = s1 + cityName.substring(1);
                        searchCity1.setCityName(finalcityName);
                        searchCity1.setCountryName(countryName.toUpperCase());
                        String state = cityInfoJSON.getJSONObject("AdministrativeArea").getString("ID");
                        searchCity1.setId(state);
                        citiesSearchedSaved.add(searchCity1);
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
            return citiesSearchedSaved;
        }

        @Override
        protected void onPostExecute(final ArrayList<SearchCity> strings) {
            super.onPostExecute(strings);
            if (strings.isEmpty()){
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(MainActivity.this, "Please check if city name is valid and Please Check api key is valid", Toast.LENGTH_SHORT).show();
            }else{
                Log.d("values", String.valueOf(strings.get(0).cityKey));
                String countryName = "";
                String cityname = "";
                final String[] cityState = new String[strings.size()];
                final HashMap<String,Integer> cityKeyPair = new HashMap<>();
                int i = 0;
                for (SearchCity listItem:strings) {
                    cityState[i] = listItem.getCityName()+", "+listItem.getId();
                    cityKeyPair.put(listItem.getCityName()+", "+listItem.getId(),listItem.cityKey);
                    countryName = listItem.getCountryName();
                    cityname = listItem.getCityName();
                    i++;
                }

                if (strings.isEmpty()){
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(MainActivity.this,"No City found",Toast.LENGTH_SHORT).show();

                }else{

                    if(typeSearchCurrent.equals("search")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        final String finalCountryName = countryName;
                        final String finalCityname = cityname;
                        builder.setTitle("Choose a Keyword").setItems(cityState, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String keyword = (String) cityState[which];
                                if(cityKeyPair.containsKey(keyword)){
                                    progressBar.setVisibility(View.VISIBLE);
                                    new GetCityWeather().execute(cityKeyPair.get(keyword).toString(), finalCountryName, finalCityname);
                                }
                            }
                        });
                        Dialog alertDialogObject = builder.create();
                        ListView listView= ((AlertDialog) alertDialogObject).getListView();
                        listView.setDivider(new ColorDrawable(Color.BLACK)); // set color
                        listView.setDividerHeight(5); // set height

                        alertDialogObject.show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                    if(typeSearchCurrent.equals("current")){
                        progressBar.setVisibility(View.VISIBLE);
                        new GetCurrentWeather().execute(String.valueOf(strings.get(0).cityKey), countryName, cityname);
                    }
                }
            }
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

            try {
                String url = currentBaseURL + cityCode + "?"
                        + "apikey=" + URLEncoder.encode(getResources().getString(R.string.api_key), "UTF-8");
                Log.d("link", "URL:" + url);
                URL urlB = new URL(url);

                connection = (HttpURLConnection) urlB.openConnection();

                connection.connect();

                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    String json = IOUtils.toString(connection.getInputStream(), "UTF8");
                    JSONArray root = new JSONArray(json);

                    City currCity = new City();
                    currCity.setCityname(cityName);
                    currCity.setCountry(CountryName);

                    currCity.setWeatherText(root.getJSONObject(0).getString("WeatherText"));
                    Log.d(TAG,root.getJSONObject(0).getString("WeatherText"));
                    currCity.setTemperature(root.getJSONObject(0).getJSONObject("Temperature").getJSONObject("Imperial").getString("Value"));
                    currCity.setUnitOfTemp(root.getJSONObject(0).getJSONObject("Temperature").getJSONObject("Imperial").getString("Unit"));


                    currCity.setLastUpdated(root.getJSONObject(0).getString("EpochTime"));

                    String lastUpdated = currCity.getLastUpdated();
                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(Long.parseLong(lastUpdated)* 1000L);
                    cal.setTimeZone(TimeZone.getTimeZone("UTC"));
                    lastUpdated = String.valueOf(cal.get(Calendar.MINUTE));
                    currCity.setLastUpdated(lastUpdated);
                   // Log.d("minutes", lastUpdated+"");

                    int imageIcon = root.getJSONObject(0).getInt("WeatherIcon");
                    String ImageIcon2 = String.format("%02d", imageIcon);
                    currCity.setImageURL(iconImageURL + ImageIcon2 + "-s.png");
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
            Log.d(TAG,currentCity.toString());
            return currentCity;
        }

        @Override
        protected void onPostExecute(ArrayList<City> cities) {
            super.onPostExecute(cities);

            currentCityNotText.setVisibility(View.INVISIBLE);
            setCurrentCity.setVisibility(View.INVISIBLE);

            msg1.setVisibility(View.INVISIBLE);
            msg2.setVisibility(View.INVISIBLE);

            RecyclerAdapterSaved.setVisibility(View.VISIBLE);

            currentCityName.setVisibility(View.VISIBLE);
            currentForecast.setVisibility(View.VISIBLE);
            currentTemp.setVisibility(View.VISIBLE);
            currentImage.setVisibility(View.VISIBLE);
            lastUpadted.setVisibility(View.VISIBLE);

            lastUpadted.setText("Last updated: "+cities.get(0).getLastUpdated()+ " min ago");

            recycleViewAdapterSave = new RecyclerAdapterSaved(cities);
            RecyclerAdapterSaved.setAdapter(recycleViewAdapterSave);

            currentCityName.setText(cities.get(0).getCityname()+ ", "+ cities.get(0).getCountry());
            currentForecast.setText(cities.get(0).getWeatherText());
            currentTemp.setText("Temperature: "+cities.get(0).getTemperature()+" "+ cities.get(0).getUnitOfTemp());
            Picasso.get().load(cities.get(0).getImageURL()).into(currentImage);

            progressBar.setVisibility(View.INVISIBLE);

            Toast.makeText(MainActivity.this, "Current City Saved", Toast.LENGTH_SHORT).show();
        }
    }


    class GetCityWeather extends AsyncTask<String, Void, ArrayList<CityWeatherData>> {

        @Override
        protected ArrayList<CityWeatherData> doInBackground(String... strings) {
            HttpURLConnection connection = null;
            //String city_key = "";
            String cityCode = strings[0];
            String CountryName = strings[1];
            String cityName = strings[2];

                ArrayList<CityWeatherData> fiveDayCityWeater = new ArrayList<>();

                try {
                    String url = fiveDayBaseURL + cityCode + "?"
                            + "apikey=" + URLEncoder.encode(getResources().getString(R.string.api_key), "UTF-8");
                    URL urlB = new URL(url);

                    connection = (HttpURLConnection) urlB.openConnection();

                    connection.connect();

                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        String json = IOUtils.toString(connection.getInputStream(), "UTF8");
                        JSONObject root = new JSONObject(json);
                        JSONArray fiveDayValue = root.getJSONArray("DailyForecasts");
                        for (int i = 0; i < fiveDayValue.length(); i++) {
                            JSONObject fiveDCityInfoJSON = fiveDayValue.getJSONObject(i);
                            CityWeatherData cityWeather = new CityWeatherData();

                            cityWeather.setCity(cityName);
                            cityWeather.setCountry(CountryName);
                            String headline = root.getJSONObject("Headline").getString("Text");

                            cityWeather.setHeadLine(headline);

                            cityWeather.setEffectiveDate(fiveDCityInfoJSON.getString("Date"));
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                            Date date = formatter.parse(cityWeather.getEffectiveDate());
                            cityWeather.setSetDate(date);
                            SimpleDateFormat newFormat = new SimpleDateFormat("MMMM dd, yyyy");
                            SimpleDateFormat newFormat2 = new SimpleDateFormat("dd MMM,yy");
                            cityWeather.setEffectiveDate(newFormat.format(date));
                            cityWeather.setEffectiveDateRecycler(newFormat2.format(date));

                            String minTemp = fiveDCityInfoJSON.getJSONObject("Temperature").getJSONObject("Minimum").getString("Value");
                            cityWeather.setMinTemp(minTemp);
                            String maxTemp = fiveDCityInfoJSON.getJSONObject("Temperature").getJSONObject("Maximum").getString("Value");
                            cityWeather.setMaxTemp(maxTemp);
                            String unitTemp = fiveDCityInfoJSON.getJSONObject("Temperature").getJSONObject("Maximum").getString("Unit");
                            cityWeather.setUnitOfTemp(unitTemp);

                            int dayIcon = fiveDCityInfoJSON.getJSONObject("Day").getInt("Icon");
                            String dayI = String.format("%02d", dayIcon);
                            cityWeather.setDayIconURL(iconImageURL + dayI + "-s.png");
                            String dayTempText = fiveDCityInfoJSON.getJSONObject("Day").getString("IconPhrase");
                            cityWeather.setDayText(dayTempText);

                            int nightIcon = fiveDCityInfoJSON.getJSONObject("Night").getInt("Icon");
                            String nightI = String.format("%02d", nightIcon);
                            cityWeather.setNightIconURL(iconImageURL + nightI + "-s.png");
                            String nightTempText = fiveDCityInfoJSON.getJSONObject("Night").getString("IconPhrase");
                            cityWeather.setNightText(nightTempText);

                            cityWeather.setMobileLink(fiveDCityInfoJSON.getString("MobileLink"));

                            if (i == 0){
                                cityWeather.setCurrent(Boolean.TRUE);
                            }
                            else{
                                cityWeather.setCurrent(Boolean.FALSE);
                            }
                            cityWeather.setCityCode(cityCode);

                            fiveDayCityWeater.add(cityWeather);
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
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return fiveDayCityWeater;
        }


        @Override
        protected void onPostExecute(ArrayList<CityWeatherData> cityWeatherData) {
            super.onPostExecute(cityWeatherData);
            progressBar.setVisibility(View.INVISIBLE);
            if (cityWeatherData.isEmpty()){

                Toast.makeText(MainActivity.this, "Please Check api key is valid and internet is connected", Toast.LENGTH_SHORT).show();
            }else{
                Intent cityWeatherActivity = new Intent(MainActivity.this,CityWeather.class);
                cityWeatherActivity.putExtra(CITYKEY,cityWeatherData);
                startActivity(cityWeatherActivity);
            }

        }
    }



}
