package com.pawcare.source.util;

import android.graphics.Bitmap;
import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.pawcare.source.Rescue;

import java.io.ByteArrayOutputStream;

/**
 * Created by anmumukh on 7/19/15.
 */
public class BitmapConverter extends AsyncTask<Void, Void, Void> {
    public Bitmap bitmap;
    public int request;
    public BitmapConverterCallback callback;

    @Override
    protected Void doInBackground(Void... objects) {
        callback.converted(convert());
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }
    public byte[]convert(){
        byte [] bytes = null;
        if (request == 1){
            Log.d("PAWED","START "+System.currentTimeMillis());
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            bytes = stream.toByteArray();
            Log.d("PAWED","STOP "+System.currentTimeMillis() + " " +bytes.length);
        }
        else if (request == 2) {
            Log.d("PAWED", "START " + System.currentTimeMillis());
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            bytes = stream.toByteArray();

            Log.d("PAWED","STOP "+System.currentTimeMillis()+ " " +bytes.length);

        }
        return bytes;
    }
}


