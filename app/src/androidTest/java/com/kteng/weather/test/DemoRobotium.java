package com.kteng.weather.test;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;

import com.kteng.weather.R;
import com.kteng.weather.activities.MainActivity;
import com.robotium.solo.By;
import com.robotium.solo.Solo;

/**
 * Created by megatron on 15/5/26.
 */
public class DemoRobotium extends ActivityInstrumentationTestCase2<MainActivity>{

    private MainActivity activity;

    private Solo solo;

    public DemoRobotium() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        activity = getActivity();
        solo = new Solo(getInstrumentation(),activity);
    }

    public void testActivityByRobotium(){
        EditText queryText = solo.getEditText(0);
        solo.enterText(queryText,"上海");
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        solo.finishOpenedActivities();
    }
}
