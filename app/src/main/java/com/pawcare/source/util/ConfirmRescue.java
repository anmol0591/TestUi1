package com.pawcare.source.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.pawcare.source.Rescue;

/**
 * Created by mur on 7/18/2015.
 */
public class ConfirmRescue extends android.support.v4.app.DialogFragment {

    public String setMessage(String message) {
        return this.message=message;
    }
    public Rescue rescue;
    String message;

    public ConfirmRescue()
    {

    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        FragmentActivity act = getActivity();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(act);
        alertDialog.setTitle("PawCare");
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton("ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //dialog.cancel();
                        //PARSE
                        Toast toast = null;
                        Log.d("PAWED", "before persist");
                        Log.d("PAWED",rescue.toString());
                        if (rescue.persist())
                        {
                            toast = Toast.makeText(getActivity().getApplicationContext(),"Rescue request sent.Thank you!!!",Toast.LENGTH_LONG);
                        }
                        else {
                            toast = Toast.makeText(getActivity().getApplicationContext(),"Error in sending request. Please try again.",Toast.LENGTH_LONG);
                        }
                        toast.show();

                    }
                });
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog dialog = alertDialog.create();
        return dialog;
    }
}
