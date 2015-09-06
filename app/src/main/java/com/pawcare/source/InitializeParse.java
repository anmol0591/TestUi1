package com.pawcare.source;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by anmumukh on 7/18/15.
 */
public class InitializeParse extends AsyncTask<Context, Void, Void> {



    @Override
    protected Void doInBackground(Context... params) {
        try {

            ParseQuery<ParseObject> query = ParseQuery.getQuery("City");
            List<ParseObject> cityObjects = query.find();
            for (ParseObject cityObject : cityObjects) {
                MainActivity.cityNameList.add(cityObject.getString("name"));
                JSONArray aliasContent = cityObject.getJSONArray("alias");
                for (int i = 0; i < aliasContent.length(); i++) {
                    MainActivity.cityNameList.add(aliasContent.get(i).toString());


                }
            }
            query = ParseQuery.getQuery("Animals");
            List<ParseObject> animalObjects = query.find();
            for (ParseObject animalObject : animalObjects) {
                MainActivity.rescueAnimalList.add(animalObject.getString("type"));
                Log.d("PAWED", " " + animalObject.getString("type"));
            }


        } catch (Exception e) {
            Log.d("PAWED", "Exception in initialising Parse" + e.toString());

        }
        return null;
    }
}
