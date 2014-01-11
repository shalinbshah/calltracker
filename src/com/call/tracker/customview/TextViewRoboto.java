package com.call.tracker.customview;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextViewRoboto extends TextView {

	// private static final String TAG = "TextViewHalvtica";

	public TextViewRoboto(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public TextViewRoboto(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public TextViewRoboto(Context context) {
		super(context);
		init();
	}

	public void init() {
		if (!isInEditMode()) {
			Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
					"TisaPro-Regular.otf");
			// "Roboto-Regular.ttf");
			setTypeface(tf);
		} else {
			// Log.d(TAG, "TextView");
		}
	}

}
