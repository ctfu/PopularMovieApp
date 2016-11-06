package com.example.ctfu.popularmovie.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.ctfu.popularmovie.R;
import com.squareup.picasso.Picasso;
import com.example.ctfu.popularmovie.model.Movie;

import java.util.List;

/**
 * Created by CFU on 10/22/16.
 */

public class MovieAdapter extends BaseAdapter {
    private List<Movie> mMovies;
    private Context context;


    public MovieAdapter(Context context, List<Movie> movies){
        this.context = context;
        mMovies = movies;
    }

    public void setData(List<Movie> movies) {
        mMovies = movies;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mMovies.size();
    }

    @Override
    public Movie getItem(int position) {
        return mMovies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder;
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.grid_movie_item, parent, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder); //store the viewHolder into the tag
        }

        viewHolder = (ViewHolder) view.getTag();
        Movie movie = getItem(position);
        ImageView imageView = viewHolder.imageView;
        String posterPath = "http://image.tmdb.org/t/p/w185/" + movie.getPosterPath();


        Picasso.with(context).load(posterPath).into(imageView);

        Log.v("Movie Adapter", posterPath);

        return view;

    }

    private  class ViewHolder{
        public  ImageView imageView;

        public ViewHolder(View view){
            imageView = (ImageView) view.findViewById(R.id.grid_movie_image);
        }
    }
}
