package com.example.lab12;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;
import com.google.maps.android.ui.IconGenerator;

import java.util.Arrays;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
    @SuppressLint("PotentialBehaviorOverride")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (checkPermission())
            mMap.setMyLocationEnabled(true);
        else
            askPermission();

        LatLng facultate = new LatLng(45.7450284, 21.2275766);
        LatLng pozitie2 = new LatLng(45.75563972227866, 21.22772502553505);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(facultate));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(13));
        IconGenerator iconGenerator = new IconGenerator(this);
        iconGenerator.setColor(ContextCompat.getColor(this, android.R.color.holo_blue_dark));
        iconGenerator.setTextAppearance(R.color.textColor);
        mMap.addMarker(new MarkerOptions()
                .position(facultate)
                .icon(BitmapDescriptorFactory.fromBitmap(iconGenerator.makeIcon("Facultate"))));

        mMap.addMarker(new MarkerOptions()
                .position(pozitie2)
                .icon(BitmapDescriptorFactory.fromBitmap(iconGenerator.makeIcon("Libertatii"))));

        List<LatLng> l = Arrays.asList(facultate, pozitie2);
        drawPolyLineOnMap(l,googleMap);
        mMap.setOnMarkerClickListener(marker -> {
            if (marker.getPosition().equals(facultate)) {
                Toast.makeText(MapsActivity.this, "I'm studying", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(MapsActivity.this, "Distanta este de: " + SphericalUtil.computeDistanceBetween(facultate, pozitie2) + " m", Toast.LENGTH_LONG).show();
            }
            return false;
        });
    }



    public void drawPolyLineOnMap(List<LatLng> list, GoogleMap googleMap) {

        PolylineOptions polyOptions = new PolylineOptions();
        polyOptions.color(Color.GREEN);
        polyOptions.width(8);
        polyOptions.addAll(list);
        googleMap.addPolyline(polyOptions);
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng latLng : list) {
            builder.include(latLng);
        }
        builder.build();
    }

    private final int REQ_PERMISSION = 5;

    // Check for permission to access Location
    private boolean checkPermission() {
        // Ask for permission if it wasn't granted yet
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED);
    }

    // Asks for permission
    private void askPermission() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQ_PERMISSION
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQ_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted
                    if (checkPermission())
                        mMap.setMyLocationEnabled(true);

                } else {
                    // Permission denied

                }
                break;
            }
        }
    }
}