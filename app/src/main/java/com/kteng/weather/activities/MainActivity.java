package com.kteng.weather.activities;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.kteng.weather.R;
import com.kteng.weather.infos.DateInfo;
import com.kteng.weather.infos.WeatherAdapter;
import com.kteng.weather.infos.WeatherForecast;
import com.kteng.weather.infos.WeatherInfo;
import com.kteng.weather.util.XMLParser;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MainActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener{

    private Handler handler;

    private String weatherResult;

    private final int RESULT_TEXT = 1;

    private Spinner provinceSpinner;

    private Spinner citySpinner;

    private Map<String,List<String>> stateMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView weather = (TextView) findViewById(R.id.text_weather);
        final TextView temp = (TextView) findViewById(R.id.text_temp);
        final TextView wind = (TextView) findViewById(R.id.text_wind);
        final TextView pm25 = (TextView) findViewById(R.id.text_pm25);

        /*final TextView d1_date = (TextView) findViewById(R.id.d1_date);
        final TextView d1_weather = (TextView) findViewById(R.id.d1_weather);
        final TextView d1_temp = (TextView) findViewById(R.id.d1_temp);
        final TextView d1_wind = (TextView) findViewById(R.id.d1_wind);

        final TextView d2_date = (TextView) findViewById(R.id.d2_date);
        final TextView d2_weather = (TextView) findViewById(R.id.d2_weather);
        final TextView d2_temp = (TextView) findViewById(R.id.d2_temp);
        final TextView d2_wind = (TextView) findViewById(R.id.d2_wind);

        final TextView d3_date = (TextView) findViewById(R.id.d3_date);
        final TextView d3_weather = (TextView) findViewById(R.id.d3_weather);
        final TextView d3_temp = (TextView) findViewById(R.id.d3_temp);
        final TextView d3_wind = (TextView) findViewById(R.id.d3_wind);*/

        provinceSpinner = (Spinner) findViewById(R.id.spinner_province);
        provinceSpinner.setOnItemSelectedListener(this);

        citySpinner = (Spinner) findViewById(R.id.spinner_city);
        citySpinner.setOnItemSelectedListener(this);

        final ListView weatherList = (ListView) findViewById(R.id.weather_list);



        handler = new Handler(){
            @Override
            public void handleMessage(Message message){
                Bundle bundle = message.getData();
                weatherResult = bundle.getString("weatherResult");

                WeatherInfo weatherInfo = JSON.parseObject(weatherResult,WeatherInfo.class);
                WeatherForecast forecast = weatherInfo.getWeatherForecast();
                List<DateInfo> dateInfoList = forecast.getDateInfoList();
                DateInfo today = dateInfoList.get(0);
                weather.setText(today.getWeather());
                wind.setText(today.getWind());
                temp.setText(today.getTemperature());
                pm25.setText(forecast.getPm25());

                /*DateInfo d1 = dateInfoList.get(1);
                d1_date.setText(d1.getDate());
                d1_temp.setText(d1.getTemperature());
                d1_weather.setText(d1.getWeather());
                d1_wind.setText(d1.getWind());

                DateInfo d2 = dateInfoList.get(2);
                d2_date.setText(d2.getDate());
                d2_temp.setText(d2.getTemperature());
                d2_weather.setText(d2.getWeather());
                d2_wind.setText(d2.getWind());

                DateInfo d3 = dateInfoList.get(3);
                d3_date.setText(d3.getDate());
                d3_temp.setText(d3.getTemperature());
                d3_weather.setText(d3.getWeather());
                d3_wind.setText(d3.getWind());*/


                /*The context here could be use follow
                * getBaseContext() / Activity.this / getApplicationContext()
                * The difference is:
                * getApplicationContext() 返回应用的上下文，生命周期是整个应用，应用摧毁它才摧毁
                * Activity.this的context 返回当前activity的上下文，属于activity ，activity 摧毁他就摧毁
                * getBaseContext()  返回由构造函数指定或setBaseContext()设置的上下文
                * */
                WeatherAdapter weatherAdapter = new WeatherAdapter(MainActivity.this,R.layout.weather_info,dateInfoList);
                weatherList.setAdapter(weatherAdapter);
            }

        };
        try {
            XMLParser xmlParser = new XMLParser(getResources().openRawResource(R.raw.cities));
            stateMap = xmlParser.parser();

            List<String> provinceList = new ArrayList<>();
            provinceList.addAll(stateMap.keySet());
            ArrayAdapter provinceAdapter = new ArrayAdapter(MainActivity.this,android.R.layout.select_dialog_item, provinceList);
            provinceSpinner.setAdapter(provinceAdapter);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }



    private void getWeatherInfo(final String cityName){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet("http://api.map.baidu.com/telematics/v3/weather?location="+cityName+"&output=json&ak=LLVStL4Ejis8FjCK8BDHtFRR");
                try {
                    HttpResponse response = httpClient.execute(httpGet);
                    if(response.getStatusLine().getStatusCode()==200){
                        HttpEntity entity = response.getEntity();
                        String result = EntityUtils.toString(entity,"utf-8");
                        Message msg = new Message();
                        Bundle bundle = new Bundle();
                        bundle.putString("weatherResult", result);
                        msg.what = RESULT_TEXT;
                        msg.setData(bundle);
                        handler.sendMessage(msg);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.spinner_province:
                String provinceName = parent.getItemAtPosition(position).toString();
                List<String> cityList = stateMap.get(provinceName);
                ArrayAdapter cityAdapter = new ArrayAdapter(MainActivity.this,android.R.layout.select_dialog_item, cityList);
                citySpinner.setAdapter(cityAdapter);
                break;
            case R.id.spinner_city:
                getWeatherInfo(parent.getItemAtPosition(position).toString());
                break;
            default:
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
