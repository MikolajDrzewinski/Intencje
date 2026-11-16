package com.example.intencje;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class ThirdActivity extends AppCompatActivity implements LocationListener {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    private TextView tvGpsStatus;
    private TextView tvLatitudeValue;
    private TextView tvLongitudeValue;
    private Button btnStartGps;
    private Button btnStopGps;

    private LocationManager locationManager;
    private boolean isTrackingGpsActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        tvGpsStatus = findViewById(R.id.tvGpsStatus);
        tvLatitudeValue = findViewById(R.id.tvLatitudeValue);
        tvLongitudeValue = findViewById(R.id.tvLongitudeValue);
        btnStartGps = findViewById(R.id.btnStartGps);
        btnStopGps = findViewById(R.id.btnStopGps);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        btnStartGps.setEnabled(true);
        btnStopGps.setEnabled(false);

        btnStartGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLocationTracking();
            }
        });

        btnStopGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopLocationTracking();
            }
        });
    }

    private void startLocationTracking() {
        if (checkLocationPermissions()) {
            try {
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            1000L,
                            1.0f,
                            this
                    );
                }
                if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            1000L,
                            1.0f,
                            this
                    );
                }

                isTrackingGpsActive = true;
                tvGpsStatus.setText(getString(R.string.gps_status_active));

                btnStartGps.setEnabled(false);
                btnStopGps.setEnabled(true);

                Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (lastLocation == null) {
                    lastLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }
                if (lastLocation != null) {
                    updateLocationUI(lastLocation);
                }

            } catch (SecurityException e) {
                e.printStackTrace();
                Toast.makeText(this, "Brak wymaganych uprawnień!", Toast.LENGTH_SHORT).show();
            }
        } else {
            requestLocationPermissions();
        }
    }

    private void stopLocationTracking() {
        if (isTrackingGpsActive) {
            locationManager.removeUpdates(this);
            isTrackingGpsActive = false;
            tvGpsStatus.setText(getString(R.string.gps_status_stopped));

            btnStartGps.setEnabled(true);
            btnStopGps.setEnabled(false);
        }
    }

    private void updateLocationUI(Location location) {
        tvLatitudeValue.setText(getString(R.string.latitude_label, String.valueOf(location.getLatitude())));
        tvLongitudeValue.setText(getString(R.string.longitude_label, String.valueOf(location.getLongitude())));
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        updateLocationUI(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        tvGpsStatus.setText(getString(R.string.gps_status_active));
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        tvGpsStatus.setText(getString(R.string.gps_status_idle));
    }

    private boolean checkLocationPermissions() {
        int finePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int coarsePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        return finePermission == PackageManager.PERMISSION_GRANTED && coarsePermission == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                LOCATION_PERMISSION_REQUEST_CODE
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationTracking();
            } else {
                tvGpsStatus.setText(getString(R.string.gps_status_permission_denied));
                Toast.makeText(this, "Uprawnienie do lokalizacji zostało odrzucone.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isTrackingGpsActive) {
            stopLocationTracking();
        }
    }
}
