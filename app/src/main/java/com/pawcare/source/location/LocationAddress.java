package com.pawcare.source.location;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


/**
 * Created by mur on 7/17/2015.
 */
public class LocationAddress {
    private static final String TAG = "LocationAddress";
    String locationResult = "";

    public String getAddressFromLocation(final double latitude, final double longitude,
                                         final Context context) {


        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        try {
            List<Address> addressList = geocoder.getFromLocation(
                    latitude, longitude, 3);
            if (addressList != null && addressList.size() > 0) {
                Address address = addressList.get(0);
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                    sb.append(address.getAddressLine(i)).append(" ");
                }
                //  sb.append(address.getLocality()).append(" ");
                locationResult = sb.toString();
            }
        } catch (IOException e) {
            Log.d("Anmol", "Unable connect to Geocoder");
        }


        return locationResult;
    }

}
