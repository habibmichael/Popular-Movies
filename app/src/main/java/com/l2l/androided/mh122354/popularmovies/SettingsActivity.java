package com.l2l.androided.mh122354.popularmovies;

import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.l2l.androided.mh122354.popularmovies.fragments.SettingsFragment;

public class SettingsActivity extends PreferenceActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //display fragment
        if(savedInstanceState==null){
            getFragmentManager().beginTransaction().
                  replace(android.R.id.content,
                          new SettingsFragment()).commit();
        }
    }
}
