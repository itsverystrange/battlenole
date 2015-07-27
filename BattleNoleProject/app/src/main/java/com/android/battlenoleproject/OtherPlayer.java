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


public class OtherPlayer extends Activity
        implements com.android.battlenoleproject.EnemyBoardFragment.OnFragmentFireInteractionListener,
        com.android.battlenoleproject.EnemyBoardFragment.OnEnemyFragmentContinueInteractionListener,
        PlayerBoardFragment.OnPlayerFragmentContinueInteractionListener{

    Ship[] player1Ships, player2Ships;
    int player1, player2, playerTurn;

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

    private int result;
    private int turn;

    private TextView topTV;

    private ImageButton overlay;

    private ImageView submarine;
    private ImageView smallShip;
    private ImageView destroyer;
    private ImageView aircraftCarrier;

    private Boolean showSmallShip1;
    private Boolean showSub1;
    private Boolean showDestroyer1;
    private Boolean showAC1;

    private Boolean showSmallShip2;
    private Boolean showSub2;
    private Boolean showDestroyer2;
    private Boolean showAC2;

    private String playerNumberText, oppositePlayerNumberText;

    private EnemyBoardFragment player1Fragment, player2Fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);

        overlay = (ImageButton) findViewById(R.id.overlayBtn);
        overlay.setVisibility(View.GONE);

        overlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overlay.setVisibility(View.GONE);
            }
        });

        Bundle bundle = getIntent().getExtras();

        turn = 1;

        Parcelable ps1[] = bundle.getParcelableArray("player1Ships");
        Parcelable ps2[] = bundle.getParcelableArray("player2Ships");

        player1Ships = new Ship[ps1.length];
        player2Ships = new Ship[ps2.length];

        System.arraycopy(ps1, 0, player1Ships, 0, ps1.length);

        System.arraycopy(ps2, 0, player2Ships, 0, ps1.length);

        getIntent().removeExtra("player1Ships");
        getIntent().removeExtra("player2Ships");


        game = new Game(player1Ships, player2Ships);
        player1 = PLAYER_PLAYER_NUMBER;
        player2 = ENEMY_PLAYER_NUMBER;

        playerTurn = 0;
        playerNumberText = "Player 1";
        oppositePlayerNumberText = "Player 2";


        topTV = (TextView) findViewById(R.id.play_tv1);

        submarine = (ImageView) findViewById(R.id.submarine);
        smallShip = (ImageView) findViewById(R.id.small_ship);
        destroyer = (ImageView) findViewById(R.id.destroyer);
        aircraftCarrier = (ImageView) findViewById(R.id.aircraft_carrier);


        showSmallShip1 = true;
        showSub1 = true;
        showDestroyer1 = true;
        showAC1 = true;

        showSmallShip2 = true;
        showSub2 = true;
        showDestroyer2 = true;
        showAC2 = true;


        makeFragments();
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

    public void makeFragments() {

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Context context = getApplicationContext();

        Board player1Board = new Board(game.getPlayerBoard(PLAYER_PLAYER_NUMBER));
        EnemyBoardFragment player1Fragment = EnemyBoardFragment.newInstance(player1Board);
        Board player2Board = new Board(game.getPlayerBoard(ENEMY_PLAYER_NUMBER));
        EnemyBoardFragment player2Fragment = EnemyBoardFragment.newInstance(player2Board);

        ViewGroup mainContainer = (ViewGroup) findViewById(R.id.grid_holder);
        fragmentTransaction.add(R.id.grid_holder, player1Fragment, "player1");
        fragmentTransaction.add(R.id.grid_holder, player2Fragment, "player2");
        fragmentTransaction.commit();
        fragmentManager.executePendingTransactions();
    }


    protected void displayArrangeGameScreen(int playerTurn){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Context context = getApplicationContext();

        topTV.setText("Pick a cell to fire on");

        if (playerTurn == 0) {
            EnemyBoardFragment oldEnemyBoardFragment = new EnemyBoardFragment();
            oldEnemyBoardFragment = (EnemyBoardFragment) fragmentManager.findFragmentByTag("player2");
            if (oldEnemyBoardFragment != null)
                fragmentTransaction.hide(oldEnemyBoardFragment);

            EnemyBoardFragment newEnemyBoardFragment = new EnemyBoardFragment();
            newEnemyBoardFragment = (EnemyBoardFragment) fragmentManager.findFragmentByTag("player1");
            fragmentTransaction.show(newEnemyBoardFragment);

        }
        else {
            EnemyBoardFragment oldEnemyBoardFragment = new EnemyBoardFragment();
            oldEnemyBoardFragment = (EnemyBoardFragment) fragmentManager.findFragmentByTag("player1");
            if (oldEnemyBoardFragment != null)
                fragmentTransaction.hide(oldEnemyBoardFragment);

            EnemyBoardFragment newEnemyBoardFragment = new EnemyBoardFragment();
            newEnemyBoardFragment = (EnemyBoardFragment) fragmentManager.findFragmentByTag("player2");
            fragmentTransaction.show(newEnemyBoardFragment);

        }

        fragmentTransaction.commit();
        fragmentManager.executePendingTransactions();


    }


    public void playGame() {

        if (playerTurn == -1)
            return;
        else {
            displayArrangeGameScreen(playerTurn);
            topTV.setText(playerNumberText + " Turn.");

        }

    }


    public void processPlayerFire(int position) {


        result = this.game.processMove(playerTurn, position);

        if (result == FIRE_BAD_FIRE) {
            topTV.setText("Cell already Selected. Please pick again");
            return;
        } else {

            EnemyBoardFragment enemyBoardFragment = new EnemyBoardFragment();

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Context context = getApplicationContext();

            if (playerTurn == 0)
                enemyBoardFragment = (EnemyBoardFragment) fragmentManager.findFragmentByTag("player1");
            else
                enemyBoardFragment = (EnemyBoardFragment) fragmentManager.findFragmentByTag("player2");
            enemyBoardFragment.fire(position, result);


            if (result == FIRE_MISS) { // it was a miss, switch turns
                playerTurn = Game.getOpposite(playerTurn);
                setPlayerNumberText();
                topTV.setText("Sorry, you missed. Switching to "  + playerNumberText + " Turn.");
            } else if (result == FIRE_HIT) {
                topTV.setText("You Hit the Enemy! Go Again.");
            } else if (result%FIRE_DESTROY_SHIP == 0) {
                topTV.setText("You Destroyed an Enemy Ship! Go Again.");
                int shipNumber = result / FIRE_DESTROY_SHIP - 1;
                if (playerTurn == 0) {
                    switch (shipNumber) {
                        case 0:
                            smallShip.setImageDrawable(null);
                            showSmallShip1 = false;
                            break;
                        case 1:
                            submarine.setImageDrawable(null);
                            showSub1 = false;
                            break;
                        case 2:
                            destroyer.setImageDrawable(null);
                            showDestroyer1 = false;
                            break;
                        case 3:
                            aircraftCarrier.setImageDrawable(null);
                            showAC1 = false;
                            break;
                        default:
                            //should never reach here
                    }
                }

                else {
                    switch (shipNumber) {
                        case 0:
                            smallShip.setImageDrawable(null);
                            showSmallShip2 = false;
                            break;
                        case 1:
                            submarine.setImageDrawable(null);
                            showSub2 = false;
                            break;
                        case 2:
                            destroyer.setImageDrawable(null);
                            showDestroyer2 = false;
                            break;
                        case 3:
                            aircraftCarrier.setImageDrawable(null);
                            showAC2 = false;
                            break;
                        default:
                            //should never reach here
                    }
                }



            } else if (result == FIRE_DESTROY_FLEET) {
                topTV.setText("YOU WIN!!!!!!!!!");
                processWinner(playerTurn);
            }


            myHandler.postDelayed(changePlayers, 1000);

        }
    }

    public void processWinner(int playerNumber) {
        int playerWinner = playerNumber + 1;
        playerTurn = -1;
        Intent endGame = new Intent(this, EndGame.class);
        endGame.putExtra("playerWinner", playerWinner);
        startActivity(endGame);
    }


    private Runnable changePlayers = new Runnable() {
        @Override
        public void run() {
      /* do what you need to do */
            if (playerTurn == 0)
            {
                if (showSmallShip1)
                    smallShip.setImageResource(R.drawable.small_ship);
                else
                    smallShip.setImageDrawable(null);
                if (showSub1)
                    submarine.setImageResource(R.drawable.submarine);
                else
                    submarine.setImageDrawable(null);
                if (showDestroyer1)
                    destroyer.setImageResource(R.drawable.destroyer);
                else
                    destroyer.setImageDrawable(null);
                if (showAC1)
                    aircraftCarrier.setImageResource(R.drawable.aircraft_carrier);
                else
                    aircraftCarrier.setImageDrawable(null);
            }

            else if (playerTurn == 1)
            {
                if (showSmallShip2)
                    smallShip.setImageResource(R.drawable.small_ship);
                else
                    smallShip.setImageDrawable(null);
                if (showSub2)
                    submarine.setImageResource(R.drawable.submarine);
                else
                    submarine.setImageDrawable(null);
                if (showDestroyer2)
                    destroyer.setImageResource(R.drawable.destroyer);
                else
                    destroyer.setImageDrawable(null);
                if (showAC2)
                    aircraftCarrier.setImageResource(R.drawable.aircraft_carrier);
                else
                    aircraftCarrier.setImageDrawable(null);
            }

            if (result == FIRE_MISS) {
                overlay.setVisibility(View.VISIBLE);
            }

            ++turn;

            playGame();
      /* and here comes the "trick" */
            // myHandler.postDelayed(this, 100);
        }
    };


    private void setPlayerNumberText() {
        if (playerTurn == 0) {
            playerNumberText = "Player 1";
            oppositePlayerNumberText = "Player 2";
        }

        else {
            playerNumberText = "Player 2";
            oppositePlayerNumberText = "Player 1";

        }
    }

}
