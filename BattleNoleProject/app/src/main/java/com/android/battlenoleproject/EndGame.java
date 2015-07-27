package com.android.battlenoleproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EndGame extends Activity {
    private Button mMainMenu;
    private TextView winText;
    boolean winner = true;
    int playerWinner = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);

        Context context = getApplicationContext();
        winText = (TextView) findViewById(R.id.winText);
        Intent endIntent = getIntent();
        winner = endIntent.getBooleanExtra("winner", false);
        playerWinner = endIntent.getIntExtra("playerWinner", 0);

        if (!winner && playerWinner == 0) {
            winText.setText("You Lose");
        }

        if (playerWinner == 1)
            winText.setText("Player 1 Wins!");
        if (playerWinner == 2)
            winText.setText("Player 2 Wins!");

        mMainMenu = (Button) findViewById(R.id.goToMain);

        mMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EndGame.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}


