package com.berryacid.maps;

import androidx.annotation.DrawableRes;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private double startLatitud;
    private double startLongitud;
    private double endLatitud;
    private double endLongitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        initViews();
    }

    private void initViews() {
        startLatitud = getIntent().getDoubleExtra("myLatitud", 0);
        startLongitud = getIntent().getDoubleExtra("myLongitud", 0);
        endLatitud = getIntent().getDoubleExtra("searchLatitud", 0);
        endLongitud = getIntent().getDoubleExtra("searchLongitud", 0);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng myLocation = new LatLng(startLatitud, startLongitud);
        LatLng searchLocation = new LatLng(endLatitud, endLongitud);
        mMap.addMarker(new MarkerOptions().position(myLocation).title("Mi ubicación").snippet(startLatitud + ", " + startLongitud).icon(BitmapDescriptorFactory.fromResource(R.drawable.person)));
        mMap.addMarker(new MarkerOptions().position(searchLocation).title("Destino").snippet(endLatitud + ", " + endLongitud));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 10));
    }
}