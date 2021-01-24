package com.example.howework03;

public class City {


    int citykey;
    String cityname;
    String country;
    String weatherText;
    String temperature;
    String unitOfTemp;
    String imageURL;
    Boolean favorite;
    String lastUpdated;


    public City() {
    }

    @Override
    public String toString() {
        return "City{" +
                "citykey=" + citykey +
                ", cityname='" + cityname + '\'' +
                ", country='" + country + '\'' +
                ", weatherText='" + weatherText + '\'' +
                ", temperature='" + temperature + '\'' +
                ", unitOfTemp='" + unitOfTemp + '\'' +
                ", imageURL='" + imageURL + '\'' +
                ", favorite=" + favorite +
                ", lastUpdated='" + lastUpdated + '\'' +
                '}';
    }

    public int getCitykey() {
        return citykey;
    }

    public void setCitykey(int citykey) {
        this.citykey = citykey;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getWeatherText() {
        return weatherText;
    }

    public void setWeatherText(String weatherText) {
        this.weatherText = weatherText;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getUnitOfTemp() {
        return unitOfTemp;
    }

    public void setUnitOfTemp(String unitOfTemp) {
        this.unitOfTemp = unitOfTemp;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
