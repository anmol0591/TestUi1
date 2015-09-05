package com.pawcare.source.location;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;

import com.pawcare.source.views.RescueFragment;

/**
 * Created by mur on 7/18/2015.
 */
public class GPSEnableDialog extends android.support.v4.app.DialogFragment {
    @Override

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        FragmentActivity act = getActivity();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(act);
        alertDialog.setTitle("SETTINGS");
        alertDialog.setMessage("Enable Location Provider! Go to settings menu?");
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        getActivity().startActivity(intent);
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
