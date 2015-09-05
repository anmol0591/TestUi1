package com.pawcare.source;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by anmumukh on 7/18/15.
 */
public class InitializeParse extends AsyncTask<Context, Void, Void> {
    @Override
    protected Void doInBackground(Context... params) {
        try {
            Parse.initialize(params[0], "J3VbZCECbLqWmVx4RcObMWuZ18JvbDMuCKWQIr47",
                    "QdqM7emSztOwSI46gwdClnZussx3x04cqnPfTAfI");
            Log.d("PAWED", "Parse initialised");

            ParseQuery<ParseObject> query = ParseQuery.getQuery("City");
            List<ParseObject> cityObjects = query.find();
            for (ParseObject cityObject : cityObjects){
                MainActivity.cityNameList.add(cityObject.getString("name"));
                JSONArray aliasContent = cityObject.getJSONArray("alias");
                for(int i = 0; i<aliasContent.length(); i++)
                {
                    MainActivity.cityNameList.add(aliasContent.get(i).toString());


                }
            }
            query = ParseQuery.getQuery("Animals");
            List<ParseObject> animalObjects = query.find();
            for (ParseObject animalObject : animalObjects){
                MainActivity.rescueAnimalList.add( animalObject.getString("type"));
                Log.d("PAWED"," "+animalObject.getString("type"));
            }


            HashMap<String, ArrayList<String>> cityAlias = new HashMap<>();


        }catch (Exception e){
            Log.d("PAWED","Exception in initialising Parse"+e.toString());
            Toast toast = Toast.makeText(params[0], "Unable to initialize Parse", Toast.LENGTH_LONG);
            toast.show();
        }
        return null;
    }
}
