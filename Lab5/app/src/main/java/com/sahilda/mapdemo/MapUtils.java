package com.sahilda.mapdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.SystemClock;
import android.view.animation.BounceInterpolator;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;

public class MapUtils {

    public static void dropPinEffect(final Marker marker) {
        final android.os.Handler handler = new android.os.Handler();
        final long start = SystemClock.uptimeMillis();
        final long duration = 1500;

        final android.view.animation.Interpolator interpolator = new BounceInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = Math.max(
                        1 - interpolator.getInterpolation((float) elapsed / duration), 0);
                marker.setAnchor(0.5f, 1.0f + 14 * t);

                if (t > 0.0) {
                    handler.postDelayed(this, 15);
                } else {
                    marker.showInfoWindow();
                }
            }
        });
    }

    public static Marker addMarker(GoogleMap map, LatLng point, String title, String snippet,
                                   BitmapDescriptor icon) {
        MarkerOptions markerOptions = new MarkerOptions()
                .position(point)
                .title(title)
                .snippet(snippet)
                .icon(icon);

        Marker marker = map.addMarker(markerOptions);
        marker.setDraggable(true);
        //dropPinEffect(marker);
        return map.addMarker(markerOptions);
    }

    public static BitmapDescriptor createBubble(Context context, int style, String title) {
        IconGenerator iconGenerator = new IconGenerator(context);
        iconGenerator.setStyle(style);
        Bitmap bitmap = iconGenerator.makeIcon(title);
        BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(bitmap);
        return icon;
    }

}
