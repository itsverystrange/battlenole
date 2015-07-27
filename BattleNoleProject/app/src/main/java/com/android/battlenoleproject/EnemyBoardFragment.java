package com.android.battlenoleproject;

import android.app.Activity;
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

import java.util.ArrayList;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EnemyBoardFragment.OnFragmentFireInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EnemyBoardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EnemyBoardFragment extends Fragment {

    GridView enemyBoardGrid;
    EnemyGridImageAdapter enemyImageAdapter;

    private final static int FIRE_MISS = 61;
    private final static int FIRE_HIT = 62;

    private final static int ENEMY_PLAYER_NUMBER = 1;
    private final static int PLAYER_PLAYER_NUMBER = 0;

    private Handler myHandler = new Handler();

    private static final Random r = new Random();

    public Game receivedGame;

    private Board board;

    private int playerTurn = PLAYER_PLAYER_NUMBER;

    OnFragmentFireInteractionListener mHandler;
    private OnEnemyFragmentContinueInteractionListener mCallback;

    //private OnFragmentFireInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    // TODO: Rename and change types and number of parameters
    public static EnemyBoardFragment newInstance(Board board) {
        EnemyBoardFragment fragment = new EnemyBoardFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Board.BOARD_KEY, board);
        fragment.setArguments(bundle);
        return fragment;
    }

    public EnemyBoardFragment() {
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

        View fragmentView = inflater.inflate(R.layout.fragment_enemy_board, container, false);


        playerTurn = 0;

        displayEnemyBoard(fragmentView);

        attachActionListeners();

        return fragmentView;





    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mHandler = (OnFragmentFireInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }

        try {
            mCallback = (OnEnemyFragmentContinueInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentContinueInteractionListener");
        }


    }

    @Override
    public void onDetach() {
        super.onDetach();
        mHandler = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentFireInteractionListener {
        // TODO: Update argument type and name
        void onFragmentFireInteraction(int position);
    }

    public interface OnEnemyFragmentContinueInteractionListener {
        // TODO: Update argument type and name
        void onEnemyFragmentContinueInteraction();
    }



    protected void displayEnemyBoard(View view){

        enemyBoardGrid = (GridView) view.findViewById(R.id.setup_gridview);
        enemyImageAdapter = new EnemyGridImageAdapter(view.getContext(), this.board, this.playerTurn);  // display grid without ships, just misses and hits
        enemyBoardGrid.setAdapter(enemyImageAdapter);



    }

    private void attachActionListeners(){



        enemyBoardGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                ImageView iv = (ImageView) v;

                enemyBoardGrid.setEnabled(false);
                mHandler.onFragmentFireInteraction(position);
                enemyBoardGrid.setEnabled(true);

            }
        });


    }



    public void fire(int position, int result) {

        board.setElementAtBoardPosition(position, result);
        enemyImageAdapter.swapBoards(board);
        enemyBoardGrid.setEnabled(false);


    }





}
