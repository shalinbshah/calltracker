package com.call.tracker.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.call.tracker.R;
import com.call.tracker.model.VoiceNotesModel;
import com.call.tracker.voicenotes.VoiceListActivity;

public class VoiceNotesMainAdapter extends BaseAdapter {

	private ArrayList<VoiceNotesModel> voiceNotesModels = new ArrayList<VoiceNotesModel>();
	private VoiceListActivity activity;

	public VoiceNotesMainAdapter(VoiceListActivity mActivity,
			ArrayList<VoiceNotesModel> callList) {
		this.activity = mActivity;
		this.voiceNotesModels = callList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return voiceNotesModels.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return voiceNotesModels.get(arg0);
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
			mView = inflater.inflate(R.layout.list_voice_main_list, parent,
					false);
			holder = new ViewHolder();

			holder.textName = (TextView) mView.findViewById(R.id.textName);
			holder.textUrgent = (ImageView) mView.findViewById(R.id.textUrgent);
			holder.textDate = (TextView) mView.findViewById(R.id.textDate);

			mView.setTag(holder);
		} else
			holder = (ViewHolder) mView.getTag();

		String name = voiceNotesModels.get(position).getContact_name();

		holder.textName.setText(name);
		boolean isUrgent = voiceNotesModels.get(position).getUrgent()
				.equalsIgnoreCase("2") ? true : false;
		if (isUrgent)
			holder.textUrgent.setImageResource(R.drawable.icon_alert_red);
		else
			holder.textUrgent.setImageResource(R.drawable.icon_alert_grey);
		holder.textDate.setText(voiceNotesModels.get(position).getDateTime());

		return mView;
	}

	public class ViewHolder {
		private TextView textName;
		private ImageView textUrgent;
		private TextView textDate;
	}
}
