package com.example.jonathan.popmovieapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends ActionBarActivity {
    private Movie[] movieList;
    private GridView gridview;
    private String Pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager.setDefaultValues(getApplication(), R.xml.pref_general, true);

        if (savedInstanceState == null || !savedInstanceState.containsKey("Movies")) {
            updateMovie();
            setContentView(R.layout.activity_main);
            gridview = (GridView) findViewById(R.id.gridview);
        } else {
            movieList = (Movie[]) savedInstanceState.getParcelableArray("Movies");
            String[] urlArray = new String[movieList.length];
            for (int i = 0; i < movieList.length; i++) {
                urlArray[i] = movieList[i].getPoster_path();
            }
            setContentView(R.layout.activity_main);
            gridview = (GridView) findViewById(R.id.gridview);
            ImageAdapter adapter = new ImageAdapter(MainActivity.this, urlArray);
            gridview.setAdapter(adapter);
        }

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Movie movieToPass = movieList[position];

                Intent intent = new Intent(getApplication(), MovieDescription.class);
                Bundle b = new Bundle();
                b.putParcelable("MOVIE", movieToPass);
                intent.putExtras(b);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        updateMovie();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        if (id == R.id.action_refresh) {

            String message = "Saving this action for future functions...";
            Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
            toast.show();

            //KEEPING FOR FUTURE USE

            /* SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            boolean syncConnPref = sharedPref.getBoolean(getString(R.string.sort_by_key), true);
            String pref = "";

            if (syncConnPref){
                pref = "POPULARITY";
            } else if(!syncConnPref) {
                pref = "HIGH_RATING";
            }
            FetchMovieTask movieTask = new FetchMovieTask();
            movieTask.execute(pref);
            return true;*/
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // save Activity details
        outState.putParcelableArray("Movies", movieList);
        outState.putString("Preference", Pref);
        super.onSaveInstanceState(outState);
    }

    private void updateMovie() {
        FetchMovieTask updateMovies = new FetchMovieTask();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Pref = sharedPref.getString(getString(R.string.pref_order), getString(R.string.pref_syncConnection_default));

        updateMovies.execute(Pref);
    }

    public class FetchMovieTask extends AsyncTask<String, Void, Movie[]> {

        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();

        private Movie[] getMovieDataFromJson(String movieJsonStr) throws JSONException {

            JSONObject jData = new JSONObject(movieJsonStr);
            JSONArray jArray = jData.getJSONArray("results");
            Movie[] movieArray = new Movie[jArray.length()];

            for (int i = 0; i < jArray.length(); i++) {

                JSONObject jActualMovie = jArray.getJSONObject(i);

                String id = jActualMovie.getString("id");
                String title = jActualMovie.getString("title");
                String posterPath = "";
                String backdropPath = "";

                if (jActualMovie.getString("poster_path") == "null"){
                    posterPath = "empty";
                }  else {
                    posterPath = getString(R.string.base_poster_path) + jActualMovie.getString("poster_path");
                }
                Log.v("POSTER: ", posterPath);

                if (jActualMovie.getString("backdrop_path") == "null"){
                    backdropPath = "empty";
                }  else {
                    backdropPath = getString(R.string.base_backdrop_path) + jActualMovie.getString("backdrop_path");
                }

                Log.v("BACKDROP: ", backdropPath);

                String release = jActualMovie.getString("release_date");

                //Log.v("RELEASE: ", release);

                String popularity = jActualMovie.getString("popularity");

                // Log.v("POP: ", popularity);

                String overview = jActualMovie.getString("overview");

                // Log.v("OVERVIEW: ", overview);

                String vote_average = jActualMovie.getString("vote_average");

                // Log.v("VOTE: ", vote_average);

                Movie m = new Movie(id, title, popularity, overview, posterPath,
                        vote_average, release, backdropPath);
                // Log.v("Movie Object: ", m.toString());

                movieArray[i] = m;
                // Log.v("Sample MOVIE: ", movieArray[0].toString());
            }

            if (movieArray == null) {
                return null;
            } else {
                return movieArray;
            }
        }

        @Override
        protected Movie[] doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // This String will contain the raw JSON response as a string.
            String movieJsonStr = null;

            // Log.v("Param in DIB: ", params[0].toString());

            try {
                Uri buildUri = Uri.parse(getString(R.string.BASE_URL_MOVIE)).buildUpon()
                        .appendQueryParameter(getString(R.string.primary_release_year), getString(R.string.year_param))
                        .appendQueryParameter(getString(R.string.cert_country), getString(R.string.search_country_param))
                        .appendQueryParameter(getString(R.string.query_param), params[0])
                        .appendQueryParameter(getString(R.string.api_param), getString(R.string.api_key))
                        .build();

                URL url = new URL(buildUri.toString());

                Log.v("MY NEW URL: ", url.toString());

                // Create the request to MDB, and open the connection
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
                // Log.v("JSON Sample: ", movieJsonStr);

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                movieJsonStr = null;
            } finally {
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
                for (int i = 0; i < movieList.length; i++) {
                    urlArray[i] = movieList[i].getPoster_path();
                }
                // Log.v("URL STRING3: ", urlArray[0]);
                GridView grid = (GridView) findViewById(R.id.gridview);
                ImageAdapter adapter = new ImageAdapter(MainActivity.this, urlArray);
                grid.setAdapter(adapter);

            }
        }
    }
}
