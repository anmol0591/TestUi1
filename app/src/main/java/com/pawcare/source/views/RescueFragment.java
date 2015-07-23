package com.pawcare.source.views;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.anm.uitest1.R;
import com.pawcare.source.Rescue;
import com.pawcare.source.location.GPSEnableDialog;


import com.pawcare.source.location.LocationAddress;
import com.pawcare.source.util.ConfirmRescue;
import com.pawcare.source.util.ImageCapture;

import java.io.File;
import java.util.regex.Pattern;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;


/*
NAME
RescueFragment.java

DESCRIPTION
RescueFragment fragment controls all the functionality on the rescue page

RELATED DOCUMENTS
None

PROTOCOLS
None

EXAMPLES
None

NOTES
None

MODIFIED   (MM/DD/YY)
Yosserian    07/17/15 - Implements RescueFragment Page
*/
public class RescueFragment extends android.support.v4.app.Fragment implements LocationListener {

    Button btnGPSShowLocation, btnRescue; // Button for GPS Location
    EditText tvAddress, et_more_info, et_email, et_type, et_contact_number, et_isd;
    ProgressBar progressBar;
    String str_location;
    View view_res;
    protected LocationManager locationManager;
    ImageView viewImage;            // For capturing image
    ImageButton captureImage;       // Button for updating image
    byte[] imageBytes;
    Bitmap imageBitmap;
    int request;
    private Location location = null;
    SharedPreferences sharedPreferences;
    Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+

