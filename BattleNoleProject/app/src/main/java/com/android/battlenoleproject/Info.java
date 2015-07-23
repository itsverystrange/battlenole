package com.android.battlenoleproject;

/**
 * Created by stephanie on 7/11/15.
 * This class contains the title and text displayed on the help and credit pages.
 * More pages can be added by simply adding data to the mPages array.
 */
public class Info {
    private Page[] mPages;

    public Info() {
        mPages = new Page[2];

        mPages[0] = new Page(
                "Rules of Engagement",
                "1. Lorem ipsum dolor\n2. Lorem ipsum dolor\n3. Lorem ipsum dolor\n4. Lorem ipsum dolor");

        mPages[1] = new Page(
                "Team Froyo",
                "Brian Randall\nJulian Loreti\nStephanie Brown\nWilliam Henry");
    }

    public Page getPage(int pageNumber) {
        return mPages[pageNumber];
    }

}
