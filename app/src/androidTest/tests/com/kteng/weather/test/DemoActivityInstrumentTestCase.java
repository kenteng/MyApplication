package com.kteng.weather.test;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Spinner;

import com.kteng.weather.R;
import com.kteng.weather.activities.MainActivity;

/**
 * Created by kteng on 2015/5/25.
 * Note the comment below in ActivityInstrumentationTestCase2 class
 *  * <p><b>NOTE:</b> Activities under test may not be started from within the UI thread.
 * If your test method is annotated with {@link android.test.UiThreadTest}, then you must call
 * {@link #setActivityInitialTouchMode(boolean)} from {@link #setUp()}.
 */
public class DemoActivityInstrumentTestCase extends ActivityInstrumentationTestCase2<MainActivity>{
    private MainActivity activity;

    private Spinner provinceSpinner;

    public DemoActivityInstrumentTestCase(){
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        activity = getActivity();
        provinceSpinner = (Spinner) activity.findViewById(R.id.spinner_province);
    }

    public void testActivity(){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                provinceSpinner.setSelection(4);
            }
        });
    }

}
