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
    private GridView grid;
    private Movie[] movieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GridView gridview = (GridView) findViewById(R.id.gridview);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        PreferenceManager.setDefaultValues(getApplication(), R.xml.pref_general, true);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Movie movieToPass = movieList[position];

                Intent intent = new Intent(getApplication(), MovieDescription.class);
                Bundle b = new Bundle();
                b.putParcelable("MOVIE", movieToPass);
                // Place parcelable inside intent ** Make sure you use the
                // "MOVIE" string ID when retrieving parcelable
                intent.putExtras(b);
                startActivity(intent);
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
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean syncConnPref = sharedPref.getBoolean(getString(R.string.pref_sort_by), false);

        Log.v("SHARED PREF: ", String.valueOf(syncConnPref));

        String pref = "";
        if (syncConnPref){
            pref = "POPULARITY";
        } else if(!syncConnPref) {
            pref = "HIGH_RATING";
        }
        updateMovies.execute(pref);

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
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        if (id == R.id.action_refresh) {

            //String message = "Refreshing...";
            //Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
            //toast.show();
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            boolean syncConnPref = sharedPref.getBoolean(String.valueOf(SettingsActivity.KEY_SORT_PREF), false);
            String pref = "";

            if (syncConnPref){
                pref = "POPULARITY";
            } else if(!syncConnPref) {
                pref = "HIGH_RATING";
            }
            FetchMovieTask movieTask = new FetchMovieTask();
            movieTask.execute(pref);
            return true;
        }

        return super.onOptionsItemSelected(item);
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
                String posterPath = "http://image.tmdb.org/t/p/w185" + jActualMovie.getString("poster_path");

                String release = jActualMovie.getString("release_date");

                // Log.v("RELEASE: ", release);

                String popularity = jActualMovie.getString("popularity");

                // Log.v("POP: ", popularity);

                String overview = jActualMovie.getString("overview");

                // Log.v("OVERVIEW: ", overview);

                String vote_average = jActualMovie.getString("vote_average");

                Log.v("VOTE: ", vote_average);

                Movie m = new Movie(id, title, popularity, overview, posterPath,
                        release, vote_average);

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

            final String BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
            final String QUERY_PARAM = "sort_by";
            final String API_PARAM = "api_key";
            String api_key = "";
            String sort_by = "";

            try {
                if (params[0] == null) {
                    return null;
                } else if (params[0] == "POPULARITY") {
                    sort_by = "popularity.desc";
                    Log.v("SORT PARAM: ", sort_by);
                } else if (params[0] == "HIGH_RATING") {
                    sort_by = "vote_average.desc";
                    Log.v("SORT PARAM: ", sort_by);
                }


                Uri buildUri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(QUERY_PARAM, sort_by)
                        .appendQueryParameter(API_PARAM, api_key)
                        .build();

                URL url = new URL(buildUri.toString());

                Log.v("MY NEW URL: ", url.toString());

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

                Log.v("URL STRING3: ", urlArray[0]);

                grid = (GridView) findViewById(R.id.gridview);
                ImageAdapter adapter = new ImageAdapter(MainActivity.this, urlArray);
                grid.setAdapter(adapter);

            }
        }

    }

}
