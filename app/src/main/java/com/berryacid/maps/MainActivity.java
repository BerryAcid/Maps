package com.berryacid.maps;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;
import java.util.List;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    Button buttonSearch;
    Button buttonGetMyLocation;
    ImageView imageViewLogo;
    private double myLatitud;
    private double myLongitud;
    private double searchLatitud;
    private double searchLongitud;
    private static int AUTOCOMPLETE_REQUEST_CODE = 1;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        onClicks();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initViews() {
        buttonSearch = findViewById(R.id.buttonSearch);
        buttonGetMyLocation = findViewById(R.id.buttonMyLocation);
        imageViewLogo = findViewById(R.id.imageViewLogo);
        gpsConfig();
        Places.initialize(getApplicationContext(), getString(R.string.apiKey));
    }

    private void autocompleteSearch(int requestCode){
         List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);

         Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields).build(this);
         startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId() + ", " + place.getLatLng());
                LatLng latLong = place.getLatLng();
                goSearchMaps(latLong);


            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void goSearchMaps(LatLng latLng) {
        String stringLatLong = String.valueOf(latLng);
        String[] lL = stringLatLong.split(",");

        double latitude = Double.parseDouble(lL[0].replaceAll("[^\\d.]", ""));
        double longitude = Double.parseDouble(lL[1].replaceAll("\\)", ""));

        goScreenMaps(myLatitud, myLongitud, latitude, longitude);
    }


    private void gpsConfig() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Se requiere dar permisos para utilizar el GPS.", Toast.LENGTH_LONG).show();
            return;
        } else {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            myLatitud = location.getLatitude();
            myLongitud = location.getLongitude();
        }
    }

    private void onClicks() {
        buttonGetMyLocation.setOnClickListener(v -> {
            goScreenMaps(myLatitud, myLongitud, 0, 0);
        });
        buttonSearch.setOnClickListener( v -> {
            autocompleteSearch(AUTOCOMPLETE_REQUEST_CODE);
        });
    }

    private void goScreenMaps(double latitud, double longitud, double sLatitud, double sLongitud ) {

        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
        intent.putExtra("myLatitud", latitud);
        intent.putExtra("myLongitud", longitud);
        intent.putExtra("searchLatitud", sLatitud);
        intent.putExtra("searchLongitud",sLongitud);
        startActivity(intent);
    }
}