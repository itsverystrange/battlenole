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
                "1. When the game launches, you can either choose to play against the computer or " +
                        "play against an opponent via pass 'n' play.\n" +
                        "2. Start by positioning your ships.  This is accomplished by clicking " +
                        "where you want the boats or having them randomly placed. " +
                        "You can click redeploy to reposition the boats randomly. " +
                        "Lock them in and begin.\n" +
                        "3. Each player fires one torpedo per turn. An additional torpedo is" +
                        " gained when an enemy boat is hit. \n" +
                        "4.First player to sink all of his/her opponent’s ships wins!");

        mPages[1] = new Page(
                "Team Froyo",
                "Brian Randall\nJulian Loreti\nStephanie Brown\nWilliam Henry");
    }

    public Page getPage(int pageNumber) {
        return mPages[pageNumber];
    }

}
