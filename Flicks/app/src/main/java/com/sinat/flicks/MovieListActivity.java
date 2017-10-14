package com.sinat.flicks;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sinat.flicks.models.Config;
import com.sinat.flicks.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static com.loopj.android.http.AsyncHttpClient.log;

public class MovieListActivity extends AppCompatActivity {

    //Constants
    //The base URL for the API
    public final static String API_BASE_URL = "http://api.themoviedb.org/3";
    //The parameter name for the API key
    public final static String API_KEY_PARAM = "api_key";
    // tag for logging this activity
    public final static String TAG = "MovieListActivity";

    //instances fields
    AsyncHttpClient client;

    // the list of currently playing movies
    ArrayList<Movie> movies;
    // the recycler view
    RecyclerView rvMovies;
    // the adapter wired to the recycler view
    MovieAdapter adapter;
    // image config
    Config config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        //initialize the client
        client = new AsyncHttpClient();
        // initialize the list of movies
        movies = new ArrayList<>();
        // initialize the adapter -- movies array cannot be reinitialized after this point
        adapter = new MovieAdapter(movies);

        // resolve the recycler view and connect a layout manager and the adapter
        rvMovies = (RecyclerView) findViewById(R.id.rvMovies);
        rvMovies.setLayoutManager(new LinearLayoutManager(this));
        rvMovies.setAdapter(adapter);

        //get the configuration on app creation
        getConfiguration();


    }

    // get the list of currently playing movies from the API
    private void getNowPlaying(){
        //create the url
        String url = API_BASE_URL + "/movie/now_playing";
        //set the request parameter
        RequestParams params = new RequestParams();
        //API key always required
        params.put(API_KEY_PARAM, getString(R.string.api_key));
        //execute a GET request expecting a JSON object response
        client.get(url, params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // load the result from movies list
                try {
                    JSONArray results = response.getJSONArray("results");
                    // iterate through result set and create movie object
                    for (int i = 0; i < results.length(); i++)
                    {

                        Movie movie = new Movie(results.getJSONObject(i));
                        movies.add(movie);
                        // notify adapter that a new row was added
                        adapter.notifyItemInserted(movies.size() - 1);
                    }
                    log.i(TAG, String.format("Loaded %s movies", results.length()));
                } catch (JSONException e) {
                    logError("Failed to parse now_playing", e, true);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failed to get data from now_playing endpoint", throwable, true);
            }
        });
    }

    //get the configuration from the API
    private void getConfiguration() {
        //create the url
        String url = API_BASE_URL + "/configuration";
        //set the request parameter
        RequestParams params = new RequestParams();
        //API key always required
        params.put(API_KEY_PARAM, getString(R.string.api_key));
        //execute a GET request expecting a JSON object response
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    config = new Config(response);
                    log.i(TAG,
                            String.format("Loaded configuration with ImageBaseUrl %s and posterSize %s",
                                    config.getImageBaseUrl(),
                                    config.getPosterSize()));
                    // pass config to the adapter
                    adapter.setConfig(config);
                    // get the now playing movie list
                    getNowPlaying();
                } catch (JSONException e) {
                    logError("Failed parsing configuration", e, true);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failed getting configuration!", throwable, true);
            }
        });

    }

    //handle errors, log and alert user
    private void logError(String message, Throwable error, Boolean alertUser) {
        //always log the error
        log.e(TAG, message, error);
        //alert the user to avoid silent errors
        if (alertUser) {
            //show a long toast with the error message
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
    }



}



