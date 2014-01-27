package com.call.tracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.call.tracker.adapter.GuidePagerAdapter;

import java.util.ArrayList;

public class GuideActivity extends BaseActivity {
    // private static final String TAG = "GuideAtivity";
    public GuidePagerAdapter pagerAdapter;
    private ViewPager myPager;
    private ArrayList<String> arrayList = new ArrayList<String>();
    private Button butNext;
    private int currntPos;
    private CheckBox checkdontshow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_guide);

        initControl();
    }

    public void callNext(View v) {
        int currentVal = myPager.getCurrentItem() + 1;

        if (currntPos == 4) {
            updatePref(IS_GUIDE, String.valueOf(checkdontshow.isChecked()));
            startHomeActivity();
        } else {
            butNext.setText(getStringFromXml(R.string.next));
            myPager.setCurrentItem(currentVal);
        }
    }

    private void initControl() {
        myPager = (ViewPager) findViewById(R.id.imageViewPager);

        butNext = (Button) findViewById(R.id.butNext);

        checkdontshow = (CheckBox) findViewById(R.id.checkdontshow);
        checkdontshow.setVisibility(View.INVISIBLE);

        arrayList.add(getStringFromXml(R.string.guide_1));
        arrayList.add(getStringFromXml(R.string.guide_2));
        arrayList.add(getStringFromXml(R.string.guide_3));
        arrayList.add(getStringFromXml(R.string.guide_4));
        arrayList.add(getStringFromXml(R.string.guide_5));

        pagerAdapter = new GuidePagerAdapter(this, arrayList);
        myPager.setAdapter(pagerAdapter);
        myPager.setCurrentItem(0);
        myPager.setOnPageChangeListener(new MyPageChangeListener());
    }

    private class MyPageChangeListener extends
            ViewPager.SimpleOnPageChangeListener {
        @Override
        public void onPageSelected(int position) {
            currntPos = position;
            if (currntPos == 4) {
                butNext.setText(getStringFromXml(R.string.let_go));
                checkdontshow.setVisibility(View.VISIBLE);
            } else {
                butNext.setText(getStringFromXml(R.string.next));
                checkdontshow.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void startHomeActivity() {
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        finish();
    }
}
