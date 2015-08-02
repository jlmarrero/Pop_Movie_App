package com.example.jonathan.popmovieapp;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

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


public class MainActivity extends ActionBarActivity {
    private ArrayList<Movie> movies;
    private ArrayAdapter<String> movieArrayAdapter;
    private GridView grid;
    private Movie[] movieList;

    private String[] mPosters = {
            "http://image.tmdb.org/t/p/w185/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg","http://image.tmdb.org/t/p/w185/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg",
            "http://image.tmdb.org/t/p/w185/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg","http://image.tmdb.org/t/p/w185/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg",
            "http://image.tmdb.org/t/p/w185/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg","http://image.tmdb.org/t/p/w185/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg",
            "http://image.tmdb.org/t/p/w185/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg","http://image.tmdb.org/t/p/w185/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg",
            "http://image.tmdb.org/t/p/w185/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg","http://image.tmdb.org/t/p/w185/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg",
            "http://image.tmdb.org/t/p/w185/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg","http://image.tmdb.org/t/p/w185/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg",
            "http://image.tmdb.org/t/p/w185/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg","http://image.tmdb.org/t/p/w185/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg",
            "http://image.tmdb.org/t/p/w185/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg","http://image.tmdb.org/t/p/w185/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg",
            "http://image.tmdb.org/t/p/w185/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg","http://image.tmdb.org/t/p/w185/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg",
            "http://image.tmdb.org/t/p/w185/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg","http://image.tmdb.org/t/p/w185/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg",
            "http://image.tmdb.org/t/p/w185/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg","http://image.tmdb.org/t/p/w185/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg",
            "http://image.tmdb.org/t/p/w185/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg","http://image.tmdb.org/t/p/w185/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg",
            "http://image.tmdb.org/t/p/w185/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg","http://image.tmdb.org/t/p/w185/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg",
            "http://image.tmdb.org/t/p/w185/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg","http://image.tmdb.org/t/p/w185/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg",
            "http://image.tmdb.org/t/p/w185/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg","http://image.tmdb.org/t/p/w185/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg",
            "http://image.tmdb.org/t/p/w185/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg","http://image.tmdb.org/t/p/w185/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this, mPosters));


        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                Movie movieToPass = movieList[position];

                Log.v("IAMHERE: ", movieToPass.getDescription());

                // String title = movieToPass.getTitle();
                // String desc = movieToPass.getDescription();
                // String pop = movieToPass.getPopularity();
                // String imageUrl = movieToPass.getPoster_path();

                Intent intent = new Intent(getApplication(), MovieDescription.class);
                Bundle b = new Bundle();
                b.putParcelable("MOVIE", movieToPass);

                Log.v("PARCEL: ", b.toString());

                intent.putExtras(b);

                Log.v("INTENT: ", intent.toString());

                startActivity(intent);


                // String title = movieList[position].getTitle();
                // Toast.makeText(MainActivity.this, "" + title, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovie();
    }

    private void updateMovie() {
        FetchMovieTask updateMovies = new FetchMovieTask();
        // This section updates the location preference by instantiating an object
        // of the SharedPreferences class
        // SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        // String location = prefs.getString(getString(R.string.pref_location_key),
        //        getString(R.string.pref_default_location));
        updateMovies.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_refresh) {
            //String message = "Refreshing...";
            //Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
            //toast.show();
            FetchMovieTask movieTask = new FetchMovieTask();
            movieTask.execute();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public class FetchMovieTask extends AsyncTask<Void, Void, Movie[]> {

        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();
        private final String BASE_POSTER_URL = "http://image.tmdb.org/t/p/w185";

        private Movie[] getMovieDataFromJson(String movieJsonStr) throws JSONException {

            JSONObject jData = new JSONObject(movieJsonStr);
            JSONArray jArray = jData.getJSONArray("results");
            Movie[] movieArray = new Movie[jArray.length()];

            for (int i=0; i < jArray.length(); i++){

                JSONObject jActualMovie = jArray.getJSONObject(i);

                String id = jActualMovie.getString("id");
                String title = jActualMovie.getString("title");
                String posterPath = jActualMovie.getString("poster_path");
                posterPath = BASE_POSTER_URL + posterPath;

                // Log.v("POSTER PATH: ", posterPath);

                String release = jActualMovie.getString("release_date");

                // Log.v("RELEASE: ", release);

                String popularity = jActualMovie.getString("popularity");

                // Log.v("POP: ", popularity);

                String overview = jActualMovie.getString("overview");

                // Log.v("OVERVIEW: ", overview);

                String vote_average = jActualMovie.getString("vote_average");

                // Log.v("VOTE: ", vote_average);

                Movie m = new Movie(id, title, popularity, overview, posterPath,
                        release, vote_average);

                // Log.v("Movie Object: ", m.toString());

                movieArray[i] = m;
                // Log.v("Sample MOVIE: ", movieArray[0].toString());
            }

            if (movieArray == null){
                return null;
            } else {
                return movieArray;
            }
        }

        @Override
        protected Movie[] doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String movieJsonStr = null;

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are available at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                URL url = new URL("http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=dd57411618238db8fbe81ec21bd45357\n");
                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    movieJsonStr = null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    movieJsonStr = null;
                }
                movieJsonStr = buffer.toString();
                Log.v("JSON Sample: ", movieJsonStr);

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                movieJsonStr = null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            try {
                Movie[] j = getMovieDataFromJson(movieJsonStr);
                return j;
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            Movie[] j = new Movie[0];
            try {
                j = getMovieDataFromJson(movieJsonStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return j;
        }
        @Override
        protected void onPostExecute(Movie[] result) {
            String[] urlArray = new String[result.length];
            if (result != null) {
                Log.v("MORE JSON: ", result[0].toString());
                movieList = result;
                for(int i = 0; i <result.length; i++){
                    urlArray[i] = result[i].getPoster_path();
                }

                Log.v("URL STRING: ", result[0].getPoster_path());

                grid = (GridView) findViewById(R.id.gridview);
                ImageAdapter adapter = new ImageAdapter(MainActivity.this, urlArray);
                grid.setAdapter(adapter);


            }
        }

    }

}
