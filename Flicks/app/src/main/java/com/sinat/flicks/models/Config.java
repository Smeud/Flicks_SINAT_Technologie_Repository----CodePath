package com.sinat.flicks.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Roseline on 10 oct. 2017.
 */

public class Config {
    //the base url for loading images
    String imageBaseUrl;
    //the poster size to use when fetching images, part of the url
    String posterSize;
    // the backdrop to use when fetching images
    String backdropSize;

    public Config(JSONObject object) throws JSONException {
        JSONObject images = object.getJSONObject("images");
        //get the image base url
        imageBaseUrl = images.getString("secure_base_url");
        //get the poster size
        JSONArray posterSizeOptions = images.getJSONArray("poster_sizes");
        //use the option at index 3 or w342 as a fallback
        posterSize = posterSizeOptions.optString(3, "w342");
        // parse the backdrop sizes and use the option at index 1 0r w780 as fallback
        JSONArray backdropSizeOptions = images.getJSONArray("backdrop_sizes");
        backdropSize = backdropSizeOptions.optString(1, "w780");

    }

    // helper method for creating urls
    public String getImageUrl(String size, String path){
        return String.format("%s%s%s", imageBaseUrl, size, path); //Concatenate all three

    }

    public String getPosterSize() {
        return posterSize;
    }

    public String getImageBaseUrl() {
        return imageBaseUrl;
    }

    public String getBackdropSize() {
        return backdropSize;
    }
}
