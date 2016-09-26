package com.l2l.androided.mh122354.popularmovies.fragments;


import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.l2l.androided.mh122354.popularmovies.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener{

    public static final String SETTINGS_FRAG = SettingsFragment.class.getSimpleName();


    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.pref_general);
       bindPreferenceSummaryToValue(findPreference(getString(R.string.pref_sort_key)));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }


    private void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(this);

        // Trigger the listener immediately with the preference's
        // current value.
        onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {



        String stringVal = newValue.toString();
        ListPreference listPreference =(ListPreference)preference;

        //find index of set value
        int prefIndex = listPreference.findIndexOfValue(stringVal);
        //set summary to corresponding value
        if(prefIndex>=0){
            preference.setSummary(listPreference.getEntries()[prefIndex]);
            Log.d(SETTINGS_FRAG,"Preferenced Changed to : "+stringVal);
        }


        return true;
    }
}
