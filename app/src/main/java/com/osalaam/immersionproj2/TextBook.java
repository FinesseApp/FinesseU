package com.osalaam.immersionproj2;

import java.util.HashMap;
import java.util.Map;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.*;
/**
 * Created by elijahc on 8/2/17.
 */

@IgnoreExtraProperties
public class TextBook {

    public String title;
    public String author;
    public String urlLink;

    public TextBook() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public TextBook(String title, String author, String urlLink) {
        this.title = title;
        this.author = author;
        this.urlLink = urlLink;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("title", title);
        result.put("author", author);
        result.put("urlLink", urlLink);
        return result;
    }

}