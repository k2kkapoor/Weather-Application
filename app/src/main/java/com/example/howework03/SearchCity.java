package com.example.howework03;

public class SearchCity {

    String cityName;
    String countryName;
    String id;
    int cityKey;

    public SearchCity() {
    }

    @Override
    public String toString() {
        return "SearchCity{" +
                "cityName='" + cityName + '\'' +
                ", countryName='" + countryName + '\'' +
                ", id='" + id + '\'' +
                ", cityKey=" + cityKey +
                '}';
    }


    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCityKey() {
        return cityKey;
    }

    public void setCityKey(int cityKey) {
        this.cityKey = cityKey;
    }
}