    @Override
    /**
     * creates and returns the view hierarchy associated with the fragment.
     * comment
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view_res = inflater.inflate(R.layout.rescue_layout, container, false);
        initializeUIElements();
        progressBar.setVisibility(View.INVISIBLE);
        et_email.setVisibility(View.GONE);
        et_contact_number.setVisibility(View.GONE);
        et_isd.setVisibility(View.GONE);
        Pattern phonePattern = Patterns.PHONE;
        Account[] accounts = AccountManager.get(getActivity().getApplicationContext()).getAccounts();
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                String possibleEmail = account.name;
                et_email.setText(possibleEmail);
            }
            if (phonePattern.matcher(account.name).matches()) {
                String possibleContact = account.name;
                et_contact_number.setText(possibleContact);
            }
        }
        TelephonyManager tm = (TelephonyManager) (getActivity().getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE));
        String number = tm.getLine1Number();
        if (number != null) {
            et_contact_number.setText(number);
        }
        // Clicked add image button.
        captureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("PAWED", "Ishita: Image button clicked.");
                selectImageDialog();
            }
        });

        btnGPSShowLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                progressBar.setVisibility(View.VISIBLE);
                locationManager = (LocationManager) ((Context) getActivity()).getSystemService(Context.LOCATION_SERVICE);
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null) {
                    progressBar.setVisibility(View.INVISIBLE);
                    LocationAddress locationAddress = new LocationAddress();
                    str_location = locationAddress.getAddressFromLocation(location.getLatitude(), location.getLongitude(), getActivity().getApplicationContext());
                    tvAddress.setText(str_location);
                } else {
                    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        GPSEnableDialog gpsEnableDialog = new GPSEnableDialog();
                        gpsEnableDialog.show(getActivity().getSupportFragmentManager(), "gps_tag");

                    }
                    progressBar.setVisibility(View.VISIBLE);
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * 60, 20, RescueFragment.this);
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000 * 60, 20, RescueFragment.this);

                }

            }

        });

        btnRescue.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Boolean validation = fieldValidation();
                ConfirmRescue confirmRescue = new ConfirmRescue();
                if (validation == true) {

                    confirmRescue.setMessage("Are you sure about rescuing this animal?");
                    confirmRescue.show(getActivity().getSupportFragmentManager(), "alert");
                    Rescue rescue = new Rescue();
                    rescue.setType(et_type.getText().toString());
                    rescue.setMoreInfo(et_more_info.getText().toString());
                    rescue.setAddress(tvAddress.getText().toString());
                    rescue.setMail(et_email.getText().toString());
                    rescue.setLocation(RescueFragment.this.location);
                    rescue.setPhone(et_contact_number.getText().toString());
                    if (imageBytes != null) {
                        try {
                            Log.d("PAWED", "image file is not null");
                            Log.d("PAWED", "byte array" + imageBytes.toString());
                            rescue.setImageFile(imageBytes);
                        } catch (Exception e) {
                            Log.d("PAWED", "This is an error" + e.toString());
                        }
                    }

                    confirmRescue.rescue = rescue;
                    //getActivity().setContentView(R.layout.rescue_layout);

                } else {
                    //parse rescue
                }


            }
        });

        TextView tv = (TextView) view_res.findViewById(R.id.textView);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_contact_number.isShown() && et_email.isShown() && et_isd.isShown()) {
                    slide_up((Context) getActivity(), et_contact_number);
                    et_contact_number.setVisibility(View.GONE);
                    slide_up((Context) getActivity(), et_email);
                    et_email.setVisibility(View.GONE);
                    slide_up((Context) getActivity(), et_isd);
                    et_isd.setVisibility(View.GONE);

                } else {
                    et_contact_number.setVisibility(View.VISIBLE);
                    slide_down((Context) getActivity(), et_contact_number);
                    et_email.setVisibility(View.VISIBLE);
                    slide_down((Context) getActivity(), et_email);
                    et_isd.setVisibility(View.VISIBLE);
                    slide_down((Context) getActivity(), et_isd);
                }
            }
        });

        sharedPreferences = ((Context) getActivity()).getSharedPreferences("MY_PREFS", (Context.MODE_PRIVATE));
        if (sharedPreferences.contains("email") && sharedPreferences.contains("contact")) {
            String savedEmail = sharedPreferences.getString("email", "");
            String savedNo = sharedPreferences.getString("contact", "");
            if (savedEmail != null && savedNo != null && !savedEmail.equals("") && !savedNo.equals("")) {
                et_email.setText(savedEmail);
                et_contact_number.setText(savedNo);
            } else {
                et_email.setVisibility(View.VISIBLE);
                et_contact_number.setVisibility(View.VISIBLE);
            }
        }
        return view_res;
    }


    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            progressBar.setVisibility(View.INVISIBLE);
            this.location = location;
            Log.v("Location Changed", location.getLatitude() + " and " + location.getLongitude());
            LocationAddress locationAddress = new LocationAddress();
            String str_location = locationAddress.getAddressFromLocation(location.getLatitude(), location.getLongitude(), getActivity().getApplicationContext());
            tvAddress.setText(str_location);

            locationManager.removeUpdates(this);
        }
        else {
            progressBar.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        } else {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        }

    }

    @Override
    public void onProviderDisabled(String provider) {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        } else {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            imageBitmap = ImageCapture.displayImage(viewImage, data, (Context) getActivity(), requestCode);
            request = requestCode;
        }
    }

    public void initializeUIElements() {
        btnGPSShowLocation = (Button) view_res.findViewById(R.id.btn_location);
        btnRescue = (Button) view_res.findViewById(R.id.btn_rescue);
        btnRescue.setAllCaps(false);
        tvAddress = (EditText) view_res.findViewById(R.id.txt_location);
        tvAddress.setTextColor(Color.parseColor("#909090"));
        tvAddress.setHint(Html.fromHtml("<small>" + "Tap to enter location" + "</small>"));

        et_type = (EditText) view_res.findViewById(R.id.et_type);
        et_type.setTextColor(Color.parseColor("#909090"));
        et_type.setHint(Html.fromHtml("<small>" + "Enter Type e.g. Dog, Cat etc" + "</small>"));

        et_email = (EditText) view_res.findViewById(R.id.et_email);
        et_email.setTextColor(Color.parseColor("#909090"));
        et_email.setHint(Html.fromHtml("<small>" + "Enter Email Address" + "</small>"));

        et_contact_number = (EditText) view_res.findViewById(R.id.et_contact_number);
        et_contact_number.setTextColor(Color.parseColor("#909090"));
        et_contact_number.setHint(Html.fromHtml("<small>" + "Enter Contact Number" + "</small>"));

        et_more_info = (EditText) view_res.findViewById(R.id.et_more_info);
        et_more_info.setTextColor(Color.parseColor("#909090"));
        et_more_info.setHint(Html.fromHtml("<small>" + "Description e.g condition" + "</small>"));

        captureImage = (ImageButton) view_res.findViewById(R.id.btn_image_capture);
        viewImage = (ImageView) view_res.findViewById(R.id.img_capture);
        progressBar = (ProgressBar) view_res.findViewById(R.id.progressBar1);

        et_isd = (EditText) view_res.findViewById(R.id.isdCode);
    }

    public Boolean fieldValidation() {
        Boolean validated = true;
        if (et_type.getText().toString().length() == 0 || et_type == null) {
            validated = false;
            et_type.setError(Html.fromHtml("<small><font color=\"#00ccff\"" + "Animal Type is required!" + "</small>"));

        } else if (tvAddress.getText().toString().length() == 0 || tvAddress == null) {
            validated = false;
            tvAddress.setError("Location is required!");
        } else if (!emailPattern.matcher(et_email.getText().toString()).matches()) {
            validated = false;
            et_email.setError("Email id is invalid!");
        } else if (et_contact_number.getText().toString().length() != 10) {
            validated = false;
            et_contact_number.setError("Contact Number is invalid!");
        }
        return validated;
    }

    // Displays the dialog for selecting/capturing image.
    private void selectImageDialog() {

        Log.d("PAWED", "Ishita: Inside selectImage");

        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder((Context) getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    Log.d("PAWED", "Ishita: Option Take Photo");
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    Log.d("PAWED", "Ishita: Option Choose from Gallery");
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(intent, 2);

                }
                else if (options[item].equals("Cancel")) {
                    Log.d("PAWED", "Ishita: Cancel");
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public static void slide_down(Context ctx, View v) {

        Animation a = AnimationUtils.loadAnimation(ctx, R.anim.slide_down);
        if (a != null) {
            a.reset();
            if (v != null) {
                v.clearAnimation();
                v.startAnimation(a);
            }
        }
    }

    public static void slide_up(Context ctx, View v) {

        Animation a = AnimationUtils.loadAnimation(ctx, R.anim.slide_up);
        if (a != null) {
            a.reset();
            if (v != null) {
                v.clearAnimation();
                v.startAnimation(a);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sharedPreferences = ((Context)getActivity()).getSharedPreferences("MY_PREFS", (Context.MODE_PRIVATE));
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", et_email.getText().toString());
        editor.putString("contact", et_contact_number.getText().toString());
        editor.commit();
    }
}