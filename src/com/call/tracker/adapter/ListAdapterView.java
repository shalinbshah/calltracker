package com.call.tracker.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.call.tracker.R;
import com.call.tracker.model.ListManagerModel;
import com.call.tracker.voicenotes.AssignContactToVoiceNoteActivity;

public class ListAdapterView extends BaseAdapter {

	public ArrayList<ListManagerModel> mArrayList = new ArrayList<ListManagerModel>();
	private AssignContactToVoiceNoteActivity activity;

	public ListAdapterView(AssignContactToVoiceNoteActivity mActivity,
			ArrayList<ListManagerModel> callList) {
		this.activity = mActivity;
		this.mArrayList = callList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mArrayList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mArrayList.get(arg0);
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
			mView = inflater.inflate(R.layout.list_check_data, parent, false);
			holder = new ViewHolder();

			holder.textName = (TextView) mView.findViewById(R.id.textName);
			holder.checkBox = (CheckBox) mView.findViewById(R.id.checkboxList);

			mView.setTag(holder);
		} else
			holder = (ViewHolder) mView.getTag();

		String name = mArrayList.get(position).getName();

		holder.textName.setText(name);

		if (mArrayList.get(position).getIsCheck().equals("0"))
			holder.checkBox.setChecked(false);
		else
			holder.checkBox.setChecked(true);
		holder.checkBox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				updateListData(-1);
				if (!((CheckBox) v).isChecked()) {
					mArrayList.get(position).setIsCheck("0");
				} else {
					mArrayList.get(position).setIsCheck("1");
				}
				activity.updateCheck();
				notifyDataSetChanged();
			}
		});

		mView.setId(position);

		mView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int id = v.getId();
				ListManagerModel dataView = (ListManagerModel) getItem(id);
				String b = mArrayList.get(id).getIsCheck();
				if (b.equals("1"))
					dataView.setIsCheck("0");
				else
					dataView.setIsCheck("1");
				updateListData(id);
				mArrayList.set(id, dataView);
				activity.updateCheck();
				notifyDataSetChanged();
			}
		});
		return mView;
	}

	public void updateListData(int val) {
		// for (int id = 0; id < mArrayList.size(); id++) {
		// ListManagerModel dataView = (ListManagerModel) getItem(id);
		// if (val == id)
		// dataView.setIsCheck("1");
		// else
		// dataView.setIsCheck("0");
		// mArrayList.set(id, dataView);
		// }
	}

	public class ViewHolder {
		private TextView textName;
		private CheckBox checkBox;
	}

	public void selectAll(String val) {
		for (int id = 0; id < mArrayList.size(); id++) {
			ListManagerModel dataView = (ListManagerModel) getItem(id);
			dataView.setIsCheck(val);
			mArrayList.set(id, dataView);
		}
		notifyDataSetChanged();
	}
}
