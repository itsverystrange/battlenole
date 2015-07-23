package com.android.battlenole;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;


public class Game extends Activity {

    private ImageButton mMyShips;
    private Button mFire;
    private TextView mHeader;

    Ship[] player1Ships, computerShips;
    int player1, computer, playerTurn;

    Boolean finished = false;
    Boolean newTurn = true;

    GameFactory myGameFactory;

    GridView playerBoardGrid;
    GridView computerBoardGrid;
    EnemyGridImageAdapter enemyImageAdapter;
    PlayerGridImageAdapter playerImageAdapter;

    private Handler myHandler = new Handler();

    private static final Random r = new Random();

    TextView directionsTextView, resultsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Bundle bundle = getIntent().getExtras();

        player1Ships = (Ship[])bundle.getSerializable("player1Ships");
        computerShips = (Ship[])bundle.getSerializable("player2Ships");

        myGameFactory = new GameFactory(player1Ships, computerShips);
        player1 = 0;
        computer = 1;

        playerTurn = 0;

        Log.d("TEST", "MADE IT PAST INTENT");

        playGame();

        // Stop annoying default activity transition
        getWindow().setWindowAnimations(0);

        mHeader = (TextView) findViewById(R.id.headerTextView);
        mMyShips = (ImageButton) findViewById(R.id.myShips);
        mFire = (Button) findViewById(R.id.fire);

        mHeader.setText("Select a target");  // This will change based on the number of ships each player has available.
/*
        mMyShips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewMyShips();
            }
        });

        mFire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fire();
            }
        });

*/
    }



    private void viewMyShips() {
        Intent intent = new Intent(this, com.android.battlenole.MyShips.class);
        startActivity(intent);
    }

    protected void displayArrangeGameScreen(int playerTurn){
        setContentView(R.layout.activity_game);

        directionsTextView = (TextView) findViewById(R.id.play_tv1);
        resultsTextView = (TextView) findViewById(R.id.play_tv2);
        playerBoardGrid = (GridView) findViewById(R.id.setup_gridview);
        computerBoardGrid = (GridView) findViewById(R.id.setup_gridview);


        if (playerTurn == 0) {
            enemyImageAdapter = new EnemyGridImageAdapter(this, this.myGameFactory, this.playerTurn);  // display grid without ships, just misses and hits
            computerBoardGrid.setAdapter(enemyImageAdapter);
        }
        else {  // it is the computer turn, use PlayerImageAdapter to display own grid with ships

            playerImageAdapter = new PlayerGridImageAdapter(this, this.myGameFactory, this.playerTurn);
            playerBoardGrid.setAdapter(playerImageAdapter);
        }
    }


    private void attachActionListeners(){



        //boardGame GridView listener sets the aimedField property and changes aim color
        computerBoardGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ImageView iv = (ImageView) v;
                fire(position);
                playGame(); // does opponent's turn
                playGame(); // resets for player's turn
                // returns control after opponent's turning awaiting another button press
            }
        });

    }

    public void playGame() // runs through a single turn
    {
        //something is causing a garbage collection block...
        if (playerTurn == 0)
        {
            displayArrangeGameScreen(playerTurn);
            directionsTextView.setText("Player 1, pick a cell to fire upon");
            attachActionListeners();
            Log.d("TEST", "ADDED ACTION LISTENERS");
        }
        else {
            displayArrangeGameScreen(playerTurn);
            int random = 0;
            while (myGameFactory.getPlayerBoard(0).get(random) > 9)
                random = r.nextInt(99);
            myHandler.postDelayed(displayTurnResults, 1000);
            Log.d("TEST", "IS COMPUTER FIRING?");
            fire(random);
        }
    }


    public void fire(int position) {

        Log.d("TEST", "A FIRE OCCURRED");

        int result = this.myGameFactory.processMove(playerTurn, position);

        Log.d("TEST_BATTLESHIP", "RESULT IS" + result);

        enemyImageAdapter.notifyDataSetChanged();

        ArrayList<Integer> boardContents = new ArrayList<>();
        boardContents = myGameFactory.getPlayerBoard(myGameFactory.getOpposite(playerTurn));

        Log.d("TEST_BATTLESHIP", "Board Grid is" + boardContents.toString());


        myHandler.postDelayed(displayTurnResults, 100);

        if (result > 4)  // it was a miss, switch turns?? Always switch turns?
        {
            playerTurn = myGameFactory.getOpposite(playerTurn);
            newTurn = true;
        }
        else {
            if (!myGameFactory.fleetStillAlive(playerTurn))
            {
                processWinner(playerTurn);
                finished = true;
            }
        }

        Log.d("TEST_BATTLESHIP", "After Switching Turns, next Turn is" + playerTurn);
        Log.d("TEST_BATTLESHIP", "newTurn is: " + newTurn);


    }


    public void processWinner(int playerNumber) {

    }


    private Runnable displayTurnResults = new Runnable() {
        public void run() {

            if (playerTurn == 0)
                resultsTextView.setText("Player1 Results");
            else
                resultsTextView.setText("Computer Results");
            myHandler.postDelayed(this, 100);
        }

    };


    private void fire() {
        Toast.makeText(this, "Fire torpedoes", Toast.LENGTH_LONG).show();
    }
}