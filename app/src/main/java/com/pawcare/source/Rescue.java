package com.pawcare.source;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.anm.uitest1.R;
import com.pawcare.source.location.GPSEnableDialog;

import com.pawcare.source.location.LocationAddress;

import java.util.Calendar;

/*
NAME
Rescue.java

DESCRIPTION
Rescue fragment controls all the functionality on the rescue page

RELATED DOCUMENTS
None

PROTOCOLS
None

EXAMPLES
None

NOTES
None

MODIFIED   (MM/DD/YY)
Yosserian    07/17/15 - Implements Rescue Page
*/
public class Rescue extends android.support.v4.app.Fragment implements LocationListener {

    Button btnGPSShowLocation; // Button for GPS Location
    TextView tvAddress;
    String str_location;
    View view_res;
    protected LocationManager locationManager;
    Location location;
    String abc;

    @Override
    /**
     * creates and returns the view hierarchy associated with the fragment.
     * comment
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view_res = inflater.inflate(R.layout.rescue_layout, container, false);
        btnGPSShowLocation = (Button) view_res.findViewById(R.id.btn_location);
        tvAddress = (EditText) view_res.findViewById(R.id.txt_location);

        btnGPSShowLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Location location = null;

                locationManager = (LocationManager) ((Context) getActivity()).getSystemService(Context.LOCATION_SERVICE);
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null) {
                    LocationAddress locationAddress = new LocationAddress();
                    str_location = locationAddress.getAddressFromLocation(location.getLatitude(), location.getLongitude(), getActivity().getApplicationContext());
                    tvAddress.setText(str_location);
                } else {
                    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        GPSEnableDialog gpsEnableDialog = new GPSEnableDialog();
                        gpsEnableDialog.show(getActivity().getSupportFragmentManager(), "gps_tag");
                    }
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * 60, 20, Rescue.this);
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000 * 60, 20, Rescue.this);

                }

            }

        });
        return view_res;
    }


    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            Log.v("Location Changed", location.getLatitude() + " and " + location.getLongitude());
            LocationAddress locationAddress = new LocationAddress();
            String str_location = locationAddress.getAddressFromLocation(location.getLatitude(), location.getLongitude(), getActivity().getApplicationContext());
            tvAddress.setText(str_location);

            locationManager.removeUpdates(this);
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        } else {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        }

    }

    @Override
    public void onProviderDisabled(String provider) {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        } else {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        }

    }
}








