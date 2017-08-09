package com.osalaam.immersionproj2;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.*;
/**
 * Created by elijahc on 8/2/17.
 */

@IgnoreExtraProperties
public class ResourceObj {

    private String title;
    private String classTitle;
    private String author;
    private String urlLink;
    private String testing;

    public ResourceObj() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public ResourceObj(String author, String classTitle, String title, String urlLink, String testing) {
        this.author = author;
        this.classTitle = classTitle;
        this.title = title;
        this.urlLink = urlLink;
        this.testing = testing;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("author", author);
        result.put("classTitle", classTitle);
        result.put("title", title);
        result.put("urlLink", urlLink);
        result.put("testing", "testing");
        return result;
    }

    public String getAuthor() {
        return author;
    }


    public String getClassTitle() {
        return classTitle;
    }

    public String getTitle() {
        return title;
    }

    public String getURL() {
        return urlLink;
    }

    public String getTest() {
        return testing;
    }

    public void setURL(String url)
    {
        this.urlLink = url;
    }

}