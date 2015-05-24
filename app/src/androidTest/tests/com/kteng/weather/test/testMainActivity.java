package com.kteng.weather.test;

import android.content.Intent;
import android.test.InstrumentationTestCase;
import android.widget.Spinner;

import com.kteng.weather.R;
import com.kteng.weather.activities.MainActivity;

/**
 * Created by kteng on 2015/5/24.
 * 1. Create new case and extends InstrumentationTestCase
 * 2. define the objects which you will use in the test in setUp method. So you hava
 * to know the type and id of the object. It requests the source code.
 * 3. After coding is done. There are 2 ways to perform the tests.
 *  3.1 Run with Gradle option, generally it will NOT work.
 *  3.2 Run with AndroidTest option.
 *  tips : if you get such error like "emulator: Failed to sync vcpu reg", make sure you do not have any
 *  virtual machine running. Close any vm like Genymotion and let avd to create the vm self.
 */
public class testMainActivity extends InstrumentationTestCase{
    private MainActivity activity;
    private Spinner provinceSpinner;
    @Override
    public void setUp() {
        try {
            super.setUp();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intent intent = new Intent();
        intent.setClassName("com.kteng.weather", MainActivity.class.getName());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //TODO don't know why
        activity = (MainActivity) getInstrumentation().startActivitySync(intent);
        provinceSpinner = (Spinner) activity.findViewById(R.id.spinner_province);
    }

    public void testActivity(){
        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                provinceSpinner.setSelection(3);
            }
        });
    }
}
