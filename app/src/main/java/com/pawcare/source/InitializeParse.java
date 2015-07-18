package com.pawcare.source;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.parse.Parse;

/**
 * Created by anmumukh on 7/18/15.
 */
public class InitializeParse extends AsyncTask<Context, Void, Void> {
    @Override
    protected Void doInBackground(Context... params) {
        try {
            Parse.initialize(params[0],"J3VbZCECbLqWmVx4RcObMWuZ18JvbDMuCKWQIr47",
                    "QdqM7emSztOwSI46gwdClnZussx3x04cqnPfTAfI");
            Log.d("PAWED", "Parse initialised");
        }catch (Exception e){
            Log.d("PAWED","Exception in initialising Parse"+e.toString());
            Toast toast = Toast.makeText(params[0], "Unable to initialize Parse", Toast.LENGTH_LONG);
            toast.show();
        }
        return null;
    }
}
