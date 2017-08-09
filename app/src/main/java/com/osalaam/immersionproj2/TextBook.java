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

    private String title;
    private String author;
    private String urlLink;
    private String classTitle;

    public TextBook() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public TextBook(String title, String author, String classTitle, String url) {
        this.title = title;
        this.author = author;
        this.urlLink = url;
        this.classTitle = classTitle;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("title", title);
        result.put("author", author);
        result.put("urlLink", urlLink);
        result.put("classTitle", classTitle);
        return result;
    }

    public String getAuthor() {
        return author;
    }


    public String getClassTitle() {
        return classTitle;
    }

    public String getTitle() {return title; }

    public String getURL() {
        return urlLink;
    }

}