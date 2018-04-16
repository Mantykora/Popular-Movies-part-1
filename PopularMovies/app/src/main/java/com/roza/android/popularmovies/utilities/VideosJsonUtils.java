package com.roza.android.popularmovies.utilities;

import com.roza.android.popularmovies.Video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public final class VideosJsonUtils {

    public static List<Video> parseVideoJson(String json) throws JSONException {

        final String NAME = "name";
        final String KEY = "key";
        final String RESULTS = "results";

        JSONObject allVideosObject = new JSONObject(json);

        JSONArray resultsArray = allVideosObject.getJSONArray(RESULTS);

        final List<Video> videos = new ArrayList<>();

        for (int i = 0; i < resultsArray.length(); i++) {

            Video video = new Video();

            JSONObject videoObject = resultsArray.getJSONObject(i);
            video.setName(videoObject.getString(NAME));
            video.setKey(videoObject.getString(KEY));

            videos.add(video);


        }
        return videos;
    }
}
