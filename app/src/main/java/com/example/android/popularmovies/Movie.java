package com.example.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseBooleanArray;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by John on 23. 10. 2015.
 */
public class Movie implements Serializable {
    private boolean isAdult;
    private String backdropPath;
    private ArrayList<Integer> genres;
    private int id;
    private String language;
    private String title;
    private String overview;
    private Calendar releaseDate;
    private String posterPath;
    private double popularity;
    private boolean video;
    private double voteAverage;
    private int voteCount;

    public Movie() {
        posterPath = "";
    }

    public Movie(boolean isAdult, String backdropPath, ArrayList<Integer> genres, int id,
                 String language, String title, String overview, Calendar releaseDate,
                 String posterPath, double popularity, boolean video, double voteAverage,
                 int voteCount) {
        this.isAdult = isAdult;
        this.backdropPath = backdropPath;
        this.genres = genres;
        this.id = id;
        this.language = language;
        this.title = title;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.popularity = popularity;
        this.video = video;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
    }

    public boolean isAdult() {
        return isAdult;
    }

    public void setIsAdult(boolean isAdult) {
        this.isAdult = isAdult;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public ArrayList<Integer> getGenres() {
        return genres;
    }

    public void setGenres(ArrayList<Integer> genres) {
        this.genres = genres;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Calendar getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Calendar releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }
}
