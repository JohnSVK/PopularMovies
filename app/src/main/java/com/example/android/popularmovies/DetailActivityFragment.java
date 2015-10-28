package com.example.android.popularmovies;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Calendar;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    private Movie mMovie;

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        Intent intent = getActivity().getIntent();

        if(intent != null && intent.hasExtra("movie")) {
            mMovie = (Movie) intent.getSerializableExtra("movie");
            //Log.v("MOVIE TITLE: ", "title: "+mMovie.getTitle());
            Log.v("MOVIE YEAR:", " " + mMovie.getReleaseDate().get(Calendar.YEAR));

            TextView title = (TextView) rootView.findViewById(R.id.movie_title);
            title.setText(mMovie.getTitle());

            ImageView thumbnail = (ImageView) rootView.findViewById(R.id.movie_thumbnail);
            Picasso.with(getActivity()).load(mMovie.getPosterPath()).into(thumbnail);

            TextView year = (TextView) rootView.findViewById(R.id.movie_year);
            String yearStr = String.valueOf(mMovie.getReleaseDate().get(Calendar.YEAR));
            year.setText(yearStr);

            TextView rating = (TextView) rootView.findViewById(R.id.movie_rating);
            rating.setText(String.valueOf(mMovie.getVoteAverage())+"/10");

            TextView overview = (TextView) rootView.findViewById(R.id.movie_overview);
            overview.setText(mMovie.getOverview());
        }

        return rootView;
    }
}
