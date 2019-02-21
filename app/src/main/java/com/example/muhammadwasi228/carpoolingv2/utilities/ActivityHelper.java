package com.example.muhammadwasi228.carpoolingv2.utilities;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by muhammadwasi228 on 12/3/2017.
 */

public class ActivityHelper {
    private static float DEFAULT_ZOOM = 15;

    public static Marker moveCameraWithZoom(Context context, GoogleMap mMap, LatLng latLng, String TAG){
        Log.d(TAG, "moveCameraWithZoom: move camera with zoom");
        Log.d(TAG, "onComplete: Latitude"+latLng.latitude);
        Log.d(TAG, "onComplete: Latitude"+latLng.latitude);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,DEFAULT_ZOOM));
        Log.d(TAG, "moveCameraWithZoom: camera updated");
        MarkerOptions markerOptions= new MarkerOptions().position(latLng).draggable(true);
        Log.d(TAG, "moveCameraWithZoom: marker made");
        Marker marker=mMap.addMarker(markerOptions);
        Log.d(TAG, "moveCameraWithZoom: marker added");
        return marker;
    }
}
