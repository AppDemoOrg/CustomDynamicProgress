package com.abt.progress;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements CustomSeekBar.ResponseOnTouch {

    private static final String TAG = MainActivity.class.getSimpleName();

    private ArrayList<String> mCodeRateYardage = new ArrayList<String>();
    private ArrayList<String> mCodeRateYardage2 = new ArrayList<String>();

    private CustomSeekBar mCodeRateSeekBar;
    private CustomSeekBar mCodeRateSeekBar2;
    private int mLevel = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initCodeRate();
        initCodeRate2();
    }

    private void initCodeRate() {
        mCodeRateYardage.add("0.6M");
        mCodeRateYardage.add("1M");
        mCodeRateYardage.add("2M");
        mCodeRateYardage.add("3M");
        mCodeRateYardage.add("4M");
        mCodeRateYardage.add("5M");
        mCodeRateYardage.add("6M");
        mCodeRateSeekBar = (CustomSeekBar) findViewById(R.id.custom_seek_bar);
        mCodeRateSeekBar.initData(mCodeRateYardage);
        mCodeRateSeekBar.setProgress(mLevel);
        mCodeRateSeekBar.setmResponseOnTouch(this);
        // activity实现了下面的接口ResponseOnTouch，每次touch会回调onTouchResponse
    }

    private void initCodeRate2() {
        mCodeRateYardage2.add("0");
        mCodeRateYardage2.add("1");
        mCodeRateYardage2.add("2");
        mCodeRateYardage2.add("3");
        mCodeRateYardage2.add("4");
        mCodeRateYardage2.add("5");
        mCodeRateYardage2.add("6");
        mCodeRateYardage2.add("7");
        mCodeRateYardage2.add("8");
        mCodeRateYardage2.add("9");
        mCodeRateYardage2.add("10");
        mCodeRateSeekBar2 = (CustomSeekBar) findViewById(R.id.custom_seek_bar2);
        mCodeRateSeekBar2.initData(mCodeRateYardage2);
        mCodeRateSeekBar2.setProgress(mLevel);
        mCodeRateSeekBar2.setmResponseOnTouch(this);
        // activity实现了下面的接口ResponseOnTouch，每次touch会回调onTouchResponse
    }

    @Override
    public void onTouchResponse(int volume) {
        Log.d(TAG, "on Touch responese volume = "+volume);
    }
}
