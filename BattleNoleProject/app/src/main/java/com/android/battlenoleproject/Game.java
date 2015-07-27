package com.android.battlenoleproject;

/**
 * Created by srandall on 7/19/15.
 */

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Game {

    private List<Board> boards;
    private List<ArrayList<Integer>> destroyedShips;

    private final static int BOARD_SIZE = 100;
    private final static int SHIP_COUNT = 4;

    private final static Integer WATER = 59;
    private final static int FIRE_MISS = 61;
    private final static int FIRE_HIT = 62;
    private final static int FIRE_DESTROY_SHIP = 71;
    private final static int FIRE_DESTROY_FLEET = 72;
    private final static int FIRE_BAD_FIRE = 73;

    private final static int PLAYER_PLAYER_NUMBER = 0;
    private final static int ENEMY_PLAYER_NUMBER = 1;

    private final static int PLAYER_COUNT = 2;


    public Game( Ship[] playerOneShips, Ship[] playerTwoShips) {

        createBoards(playerOneShips, playerTwoShips);

        ArrayList<Integer> player1Destroyed = new ArrayList<>();
        ArrayList<Integer> player2Destroyed = new ArrayList<>();

        this.destroyedShips = new ArrayList<>();

        this.destroyedShips = new ArrayList<ArrayList<Integer>>(PLAYER_COUNT);
        this.destroyedShips.add(PLAYER_PLAYER_NUMBER, player1Destroyed);
        this.destroyedShips.add(ENEMY_PLAYER_NUMBER, player2Destroyed);

    }

    public Game (Game sentGame) {

        Board  board0 = new Board(sentGame.getPlayerBoard(PLAYER_PLAYER_NUMBER));
        Board  board1 = new Board(sentGame.getPlayerBoard(ENEMY_PLAYER_NUMBER));

        this.boards = new ArrayList<Board>(PLAYER_COUNT);
        this.boards.add(board0);
        this.boards.add(board1);

        this.destroyedShips = new ArrayList<ArrayList<Integer>>(PLAYER_COUNT);

        for (int i = 0; i < PLAYER_COUNT; ++i) {

            if (sentGame.destroyedShips.get(PLAYER_PLAYER_NUMBER).size() > 0) {
                this.destroyedShips.add(i, sentGame.destroyedShips.get(PLAYER_PLAYER_NUMBER));
            } else {
                ArrayList<Integer> playerDestroyed = new ArrayList<>();
                this.destroyedShips.add(i, playerDestroyed);
            }
        }

    }

/* LEAVING HERE JUST IN CASE WE NEED TO PARCEL GAME LATER
    public void writeToParcel ( Parcel out, int flags) {
        out.writeTypedList(boards);
        out.write
    }

    public Game (Parcel in) {
        this.boards = new ArrayList<Board>(2);
        this.destroyedShips = new ArrayList<ArrayList<Integer>>(2);
        in.readTypedList(boards, Board.CREATOR);
        in.readList(destroyedShips, Integer.class.getClassLoader());
    }

    public int describeContents() {
        return this.hashCode();
    }


    public static final Parcelable.Creator<Game> CREATOR =
            new Parcelable.Creator<Game>() {
                public Game createFromParcel(Parcel in) {
                    return new Game(in);
                }

                public Game[] newArray(int size) {
                    return new Game[size];
                }
            };

*/
    public boolean isGameOver() {
        boolean gameOver = true;


        return gameOver;
    }



    private void createBoards(Ship[] player1Ships, Ship[] player2Ships) {
        Board  board0 = new Board();
        Board  board1 = new Board();

        int size = board0.getBoardSize();

        Log.d("TEST", "BOARD0 SIZE IS " + size);


        for (int i = 0; i < BOARD_SIZE; ++i) {
            board0.addElement(WATER);
            board1.addElement(WATER);
        }

        for (int j = 0; j < SHIP_COUNT; ++j) {

            ArrayList<Integer>  player1ShipCoordinates = new ArrayList<Integer>(player1Ships[j].getLength());
            player1ShipCoordinates.addAll(player1Ships[j].getCoordinates());

            ArrayList<Integer>  player2ShipCoordinates = new ArrayList<Integer>(player2Ships[j].getLength());
            player2ShipCoordinates.addAll(player2Ships[j].getCoordinates());

            for (int k = 0; k < player1Ships[j].getLength(); ++k )
                board0.setElementAtBoardPosition(player1ShipCoordinates.get(k), j*10+k);

            for (int k = 0; k < player2Ships[j].getLength(); ++k )
                board1.setElementAtBoardPosition(player2ShipCoordinates.get(k), j*10+k);

        }


        this.boards = new ArrayList<Board>(PLAYER_COUNT);
        this.boards.add(board0);
        this.boards.add(board1);

    }

    public Board getPlayerBoard(int playernumber) {

        return this.boards.get(playernumber);

    }
/*
    public ArrayList<Integer> getPlayerDestroyedShips(int playernumber) {
        ArrayList<Integer>  newDestroyedShips = new ArrayList<Integer>(SHIP_COUNT);
        newDestroyedShips.addAll(this.destroyedShips.get(playernumber));

        return newDestroyedShips;
    }

*/
    public void markBoardWithHit(int playerNumber, int position) {
        this.boards.get(playerNumber).setElementAtBoardPosition(position, FIRE_HIT);
    }

    public void markBoardWithMiss(int playerNumber, int position) {
        Log.d("TEST", "playerNumber SET TO " + playerNumber + "position SET  TO " + position);
        this.boards.get(playerNumber).setElementAtBoardPosition(position, FIRE_MISS);  // 6 will mean a miss
    }


    public int processMove(int attackingPlayer, int position) {

        int attackedPlayer = getOpposite(attackingPlayer);
        int positionResult = FIRE_MISS;


        // check if this is position on any of attackedPlayers ships


        Board boardGrid = new Board(this.boards.get(attackedPlayer));

        int positionContains = boardGrid.getElementAtBoardPosition(position);

        if (positionContains == FIRE_MISS || positionContains == FIRE_HIT)
            return FIRE_BAD_FIRE;


        if (positionContains < 50) {  // this is a hit

            markBoardWithHit(attackedPlayer, position);  // 61 will mean a hit
            positionResult = FIRE_HIT;

            boolean shipAlive = shipStillAlive(attackedPlayer, positionContains);

            if (shipAlive == false) {
                int shipNumber = positionContains/10;
                this.destroyedShips.get(attackedPlayer).add(shipNumber);
                positionResult = FIRE_DESTROY_SHIP  * (shipNumber + 1);

                // check the fleet

                if (fleetStillAlive(attackedPlayer) == false) {
                    positionResult = FIRE_DESTROY_FLEET;
                }
            }


        }


        else { // it did not hit a ship, add board miss and return FIRE_MISS
            markBoardWithMiss(attackedPlayer, position);
            positionResult = FIRE_MISS;

        }

        return positionResult;
    }


    public static int getOpposite(int player) {
        int returnPlayer = 0;
        if (player == 0)
            returnPlayer = 1;
        else
            returnPlayer = 0;

        return returnPlayer;

    }

    public boolean fleetStillAlive(int playerNumber) {
        boolean alive = true;

        if (this.destroyedShips.get(playerNumber).size() == SHIP_COUNT) {
            alive = false;
            // perform a safety check before declaring dead

            for (int i = 0; i < BOARD_SIZE; ++ i) {
                if (boards.get(playerNumber).getElementAtBoardPosition(i) < 50)
                    alive = true;
            }

        }

        return alive;

    }

    public boolean shipStillAlive(int playerNumber, int positionNumber) {
        int shipNumber = positionNumber/10;
        int shipStartPosition = shipNumber * 10;
        Log.d("TEST", "SHIP NUMBER from shipStillAlive() is " + shipNumber + "and shipStartPosition is " + shipStartPosition );
        boolean alive = false;


            for (int i = 0; i < Ship.getShipLength(shipNumber); ++i) {
                if (boards.get(playerNumber).boardContains(shipStartPosition + i)) {
                    Log.d("TEST", "SHIP POSITION from shipStillAlive() is " + shipStartPosition+i);
                    alive = true;
                }
            }

        return alive;

    }


    public int getBoardCellValueByPlayer(int playerNumber, int position) {
        return this.getPlayerBoard(playerNumber).getElementAtBoardPosition(position);
    }





}
