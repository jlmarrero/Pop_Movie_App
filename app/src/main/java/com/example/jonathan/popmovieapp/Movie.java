package com.example.jonathan.popmovieapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Jonathan on 7/27/2015.
 */
public class Movie implements Parcelable {
    private String id;
    private String title;
    private String popularity;
    private String description;
    private String poster_path;
    private String release_date;
    private String vote_average;

    public Movie(String ref, String tit, String pop, String desc, String image, String vote, String release){
        this.id = ref;
        this.title = tit;
        this.popularity = pop;
        this.description = desc;
        this.poster_path = image;
        this.release_date = release;
        this.vote_average = vote;
    }

    private Movie(Parcel in){
        id = in.readString();
        title = in.readString();
        popularity = in.readString();
        description = in.readString();
        poster_path = in.readString();
        release_date = in.readString();
        vote_average = in.readString();
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getVote_average() {
        return vote_average;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeString(id);
        out.writeString(title);
        out.writeString(popularity);
        out.writeString(description);
        out.writeString(poster_path);
        out.writeString(release_date);
        out.writeString(vote_average);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>(){
        @Override
        public Movie createFromParcel(Parcel parcel){
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int i){
            return new Movie[i];
        }
    };
}
