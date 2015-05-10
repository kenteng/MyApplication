package com.kteng.weather.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Handler;
import android.os.Message;
import android.provider.BaseColumns;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
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
import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class MainActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener{

    private Handler handler;

    private String weatherResult;

    private final int RESULT_TEXT = 1;

    private Spinner provinceSpinner;

    private Spinner citySpinner;

    private Map<String,List<String>> stateMap;

    private String[] testArray = {"aaaa","bbbb"};

    private SharedPreferences sharedPreferences;

    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView weather = (TextView) findViewById(R.id.text_weather);
        final TextView temp = (TextView) findViewById(R.id.text_temp);
        final TextView wind = (TextView) findViewById(R.id.text_wind);
        final TextView pm25 = (TextView) findViewById(R.id.text_pm25);
        provinceSpinner = (Spinner) findViewById(R.id.spinner_province);
        provinceSpinner.setOnItemSelectedListener(this);

        citySpinner = (Spinner) findViewById(R.id.spinner_city);
        citySpinner.setOnItemSelectedListener(this);

        final ListView weatherList = (ListView) findViewById(R.id.weather_list);
        //to store selected the province and city information.
        sharedPreferences = getSharedPreferences("weather", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        try {
            XMLParser xmlParser = new XMLParser(getResources().openRawResource(R.raw.cities));
            stateMap = xmlParser.parser();
            List<String> provinceList = new ArrayList<>();
            provinceList.add("-----");
            provinceList.addAll(stateMap.keySet());
            ArrayAdapter provinceAdapter = new ArrayAdapter(MainActivity.this,android.R.layout.select_dialog_item, provinceList);
            provinceSpinner.setAdapter(provinceAdapter);
            if(sharedPreferences.contains("province")){
                String province = sharedPreferences.getString("province","");
                int index = getProvinceIndex(province);
                provinceSpinner.setSelection(index,true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        final SearchView searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setQueryHint("Search for countries…");
        /*Define the columns of a table.
        *Must have the column "_id" otherwise will crash.
        */
        final String[] tableCursor = new String[] {BaseColumns._ID,"city"};

        /*MatrixCursor could simulate a table actually*/
        final MatrixCursor matrixCursor = new MatrixCursor(tableCursor);
        if(stateMap!=null){
            int i = 0;
            for(String province:stateMap.keySet()){
                for(String city:stateMap.get(province)){
                    i++;
                    matrixCursor.addRow(new Object[]{i,city});
                }
            }
        }

        final String[] from = {"city"};
        final int[] to = {R.id.textview};

        final SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(this,R.layout.searchtextview,matrixCursor,from,to);
        cursorAdapter.setFilterQueryProvider(new FilterQueryProvider() { //used to filter the suggestion list.
            @Override
            public Cursor runQuery(CharSequence constraint) {
                MatrixCursor _matrixCursor = matrixCursor;
                if(constraint!=null){
                    cursorAdapter.changeCursor(matrixCursor); //it is the full list at the beginning. Then start to filter.
                    Cursor cursor = cursorAdapter.getCursor();
                    _matrixCursor = new MatrixCursor(tableCursor); //init a new Cursor.
                    int i = 0;
                    cursor.moveToFirst();  // move to first.
                    do{
                        String cityName = cursor.getString(1);
                        if(cityName.contains(constraint)){
                            i++;
                            _matrixCursor.addRow(new Object[]{i,cityName});
                        }
                    }while(cursor.moveToNext());
                    return _matrixCursor;
                }
                return _matrixCursor;
            }
        });

        searchView.setSuggestionsAdapter(cursorAdapter);
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return true;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                if(TextUtils.isEmpty(s)){
                }
                else{
                    //cursorAdapter.getFilter().filter(s);
                }
                return true;
            }
        });
        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                return false;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                Cursor cursor = (Cursor) cursorAdapter.getItem(position);
                String cityName = cursor.getString(1);

                //set the selection of drop down list.
                provinceSpinner.setSelection(getProvinceIndex(cityName));
                citySpinner.setSelection(getCityIndex(cityName));

                //send to query, it will jump into onQueryTextSubmit() in onQueryTextListener
                searchView.setQuery(cityName,true);

                return true;
            }
        });


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
    private int getProvinceIndex(String name){
        int index = 0; //the index mush starts from 0 not -1 since the stateMap does not contain the string "----"
        for(String key : stateMap.keySet()){
            index++;
            if(key.equals(name)){ //name is a province name.
                break;
            }
            if(stateMap.get(key).contains(name)){ //name is a city name.
                break;
            }
        }
        return index;
    }

    private int getCityIndex(String cityName){
        int index = -1; //the index mush starts from 0 not -1 since the stateMap does not contain the string "----"
        for(String key : stateMap.keySet()){
            List<String> cityList = stateMap.get(key);
            if(cityList.contains(cityName)){ //name is a city name.
                for(String s:cityList){
                    index++;
                    if(s.equals(cityName))
                        return index;
                }
            }
        }
        return index;
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
                List<String> cityList;
                /*Set to default string if province is selected as default.*/
                if(provinceName.equals("-----")){
                    cityList = Arrays.asList("-----");
                }
                else{
                    cityList = stateMap.get(provinceName);
                    editor.putString("province",provinceName);
                }
                ArrayAdapter cityAdapter = new ArrayAdapter(MainActivity.this,android.R.layout.select_dialog_item, cityList);
                citySpinner.setAdapter(cityAdapter);
                break;
            case R.id.spinner_city:
                String cityName = parent.getItemAtPosition(position).toString();
                if(!cityName.equals("-----")){
                    getWeatherInfo(cityName);
                    editor.putString("city",cityName);
                    editor.commit();
                }
                break;
            default:
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
