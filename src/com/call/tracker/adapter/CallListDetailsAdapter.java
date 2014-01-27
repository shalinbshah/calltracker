package com.call.tracker.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.call.tracker.R;
import com.call.tracker.calllist.DetailActivity;
import com.call.tracker.model.CallNumberModel;

import java.util.ArrayList;

public class CallListDetailsAdapter extends BaseAdapter {

    private ArrayList<CallNumberModel> callListModels = new ArrayList<CallNumberModel>();
    private DetailActivity activity;

    public CallListDetailsAdapter(DetailActivity mActivity,
                                  ArrayList<CallNumberModel> callList) {
        this.activity = mActivity;
        this.callListModels = callList;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return callListModels.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return callListModels.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View mView = convertView;
        ViewHolder holder = null;
        if (mView == null) {
            LayoutInflater inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mView = inflater.inflate(R.layout.list_call_detail_list, parent,
                    false);
            holder = new ViewHolder();

            holder.textNum = (TextView) mView.findViewById(R.id.textNum);
            holder.textType = (TextView) mView.findViewById(R.id.textType);
            holder.callButton = (Button) mView.findViewById(R.id.butCall);

            mView.setTag(holder);
        } else
            holder = (ViewHolder) mView.getTag();

        String name = callListModels.get(position).getNumber();

        if (name.length() == 0)
            name = callListModels.get(position).getNumber();
        holder.textNum.setText(name);
        holder.textType.setText(callListModels.get(position).getNumberType());
        holder.callButton.setTag(name);
        holder.callButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                callNumber(v.getTag().toString());
            }
        });

        return mView;
    }

    protected void callNumber(String string) {
        // TODO Auto-generated method stub
        Intent intent = new Intent(Intent.ACTION_CALL);

        intent.setData(Uri.parse("tel:" + string));
        activity.startActivity(intent);
    }

    public class ViewHolder {
        private TextView textNum;
        private TextView textType;
        private Button callButton;
    }
}
