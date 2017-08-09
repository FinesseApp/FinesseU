package com.osalaam.immersionproj2;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.*;
/**
 * Created by elijahc on 8/2/17.
 */

@IgnoreExtraProperties
public class Homework {

    public String title;
    public String classTitle;
    public String author;
    public String urlLink;

    public Homework() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Homework(String author, String classTitle, String title,  String urlLink)
    {
        this.author = author;
        this.classTitle = classTitle;
        this.title = title;
        this.urlLink = urlLink;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("author", author);
        result.put("classTitle", classTitle);
        result.put("title", title);
        result.put("urlLink", urlLink);
        return result;
    }

}