package com.l2l.androided.mh122354.popularmovies.fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.l2l.androided.mh122354.popularmovies.BuildConfig;
import com.l2l.androided.mh122354.popularmovies.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieGridFragment extends Fragment {


    public MovieGridFragment() {
        // Required empty public constructor
    }

ls
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie_grid, container, false);
    }

    public class FetchMovieTask extends AsyncTask<Void , Void , Void>{
        @Override
        protected Void doInBackground(Void... voids) {

            String apiKey = BuildConfig.MOVIE_DB_API_KEY;




            return null;
        }
    }

}
