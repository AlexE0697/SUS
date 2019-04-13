package com.example.sus.Activities;

import android.Manifest;
import android.app.ActionBar;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.sus.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocationActivity extends FragmentActivity implements OnMapReadyCallback {


    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
    }


                /**
                 * Manipulates the map once available.
                 * This callback is triggered when the map is ready to be used.
                 * This is where we can add markers or lines, add listeners or move the camera. In this case,
                 * we just add a marker near Sydney, Australia.
                 * If Google Play services is not installed on the device, the user will be prompted to install
                 * it inside the SupportMapFragment. This method will only be triggered once the user has
                 * installed Google Play services and returned to the app.
                 */
                @Override
                public void onMapReady (GoogleMap googleMap){
                    mMap = googleMap;

                    // Add a marker in Dublin and move the camera
                    LatLng dublin = new LatLng(53.3487, 6.2431);
                    mMap.addMarker(new MarkerOptions().position(dublin).title("Marker in Dublin"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(dublin));
                }

}