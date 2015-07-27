package com.android.battlenoleproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * Created by Julian on 7/24/2015.
 */
public class WaitingRoom extends Activity {

    private ImageButton player2Ready;

    private Ship[] player1Ships;


    // Member object for the game services



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_waiting);

        Bundle bundle = getIntent().getExtras();


        Parcelable ps1[] = bundle.getParcelableArray("player1Ships");
        player1Ships = new Ship[ps1.length];
        System.arraycopy(ps1, 0, player1Ships, 0, ps1.length);
        //  Parcelable ps2[] = bundle.getParcelableArray("player2Ships");



        // computerShips = new Ship[ps2.length];

        // System.arraycopy(ps2, 0, computerShips, 0, ps1.length);

        getIntent().removeExtra("player1Ships");

        //  getIntent().removeExtra("player2Ships");


        player2Ready = (ImageButton) findViewById(R.id.btnGo);


        player2Ready.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player2Setup();
            }

        });


    }

    private void player2Setup() {
        //setupIntent = new Intent(this, com.android.battlenoleproject.SetupActivity.class);
        Intent setupIntent = new Intent(this, SetupActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArray("player1Ships", player1Ships);
        setupIntent.putExtras(bundle);
        int playerNext = 2;
        setupIntent.putExtra("playerSetting", playerNext);
        setupIntent.putExtra("playComputer", false);
        startActivity(setupIntent);
        finish();
    }

}
