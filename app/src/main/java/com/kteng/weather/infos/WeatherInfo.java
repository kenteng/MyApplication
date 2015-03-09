package com.kteng.weather.infos;

import com.alibaba.fastjson.JSONArray;

import java.io.Serializable;

/**
 * Created by kteng on 2015/3/8.
 */
public class WeatherInfo implements Serializable {
    private String errorCode;

    private String status;

    private String date;

    private JSONArray results;

    private WeatherForecast weatherForecast;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public WeatherForecast getWeatherForecast(){
        JSONArray array = getResults();
        WeatherForecast forecast = array.getObject(0,WeatherForecast.class);
        return forecast;
    }
    public JSONArray getResults() {
        return results;
    }

    public void setResults(JSONArray results) {
        this.results = results;
    }
}
