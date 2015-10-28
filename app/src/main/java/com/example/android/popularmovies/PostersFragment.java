package com.example.android.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/**
 * A placeholder fragment containing a simple view.
 */
public class PostersFragment extends Fragment {

    private ImageAdapter mPostersAdapter;
    private ArrayList<ImageView> mImageViews;
    private ArrayList<Movie> mMovies;

    public PostersFragment() {
        mImageViews = new ArrayList<>();
        mPostersAdapter = new ImageAdapter(getContext(), mImageViews);
        mMovies = new ArrayList<>();

        this.setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ImageView imageView = new ImageView(getActivity());
        Picasso.with(getActivity())
                .load("http://www.jpl.nasa.gov/spaceimages/images/mediumsize/PIA17011_ip.jpg")
                .into(imageView);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        mImageViews.add(imageView);



        GridView postersGridView = (GridView) rootView.findViewById(R.id.gridview_posters);
        postersGridView.setAdapter(mPostersAdapter);

        postersGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = mMovies.get(position);

                Intent detailIntent = new Intent(getActivity(), DetailActivity.class);
                detailIntent.putExtra("movie", movie);

                startActivity(detailIntent);
            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        updateMovies();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_postersfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            updateMovies();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateMovies() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        String sortby = preferences.getString(getString(R.string.pref_sort_key), getString(R.string.pref_sort_default));

        new FetchMoviesTask().execute(sortby);
    }

    private class FetchMoviesTask extends AsyncTask<String, Void, ArrayList<Movie>> {

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();
        private String basePosterUrl = "http://image.tmdb.org/t/p/w185/";

        @Override
        protected ArrayList<Movie> doInBackground(String... params) {
            if(params.length == 0) {
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String moviesJsonStr = null;

            try {
                /*URL url = new URL("http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc" +
                        "&api_key=2586cf23901dcebad4b7f9d3b9a9462b");*/

                final String MOVIE_BASE_URL = "http://api.themoviedb.org/3/discover/movie";
                final String SORTBY_PARAM = "sort_by";
                final String APIKEY_PARAM = "api_key";

                Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                        .appendQueryParameter(SORTBY_PARAM, params[0]+".desc")
                        .appendQueryParameter(APIKEY_PARAM, "2586cf23901dcebad4b7f9d3b9a9462b")
                        .build();

                URL url = new URL(builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                if(inputStream == null) {
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuffer buffer = new StringBuffer();

                String line;
                while((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                if(buffer.length() == 0) {
                    return null;
                }

                //LOAD data from buffer to Json string
                moviesJsonStr = buffer.toString();

                //Log.v(LOG_TAG, "Movies JSON string: "+ moviesJsonStr);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error", e);

                return null;
            } finally {
                if(urlConnection != null) {
                    urlConnection.disconnect();
                }

                if(reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            String[] resultStr = null;
            ArrayList<Movie> resultObjects = new ArrayList<>();
            try {
                //resultStr = getMoviesDataFromJson(moviesJsonStr);
                resultObjects = getMoviesDataFromJson(moviesJsonStr);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return resultObjects;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> result) {
            if(result != null) {
                mMovies = result;

                ArrayList<String> moviePosterList = new ArrayList<>();
                for(Movie movie : result) {
                    moviePosterList.add(movie.getPosterPath());
                    //Log.v(LOG_TAG, "Poster url: "+basePosterUrl+movie.getPosterPath());

                    //Log.v(LOG_TAG,"DATE STRING: "+ movie.getReleaseDate().get(Calendar.YEAR));
                }

                try {
                    mImageViews = loadStringArrayListToPosterImageViewList(moviePosterList);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                mPostersAdapter.setmImageViews(mImageViews);
                mPostersAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getActivity(), "HTTP Connection Error", Toast.LENGTH_LONG).show();
            }
        }

        private ArrayList<ImageView> loadStringArrayListToPosterImageViewList(ArrayList<String> moviesList)
                throws MalformedURLException {
            ArrayList<ImageView> postersList = new ArrayList<>();

            for(int i=0;i<moviesList.size();i++) {
                ImageView poster = new ImageView(getActivity());
                poster.setScaleType(ImageView.ScaleType.CENTER_CROP);
                poster.setAdjustViewBounds(true);

                Picasso.with(getContext()).load(moviesList.get(i)).into(poster);
                postersList.add(poster);
            }

            return postersList;
        }

        private ArrayList<Movie> getMoviesDataFromJson(String moviesJsonStr) throws JSONException {

            JSONObject moviesJson = new JSONObject(moviesJsonStr);
            JSONArray moviesArray = moviesJson.getJSONArray("results");
            int numMovies = moviesArray.length();

            String posterPath;
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            String[] resultStrs = new String[numMovies];
            ArrayList<Movie> resultObjects = new ArrayList<>();

            for(int i=0;i<numMovies;i++) {
                String title;
                String thumbnail;
                Calendar releaseDate = Calendar.getInstance();
                double rating;
                String overview;

                JSONObject movieJson = moviesArray.getJSONObject(i);

                posterPath = movieJson.getString("poster_path");
                title = movieJson.getString("title");
                thumbnail = movieJson.getString("backdrop_path");
                try {
                    releaseDate.setTime(format.parse(movieJson.getString("release_date")));
                    //Log.v(LOG_TAG,"DATE STRING: "+ releaseDate.get(Calendar.YEAR));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //Log.v(LOG_TAG,"DATE STRING: "+ releaseDate.get(Calendar.YEAR));

                rating = movieJson.getDouble("vote_average");
                overview = movieJson.getString("overview");

                Movie movie = new Movie();

                movie.setPosterPath(basePosterUrl + posterPath);

                movie.setTitle(title);
                movie.setBackdropPath(basePosterUrl + thumbnail);
                movie.setReleaseDate(releaseDate);
                //Log.v(LOG_TAG,"DATE STRING: "+ movie.getReleaseDate().get(Calendar.YEAR));

                movie.setVoteAverage(rating);
                movie.setOverview(overview);

                resultObjects.add(movie);
            }

            for(Movie movie : resultObjects) {

                //Log.v(LOG_TAG,"DATE STRING: "+ movie.getReleaseDate().get(Calendar.YEAR));
            }
            return resultObjects;
        }
    }
}
