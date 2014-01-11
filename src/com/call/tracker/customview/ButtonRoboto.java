package com.call.tracker.customview;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

public class ButtonRoboto extends Button {

	// private static final String TAG = "TextViewHalvtica";

	public ButtonRoboto(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public ButtonRoboto(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ButtonRoboto(Context context) {
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
