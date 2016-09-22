package com.l2l.androided.mh122354.popularmovies.fragments;


import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.l2l.androided.mh122354.popularmovies.BuildConfig;
import com.l2l.androided.mh122354.popularmovies.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieGridFragment extends Fragment {



    public static final String MOVIE_FRAG_NAME =  MovieGridFragment.class.getSimpleName();


    public MovieGridFragment() {
        setHasOptionsMenu(true);
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie_grid, container, false);
    }

    public class FetchMovieTask extends AsyncTask<Void , Void , Void>{
        @Override
        protected Void doInBackground(Void... voids) {

            //api key for authentication
            String apiKey = BuildConfig.MOVIE_DB_API_KEY;

            HttpURLConnection urlConnection = null;
            BufferedReader reader =null;
            String movieDBJsonStr=null;

            //Temporary values for query params
            String sortFilter = "popularity.dsc";
            int page=1;
            String language="en-US";

            //Parameters & base url for retreiving data form movie db api
            final String  BASE_URL ="https://api.themoviedb.org/3/discover/movie?";
            final String SORT_PARAM ="sort_by";
            final String API_KEY_PARAM="api_key";
            final String LANGAUGE_PARAM="language";
            final String PAGE_PARAM="page";

            try{
                //build uri based on needed params
                Uri builtUrl = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(SORT_PARAM,sortFilter)
                        .appendQueryParameter(LANGAUGE_PARAM,language)
                        .appendQueryParameter(PAGE_PARAM,Integer.toString(page))
                        .appendQueryParameter(API_KEY_PARAM,apiKey).build();

                //create url based on built uri
                URL url = new URL(builtUrl.toString());
                Log.d(MOVIE_FRAG_NAME,url.toString());

                //create request & open connection
                urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream is = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if(is==null){
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(is));

                //read data into line and put into buffer
                String line;
                while((line=reader.readLine())!=null){
                    buffer.append(line+"\n");
                }

                if(buffer.length()==0){
                    return null;
                }
                //store data into movie json str
                movieDBJsonStr = buffer.toString();
                Log.d(MOVIE_FRAG_NAME,movieDBJsonStr);

            }catch(IOException e){
                return null;
            }
            finally {
                if(urlConnection==null){
                    urlConnection.disconnect();
                }
                if(reader!=null){
                    try{
                        reader.close();
                    }catch(IOException e){
                        return  null;
                    }
                }
            }

            return null;
        }
    }

}
