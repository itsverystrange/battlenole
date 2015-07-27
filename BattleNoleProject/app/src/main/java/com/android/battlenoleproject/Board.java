package com.android.battlenoleproject;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by srandall on 7/21/15.
 */
public class Board implements Parcelable {

    public final static String BOARD_KEY = "BOARDKEY";
    private final static int BOARD_DESC = 0;
    ArrayList<Integer>  board;

    private final static int BOARD_SIZE = 100;

    public Board() {
        this.board = new ArrayList<Integer>(BOARD_SIZE);

    }

    public Board(ArrayList<Integer> originalBoard) {
        this.board = new ArrayList<Integer>(BOARD_SIZE);
        board.addAll(originalBoard);
    }

    public Board(Board originalBoard) {
        this.board = new ArrayList<Integer>(BOARD_SIZE);
        for (int i = 0; i < originalBoard.BOARD_SIZE; ++i) {
            this.board.add(originalBoard.getElementAtBoardPosition(i));
        }
    }

    public void writeToParcel ( Parcel out, int flags) {
        out.writeList(board);
    }

    public Board (Parcel in) {
        board = new ArrayList<Integer>();
        in.readList(board, Integer.class.getClassLoader());

    }





    public int describeContents() {
        return BOARD_DESC;
    }


    public static final Parcelable.Creator<Board> CREATOR =
            new Parcelable.Creator<Board>() {
                public Board createFromParcel(Parcel in) {
                    return new Board(in);
                }

                public Board[] newArray(int size) {
                    return new Board[size];
                }
            };




    public ArrayList<Integer> getBoard() {
        return this.board;
    }

    public int getElementAtBoardPosition ( int position) {
        return this.board.get(position);
    }

    public void setElementAtBoardPosition (int position, int element) {
        this.board.set(position, element);
    }

    public void addElement (int element) {
        this.board.add(element);
    }

    public boolean boardContains (int element) {
        return this.board.contains(element);
    }

    public int getBoardSize () {
        return this.board.size();
    }



}
