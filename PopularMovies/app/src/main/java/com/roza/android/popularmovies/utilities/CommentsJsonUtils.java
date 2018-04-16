package com.roza.android.popularmovies.utilities;

import com.roza.android.popularmovies.Comment;
import com.roza.android.popularmovies.Video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public final class CommentsJsonUtils {

    public static List<Comment> parseCommentJson(String json) throws JSONException {

        final String CONTENT = "content";
        final String AUTHOR = "author";
        final String RESULTS = "results";

        JSONObject allCommentsObject = new JSONObject(json);

        JSONArray resultsArray = allCommentsObject.getJSONArray(RESULTS);

        final List<Comment> comments = new ArrayList<>();

        for (int i = 0; i < resultsArray.length(); i++) {

            Comment comment = new Comment();

            JSONObject commentObject = resultsArray.getJSONObject(i);
            comment.setContent(commentObject.getString(CONTENT));
            comment.setAuthor(commentObject.getString(AUTHOR));

            comments.add(comment);
        }

        return comments;
    }


}


