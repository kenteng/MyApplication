package com.kteng.weather.infos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kteng.weather.R;

import java.util.List;

/**
 * Created by megatron on 15/3/22.
 */
public class WeatherAdapter extends ArrayAdapter<DateInfo>{
    private int resource;

    public WeatherAdapter(Context context, int resource,List<DateInfo> objects) {
        super(context, resource,objects);
        this.resource = resource;
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent){
        DateInfo dateInfo = getItem(position);
        View view;
        ViewHolder viewHolder = null;
        if(convertView==null){
            view = LayoutInflater.from(getContext()).inflate(resource, null);
            viewHolder = new ViewHolder();
            viewHolder.date = (TextView) view.findViewById(R.id.wi_date);
            viewHolder.temp = (TextView) view.findViewById(R.id.wi_temp);
            viewHolder.weather = (TextView) view.findViewById(R.id.wi_weather);
            viewHolder.wind = (TextView) view.findViewById(R.id.wi_wind);
            view.setTag(viewHolder);
        }
        else {
            view = convertView;
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.wind.setText(dateInfo.getWind());
        viewHolder.weather.setText(dateInfo.getWeather());
        viewHolder.temp.setText(dateInfo.getTemperature());
        viewHolder.date.setText(dateInfo.getDate());
        return view;
    }

    class ViewHolder{
        TextView date;
        TextView weather;
        TextView wind;
        TextView temp;
    }
}
