package com.android.battlenoleproject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by stephanie on 7/11/15.
 */
public class InfoActivity extends Activity {
    private Info mInfo = new Info();
    private TextView mTitle;
    private TextView mBody;
    private ImageButton mPrevious;
    private Page mPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pages);

        // Stop annoying default activity transition
        getWindow().setWindowAnimations(0);

        // Get passed intent (from MainActivity); -1 is default value for error
        int mPageNumber = getIntent().getIntExtra("page", -1);

        mTitle = (TextView) findViewById(R.id.pageTitle);
        mBody = (TextView) findViewById(R.id.pageBody);
        mPrevious = (ImageButton) findViewById(R.id.previous);

        // Return to main menu
        mPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Display appropriate page
        loadPage(mPageNumber);
    }

    private void loadPage(int pageNumber) {
        mPage = mInfo.getPage(pageNumber);

        // Retrieve title from Info.class and update placeholder
        String pageTitle = (String) mPage.getTitle();
        mTitle.setText(pageTitle);

        // Retrieve text from Info.class and update placeholder
        String pageText = mPage.getText();
        mBody.setText(pageText);
    }
}
