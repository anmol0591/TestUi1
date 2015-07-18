package com.pawcare.source.views;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilterInputStream;

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
    EditText tvAddress, et_more_info, et_email, et_type, et_condition, et_contact_number;
    ProgressBar progressBar;
    String str_location;
    View view_res;
    protected LocationManager locationManager;
    ImageView viewImage;            // For capturing image
    ImageButton captureImage;       // Button for updating image
    File imageFile;
    private Location location = null;

    @Override
    /**
     * creates and returns the view hierarchy associated with the fragment.
     * comment
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view_res = inflater.inflate(R.layout.rescue_layout, container, false);
        initializeUIElements();
        progressBar.setVisibility(View.INVISIBLE);


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
                    rescue.setCondition(et_condition.getText().toString());
                    rescue.setMoreInfo(et_more_info.getText().toString());
                    rescue.setAddress(tvAddress.getText().toString());
                    rescue.setMail(et_email.getText().toString());
                    rescue.setLocation(RescueFragment.this.location);
                    rescue.setPhone(et_contact_number.getText().toString());
                    if (imageFile != null){
                        try{
                            Log.d("PAWED", "image file is not null");
                            byte[] imageBytes = new byte[(int)imageFile.length()];
                            BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(imageFile));
                            inputStream.read(imageBytes,0,imageBytes.length);
                            Log.d("PAWED", "converted" + imageBytes.length);
                            Log.d("PAWED","byte array"+imageBytes.toString());
                            rescue.setImageFile(imageBytes);
                        }catch (Exception e){
                            Log.d("PAWED","This is an error"+e.toString());
                        }
                    }

                    confirmRescue.rescue = rescue;

                } else {
                    //parse rescue
                }


            }
        });

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
            imageFile = ImageCapture.displayImage(viewImage, data, (Context) getActivity(), requestCode);
        }
    }

    public void initializeUIElements() {
        btnGPSShowLocation = (Button) view_res.findViewById(R.id.btn_location);
        btnRescue = (Button) view_res.findViewById(R.id.btn_rescue);
        tvAddress = (EditText) view_res.findViewById(R.id.txt_location);
        et_type = (EditText) view_res.findViewById(R.id.et_type);
        et_email = (EditText) view_res.findViewById(R.id.et_email);
        et_contact_number = (EditText) view_res.findViewById(R.id.et_contact_number);
        et_condition = (EditText) view_res.findViewById(R.id.et_condition);
        et_more_info = (EditText) view_res.findViewById(R.id.et_more_info);
        captureImage = (ImageButton) view_res.findViewById(R.id.btn_image_capture);
        viewImage = (ImageView) view_res.findViewById(R.id.img_capture);
        progressBar = (ProgressBar) view_res.findViewById(R.id.progressBar1);
    }

    public Boolean fieldValidation() {
        Boolean validated = true;
        if (et_type.getText().toString().length() == 0 || et_type == null) {
            validated = false;
            et_type.setError("Animal Type is required!");

        } else if (et_condition.getText().toString().length() == 0 || et_type == null) {
            validated = false;
            et_condition.setError("Animal Condition is required!");
        } else if (tvAddress.getText().toString().length() == 0 || tvAddress == null) {
            validated = false;
            tvAddress.setError("Location is required!");
        } else if (et_email.getText().toString().length() == 0 || !et_email.getText().toString().contains("@")) {
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
}








