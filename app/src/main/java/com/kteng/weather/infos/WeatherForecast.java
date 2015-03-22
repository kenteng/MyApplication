package com.kteng.weather.infos;

import com.alibaba.fastjson.JSONArray;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kteng on 2015/3/8.
 */
public class WeatherForecast implements Serializable {
    private String city;

    private String pm25;

    private String[] index;

    private JSONArray weather_data;

    public JSONArray getWeather_data() {
        return weather_data;
    }

    public void setWeather_data(JSONArray weather_data) {
        this.weather_data = weather_data;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPm25() {
        return pm25;
    }

    public void setPm25(String pm25) {
        this.pm25 = pm25;
    }

    public String[] getIndex() {
        return index;
    }

    public void setIndex(String[] index) {
        this.index = index;
    }

    public List<DateInfo> getDateInfoList(){
        JSONArray infos = getWeather_data();
        List<DateInfo> dateInfoList = new ArrayList<>();
        for(int i =0;i<infos.size();i++){
            dateInfoList.add(infos.getObject(i,DateInfo.class));
        }
        return dateInfoList;
    }
}
