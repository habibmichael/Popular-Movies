package com.l2l.androided.mh122354.popularmovies.fragments;


import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;

import com.l2l.androided.mh122354.popularmovies.BuildConfig;
import com.l2l.androided.mh122354.popularmovies.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieGridFragment extends Fragment {

    GridLayout layout;

    public static final String MOVIE_FRAG_NAME =  MovieGridFragment.class.getSimpleName();


    public MovieGridFragment() {
        setHasOptionsMenu(true);
        // Required empty public constructor
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id= item.getItemId();

        if(id==R.id.action_refresh){
            new FetchMovieTask().execute();
            return  true;

        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.movie_view_fragment,menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_movie_grid, container, false);

        layout= (GridLayout)rootView.findViewById(R.id.gird_layout);
        System.out.println(layout.getChildCount());


        return rootView;
    }

    public class FetchMovieTask extends AsyncTask<Void , Void , String[]> {


        @Override
        protected void onPostExecute(String[] imagePaths) {

            ImageView child;


            for(int i=0;i<layout.getChildCount();i++){

                //insert images into each image view in grid layout
                child= (ImageView)layout.getChildAt(i);
                Picasso.with(getActivity()).load(imagePaths[i]).into(child);
                Log.d(MOVIE_FRAG_NAME,imagePaths[i]);

            }






        }


        @Override
        protected String[] doInBackground(Void... voids) {

            //api key for authentication
            String apiKey = BuildConfig.MOVIE_DB_API_KEY;

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String movieDBJsonStr = null;

            //Temporary values for query params
            String sortFilter = "popularity.dsc";
            int page = 1;
            String language = "en-US";

            //Parameters & base url for retreiving data form movie db api
            final String BASE_URL = "https://api.themoviedb.org/3/discover/movie?";
            final String SORT_PARAM = "sort_by";
            final String API_KEY_PARAM = "api_key";
            final String LANGAUGE_PARAM = "language";
            final String PAGE_PARAM = "page";

            try {
                //build uri based on needed params
                Uri builtUrl = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(API_KEY_PARAM, apiKey)
                        .appendQueryParameter(LANGAUGE_PARAM, language)
                        .appendQueryParameter(SORT_PARAM, sortFilter)
                        .appendQueryParameter(PAGE_PARAM, Integer.toString(page))
                        .build();

                //create url based on built uri
                URL url = new URL(builtUrl.toString());


                //create request & open connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream is = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (is == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(is));

                //read data into line and put into buffer
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                //store data into movie json str
                movieDBJsonStr = buffer.toString();
                getMovieDataFromJson(movieDBJsonStr);

            } catch (IOException e) {
                return null;
            } finally {
                if (urlConnection == null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        return null;
                    }
                }
            }
            Log.d(MOVIE_FRAG_NAME, "Fetching Data");
            return getMovieDataFromJson(movieDBJsonStr);
        }
    }


    private String[] getMovieDataFromJson(String movieForecastJson){

        //Data needed from json
        final String MDB_RESULTS="results";
        final String MDB_POSTER_PATH ="poster_path";
        final String MDB_RELEASE_DATE="release_date";
        final String MDB_TITLE="original_title";
        final String MDB_RATING="vote_average";
        final String MDB_DESC="overview";

        ArrayList<String> paths = new ArrayList<>();



        try {


            JSONObject movieObjJson = new JSONObject(movieForecastJson);
            JSONArray movieArray = movieObjJson.getJSONArray(MDB_RESULTS);
            final String IMAGE_BASE_URL ="https://image.tmdb.org/t/p/";
            String IMAGE_SIZE ="/w342";
            boolean arrayFull=false;
            int i=0;
            int addedPathCount=0;


            //Add image paths into list until all image views are accounted for
            while(addedPathCount!=layout.getChildCount()) {
                if (movieArray.getJSONObject(i).getString(MDB_POSTER_PATH).equals("null")) {

                    i++;


                } else {
                    paths.add(IMAGE_BASE_URL+IMAGE_SIZE+movieArray.getJSONObject(i).getString(MDB_POSTER_PATH));
                    addedPathCount++;
                    i++;


                }
            }









        } catch(JSONException e){
            Log.d(MOVIE_FRAG_NAME,"Json Error");
        }

        Log.d(MOVIE_FRAG_NAME,"Return to Post Execute");

        return paths.toArray(new String[paths.size()]);

    }

}
