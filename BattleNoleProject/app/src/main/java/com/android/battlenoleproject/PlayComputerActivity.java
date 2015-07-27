package com.android.battlenoleproject;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Parcelable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;


public class PlayComputerActivity extends Activity
    implements com.android.battlenoleproject.EnemyBoardFragment.OnFragmentFireInteractionListener,
        com.android.battlenoleproject.EnemyBoardFragment.OnEnemyFragmentContinueInteractionListener,
        PlayerBoardFragment.OnPlayerFragmentContinueInteractionListener{

    Ship[] player1Ships, computerShips;
    int player1, computer, playerTurn;

    Game game;

    private final static int ENEMY_PLAYER_NUMBER = 1;
    private final static int PLAYER_PLAYER_NUMBER = 0;

    private final static int FIRE_MISS = 61;
    private final static int FIRE_HIT = 62;
    private final static int FIRE_DESTROY_SHIP = 71;
    private final static int FIRE_DESTROY_FLEET = 72;
    private final static int FIRE_BAD_FIRE = 73;

    private static final String BOARD_KEY = "board";

    private Handler myHandler = new Handler();

    private static final Random r = new Random();

    protected int random;

    private int lastEnemyHit;

    private TextView topTV;

    private ImageButton overlay;

    private ImageView submarine;
    private ImageView smallShip;
    private ImageView destroyer;
    private ImageView aircraftCarrier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);

        overlay = (ImageButton) findViewById(R.id.overlayBtn);
        overlay.setVisibility(View.GONE);

        Bundle bundle = getIntent().getExtras();

        Parcelable ps1[] = bundle.getParcelableArray("player1Ships");
        Parcelable ps2[] = bundle.getParcelableArray("player1Ships");

        player1Ships = new Ship[ps1.length];
        computerShips = new Ship[ps2.length];

        System.arraycopy(ps1, 0, player1Ships, 0, ps1.length);

        System.arraycopy(ps2, 0, computerShips, 0, ps1.length);

        getIntent().removeExtra("player1Ships");
        getIntent().removeExtra("player2Ships");


        game = new Game(player1Ships, computerShips);
        player1 = PLAYER_PLAYER_NUMBER;
        computer = ENEMY_PLAYER_NUMBER;

        playerTurn = 0;  // changed this to test, needs to be 0


        topTV = (TextView) findViewById(R.id.play_tv1);

        submarine = (ImageView) findViewById(R.id.submarine);
        smallShip = (ImageView) findViewById(R.id.small_ship);
        destroyer = (ImageView) findViewById(R.id.destroyer);
        aircraftCarrier = (ImageView) findViewById(R.id.aircraft_carrier);

        playGame();

    }

    @Override
    public void onFragmentFireInteraction(int position) {

        processPlayerFire(position);

    }

    @Override
    public void onPlayerFragmentContinueInteraction() {

        playGame();

    }

    @Override
    public void onEnemyFragmentContinueInteraction() {

        playGame();

    }


    protected void displayArrangeGameScreen(int playerTurn){


        if (playerTurn == ENEMY_PLAYER_NUMBER) {

            topTV.setText("Enemy is Firing!");

            Board playerBoard = new Board(game.getPlayerBoard(PLAYER_PLAYER_NUMBER));

            PlayerBoardFragment playerFragment = PlayerBoardFragment.newInstance(playerBoard);


            EnemyBoardFragment enemyBoardFragment = new EnemyBoardFragment();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Context context = getApplicationContext();
            enemyBoardFragment =  (EnemyBoardFragment) fragmentManager.findFragmentByTag("enemyBoard");

            if (enemyBoardFragment != null)
                fragmentTransaction.remove(enemyBoardFragment);



            ViewGroup mainContainer = (ViewGroup) findViewById(R.id.grid_holder);


            fragmentTransaction.replace(mainContainer.getId(), playerFragment, "playerBoard");
            //fragmentTransaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
            fragmentTransaction.commit();
            fragmentManager.executePendingTransactions();

        }

        else {

            topTV.setText("Pick a cell to fire on");

            Board enemyBoard = new Board(game.getPlayerBoard(ENEMY_PLAYER_NUMBER));

            EnemyBoardFragment enemyFragment = EnemyBoardFragment.newInstance(enemyBoard);

            PlayerBoardFragment playerBoardFragment = new PlayerBoardFragment();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Context context = getApplicationContext();
            playerBoardFragment =  (PlayerBoardFragment) fragmentManager.findFragmentByTag("playerBoard");

            if (playerBoardFragment != null)
                fragmentTransaction.remove(playerBoardFragment);


            ViewGroup mainContainer = (ViewGroup) findViewById(R.id.grid_holder);


            fragmentTransaction.replace(mainContainer.getId(), enemyFragment, "enemyBoard");
           // fragmentTransaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
            fragmentTransaction.commit();
            fragmentManager.executePendingTransactions();

        }


    }


    public void playGame() {

            if (playerTurn == -1)
                return;


            if (playerTurn == 0) {                          // It is Player's turn show enemy board
                displayArrangeGameScreen(playerTurn);
            }

            else {                                        // It is enemy's turn show player's board
                displayArrangeGameScreen(playerTurn);
                while (game.getPlayerBoard(0).getElementAtBoardPosition(random) >= FIRE_MISS )  //  (don't select a cell that we have already hit
                    random = r.nextInt(99);

                Handler handler = new Handler();       // delay here to show screen and then show fire position
                handler.postDelayed(new Runnable() {
                    public void run() {
                            processEnemyFire(random);

                    }
                }, 2000);

            }

    }


    public void processPlayerFire(int position) {


        int result = this.game.processMove(playerTurn, position);

        if (result == FIRE_BAD_FIRE) {
            topTV.setText("Cell already Selected. Please pick again");
            return;
        } else {

            EnemyBoardFragment enemyBoardFragment = new EnemyBoardFragment();

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Context context = getApplicationContext();
            enemyBoardFragment = (EnemyBoardFragment) fragmentManager.findFragmentByTag("enemyBoard");
            enemyBoardFragment.fire(position, result);


            if (result == FIRE_MISS) { // it was a miss, switch turns
                topTV.setText("Sorry, you missed. Switching to Enemy Turn.");
                playerTurn = Game.getOpposite(playerTurn);
            } else if (result == FIRE_HIT) {
                topTV.setText("You Hit the Enemy! Go Again.");
            } else if (result%FIRE_DESTROY_SHIP == 0) {
                topTV.setText("You Destroyed an Enemy Ship! Go Again.");
                int shipNumber = result / FIRE_DESTROY_SHIP - 1;
                switch(shipNumber)
                {
                    case 0:
                        smallShip.setImageDrawable(null);
                        break;
                    case 1:
                        submarine.setImageDrawable(null);
                        break;
                    case 2:
                        destroyer.setImageDrawable(null);
                        break;
                    case 3:
                        aircraftCarrier.setImageDrawable(null);
                        break;
                    default:
                        //should never get here

                }




            } else if (result == FIRE_DESTROY_FLEET) {
                topTV.setText("YOU WIN!!!!!!!!!");
                processWinner(playerTurn);
            }


            myHandler.postDelayed(changePlayers, 1000);

        }
    }


    public void processEnemyFire(int position) {


        int result = this.game.processMove(playerTurn, position);


        PlayerBoardFragment playerBoardFragment = new PlayerBoardFragment();

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Context context = getApplicationContext();
        playerBoardFragment =  (PlayerBoardFragment) fragmentManager.findFragmentByTag("playerBoard");
        playerBoardFragment.fire(position, result);




        if (result == FIRE_MISS) { // it was a miss, switch turns
            topTV.setText("Sorry, you missed. Switching to Enemy Turn.");
            playerTurn = Game.getOpposite(playerTurn);
        }
        else if (result == FIRE_HIT) {
            topTV.setText("You Hit the Enemy! Go Again.");
        }
        else if (result == FIRE_DESTROY_SHIP) {
            topTV.setText("Enemy Destroyed Your Ship! Go Again.");
        }
        else if (result == FIRE_DESTROY_FLEET) {
            topTV.setText("ENEMY WINS!!!!!!!!!");
            processWinner(playerTurn);
        }


        myHandler.postDelayed(changePlayers, 2000);

    }


    public void processWinner(int playerNumber) {
        int playerCheck = playerNumber;
        playerTurn = -1;
        Intent endGame = new Intent(this, EndGame.class);
        Boolean won = true;
        if (playerCheck == ENEMY_PLAYER_NUMBER)
            won = false;
        endGame.putExtra("winner", won);
        startActivity(endGame);
    }


    private Runnable changePlayers = new Runnable() {
        @Override
        public void run() {
      /* do what you need to do */
            playGame();
      /* and here comes the "trick" */
            // myHandler.postDelayed(this, 100);
        }
    };


}
