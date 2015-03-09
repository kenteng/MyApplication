package com.kteng.weather.activities;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.kteng.weather.R;
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



public class MainActivity extends ActionBarActivity {

    private Handler handler;

    private String weatherResult;

    private final int RESULT_TEXT = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView text_main = (TextView) findViewById(R.id.text_main);
        handler = new Handler(){
            @Override
            public void handleMessage(Message message){
                Bundle bundle = message.getData();
                weatherResult = bundle.getString("weatherResult");
                text_main.setText(weatherResult);
            }

        };
        getWeatherInfo();
        try {
            XMLParser xmlParser = new XMLParser(getResources().openRawResource(R.raw.cities));
            xmlParser.parser();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void getWeatherInfo(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet("http://api.map.baidu.com/telematics/v3/weather?location=%E9%95%87%E6%B1%9F&output=json&ak=LLVStL4Ejis8FjCK8BDHtFRR");
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
}
