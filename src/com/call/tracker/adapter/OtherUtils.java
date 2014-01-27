package com.call.tracker.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class OtherUtils {

    Context context;

    public boolean isNetworkAvailable(Context context) {
        this.context = context;

        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            // boitealerte(this.getString(R.string.alertNoNetwork),"getSystemService rend null");
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean hasImageCaptureBug() {

        // list of known devices that have the bug
        ArrayList<String> devices = new ArrayList<String>();
        devices.add("android-devphone1/dream_devphone/dream");
        devices.add("generic/sdk/generic");
        devices.add("vodafone/vfpioneer/sapphire");
        devices.add("tmobile/kila/dream");
        devices.add("verizon/voles/sholes");
        devices.add("google_ion/google_ion/sapphire");

        return devices.contains(android.os.Build.BRAND + "/"
                + android.os.Build.PRODUCT + "/" + android.os.Build.DEVICE);

    }

    public static void setDefaultTypeFace(Context c, TextView view) {
        Typeface externalFont = Typeface.createFromAsset(c.getAssets(),
                "fonts/TrebuchetMS.ttf");
        view.setTypeface(externalFont);
    }

    public static void setDefaultTypeFace(Context c, Button view) {
        Typeface externalFont = Typeface.createFromAsset(c.getAssets(),
                "fonts/TrebuchetMS.ttf");
        view.setTypeface(externalFont);
    }

    public static void setDefaultTypeFace(Context c, TextView view, int Style) {
        Typeface externalFont = Typeface.createFromAsset(c.getAssets(),
                "fonts/TrebuchetMS.ttf");
        view.setTypeface(externalFont, Style);
    }

    public static boolean isTabletDevice(Context activityContext) {
        // Verifies if the Generalized Size of the device is XLARGE to be
        // considered a Tablet
        boolean xlarge = ((activityContext.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE);

        // If XLarge, checks if the Generalized Density is at least MDPI
        // (160dpi)
        if (xlarge) {
            DisplayMetrics metrics = new DisplayMetrics();
            Activity activity = (Activity) activityContext;
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

            // MDPI=160, DEFAULT=160, DENSITY_HIGH=240, DENSITY_MEDIUM=160,
            // DENSITY_TV=213, DENSITY_XHIGH=320
            if (metrics.densityDpi == DisplayMetrics.DENSITY_DEFAULT
                    || metrics.densityDpi == DisplayMetrics.DENSITY_HIGH
                    || metrics.densityDpi == DisplayMetrics.DENSITY_MEDIUM
                    // || metrics.densityDpi == DisplayMetrics.DENSITY_TV
                    || metrics.densityDpi == DisplayMetrics.DENSITY_XHIGH) {

                // Yes, this is a tablet!
                return true;
            }
        }

        // No, this is not a tablet!
        return false;
    }
}