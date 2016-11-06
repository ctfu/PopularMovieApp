package com.example.ctfu.popularmovie;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.ctfu.popularmovie.adapters.MovieAdapter;
import com.example.ctfu.popularmovie.model.Movie;

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
import java.util.List;

/**
 * Created by CFU on 10/21/16.
 */

public class MovieFragment extends Fragment {
    private MovieAdapter mMovieAdapter = null;
    private GridView gridView;
    private List<Movie> mMovies;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onStart() {
        super.onStart();
        updateMovies();
    }

    public void updateMovies(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String movieSorting = sharedPreferences.getString(getString(R.string.pref_movie_sorting_key),
                getString(R.string.pref_movie_sorting_popularity));
        FetchMovieTask fetchMovieTask = new FetchMovieTask();
        fetchMovieTask.execute(movieSorting);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        gridView = (GridView) rootView.findViewById(R.id.gridView_movie);

        //set number of columns based on the screen orientation
        int ot = getResources().getConfiguration().orientation;
        gridView.setNumColumns(ot == Configuration.ORIENTATION_LANDSCAPE ? 3 : 2);

        mMovieAdapter = new MovieAdapter(getActivity(), new ArrayList<Movie>());
        gridView.setAdapter(mMovieAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = mMovieAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), MovieDetail.class);
                intent.putExtra("ctfu", movie);
                startActivity(intent);
            }
        });

        return rootView;
    }

    public class FetchMovieTask extends AsyncTask<String, Void, List<Movie>>{
        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();

        private List<Movie> getMovieDataFromJSON(String movieJsonStr) throws JSONException{
            List<Movie> movies = new ArrayList<>();

            final String MOVIE_REUSLTS = "results";
            final String MOVIE__ID = "id";
            final String MOVIE_VOTE_AVERAGE = "vote_average";
            final String MOVIE_TITLE = "title";
            final String MOVIE_POSTER_PATH = "poster_path";
            final String MOVIE_OVERVIEW = "overview";
            final String MOVIE_RELEASED_DATE = "release_date";

            JSONObject jsonObject = new JSONObject(movieJsonStr);
            JSONArray results = jsonObject.getJSONArray(MOVIE_REUSLTS);
            for(int i = 0; i < results.length(); i++){
                Movie movie = new Movie();
                JSONObject movieObject = results.getJSONObject(i);
                movie.setId(movieObject.getInt(MOVIE__ID));
                movie.setRating(movieObject.getInt(MOVIE_VOTE_AVERAGE));
                movie.setTitle(movieObject.getString(MOVIE_TITLE));
                movie.setPosterPath(movieObject.getString(MOVIE_POSTER_PATH));
                movie.setOverview(movieObject.getString(MOVIE_OVERVIEW));
                movie.setReleasedDate(movieObject.getString(MOVIE_RELEASED_DATE));
                movies.add(movie);
            }

            return movies;

        }

        @Override
        protected List<Movie> doInBackground(String... params) {
            //if there is no specify sort_order
            if(params.length == 0){
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String movieJsonStr;

            try{
                final String MOVIE_BASE_RUL = "http://api.themoviedb.org/3/discover/movie?";
                final String SORT_ORDER_PARAM = "sort_by";
                final String API_KEY_PARAM = "api_key";
                Uri builtUri = Uri.parse(MOVIE_BASE_RUL).buildUpon()
                        .appendQueryParameter(SORT_ORDER_PARAM, params[0])
                        .appendQueryParameter(API_KEY_PARAM, "0befc99734129b5dd780c7c72d620db4")
                        .build();

                URL url = new URL(builtUri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();

                if(inputStream == null){
                    //if nothing has return
                    return null;
                }

                StringBuffer buffer = new StringBuffer();
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while((line = reader.readLine()) != null){
                    //added a new line for debug purpose
                    buffer.append(line + "\n");
                }

                if(buffer.length() == 0){
                    return null;
                }
                movieJsonStr = buffer.toString();
                Log.v(LOG_TAG, movieJsonStr);

            }catch(IOException e){
                Log.v(LOG_TAG, "error getting JSON string", e);
                return null; //if can't get JSON string, there is no need to perform further action
            }finally{
                if(urlConnection != null){
                    urlConnection.disconnect();
                }
                if(reader != null){
                    try{
                        reader.close();
                    }catch(final IOException e){
                        Log.v(LOG_TAG, "Error closing the input stream", e);
                    }
                }
            }

            //if everything above goes well, we should have a proper JSON string
            try{
                return getMovieDataFromJSON(movieJsonStr);
            }catch(JSONException e){
                Log.v(LOG_TAG, "Error parsing Json string", e);
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            if(movies != null){
                Log.v(LOG_TAG, "Hello form postExecute");
                mMovieAdapter.setData(movies);
                mMovies = new ArrayList<>();
                mMovies.addAll(movies);
            }
        }
    }
}
