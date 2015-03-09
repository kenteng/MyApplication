package com.kteng.weather;

import android.app.Application;
import android.test.ApplicationTestCase;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public void test() throws Exception{
        String jsonString = "{error:0,status:success,date:2015-03-07," +
                "results:[" +
                "{currentCity:镇江," +
                "pm25:102,index:[]," +
                "weather_data:[{date:周六 03月07日 (实时：8℃),dayPictureUrl:http://api.map.baidu.com/images/weather/day/duoyun.png,nightPictureUrl:http://api.map.baidu.com/images/weather/night/yin.png,weather:多云转阴,wind:东南风微风,temperature:11 ~ 5℃},{date:周日,dayPictureUrl:http://api.map.baidu.com/images/weather/day/xiaoyu.png,nightPictureUrl:http://api.map.baidu.com/images/weather/night/yin.png,weather:小雨转阴,wind:东风3-4级,temperature:11 ~ 6℃},{date:周一,dayPictureUrl:http://api.map.baidu.com/images/weather/day/duoyun.png,nightPictureUrl:http://api.map.baidu.com/images/weather/night/qing.png,weather:多云转晴,wind:东北风3-4级,temperature:10 ~ 0℃},{date:周二,dayPictureUrl:http://api.map.baidu.com/images/weather/day/duoyun.png,nightPictureUrl:http://api.map.baidu.com/images/weather/night/duoyun.png,weather:多云,wind:东风微风,temperature:8 ~ 2℃}]" +
                "}" +
                "" +
                "]}";
        JSONArray jsonArray = new JSONArray(jsonString);
        JSONObject object = jsonArray.getJSONObject(0);
        String errorCode = object.getString("error");
        String status = object.getString("status");
    }
}