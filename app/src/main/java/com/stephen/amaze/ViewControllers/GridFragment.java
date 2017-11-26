package com.stephen.amaze.ViewControllers;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.stephen.amaze.ExtAdapter.GridAdapter;
import com.stephen.amaze.ExtViews.NonScrollableGridView;
import com.stephen.amaze.Models.Maze;
import com.stephen.amaze.Models.MazeSquare;
import com.stephen.amaze.R;

import java.util.ArrayList;
import java.util.Arrays;

import static com.stephen.amaze.Models.MazeSquare.END;
import static com.stephen.amaze.Models.MazeSquare.NO_PATH;
import static com.stephen.amaze.Models.MazeSquare.PATH;
import static com.stephen.amaze.Models.MazeSquare.POSSIBLE_PATH;
import static com.stephen.amaze.Models.MazeSquare.START;
import static com.stephen.amaze.Models.MazeSquare.TRAVERSABLE_PASSAGE_WAY;
import static com.stephen.amaze.Models.MazeSquare.WALL;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GridFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GridFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GridFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public GridFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GridFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GridFragment newInstance(String param1, String param2) {
        GridFragment fragment = new GridFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    private GridAdapter adapter;

    private Maze maze;

    private int pickups, walls;

    private TextView pickupsText, wallsText;

    private FrameLayout progressBar;

    private ImageButton refreshButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_grid, container, false);

        NonScrollableGridView gridView = (NonScrollableGridView) v.findViewById(R.id.grid_maze);
        refreshButton = (ImageButton) v.findViewById(R.id.grid_refreshbutton);

        pickupsText = (TextView) v.findViewById(R.id.grid_pickups);
        wallsText = (TextView) v.findViewById(R.id.grid_walls);

        progressBar = (FrameLayout) v.findViewById(R.id.grid_progressbar);

        maze = new Maze();

        maze.generateMaze();

        //squares = maze.getSquares();

        pickups = 0; walls = 0;

        //progressBar.setVisibility(View.VISIBLE);
        new FindPathTask().execute(maze.getStart());

        pickupsText.setText(Integer.toString(pickups) + "/" + maze.getNumberOfPickups());
        calculatePathDirectionForEachSquare();

        wallsText.setText(Integer.toString(walls));

        adapter = new GridAdapter(getActivity(), maze.getSquares());
        gridView.setAdapter(adapter);

        gridView.setExpanded(true);

        gridView.setNumColumns(maze.getWidth());

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshButton.setClickable(false);

                maze.generateMaze();

                pickups = 0; walls = 0;
                wallsText.setText(Integer.toString(walls));

                new FindPathTask().execute(maze.getStart());

                //pickupsText.setText(Integer.toString(pickups) + "/" + maze.getNumberOfPickups());
                //calculatePathDirectionForEachSquare();


                adapter.notifyDataSetChanged();
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapter.getItem(i).getValue() != END
                        && adapter.getItem(i).getValue() != START
                        && adapter.getItem(i).getValue() != WALL) {

                    maze.getSquares().get(i).setValue(WALL);

                    walls++;
                    wallsText.setText(Integer.toString(walls));

                    pickups = 0;

                    new FindPathTask().execute(maze.getStart());
                    //clearSquares();
                    //plotPath(maze.getStart(), 0);

                    //pickupsText.setText(Integer.toString(pickups) + "/" + maze.getNumberOfPickups());
                    //calculatePathDirectionForEachSquare();


                } else if (adapter.getItem(i).getValue() != END
                        && adapter.getItem(i).getValue() != START
                        && adapter.getItem(i).getValue() != TRAVERSABLE_PASSAGE_WAY) {

                    maze.getSquares().get(i).setValue(TRAVERSABLE_PASSAGE_WAY);

                    walls--;
                    wallsText.setText(Integer.toString(walls));

                    pickups = 0;

                    new FindPathTask().execute(maze.getStart());
                    //clearSquares();
                    //plotPath(maze.getStart(), 0);

                    //pickupsText.setText(Integer.toString(pickups) + "/" + maze.getNumberOfPickups());
                    //calculatePathDirectionForEachSquare();
                }

                //adapter.notifyDataSetChanged();
            }
        });

        return v;
    }

    private void incrementPickups(int y, int x) {

        int index = Maze.getMazeSquareIndexWithCoord(y, x, maze.getWidth());

        if (maze.getSquares().get(index).isPickup()) pickups++;
    }

    private void clearSquares() {

        for (int i = 0; i< maze.getSquares().size(); i++) {

            if (maze.getSquares().get(i).getValue() == PATH)
                maze.getSquares().get(i).setValue(TRAVERSABLE_PASSAGE_WAY);

            maze.setSquaresToDirection(i, NO_PATH);
            maze.setSquaresFromDirection(i, NO_PATH);
            maze.setSquaresPathDirection(i, null);
        }
    }

    private void calculatePathDirectionForEachSquare() {

        for (int i = 0; i< maze.getSquares().size(); i++) {

            Drawable direction = null;

            int from = maze.getSquares().get(i).getFromDirection();
            int to = maze.getSquares().get(i).getToDirection();

            if (from == NORTH) {
                switch (to) {
                    case NORTH:
                        direction = getActivity().getDrawable(R.drawable.vert);
                        break;
                    case EAST:
                        direction = getActivity().getDrawable(R.drawable.up_right);
                        break;
                    case WEST:
                        direction = getActivity().getDrawable(R.drawable.up_left);
                        break;
                    case NO_PATH:
                        direction = getActivity().getDrawable(R.drawable.up);
                        break;
                    default:
                        direction = null;
                        break;
                }

            } else if (from == SOUTH) {
                switch (to) {
                    case SOUTH:
                        direction = getActivity().getDrawable(R.drawable.vert);
                        break;
                    case EAST:
                        direction = getActivity().getDrawable(R.drawable.down_right);
                        break;
                    case WEST:
                        direction = getActivity().getDrawable(R.drawable.down_left);
                        break;
                    case NO_PATH:
                        direction = getActivity().getDrawable(R.drawable.down);
                        break;
                    default:
                        direction = null;
                        break;
                }

            } else if (from == EAST) {
                switch (to) {
                    case NORTH:
                        direction = getActivity().getDrawable(R.drawable.down_left);
                        break;
                    case EAST:
                        direction = getActivity().getDrawable(R.drawable.horz);
                        break;
                    case SOUTH:
                        direction = getActivity().getDrawable(R.drawable.up_left);
                        break;
                    case NO_PATH:
                        direction = getActivity().getDrawable(R.drawable.right);
                        break;
                    default:
                        direction = null;
                        break;
                }

            } else if (from == WEST) {
                switch (to) {
                    case NORTH:
                        direction = getActivity().getDrawable(R.drawable.down_right);
                        break;
                    case SOUTH:
                        direction = getActivity().getDrawable(R.drawable.up_right);
                        break;
                    case WEST:
                        direction = getActivity().getDrawable(R.drawable.horz);
                        break;
                    case NO_PATH:
                        direction = getActivity().getDrawable(R.drawable.left);
                        break;
                    default:
                        direction = null;
                        break;
                }

            } else if (from == NO_PATH) {
                switch (to) {
                    case NORTH:
                        direction = getActivity().getDrawable(R.drawable.down);
                        break;
                    case SOUTH:
                        direction = getActivity().getDrawable(R.drawable.up);
                        break;
                    case EAST:
                        direction = getActivity().getDrawable(R.drawable.left);
                        break;
                    case WEST:
                        direction = getActivity().getDrawable(R.drawable.right);
                        break;
                    default:
                        direction = null;
                        break;
                }

            }

            maze.setSquaresPathDirection(i, direction);
        }
    }

    private static final int NORTH = 1;
    private static final int SOUTH = 2;
    private static final int EAST = 3;
    private static final int WEST = 4;

    private boolean plotPath(int[] current, int previous) {

        if (Arrays.equals(maze.getEnd(), current)) {
            maze.setSquareValue(current[0], current[1], END);

            // Set the direction of the path
            maze.setSquaresFromDirection(current[0], current[1], previous);
            maze.setSquaresToDirection(current[0], current[1], NO_PATH);

            incrementPickups(current[0], current[1]);
            return true;

        } else {

            int y_diff = maze.getEnd()[0] - current[0];
            y_diff = (y_diff < 0) ? (-1 * y_diff) : y_diff;

            int x_diff = maze.getEnd()[1] - current[1];
            x_diff = (x_diff < 0) ? (-1 * x_diff) : x_diff;


            if (y_diff > x_diff) {

                if (current[0] > maze.getEnd()[0]) {
                    if (checkNorth(current, previous)) return true;
                    if (checkSouth(current, previous)) return true;

                } else {
                    if (checkSouth(current, previous)) return true;
                    if (checkNorth(current, previous)) return true;
                }

                if (current[1] < maze.getEnd()[1]) {
                    if (checkEast(current, previous)) return true;
                    if (checkWest(current, previous)) return true;

                } else {
                    if (checkWest(current, previous)) return true;
                    if (checkEast(current, previous)) return true;
                }

            } else {

                if (current[1] < maze.getEnd()[1]) {
                    if (checkEast(current, previous)) return true;
                    if (checkWest(current, previous)) return true;

                } else {
                    if (checkWest(current, previous)) return true;
                    if (checkEast(current, previous)) return true;
                }

                if (current[0] > maze.getEnd()[0]) {
                    if (checkNorth(current, previous)) return true;
                    if (checkSouth(current, previous)) return true;

                } else {
                    if (checkSouth(current, previous)) return true;
                    if (checkNorth(current, previous)) return true;
                }
            }

            return false;
        }
    }

    private boolean checkNorth(int[] current, int previous) {
        // If not at the top edge, and did not just move South...
        if (current[0] - 1 >= 0 && previous != SOUTH) {

            // Check North for Traversable Passage Way. If passage way is traversable...
            if (maze.getSquareValue(current[0] - 1, current[1]) != WALL
                    && maze.getSquareValue(current[0] - 1, current[1]) != POSSIBLE_PATH
                    && maze.getSquareValue(current[0] - 1, current[1]) != START) {
                int y = current[0] - 1;

                // Set Square Value to POSSIBLE_PATH
                maze.setSquareValue(current[0], current[1], POSSIBLE_PATH);

                // Recursively call "plotPath" to return true if passage reaches the "end" coordinates
                if (plotPath(new int[]{y, current[1]}, NORTH)) {
                    int value = Arrays.equals(current, maze.getStart()) ? START : PATH;
                    maze.setSquareValue(current[0], current[1], value);

                    // Set the direction of the path
                    maze.setSquaresFromDirection(current[0], current[1], previous);
                    maze.setSquaresToDirection(current[0], current[1], NORTH);

                    incrementPickups(current[0], current[1]);
                    return true;

                } else {
                    maze.setSquareValue(current[0], current[1], TRAVERSABLE_PASSAGE_WAY);
                    maze.setSquaresFromDirection(current[0], current[1], NO_PATH);
                    maze.setSquaresToDirection(current[0], current[1], NO_PATH);
                }
            }
        }

        return false;
    }

    private boolean checkSouth(int[] current, int previous) {
        if (current[0] + 1 < maze.getHeight() && previous != NORTH) {

            // Check South for Traversable Passage Way. If passage way is traversable...
            if (maze.getSquareValue(current[0] + 1, current[1]) != WALL
                    && maze.getSquareValue(current[0] + 1, current[1]) != POSSIBLE_PATH
                    && maze.getSquareValue(current[0] + 1, current[1]) != START) {
                int y = current[0] + 1;

                // Set Square Value to POSSIBLE_PATH
                maze.setSquareValue(current[0], current[1], POSSIBLE_PATH);

                // Recursively call "plotPath" to return true if passage reaches the "end" coordinates
                if (plotPath(new int[]{y, current[1]}, SOUTH)) {
                    int value = Arrays.equals(current, maze.getStart()) ? START : PATH;
                    maze.setSquareValue(current[0], current[1], value);

                    // Set the direction of the path
                    maze.setSquaresFromDirection(current[0], current[1], previous);
                    maze.setSquaresToDirection(current[0], current[1], SOUTH);

                    incrementPickups(current[0], current[1]);
                    return true;

                } else {
                    maze.setSquareValue(current[0], current[1], TRAVERSABLE_PASSAGE_WAY);
                    maze.setSquaresFromDirection(current[0], current[1], NO_PATH);
                    maze.setSquaresToDirection(current[0], current[1], NO_PATH);
                }
            }
        }

        return false;
    }

    private boolean checkEast(int[] current, int previous) {
        // If not at the right edge, and did not just move West...
        if (current[1] + 1 < maze.getWidth() && previous != WEST) {

            // Check East for Traversable Passage Way. If passage way is traversable...
            if (maze.getSquareValue(current[0], current[1] + 1) != WALL
                    && maze.getSquareValue(current[0], current[1] + 1) != POSSIBLE_PATH
                    && maze.getSquareValue(current[0], current[1] + 1) != START) {
                int x = current[1] + 1;

                // Set Square Value to POSSIBLE_PATH
                maze.setSquareValue(current[0], current[1], POSSIBLE_PATH);

                // Recursively call "plotPath" to return true if passage reaches the "end" coordinates
                if (plotPath(new int[]{current[0], x}, EAST)) {
                    int value = Arrays.equals(current, maze.getStart()) ? START : PATH;
                    maze.setSquareValue(current[0], current[1], value);

                    // Set the direction of the path
                    maze.setSquaresFromDirection(current[0], current[1], previous);
                    maze.setSquaresToDirection(current[0], current[1], EAST);

                    incrementPickups(current[0], current[1]);
                    return true;

                } else {
                    maze.setSquareValue(current[0], current[1], TRAVERSABLE_PASSAGE_WAY);
                    maze.setSquaresFromDirection(current[0], current[1], NO_PATH);
                    maze.setSquaresToDirection(current[0], current[1], NO_PATH);
                }
            }
        }

        return false;
    }

    private boolean checkWest(int[] current, int previous) {
        // If not at the left edge, and did not just move East...
        if (current[1] - 1 >= 0 && previous != EAST) {

            // Check West for Traversable Passage Way. If passage way is traversable...
            if (maze.getSquareValue(current[0], current[1] - 1) != WALL
                    && maze.getSquareValue(current[0], current[1] - 1) != POSSIBLE_PATH
                    && maze.getSquareValue(current[0], current[1] - 1) != START) {
                int x = current[1] - 1;

                // Set Square Value to POSSIBLE_PATH
                maze.setSquareValue(current[0], current[1], POSSIBLE_PATH);

                // Recursively call "plotPath" to return true if passage reaches the "end" coordinates
                if (plotPath(new int[]{current[0], x}, WEST)) {
                    int value = Arrays.equals(current, maze.getStart()) ? START : PATH;
                    maze.setSquareValue(current[0], current[1], value);

                    // Set the direction of the path
                    maze.setSquaresFromDirection(current[0], current[1], previous);
                    maze.setSquaresToDirection(current[0], current[1], WEST);

                    incrementPickups(current[0], current[1]);
                    return true;

                } else {
                    // No path so remove Direction and set as TRAVERSABLE_PASSAGE_WAY
                    maze.setSquareValue(current[0], current[1], TRAVERSABLE_PASSAGE_WAY);
                    maze.setSquaresFromDirection(current[0], current[1], NO_PATH);
                    maze.setSquaresToDirection(current[0], current[1], NO_PATH);
                }
            }
        }

        return false;
    }

    private class FindPathTask extends AsyncTask<int[], Integer, Boolean> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(int[]... ints) {

            clearSquares();

            boolean solved = plotPath(ints[0], NO_PATH);

            if (solved) {
                calculatePathDirectionForEachSquare();
            }

            return solved;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            pickupsText.setText(Integer.toString(pickups) + "/" + maze.getNumberOfPickups());
            progressBar.setVisibility(View.INVISIBLE);
            adapter.notifyDataSetChanged();
            refreshButton.setClickable(true);
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    /*public void refresh() {
        if (mListener != null) {
            mListener.onRefresh();
        }
    }*/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
    }
}
