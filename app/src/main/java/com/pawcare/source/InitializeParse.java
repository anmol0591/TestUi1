package com.pawcare.source;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.pawcare.source.util.Persistence;

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
            List<String> cityNameList = new ArrayList<>();
            for (ParseObject cityObject : cityObjects) {

                cityNameList.add(cityObject.getString("name"));
                JSONArray aliasContent = cityObject.getJSONArray("alias");
                for (int i = 0; i < aliasContent.length(); i++) {
                    cityNameList.add(aliasContent.get(i).toString());
                }
            }
            Persistence p = new Persistence(params[0]);
            p.persistList(cityNameList,"city");

            ArrayList<String> animals = new ArrayList<>();
            query = ParseQuery.getQuery("Animals");
            query.setLimit(300);
            List<ParseObject> animalObjects = query.find();
            for (ParseObject animalObject : animalObjects) {
                animals.add(animalObject.getString("type"));
            }
            Log.d("PAWEDX",animals.toString());
            p.persistList(animals,"animals");
            Log.d("PAWEDX","INIT DONE");
        } catch (Exception e) {
            Log.d("PAWED", "Exception in initialising Parse" + e.toString());

        }
        return null;
    }
}
