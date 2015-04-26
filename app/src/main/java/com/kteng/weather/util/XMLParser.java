package com.kteng.weather.util;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kteng on 2015/3/8.
 */
public class XMLParser {

    private InputStream inputStream;

    public XMLParser(InputStream inputStream){
        this.inputStream = inputStream;
    }

    public Map<String,List<String>> parser(){
        Map<String,List<String>> stateMaps = new LinkedHashMap<>();
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(inputStream,"utf-8");
            int eventType = parser.getEventType();
            String province = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType){
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        String nodeName = parser.getName();
                        if(nodeName.equals("Province")){
                            province = parser.getAttributeValue(0);
                            if(!stateMaps.containsKey(province)){
                                stateMaps.put(province, new ArrayList<String>());
                            }
                        }
                        else if(nodeName.equals("City")){
                            String city = parser.getAttributeValue(0);
                            List<String> cities = stateMaps.get(province);
                            cities.add(city);
                            stateMaps.put(province, cities);
                        }
                        break;
                    default:
                        break;

                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stateMaps;
    }
}
