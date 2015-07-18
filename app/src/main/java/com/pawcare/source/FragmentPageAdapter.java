package com.pawcare.source;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.pawcare.source.views.RescueFragment;

/**
 * Created by mur on 5/31/2015.
 */
public class FragmentPageAdapter extends FragmentPagerAdapter {

    public  FragmentPageAdapter(FragmentManager fm)
    {
        super(fm);
    }
    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0 : return new RescueFragment();
            case 1: return new Adoption();
            case 2: return new FosterCare();
            default: break;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
