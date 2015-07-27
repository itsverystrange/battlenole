package com.android.battlenoleproject;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;


public class PlayerBoardFragment extends Fragment {

    GridView playerBoardGrid;
    PlayerGridImageAdapter playerImageAdapter;

    private final static int FIRE_MISS = 61;
    private final static int FIRE_HIT = 62;

    private final static int ENEMY_PLAYER_NUMBER = 1;
    private final static int PLAYER_PLAYER_NUMBER = 0;

    private static final Random r = new Random();

    private Board board;

    private int playerTurn = ENEMY_PLAYER_NUMBER;

    private Handler myHandler = new Handler();


    private OnPlayerFragmentContinueInteractionListener mCallback;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     */
    // TODO: Rename and change types and number of parameters
    public static PlayerBoardFragment newInstance(Board board) {
        PlayerBoardFragment fragment = new PlayerBoardFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Board.BOARD_KEY, board);
        fragment.setArguments(bundle);
        return fragment;
    }

    public PlayerBoardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            board = getArguments().getParcelable(Board.BOARD_KEY);
        }

        else
            Log.d("TEST", "ARGS ARE NULL !!!!!!!");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.fragment_player_board, container, false);

        playerTurn = 1;

        displayPlayerBoard(fragmentView);
        return fragmentView;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnPlayerFragmentContinueInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentContinueInteractionListener");
        }

    }

        @Override
        public void onDetach () {
            super.onDetach();
        }



 /*
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     *
     *
*/
    public interface OnPlayerFragmentContinueInteractionListener {
        // TODO: Update argument type and name
        public void onPlayerFragmentContinueInteraction();
    }



    protected void displayPlayerBoard(View view){

        playerBoardGrid = (GridView) view.findViewById(R.id.setup_gridview);
        playerImageAdapter = new PlayerGridImageAdapter(view.getContext(), this.board, this.playerTurn);
        playerBoardGrid.setAdapter(playerImageAdapter);

    }


    public void fire(int position, int result) {

        this.board.setElementAtBoardPosition(position, result);
        playerImageAdapter.swapBoards(this.board);


    }


}
