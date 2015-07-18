package com.pawcare.source;

import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseSession;
import com.parse.SaveCallback;

import java.util.Arrays;

/**
 * Created by anmumukh on 7/18/15.
 */
public class Rescue {
    /*
    Condition
Location GeoPoint
Type
ImageFile
MoreInfo
Address
Mail
     */
    public static final String RESCUE_TYPE = "Type";
    public static final String RESCUE_CONDITION = "Condition";
    public static final String RESCUE_MORE_INFO = "MoreInfo";
    public static final String RESCUE_ADDRESS = "Address";
    public static final String RESCUE_MAIL = "Mail";
    public static final String RESCUE_IMAGE_FILE = "ImageFile";
    public static final String RESCUE_LOCATION = "Location";
    public static final String RESCUE_PHONE = "Phone";

    private String type;
    private String condition;
    private String moreInfo;
    private String address;
    private String mail;

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

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
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
        Log.d("PAWED","persist");
        ParseObject rescue = new ParseObject("Rescue");
        rescue.put(RESCUE_CONDITION,condition);
        rescue.put(RESCUE_ADDRESS,address);
        ParseGeoPoint rescueLocation = new ParseGeoPoint(location.getLatitude(),location.getLongitude());
        rescue.put(RESCUE_LOCATION,rescueLocation);
        rescue.put(RESCUE_MAIL,mail);
        rescue.put(RESCUE_MORE_INFO,moreInfo);
        rescue.put(RESCUE_TYPE,type);
        if (imageFile != null){
            Log.d("PAWED", "setting image parse file");
            rescue.put(RESCUE_IMAGE_FILE, new ParseFile("rescueImage.jpg",imageFile));
        }
        int x;
        Log.d("PAWED", "CLOSE " + toString());
        rescue.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null){
                    Log.d("PAWED","PE "+e.toString());
                }
            }
        });
        return true;
    }

    @Override
    public String toString() {
        Log.d("PAWED","inTS");
//        String str = "Rescue{" +
//                "type='" + type + '\'' +
//                ", condition='" + condition + '\'' +
//                ", moreInfo='" + moreInfo + '\'' +
//                ", address='" + address + '\'' +
//                ", mail='" + mail + '\'' +
//                ", phone='" + phone + '\'' +
//                ", imageFile=" + Arrays.toString(imageFile) +
//                ", location=" + location +
//                '}';
        Log.d("PAWED","printed");
        return "";
    }
}
