package com.example.amit.hickerwatch;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;
    TextView latitude,logitude,accuracy,altitude,address;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1)
        {
            if (grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults.length>0)
            {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)

                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);

            }
        }

    }

    public void startListening()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);

        }
    }

    public void updateLocation(Location location)
    {
        String address1 = "Could not Found Address";
        latitude = (TextView)findViewById(R.id.latitude);
        logitude = (TextView)findViewById(R.id.logitude);
        accuracy = (TextView)findViewById(R.id.accuracy);
        altitude = (TextView)findViewById(R.id.altitude);
        address = (TextView)findViewById(R.id.address);

        latitude.setText("Latitude :"+location.getLatitude());
        logitude.setText("Longitude :"+location.getLongitude());
        accuracy.setText("Accuracy :"+location.getAccuracy());
        altitude.setText("Altitude :"+location.getAltitude());
        address1 = "Address: \n";
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            if (addresses.size()>0 & addresses!= null)
            {
                if (addresses.get(0).getSubThoroughfare()!= null) {
                    address1 += addresses.get(0).getSubThoroughfare() + " ";
                }
                if (addresses.get(0).getThoroughfare()!= null) {
                    address1 += addresses.get(0).getThoroughfare() + " \n";
                }
                if (addresses.get(0).getLocality()!= null) {
                    address1 += addresses.get(0).getLocality() + " \n";
                }
                if (addresses.get(0).getPostalCode()!= null) {
                    address1 += addresses.get(0).getPostalCode() + " \n";
                }
                if (addresses.get(0).getCountryName()!= null) {
                    address1 += addresses.get(0).getCountryName() + " \n";
                }
            }

            address.setText(address1);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                updateLocation(location);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };
        if (Build.VERSION.SDK_INT < 23)
        {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);

        }else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);

            else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                Location locationlast = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                if (locationlast != null) {
                    updateLocation(locationlast);
                }
            }
        }
    }
}
