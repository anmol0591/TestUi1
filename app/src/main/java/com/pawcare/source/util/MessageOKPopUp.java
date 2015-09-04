package com.pawcare.source.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

/**
 * Created by mur on 8/23/2015.
 */
public class MessageOKPopUp extends android.support.v4.app.DialogFragment  {
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    String message;
    public MessageOKPopUp(){

    }
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final FragmentActivity act = getActivity();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(act);
        alertDialog.setTitle("Alert");
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton("ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });


        AlertDialog dialog = alertDialog.create();
        return dialog;
    }

}
