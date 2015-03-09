package com.kteng.weather.util;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kteng on 2015/3/8.
 */
public class XMLParser {

    private Map<String,String> cityinfo = new HashMap<>();

    private InputStream inputStream;

    public XMLParser(InputStream inputStream){
        this.inputStream = inputStream;
    }

    public Map<String,List<String>> parser(){
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(inputStream,"utf-8");
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType){
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        String nodeName = parser.getName();
                        if(nodeName.equals("Province")){
                            String province = parser.getAttributeValue(0);
                        }
                        else if(nodeName.equals("City")){
                            String city = parser.getAttributeValue(0);
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
        return null;
    }
}
