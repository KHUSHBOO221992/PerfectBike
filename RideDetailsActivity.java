package com.example.prith.perfectbike;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RideDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    Double lat = 0.0;
    Double lng = 0.0;
    Double currentLat = 0.0;
    Double currentLng = 0.0;
    int distanceInKm = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_details);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabbtnBack);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent menuIntent = new Intent(RideDetailsActivity.this, MenuActivity.class);
                startActivity(menuIntent);
            }
        });

        Intent intent = getIntent();

        lat = intent.getDoubleExtra("lat", 0.0);
        lng = intent.getDoubleExtra("lng",0.0);
        currentLat = intent.getDoubleExtra("currentLat",0.0);
        currentLng = intent.getDoubleExtra("currentLng",0.0);
        distanceInKm = (int) intent.getDoubleExtra("distanceInKm", 0.0);

//        int lat = intent.getIntExtra("lat", 0);
//        int lng = intent.getIntExtra("lng", 0);
//        int currentLat = intent.getIntExtra("currentLat", 0);
//        int currentLng = intent.getIntExtra("currentLng", 0);
//        int distanceInKm = intent.getIntExtra("distanceInKm", 0);

        EditText etCurrentLoc = findViewById(R.id.etCurrentLoc);
        EditText etDestinationLoc = findViewById(R.id.etDestinationLoc);
        EditText etDistance = findViewById(R.id.etDistance);
        EditText etCalories = findViewById(R.id.etCalories);

        etCurrentLoc.setText("Current Lat : " + String.valueOf(currentLat) + "Lng : " + String.valueOf(currentLng));
        etDestinationLoc.setText("Dest. Lat : " + String.valueOf(lat) + " Lng : " + String.valueOf(lng));
        etDistance.setText(String.valueOf(String.valueOf(distanceInKm)) + " KM");
        etCalories.setText(String.valueOf(distanceInKm * 100) + " Calories");

        Button map = findViewById(R.id.btnMap);
        map.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnMap){
            Intent intent = new Intent(RideDetailsActivity.this, MapsActivity.class);
            intent.putExtra("lat", lat);
            intent.putExtra("lng", lng);
            intent.putExtra("currentLat", currentLat);
            intent.putExtra("currentLng", currentLng);
            intent.putExtra("distanceInKm", distanceInKm);
            startActivity(intent);
        }
    }
}
