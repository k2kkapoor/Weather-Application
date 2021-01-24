package com.example.howework03;

import java.io.Serializable;
import java.util.Date;

public class CityWeatherData implements Serializable {
    String cityCode;
    String city;
    String country;
    String headLine;
    String mobileLink;
    String minTemp;
    String maxTemp;
    String dayIconURL;
    String dayText;
    String nightIconURL;
    String nightText;
    String unitOfTemp;
    String effectiveDate;
    String effectiveDateRecycler;
    Boolean isCurrent;
    Date setDate;

    public CityWeatherData() {
    }

    @Override
    public String toString() {
        return "CityWeatherData{" +
                "cityCode='" + cityCode + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", headLine='" + headLine + '\'' +
                ", mobileLink='" + mobileLink + '\'' +
                ", minTemp='" + minTemp + '\'' +
                ", maxTemp='" + maxTemp + '\'' +
                ", dayIconURL='" + dayIconURL + '\'' +
                ", dayText='" + dayText + '\'' +
                ", nightIconURL='" + nightIconURL + '\'' +
                ", nightText='" + nightText + '\'' +
                ", unitOfTemp='" + unitOfTemp + '\'' +
                ", effectiveDate='" + effectiveDate + '\'' +
                ", effectiveDateRecycler='" + effectiveDateRecycler + '\'' +
                ", isCurrent=" + isCurrent +
                ", setDate=" + setDate +
                '}';
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHeadLine() {
        return headLine;
    }

    public void setHeadLine(String headLine) {
        this.headLine = headLine;
    }

    public String getMobileLink() {
        return mobileLink;
    }

    public void setMobileLink(String mobileLink) {
        this.mobileLink = mobileLink;
    }

    public String getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(String minTemp) {
        this.minTemp = minTemp;
    }

    public String getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(String maxTemp) {
        this.maxTemp = maxTemp;
    }

    public String getDayIconURL() {
        return dayIconURL;
    }

    public void setDayIconURL(String dayIconURL) {
        this.dayIconURL = dayIconURL;
    }

    public String getDayText() {
        return dayText;
    }

    public void setDayText(String dayText) {
        this.dayText = dayText;
    }

    public String getNightIconURL() {
        return nightIconURL;
    }

    public void setNightIconURL(String nightIconURL) {
        this.nightIconURL = nightIconURL;
    }

    public String getNightText() {
        return nightText;
    }

    public void setNightText(String nightText) {
        this.nightText = nightText;
    }

    public String getUnitOfTemp() {
        return unitOfTemp;
    }

    public void setUnitOfTemp(String unitOfTemp) {
        this.unitOfTemp = unitOfTemp;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getEffectiveDateRecycler() {
        return effectiveDateRecycler;
    }

    public void setEffectiveDateRecycler(String effectiveDateRecycler) {
        this.effectiveDateRecycler = effectiveDateRecycler;
    }

    public Boolean getCurrent() {
        return isCurrent;
    }

    public void setCurrent(Boolean current) {
        isCurrent = current;
    }

    public Date getSetDate() {
        return setDate;
    }

    public void setSetDate(Date setDate) {
        this.setDate = setDate;
    }
}
