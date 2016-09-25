package com.l2l.androided.mh122354.popularmovies.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.l2l.androided.mh122354.popularmovies.R;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment {

    String[] movieDetails;
    TextView titleTextView;
    TextView overViewTextView;
    TextView ratingTextView;
    TextView releaseDateTextView;

    ImageView posterImageView;

    public static final String DETAIL_FRAGMENT = DetailsFragment.class.getSimpleName();


    public DetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
   View rootView= inflater.inflate(R.layout.fragment_details, container, false);

        titleTextView = (TextView)rootView.findViewById(R.id.titleTextView);
        ratingTextView=(TextView)rootView.findViewById(R.id.ratingTextView);
        posterImageView=(ImageView)rootView.findViewById(R.id.posterImageView);
        overViewTextView=(TextView)rootView.findViewById(R.id.overviewTextView);
        releaseDateTextView=(TextView)rootView.findViewById(R.id.releaseDateTextView);


        Intent intent = getActivity().getIntent();
        if(intent!=null && intent.hasExtra(Intent.EXTRA_TEXT)){

            //get String[] from movie view activity
            movieDetails= intent.getStringArrayExtra(Intent.EXTRA_TEXT);

        }

        populateViews();




        return rootView;
    }

    private void populateViews() {

        titleTextView.setText(movieDetails[0]);
        overViewTextView.setText(movieDetails[1]);
        releaseDateTextView.setText("Release Date: "+movieDetails[2]);
        Picasso.with(getActivity()).load(movieDetails[3]).into(posterImageView);
        ratingTextView.setText("Rating: "+movieDetails[4]);

    }

}
