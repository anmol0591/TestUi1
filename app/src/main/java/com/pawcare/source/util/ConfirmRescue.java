package com.pawcare.source.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;

/**
 * Created by mur on 7/18/2015.
 */
public class ConfirmRescue extends android.support.v4.app.DialogFragment {

    public String setMessage(String message) {
        return this.message=message;
    }

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
