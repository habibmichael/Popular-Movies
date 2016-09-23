package com.l2l.androided.mh122354.popularmovies;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.l2l.androided.mh122354.popularmovies.fragments.MovieGridFragment;

public class MovieGridActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_grid);

        if(savedInstanceState==null){

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container,new MovieGridFragment())
                    .commit();

        }

    }
}
