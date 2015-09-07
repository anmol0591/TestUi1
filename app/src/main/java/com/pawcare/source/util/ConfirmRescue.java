package com.pawcare.source.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.anm.uitest1.R;
import com.pawcare.source.Rescue;
import com.pawcare.source.views.RescueFragment;

import java.util.logging.Handler;

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
        final FragmentActivity act = getActivity();
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(act);
        alertDialog.setTitle("PawCare");
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton("ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //dialog.cancel();
                        //PARSE
                        Toast toast = null;
                        toast = Toast.makeText(getActivity().getApplicationContext(),"Sending request to Rescuer",Toast.LENGTH_LONG);
                        toast.show();
                        rescue.persist();

                        ProgressBar prgBar =(ProgressBar)getActivity().findViewById(R.id.rescueProgressBar);
                        prgBar.setVisibility(View.VISIBLE);

                        Button resBtn = (Button)getActivity().findViewById(R.id.btn_rescue);
                        resBtn.setBackgroundColor(Color.parseColor("#d3d3d3"));
                        resBtn.setEnabled(false);

                        EditText type = (EditText) getActivity().findViewById(R.id.et_type);
                        type.setEnabled(false);
                        type.setBackgroundColor(Color.parseColor("#F0F0F0"));

                        EditText moreInfo = (EditText)getActivity().findViewById(R.id.et_more_info);
                        moreInfo.setEnabled(false);
                        moreInfo.setBackgroundColor(Color.parseColor("#F0F0F0"));

                        EditText phoneNumber = (EditText)getActivity().findViewById(R.id.et_contact_number);
                        phoneNumber.setEnabled(false);
                        phoneNumber.setBackgroundColor(Color.parseColor("#F0F0F0"));

                        EditText email = (EditText)getActivity().findViewById(R.id.et_email);
                        email.setEnabled(false);




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

