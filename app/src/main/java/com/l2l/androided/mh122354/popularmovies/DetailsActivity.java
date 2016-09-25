package com.l2l.androided.mh122354.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.l2l.androided.mh122354.popularmovies.fragments.DetailsFragment;
import com.l2l.androided.mh122354.popularmovies.fragments.MovieGridFragment;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        if(savedInstanceState==null){

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.details_container,new DetailsFragment())
                    .commit();

        }
    }
}
