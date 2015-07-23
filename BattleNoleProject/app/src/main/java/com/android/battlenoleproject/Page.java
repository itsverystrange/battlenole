package com.android.battlenoleproject;

/**
 * Created by stephanie on 7/11/15.
 * This class is for displaying the help, credit and any other pages.
 */
public class Page {
    private String mTitle;
    private String mText;

    public Page(String title, String text) {
        mTitle = title;
        mText = text;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }
}
