package com.android.battlenoleproject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by stephanie on 7/19/15.
 */
public class MyShips extends Activity {
    public TextView mHeader;
    private ImageButton mBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_ships);

        // Stop annoying default activity transition
        getWindow().setWindowAnimations(0);

        mHeader = (TextView) findViewById(R.id.headerTextView);
        mBack = (ImageButton) findViewById(R.id.btnBack);

        mHeader.setText("Your ships");  // This will change based on the number of ships each player has available.

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
