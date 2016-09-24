package com.l2l.androided.mh122354.popularmovies.fragments;


import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;

import com.l2l.androided.mh122354.popularmovies.BuildConfig;
import com.l2l.androided.mh122354.popularmovies.Movie;
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
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieGridFragment extends Fragment {

    ImageAdapter mMovieAdapter;
    GridView movieGridView;
    ImageView movieImageView;
    Movie movie;


    private static String[] images;

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

        //create custom adapter
        mMovieAdapter = new ImageAdapter(
                getActivity(),
                R.layout.grid_item_movie,
                R.id.grid_item_movie_imageview,
                new ArrayList<String>()
        );


        movieImageView=(ImageView)rootView.findViewById(R.id.grid_item_movie_imageview);
        movieGridView = (GridView)rootView.findViewById(R.id.gridview_movies);
        movieGridView.setAdapter(mMovieAdapter);

        //TODO add movie grid view click listener

        return rootView;
    }

    public class FetchMovieTask extends AsyncTask<Void , Void , Movie[]> {


        @Override
        protected void onPostExecute(Movie[] movies) {
            if(movies!=null){

                mMovieAdapter.clear();

                for(int i=0;i<movies.length;i++){

                    Log.d(MOVIE_FRAG_NAME,movies[i].getImagePath());

                    mMovieAdapter.add(movies[i].getImagePath());
                }


            }

        }


        @Override
        protected Movie[] doInBackground(Void... voids) {

            //api key for authentication
            String apiKey = BuildConfig.MOVIE_DB_API_KEY;

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String movieDBJsonStr = null;

            //Temporary values for query params
          //  String sortFilter = "popularity.dsc";
            int page = 1;
            String language = "en-US";

            //Parameters & base url for retreiving data form movie db api
            final String BASE_URL = "https://api.themoviedb.org/3/movie/popular?";
            //TODO use movie/popular and movie/top_rated

           // final String SORT_PARAM = "sort_by";
            final String API_KEY_PARAM = "api_key";
            final String LANGAUGE_PARAM = "language";
            final String PAGE_PARAM = "page";

            try {
                //build uri based on needed params
                Uri builtUrl = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(API_KEY_PARAM, apiKey)
                        .appendQueryParameter(LANGAUGE_PARAM, language)
                  //      .appendQueryParameter(SORT_PARAM, sortFilter)
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

            } catch (IOException e) {
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        //
                    }
                }
            }
            Log.d(MOVIE_FRAG_NAME, "Fetching Data");
            return getMovieDataFromJson(movieDBJsonStr);
        }
    }


    private Movie[] getMovieDataFromJson(String movieForecastJson){

        //Value to retrieve page of results
        final String MDB_RESULTS="results";

        Movie[] movies= null;

        try {


            //retrieve results from query
            JSONObject movieObjJson = new JSONObject(movieForecastJson);
            JSONArray movieArray = movieObjJson.getJSONArray(MDB_RESULTS);


            movies= makeMoviesFromJson(movieArray);


        } catch(JSONException e){
            Log.d(MOVIE_FRAG_NAME,"Json Error");
        }

        return movies;

    }

    private Movie[] makeMoviesFromJson(JSONArray movieArray) {

        //Data needed from json
        final String MDB_POSTER_PATH ="poster_path";
        final String MDB_RELEASE_DATE="release_date";
        final String MDB_TITLE="original_title";
        final String MDB_RATING="vote_average";
        final String MDB_DESC="overview";

        //image uri requirements
        final String IMAGE_BASE_URL ="https://image.tmdb.org/t/p/";
        String IMAGE_SIZE ="/w342";

        Movie[] movies= new Movie[movieArray.length()];



        for(int i=0;i<movieArray.length();i++) {

            try {
                //Get required fields from json array
                String title = movieArray.getJSONObject(i).getString(MDB_TITLE);
                String overview = movieArray.getJSONObject(i).getString(MDB_DESC);
                int rating = movieArray.getJSONObject(i).getInt(MDB_RATING);
                String releaseDate = movieArray.getJSONObject(i).getString(MDB_RELEASE_DATE);
                String posterPath = IMAGE_BASE_URL+IMAGE_SIZE+ movieArray.getJSONObject(i).getString(MDB_POSTER_PATH);

                //create movie from data
                movies[i] = new Movie(overview, releaseDate, posterPath, title, rating);

            } catch (JSONException e) {
                //
            }

        }
            return movies;




    }

    public static class ImageAdapter extends ArrayAdapter{

        private Context mContext;
        private ArrayList<String> imagePathList;


        public ImageAdapter(Context context, int resourceLayout,int resourceItem, ArrayList<String> objects) {

            super(context,resourceLayout,resourceItem,objects);
            mContext=context;
            imagePathList=objects;

      }



        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ImageView imageView;

            if(convertView==null){
                imageView=new ImageView(mContext);
            }else{
                imageView=(ImageView)convertView;
            }

            //Set each image view with image from the url
            Picasso.with(mContext).load(imagePathList.get(position)).into(imageView);


            return imageView;


        }
    }



    }


