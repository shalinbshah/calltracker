package com.call.tracker.contactmanager;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.call.tracker.R;
import com.call.tracker.listmanager.ListManagerDetails;
import com.call.tracker.model.ListManagerModel;

public class CollectionListAdapter extends BaseAdapter {

	private final Activity activity;
	private final ArrayList<ListManagerModel> data;
	private static LayoutInflater inflater = null;
	static int addCOllectionSuccess = 123;
	static int posToDelete;
	Intent callingIntent;

	public CollectionListAdapter(Activity mainActivity, Intent intent,
			ArrayList<ListManagerModel> listCollectionDetails) {
		activity = mainActivity;
		data = listCollectionDetails;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.callingIntent = intent;
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		convertView = null;
		ViewHolder viewHolder = new ViewHolder();
		if (convertView == null) {
			view = inflater.inflate(R.layout.albumlist_cell, null);

			viewHolder.tvAlbumName = (TextView) view
					.findViewById(R.id.tvAlbumName);

			// OtherUtils.setDefaultTypeFace(activity, viewHolder.tvAlbumName);

			viewHolder.llCellClickableList = (LinearLayout) view
					.findViewById(R.id.llCellClickableList);
			// if (position % 2 == 0)
			// view.setBackgroundColor(activity.getResources().getColor(
			// R.color.theme_color));
			ListManagerModel collection = new ListManagerModel();
			collection = data.get(position);
			viewHolder.llCellClickableList.setTag(collection.getId());
		} /*
		 * else { viewHolder = (ViewHolder) view.getTag(); }
		 */

		// Bind the data with the holder.
		ListManagerModel collection = new ListManagerModel();
		collection = data.get(position);
		view.setTag(collection.getId());
		viewHolder.tvAlbumName.setText(collection.getName());

		viewHolder.llCellClickableList
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						Intent intent = new Intent();
						intent.putExtra(ListManagerDetails.GROUP_ID_KEY, view
								.getTag().toString());
						TempHolder.selectedGroup = view.getTag().toString();
						// case GRP_PICKER_RESULT:
						ContactManagerUtility utility = new ContactManagerUtility();
						if (utility.addContactInDB(activity,
								TempHolder.pickedContact)) {
							Toast.makeText(activity,
									"Contact Added Successfully !!!",
									Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(activity,
									"Contact Add UnSuccessfull !!!",
									Toast.LENGTH_SHORT).show();
						}
						activity.finish();
					}
				});

		return view;
	}

	public class ViewHolder {
		TextView tvAlbumName;
		ImageView ivLeftideFolder;
		LinearLayout llCellClickableList;
	}

}
