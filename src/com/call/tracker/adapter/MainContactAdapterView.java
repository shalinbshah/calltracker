package com.call.tracker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.call.tracker.R;
import com.call.tracker.model.ContactModel;
import com.call.tracker.voicenotes.SelectContactOfGroupActivity;

import java.util.ArrayList;

public class MainContactAdapterView extends BaseAdapter {

    public ArrayList<ContactModel> mListManagerModels = new ArrayList<ContactModel>();
    private SelectContactOfGroupActivity activity;

    public MainContactAdapterView(SelectContactOfGroupActivity mActivity,
                                  ArrayList<ContactModel> callList) {
        this.activity = mActivity;
        this.mListManagerModels = callList;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mListManagerModels.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return mListManagerModels.get(arg0);
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
            mView = inflater.inflate(R.layout.list_main_contact, parent, false);
            holder = new ViewHolder();

            holder.textGroup = (TextView) mView.findViewById(R.id.textGroup);
            holder.checkBox = (CheckBox) mView.findViewById(R.id.checkBox);

            mView.setTag(holder);
        } else
            holder = (ViewHolder) mView.getTag();

        String name = mListManagerModels.get(position).getName();

        holder.textGroup.setText("Default");

        holder.checkBox.setText(name);

        if (!mListManagerModels.get(position).isCheck())
            holder.checkBox.setChecked(false);
        else
            holder.checkBox.setChecked(true);
        holder.checkBox.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (((CheckBox) v).isChecked()) {
                    mListManagerModels.get(position).setCheck(true);
                } else {
                    mListManagerModels.get(position).setCheck(false);
                }
                notifyDataSetChanged();
            }
        });

        mView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                int id = v.getId();
                ContactModel dataView = (ContactModel) getItem(id);
                boolean b = mListManagerModels.get(id).isCheck();
                dataView.setCheck(!b);
                mListManagerModels.set(id, dataView);
                notifyDataSetChanged();
            }
        });
        mView.setId(position);
        return mView;
    }

    public class ViewHolder {
        private TextView textGroup;
        private CheckBox checkBox;
    }
}
