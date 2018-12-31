package com.example.zyx.weather.db;
//WeatherInfo.class
import java.util.List;

public class WeatherInfo {
    public cityInfo cityInfo;
    public String time;
    public String date;
    public String message;
    public DataBean data;
    public String status;
    public static class cityInfo{
        public String city;
        public String cityId;
        public String parents;
        public String updateTime;
    }
    public static class DataBean {
        public String shidu;
        public String pm25;
        public String pm10;
        public String quality;
        public String wendu;
        public String ganmao;
        public List<forecast> forecast;
    }
    public static class forecast {
        public String date;
        public String sunrise;
        public String high;
        public String low;
        public String sunset;
        public String aqi;
        public String ymd;
        public String week;
        public String fx;
        public String fl;
        public String type;
        public String notice;
        }
}
