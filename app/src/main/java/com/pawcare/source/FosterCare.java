package com.pawcare.source;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.anm.uitest1.R;


/**
 * Created by mur on 5/31/2015.
 */
public class FosterCare extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.foster_care_layout, container, false);
    }
}
