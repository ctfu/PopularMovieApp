package com.example.ctfu.popularmovie;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ctfu.popularmovie.model.Movie;
import android.support.v7.widget.ShareActionProvider;
import com.squareup.picasso.Picasso;


/**
 * Created by CFU on 10/22/16.
 */

public class DetailFragment extends Fragment {
    final String MOVIE_SHARE_HASHTAG = "#Popular Movie";


    private Movie mMovie;
    private TextView title;
    private ImageView poster;
    private TextView releaseDate;
    private TextView rating;
    private TextView overview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.movie_fragment, menu);

        MenuItem menuItem = menu.findItem(R.id.action_share);

        ShareActionProvider mShareActionProvider;
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        if(mShareActionProvider != null){
            mShareActionProvider.setShareIntent(createShareMovieIntent());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        Intent intent = getActivity().getIntent();
        if(intent != null) {
            mMovie = intent.getExtras().getParcelable("ctfu");
            title = (TextView) rootView.findViewById(R.id.detail_movie_title);
            poster = (ImageView) rootView.findViewById(R.id.detail_movie_poster);
            releaseDate = (TextView) rootView.findViewById(R.id.detail_movie_release_date);
            rating = (TextView) rootView.findViewById(R.id.detail_movie_rating);
            overview = (TextView) rootView.findViewById(R.id.detail_movie_overview);

            title.setText(mMovie.getTitle());
            String posterPath = "http://image.tmdb.org/t/p/w185/" + mMovie.getPosterPath();
            Picasso.with(getActivity()).load(posterPath).into(poster);
            releaseDate.setText(mMovie.getReleasedDate());
            rating.setText(mMovie.getRating() + " / 10");
            overview.setText(mMovie.getOverview());

        }

        return rootView;
    }

    private Intent createShareMovieIntent() {
        Intent forecastSharedIntent = new Intent(Intent.ACTION_SEND);
        forecastSharedIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        forecastSharedIntent.setType("text/plain");
        forecastSharedIntent.putExtra(Intent.EXTRA_TEXT, MOVIE_SHARE_HASHTAG + " " + title.getText());

        return forecastSharedIntent;
    }
}
