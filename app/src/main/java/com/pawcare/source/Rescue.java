package com.pawcare.source;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;


import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseSession;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

/**
 * Created by anmumukh on 7/18/15.
 */
public class Rescue {
    public static final String RESCUE_TYPE = "Type";
    public static final String RESCUE_MORE_INFO = "MoreInfo";
    public static final String RESCUE_ADDRESS = "Address";
    public static final String RESCUE_MAIL = "Mail";
    public static final String RESCUE_IMAGE_FILE = "ImageFile";
    public static final String RESCUE_LOCATION = "Location";
    public static final String RESCUE_PHONE = "Phone";
    public static final String RESCUE_TABLE = "Rescue";
    public static final String RESCUE_IMAGE_NAME = "RescueImage.jpg";

    private String type;

    private String moreInfo;
    private String address;
    private String mail;

    public PersistCallback callback;



    public Bitmap getImageMap() {
        return imageMap;
    }

    public void setImageMap(Bitmap imageMap) {

        this.imageMap = imageMap;
    }

    private Bitmap imageMap;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    private String phone;
    private byte[] imageFile;
    private Location location;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMoreInfo() {
        return moreInfo;
    }

    public void setMoreInfo(String moreInfo) {
        this.moreInfo = moreInfo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public byte[] getImageFile() {
        return imageFile;
    }

    public void setImageFile(byte[] imageFile) {
        this.imageFile = imageFile;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public boolean persist()
    {
        ParseObject rescue = new ParseObject(RESCUE_TABLE);
        if (address != null){
            rescue.put(RESCUE_ADDRESS,address);
        }
        if (location != null){
            ParseGeoPoint rescueLocation = new ParseGeoPoint(location.getLatitude(),location.getLongitude());
            rescue.put(RESCUE_LOCATION,rescueLocation);
        }
        if (mail != null){
            rescue.put(RESCUE_MAIL,mail);
        }
        if (phone != null){
            rescue.put(RESCUE_PHONE, phone);
        }
        if (moreInfo != null){
            rescue.put(RESCUE_MORE_INFO,moreInfo);
        }
        rescue.put(RESCUE_TYPE,type);
        if (imageFile != null){
            Log.d("PAWED", "setting image parse file");
            rescue.put(RESCUE_IMAGE_FILE, new ParseFile(RESCUE_IMAGE_NAME,imageFile));
        }
        Log.d("PAWED","Save in background");
        rescue.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Rescue.this.callback.persisted(e);
            }
        });
        return true;
    }

    @Override
    public String toString() {
        String string = "";
        if (type != null){
            string += type;
        }
        if (moreInfo != null){
            string += type;
        }
        if (address != null){
            string += address;
        }
        if (mail != null){
            string += mail;
        }
        if (phone != null){
            string += phone;
        }
        if (imageFile != null){
            string += imageFile.toString();
        }
        if (location != null){
            string += location.toString();
        }
        return string;
    }
}


