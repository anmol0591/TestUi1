package com.pawcare.source;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.anm.uitest1.R;
import com.pawcare.source.util.Persistence;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;



public class MainActivity extends ActionBarActivity implements ActionBar.TabListener {
    ActionBar actionBar;
    ViewPager viewPager;
    FragmentPageAdapter fragmentPageAdapter;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Persistence persistence = new Persistence(getApplicationContext());
        if (!persistence.isSeeded()){
            persistence.seedAnimals();
            Log.d("PAWEDX","SEEDED ANIMALS");
            persistence.seedCity();
        }

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
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpTo(this, getIntent());
                return true;
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
    }


}