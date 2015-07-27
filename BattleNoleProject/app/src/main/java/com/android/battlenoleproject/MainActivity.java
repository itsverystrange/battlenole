package com.android.battlenoleproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
    private Button mComputerGame;
    private Button mHumanGame;
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

        mComputerGame = (Button) findViewById(R.id.computerGame);
        mHumanGame = (Button) findViewById(R.id.humanGame);
        mHelp = (Button) findViewById(R.id.help);
        mCredits = (Button) findViewById(R.id.credits);
        mExit = (Button) findViewById(R.id.exit);

        mComputerGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playComputer();
            }
        });

        mHumanGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playHuman();
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
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
    }

    // Start a new game
    private void playComputer() {
        //setupIntent = new Intent(this, com.android.battlenoleproject.SetupActivity.class);
        setupIntent = new Intent(this, SetupActivity.class);
        setupIntent.putExtra("playComputer", true);
        startActivity(setupIntent);
    }

    private void playHuman() {
        Intent intent = new Intent(this, SetupActivity.class);
        intent.putExtra("playComputer", false);
        intent.putExtra("playerSetting", 1);
        startActivity(intent);
    }


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
