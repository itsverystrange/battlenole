package com.android.battlenoleproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
    private Button mNewGame;
    private Button mJoinGame;
    private Button mHelp;
    private Button mCredits;
    private Button mExit;
    protected static Intent setupIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Stop annoying default activity transition
        getWindow().setWindowAnimations(0);

        mNewGame = (Button) findViewById(R.id.newGame);
        mJoinGame = (Button) findViewById(R.id.joinGame);
        mHelp = (Button) findViewById(R.id.help);
        mCredits = (Button) findViewById(R.id.credits);
        mExit = (Button) findViewById(R.id.exit);

        mNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
            }
        });

        mJoinGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                joinGame();
            }
        });

        // Get help
        mHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInfo(0); // Pass appropriate page number from Info.java
            }
        });

        // Get game credit info
        mCredits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInfo(1); // Pass appropriate page number from Info.java
            }
        });

        // Exit app
        mExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });
    }

    // Start a new game
    private void startGame() {
        setupIntent = new Intent(this, com.android.battlenoleproject.SetupActivity.class);
        startActivity(setupIntent);
    }

    /*private void joinGame() {
        Intent intent = new Intent(this, JoinGame.class);
        startActivity(intent);
    }*/

    // Show game info like help and credits
    private void showInfo(int pageNumber) {
        Intent intent = new Intent(this, com.android.battlenoleproject.InfoActivity.class);
        intent.putExtra("page", pageNumber);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
