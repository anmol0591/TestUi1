package com.pawcare.source.location;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Telephony;
import android.util.Log;

import com.pawcare.source.GeoCodeCallback;

import java.io.IOException;
import java.util.List;
import java.util.Locale;



/**
 * Created by mur on 7/17/2015.
 */
public class LocationAddress extends AsyncTask<Double, Void, Void> {
    public Context context = null;
    public GeoCodeCallback callback;

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Address address;
    @Override
    protected Void doInBackground(Double... doubles) {
        double latitude = doubles[0];
        double longitude = doubles[1];
        this.address = getAddressFromLocation(latitude,longitude,context);
        this.callback.convertedAddress(getAddressString(this.address));
        return null;
    }

    private static final String TAG = "LocationAddress";
    String locationResult = "";

    public String getAddressString(Address address) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
            sb.append(address.getAddressLine(i)).append(" ");
        }
        locationResult = sb.toString();
        return locationResult;
    }
    public String getCity(Address address){
        return address.getLocality();
    }
    public Address getAddressFromLocation(final double latitude, final double longitude,
                                          final Context context){
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        Address address = null;
        try {
            List<Address> addressList = geocoder.getFromLocation(
                    latitude, longitude, 3);
            if (addressList != null && addressList.size() > 0) {
                address =  addressList.get(0);
            }
        } catch (IOException e) {
            Log.d("Anmol", "Unable connect to Geocoder");
        }
        return address;
    }

}
