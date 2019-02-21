package com.example.muhammadwasi228.carpoolingv2;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.example.muhammadwasi228.carpoolingv2.models.PlaceInfo;
import com.example.muhammadwasi228.carpoolingv2.utilities.ActivityHelper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SelectLocationActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener,GoogleMap.OnMarkerDragListener {
    private GoogleMap mMap;

    private AutoCompleteTextView mSearchText;
    private static final String TAG = "SelectLocationActivity";
    private PlaceAutocompleteAdapter mPlaceAutocompleteAdapter;
    //GoogleApiClient mGoogleApiClient;
    GeoDataClient mGeoDataClient;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(new LatLng(-40, -168), new LatLng(71, 136));
    PlaceInfo mPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);
        mSearchText = findViewById(R.id.input_search);
        init();
    }

    public void init() {
        showMap();
        mGeoDataClient = Places.getGeoDataClient(this, null);
        Log.d(TAG, "init:initializing ");
//        mGoogleApiClient=new GoogleApiClient.Builder(this)
//                .addApi(Places.GEO_DATA_API)
//                .addApi(Places.PLACE_DETECTION_API)
//                .enableAutoManage(this, this)
//                .build();
        mSearchText.setOnItemClickListener(mAutocompleteClickListener);
        mPlaceAutocompleteAdapter = new PlaceAutocompleteAdapter(this, mGeoDataClient, LAT_LNG_BOUNDS, null);
        mSearchText.setAdapter(mPlaceAutocompleteAdapter);
        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == keyEvent.ACTION_DOWN
                        || keyEvent.getAction() == keyEvent.KEYCODE_ENTER) {
                }

                return false;
            }
        });
    }

    private Address geoLocate(LatLng latLng) {

        Log.d(TAG, "geoLocate: geolocating");
        Address address=null;
        String searchString = mSearchText.getText().toString();

        Geocoder geocoder = new Geocoder(SelectLocationActivity.this);
        List<Address> addressList = new ArrayList<>();
        try {
            addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude,1);
        } catch (IOException e) {
            Log.d(TAG, "geoLocate: IOEXCEPTIOn  " + e.getMessage());
            e.printStackTrace();
        }
        if (addressList.size() > 0) {
           address  = addressList.get(0);
            Log.d(TAG, "geoLocate: found a location: " + address);

        }
        return address;
    }

    private void showMap() {
        Log.d(TAG, "showMap: called");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment_selectloc_activity);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMarkerDragListener(this);
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed: Map not initialized");

    }

    private void hideSoftKeyboard(){
        Log.d(TAG, "hideSoftKeyboard: Hide soft  keyboard");
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private Marker marker;
    private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            hideSoftKeyboard();
            final AutocompletePrediction item = mPlaceAutocompleteAdapter.getItem(i);
            final String placeId= item.getPlaceId();

          Task<PlaceBufferResponse> placeResult = mGeoDataClient.getPlaceById(placeId);
          placeResult.addOnCompleteListener(new OnCompleteListener<PlaceBufferResponse>() {
              @Override
              public void onComplete(@NonNull Task<PlaceBufferResponse> task) {
                 if( task.isSuccessful() && task.getResult() != null  ){
                     Log.d(TAG, "onComplete: placeResult is successful");
                     PlaceBufferResponse places=task.getResult();
                      Place place = places.get(0);

                      try{
                          mPlace=new PlaceInfo();
                          mPlace.setName(place.getName().toString());
                          mPlace.setAddress(place.getAddress().toString());
                          mPlace.setId(place.getId());
                          mPlace.setLatLng(place.getLatLng());}
                      catch (NullPointerException e){
                          Log.e(TAG, "onComplete: Nulll Pointer Exception",e );
                      }
                    if(marker!=null){
                          marker.remove();
                    }
                    marker= ActivityHelper.moveCameraWithZoom(getApplicationContext(),mMap,
                            mPlace.getLatLng(),TAG);
                      places.release();
                 }else {
                     Log.d(TAG, "onComplete: placeResult task not successsful");
                     return;
                 }
              }
          });
        }
    };


    @Override
    public void onMarkerDragStart(Marker marker) {
    }

    @Override
    public void onMarkerDrag(Marker marker) {
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        Log.d(TAG, "onMarkerDragEnd: "+marker.getPosition().latitude+" ,"+marker.getPosition().longitude);
        Address address=geoLocate(new LatLng(marker.getPosition().latitude,marker.getPosition().longitude));
        mSearchText.setText(address.getAddressLine(0));

        Log.d(TAG, "onMarkerDragEnd: "+address.getLocality()+address.getAddressLine(0));
        }
}
