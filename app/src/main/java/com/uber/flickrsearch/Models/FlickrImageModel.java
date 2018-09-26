package com.uber.flickrsearch.Models;

import org.json.JSONException;
import org.json.JSONObject;

/*
 * Model class for parsing individual image information from
 * the api.
 * Provides methods to get image url, title, etc.
 */

public class FlickrImageModel {

    private String id, title, url;

    public FlickrImageModel(JSONObject object) {
        try {
            id = object.getString("id");
            String owner = object.getString("owner");
            String secret = object.getString("secret");
            String server = object.getString("server");
            int farm = object.getInt("farm");
            title = object.getString("title");
            url = "http://farm" + farm + ".static.flickr.com/" + server +"/" + id + "_" + secret +"_s.jpg";
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }
}
