package com.example.ctfu.popularmovie.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by CFU on 10/22/16.
 */

public class Movie implements Parcelable{
    private int id;
    private int rating;
    private String title;
    private String posterPath;
    private String overview;
    private String releasedDate;

    public Movie(){

    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getRating(){
        return rating;
    }

    public void setRating(int rating){
        this.rating = rating;
    }

    public String getPosterPath(){
        return posterPath;
    }

    public void setPosterPath(String posterPath){
        this.posterPath = posterPath;
    }

    public String getOverview(){
        return overview;
    }

    public void setOverview(String overview){
        this.overview = overview;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getReleasedDate(){
        return releasedDate;
    }

    public void setReleasedDate(String releasedDate){
        this.releasedDate = releasedDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(rating);
        dest.writeString(title);
        dest.writeString(posterPath);
        dest.writeString(overview);
        dest.writeString(releasedDate);
    }

    private Movie(Parcel in){
        id = in.readInt();
        rating = in.readInt();
        title = in.readString();
        posterPath = in.readString();
        overview = in.readString();
        releasedDate = in.readString();
    }

    public final static Parcelable.Creator<Movie> CREATOR =
            new Parcelable.Creator<Movie>(){
                @Override
                public Movie createFromParcel(Parcel source) {
                    return new Movie(source);
                }

                @Override
                public Movie[] newArray(int size) {
                    return new Movie[size];
                }
            };
}
