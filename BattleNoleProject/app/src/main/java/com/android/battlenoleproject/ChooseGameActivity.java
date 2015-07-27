package com.android.battlenoleproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * Created by Julian on 7/24/2015.
 */
public class ChooseGameActivity extends Activity {
    private Button multiplayer;
    private Button computer;
    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_game);


        multiplayer = (Button) findViewById(R.id.multiplayer);
        computer = (Button) findViewById(R.id.againstComputer);
        back = (ImageButton) findViewById(R.id.previous);

        multiplayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(v.getContext(), SetupActivity.class);
                i.putExtra("playComputer", false);
                startActivity(i);
                finish();
            }
        });

        computer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(
                        ChooseGameActivity.this,
                        SetupActivity.class);
                i.putExtra("playComputer", true);
                startActivity(i);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    finish();
            }
        });
    }
}
