package com.inqbarna.tablefixheaders;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.util.TypedValue;

import com.call.tracker.R;

public class MyAdapter extends SampleTableAdapter {

	private final int width;
	private final int height;
	private final static int WIDTH_DIP = 110;
	private final static int HEIGHT_DIP = 32;
	private String[][] table;

	public MyAdapter(Context context) {
		super(context);

		Resources r = context.getResources();

		width = Math.round(TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, WIDTH_DIP, r.getDisplayMetrics()));
		height = Math
				.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
						HEIGHT_DIP, r.getDisplayMetrics()));
	}

	public MyAdapter(Context context, String[][] a) {
		this(context);
		this.table = a;
	}

	@Override
	public int getRowCount() {
		return table.length - 1;
	}

	@Override
	public int getColumnCount() {
		return table[0].length - 1;
	}

	@Override
	public int getWidth(int column) {
		return width;
	}

	@Override
	public int getHeight(int row) {
		return height;
	}

	@Override
	public String getCellString(int row, int column) {
		Log.d("callTracker", "row : " + row + " col : " + column);
		return table[row + 1][column + 1].toString();
	}

	@Override
	public int getLayoutResource(int row, int column) {
		final int layoutResource;
		switch (getItemViewType(row, column)) {
		case 0:
			layoutResource = R.layout.item_table1_header;
			break;
		case 1:
			layoutResource = R.layout.item_table1;
			break;
		default:
			throw new RuntimeException("wtf?");
		}
		return layoutResource;
	}

	@Override
	public int getItemViewType(int row, int column) {
		if (row < 0) {
			return 0;
		} else {
			return 1;
		}
	}

	@Override
	public int getViewTypeCount() {
		return 3;
	}
}
