package com.pawcare.source;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.anm.uitest1.R;
import com.pawcare.source.util.MessageOKPopUp;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class MainActivity extends ActionBarActivity implements ActionBar.TabListener {
    ActionBar actionBar;
    ViewPager viewPager;
    FragmentPageAdapter fragmentPageAdapter;
    SharedPreferences sharedPreferences;
    public static List<String> cityNameList = new ArrayList<String>();
    public static List<String> rescueAnimalList = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        InitializeParse initializeParse = new InitializeParse();
        initializeParse.execute(getApplicationContext());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = (ViewPager) findViewById(R.id.pager);
        fragmentPageAdapter = new FragmentPageAdapter(getSupportFragmentManager());
        actionBar = getSupportActionBar();
        viewPager.setAdapter(fragmentPageAdapter);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.addTab(actionBar.newTab().setText("Rescue").setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText("Adoption").setTabListener(this));
        actionBar.addTab(actionBar.newTab().setText("Foster Care").setTabListener(this));

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub
                getSupportActionBar().setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        viewPager.setCurrentItem(tab.getPosition());

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
       // menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_action_navigation_more_vert));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpTo(this, getIntent());
                return true;
            case R.id.action_settings:
                MessageOKPopUp msgpop = new MessageOKPopUp();
                msgpop.setMessage("PawCare is created with the hope of saving animals. We work to rescue and rehabilitate sick and needy animals in co-operation with People For Animals, NGO, Bangalore. " +
                        "\nCurrently, we are available in Bangalore only but we hope to be present in many more cities.\n" +
                                "\n" +
                                "How to Paw:\n" +
                                "\n" +
                                "1. Fill the animal type, location, image and description. \n" +
                                "2. Hit rescue.\n" +
                                "3. If we support the rescue of the animal, a sms and email goes to the NGO who will track the animal.\n" +
                                "\n" +
                                "We currently support rescue of limited number of animals. We will be supporting many more in coming days. \n" +
                                "\n" +
                                "For any query contact us at pawcarehelp@gmail.com\n" +
                                "\n Love,\n Team PawCare");
                msgpop.show(getSupportFragmentManager(), "alert");
        }
        return super.onOptionsItemSelected(item);
    }

    long lastPress;
    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if(currentTime - lastPress > 5000){
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_LONG).show();
            lastPress = currentTime;
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        sharedPreferences = getSharedPreferences("MY_PREFS", (Context.MODE_PRIVATE));
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet("rescueAnimalList", new HashSet<String>(rescueAnimalList));

    }


}