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
import android.text.Html;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.anm.uitest1.R;
import com.pawcare.source.GeoCodeCallback;
import com.pawcare.source.MainActivity;
import com.pawcare.source.PersistCallback;
import com.pawcare.source.Rescue;
import com.pawcare.source.location.GPSEnableDialog;


import com.pawcare.source.location.LocationAddress;
import com.pawcare.source.util.BitmapConverter;
import com.pawcare.source.util.BitmapConverterCallback;
import com.pawcare.source.util.ConfirmRescue;
import com.pawcare.source.util.ImageCapture;
import com.pawcare.source.util.MessageOKPopUp;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
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
    EditText tvAddress, et_more_info, et_email, et_contact_number, et_isd;
    AutoCompleteTextView et_type;
   ProgressBar progressBar;
    String str_location;
    View view_res;
    protected LocationManager locationManager;
    ImageView viewImage;            // For capturing image
    ImageButton captureImage;       // Button for updating image
    byte[] imageBytes;
    ArrayAdapter<String> dropDownAdapter;
    Bitmap imageBitmap;
    int request;
    private Location location = null;
    SharedPreferences sharedPreferences;
    Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
    Pattern phoneNumberPattern = Patterns.PHONE;

    Rescue rescue = new Rescue();
    @Override
    /**
     * creates and returns the view hierarchy associated with the fragment.
     * comment
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view_res = inflater.inflate(R.layout.rescue_layout, container, false);
        initializeUIElements();
        btnRescue.setEnabled(false);
        btnRescue.setBackgroundColor(Color.parseColor("#d3d3d3"));
        sharedPreferences = ((Context) getActivity()).getSharedPreferences("MY_PREFS", (Context.MODE_PRIVATE));
        et_type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                String selection = (String)parent.getItemAtPosition(position);
                et_type.setText(selection);
                //TODO Do something with the selected text
            }
        });
        progressBar.setVisibility(View.INVISIBLE);
        if(et_email.getText() != null && et_contact_number.getText() != null) {
            et_email.setVisibility(View.GONE);
            et_contact_number.setVisibility(View.GONE);
            et_isd.setVisibility(View.GONE);
        }
        Account[] accounts = AccountManager.get(getActivity().getApplicationContext()).getAccounts();
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                String possibleEmail = account.name;
                et_email.setText(possibleEmail);
            }
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
                RescueFragment.this.btnRescue.setBackgroundColor(Color.parseColor("#D3D3D3"));
                RescueFragment.this.btnRescue.setTextColor(Color.parseColor("#FFFFFF"));
                progressBar.setVisibility(View.VISIBLE);
               // RescueFragment.this.btnRescue.setEnabled(false);
                locationManager = (LocationManager) ((Context) getActivity()).getSystemService(Context.LOCATION_SERVICE);
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null && ((System.currentTimeMillis() - location.getTime()) < 30 * 60 * 1000) ) {
                    final LocationAddress locationAddress = new LocationAddress();
                    locationAddress.context = getActivity().getApplicationContext();
                    locationAddress.execute(location.getLatitude(),location.getLongitude());
                    locationAddress.callback = new GeoCodeCallback() {
                        @Override
                        public void convertedAddress(final String address) {
                            try {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        RescueFragment.this.btnRescue.setBackgroundColor(Color.parseColor("#00CCFF"));
                                        RescueFragment.this.btnRescue.setTextColor(Color.parseColor("#FFFFFF"));
                                        if(MainActivity.cityNameList.contains(locationAddress.getCity(locationAddress.getAddress())))
                                        {   RescueFragment.this.btnRescue.setEnabled(true);
                                            RescueFragment.this.tvAddress.setText(address);
                                            RescueFragment.this.progressBar.setVisibility(View.INVISIBLE);

                                        }
                                        else
                                        {
                                            RescueFragment.this.btnRescue.setEnabled(false);
                                            RescueFragment.this.tvAddress.setText(address);
                                            RescueFragment.this.progressBar.setVisibility(View.INVISIBLE);
                                            RescueFragment.this.btnRescue.setBackgroundColor(Color.parseColor("#D3D3D3"));
                                            Toast toast = null;
                                            toast = Toast.makeText(getActivity().getApplicationContext(), "We're not there in your city yet! Be good to animals, ok?", Toast.LENGTH_LONG);
                                            toast.show();

                                        }

                                    }
                                });
                            }catch (Exception e){
                                Log.d("PAWED",e.toString());
                            }

                        }
                    };
                } else {
                    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        GPSEnableDialog gpsEnableDialog = new GPSEnableDialog();
                        gpsEnableDialog.show(getActivity().getSupportFragmentManager(), "gps_tag");

                    }
                    progressBar.setVisibility(View.VISIBLE);
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * 10, 20, RescueFragment.this);
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000 * 10, 20, RescueFragment.this);

                }

            }

        });
        btnRescue.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Boolean validation = fieldValidation();
                ConfirmRescue confirmRescue = new ConfirmRescue();

                if (validation == true) {
                    List<String> lst_animals = MainActivity.rescueAnimalList;

                    if(et_type.getText()==null || et_more_info == null || et_email == null || et_contact_number == null || tvAddress == null
                            || trimmedString(et_type.getText().toString()) ==0 ||
                            trimmedString(et_more_info.getText().toString())==0 ||
                            trimmedString(et_email.getText().toString())==0 ||
                            trimmedString(et_contact_number.getText().toString())==0 ||
                            trimmedString(tvAddress.getText().toString())==0 )
                    {
                        MessageOKPopUp msgpop = new MessageOKPopUp();
                        msgpop.setMessage("Please fill all the details for the rescue!");
                        msgpop.show(getActivity().getSupportFragmentManager(), "alert");

                    }
                    else {
                        if (!lst_animals.contains(et_type.getText().toString().toLowerCase())) {
                            MessageOKPopUp msgpop = new MessageOKPopUp();
                            msgpop.setMessage("We currently don't support rescue of " + et_type.getText() + ".");
                            msgpop.show(getActivity().getSupportFragmentManager(), "alert");

                        } else {

                            confirmRescue.setMessage("Are you sure about rescuing this animal?");
                            confirmRescue.show(getActivity().getSupportFragmentManager(), "alert");

                            rescue.setType(et_type.getText().toString());
                            rescue.setMoreInfo(et_more_info.getText().toString());
                            rescue.setAddress(tvAddress.getText().toString());
                            rescue.setMail(et_email.getText().toString());
                            rescue.setLocation(RescueFragment.this.location);
                            rescue.setPhone(et_contact_number.getText().toString());
                            rescue.callback = new PersistCallback() {
                                @Override
                                public void persisted(Exception e) {
                                    if (e != null) {
                                        Toast toast = null;
                                        toast = Toast.makeText(getActivity().getApplicationContext(), "Error in sending rescue request. Try again.", Toast.LENGTH_LONG);
                                        toast.show();
                                    } else {
                                        Toast toast = null;
                                        toast = Toast.makeText(getActivity().getApplicationContext(), "Rescue request sent successfully. Thank you!!!", Toast.LENGTH_LONG);
                                        toast.show();
                                        reset();
                                    }
                                }
                            };
                            confirmRescue.rescue = rescue;
                            //getActivity().setContentView(R.layout.rescue_layout);
                        }
                    }
                } else {
                    // do nothing right now, validation error message is shown bu each textfield
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


        if (sharedPreferences.contains("email") && sharedPreferences.contains("contact")) {
            String savedEmail = sharedPreferences.getString("email", "");
            String savedNo = sharedPreferences.getString("contact", "");
            if (savedEmail != null && savedNo != null && !savedEmail.equals("") && !savedNo.equals("")) {
                et_email.setText(savedEmail);
                et_contact_number.setText(savedNo);
            } else {
                et_email.setVisibility(View.VISIBLE);
                et_contact_number.setVisibility(View.VISIBLE);
                et_isd.setVisibility(View.VISIBLE);
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
            final LocationAddress locationAddress = new LocationAddress();
            locationAddress.context = getActivity().getApplicationContext();
            locationAddress.execute(location.getLatitude(),location.getLongitude());
            locationAddress.callback = new GeoCodeCallback() {
                @Override
                public void convertedAddress(final String address) {
                    Log.d("PAWED", "this is a key" + address);
                    try {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                RescueFragment.this.btnRescue.setBackgroundColor(Color.parseColor("#00CCFF"));
                                RescueFragment.this.btnRescue.setTextColor(Color.parseColor("#FFFFFF"));
                                if(MainActivity.cityNameList.contains(locationAddress.getCity(locationAddress.getAddress())))
                                {   RescueFragment.this.btnRescue.setEnabled(true);
                                    RescueFragment.this.tvAddress.setText(address);
                                    RescueFragment.this.progressBar.setVisibility(View.INVISIBLE);

                                }
                                else
                                {
                                    RescueFragment.this.btnRescue.setEnabled(false);
                                    RescueFragment.this.tvAddress.setText(address);
                                    RescueFragment.this.btnRescue.setBackgroundColor(Color.parseColor("#D3D3D3"));
                                    RescueFragment.this.progressBar.setVisibility(View.INVISIBLE);
                                    Toast toast = null;
                                    toast = Toast.makeText(getActivity().getApplicationContext(), "We're not there in your city yet! Be good to animals, ok?", Toast.LENGTH_LONG);
                                    toast.show();

                                }

                            }
                        });
                    }catch (Exception e){
                        Log.d("PAWED",e.toString());
                    }
                }
            };
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
    public void onActivityCreated(Bundle savedInstance)
    {
        super.onActivityCreated(savedInstance);
        dropDownAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),R.layout.simple_dropdown_item_1line , MainActivity.rescueAnimalList);
        et_type.setAdapter(dropDownAdapter);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            imageBitmap = ImageCapture.displayImage(viewImage, data, (Context) getActivity(), requestCode);
            BitmapConverter converter = new BitmapConverter();
            converter.bitmap = imageBitmap;
            converter.request = requestCode;
            request = requestCode;
            converter.callback = new BitmapConverterCallback() {
                @Override
                public void converted(byte[] imageBytes) {
                    rescue.setImageFile(imageBytes);
                }
            };
            converter.execute();
        }
    }

    public void initializeUIElements() {
        btnGPSShowLocation = (Button) view_res.findViewById(R.id.btn_location);
        btnRescue = (Button) view_res.findViewById(R.id.btn_rescue);
        btnRescue.setAllCaps(false);
        btnRescue.setTextColor(Color.parseColor("#FFFFFF"));

        tvAddress = (EditText) view_res.findViewById(R.id.txt_location);
        tvAddress.setTextColor(Color.parseColor("#909090"));
        tvAddress.setHint(Html.fromHtml("<small>" + "Tap to enter location" + "</small>"));

        et_type = (AutoCompleteTextView) view_res.findViewById(R.id.et_type);
        et_type.setTextColor(Color.parseColor("#909090"));
        et_type.setHint(Html.fromHtml("<small>" + "Enter Type e.g. Owl etc" + "</small>"));

        et_email = (EditText) view_res.findViewById(R.id.et_email);
        et_email.setTextColor(Color.parseColor("#909090"));
        et_email.setHint(Html.fromHtml("<small>" + "Enter Email Address" + "</small>"));

        et_contact_number = (EditText) view_res.findViewById(R.id.et_contact_number);
        et_contact_number.setTextColor(Color.parseColor("#909090"));
        et_contact_number.setHint(Html.fromHtml("<small>" + "Enter Contact Number" + "</small>"));

        et_more_info = (EditText) view_res.findViewById(R.id.et_more_info);
        et_more_info.setTextColor(Color.parseColor("#909090"));
        et_more_info.setHint(Html.fromHtml("<small>" + "Description" + "</small>"));

        captureImage = (ImageButton) view_res.findViewById(R.id.btn_image_capture);
        viewImage = (ImageView) view_res.findViewById(R.id.img_capture);
        progressBar = (ProgressBar) view_res.findViewById(R.id.progressBar1);

        et_isd = (EditText) view_res.findViewById(R.id.isdCode);
    }

    public Boolean fieldValidation() {
        Boolean validated = true;
        if (et_type.getText().toString().trim().length() == 0 || et_type == null) {
            validated = false;
            et_type.setError("Animal Type is required!");
        } else if (tvAddress.getText().toString().trim().length() == 0 || tvAddress == null) {
            validated = false;
            tvAddress.setError("Location is required!");
        } else if (!emailPattern.matcher(et_email.getText().toString()).matches()) {
            validated = false;
            et_email.setError("Email id is invalid!");
        } else if (et_contact_number.getText().toString().trim().length() != 10) {
            validated = false;
            et_contact_number.setError("Contact Number is invalid!");
        } else if (et_more_info.getText().toString().length() == 0 || et_more_info == null) {
            validated = false;
            et_more_info.setError("Description is empty!");
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

    public void reset()
    {
        btnRescue.setBackgroundColor(Color.parseColor("#d3d3d3"));
        et_type.setText("");
        et_more_info.setText("");
        tvAddress.setText("");

    }

    public int trimmedString(String str)
    {
        return str.trim().length();
    }
}