package com.call.tracker.adapter;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.call.tracker.GuideActivity;
import com.call.tracker.R;
import com.call.tracker.interfaces.Constants;

import java.util.ArrayList;

public class GuidePagerAdapter extends PagerAdapter implements Constants {

    private ArrayList<String> mainArray = new ArrayList<String>();
    private GuideActivity activity;

    public GuidePagerAdapter(GuideActivity mContext, ArrayList<String> arrayList) {
        mainArray = arrayList;
        activity = mContext;
    }

    public int getCount() {
        return mainArray.size();
    }

    public Object instantiateItem(View collection, final int position) {

        View mView = collection;
        LayoutInflater rowView1 = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = rowView1.inflate(R.layout.pager_guide, null);

        TextView textGuideName = (TextView) mView.findViewById(R.id.textguide);
        // TextView textEventNameBelow = (TextView) mView
        // .findViewById(R.id.textguideBelow);
        textGuideName.setText(mainArray.get(position).toString());

        LinearLayout linearLayout = (LinearLayout) mView
                .findViewById(R.id.layoutguideBelow);
        linearLayout.setVisibility(View.INVISIBLE);
        if (mainArray.size() - 1 == position) {
            linearLayout.setVisibility(View.VISIBLE);
            // activity.startHomeActivity();
        }
        ((ViewPager) collection).addView(mView, 0);
        return mView;
    }

    @Override
    public void destroyItem(View arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView((View) arg2);
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == ((View) arg1);
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

}
