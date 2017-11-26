package com.stephen.amaze.ViewControllers;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.stephen.amaze.ExtAdapter.GridAdapter;
import com.stephen.amaze.ExtViews.NonScrollableGridView;
import com.stephen.amaze.Models.Maze;
import com.stephen.amaze.Models.MazeItem;
import com.stephen.amaze.Models.MazeSquare;
import com.stephen.amaze.R;

import java.util.ArrayList;
import java.util.Arrays;

import static com.stephen.amaze.Models.MazeSquare.DOWN_LEFT;
import static com.stephen.amaze.Models.MazeSquare.DOWN_RIGHT;
import static com.stephen.amaze.Models.MazeSquare.END;
import static com.stephen.amaze.Models.MazeSquare.HORZ;
import static com.stephen.amaze.Models.MazeSquare.NO_PATH;
import static com.stephen.amaze.Models.MazeSquare.PATH;
import static com.stephen.amaze.Models.MazeSquare.POSSIBLE_PATH;
import static com.stephen.amaze.Models.MazeSquare.START;
import static com.stephen.amaze.Models.MazeSquare.TRAVERSABLE_PASSAGE_WAY;
import static com.stephen.amaze.Models.MazeSquare.UP_LEFT;
import static com.stephen.amaze.Models.MazeSquare.UP_RIGHT;
import static com.stephen.amaze.Models.MazeSquare.VERT;
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

    private ArrayList<MazeSquare> squares;

    private GridAdapter adapter;

    private Maze maze;

    private MazeItem mazeItem;

    private int pickups = 0, walls = 0;

    private TextView pickupsText, wallsText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_grid, container, false);

        NonScrollableGridView gridView = (NonScrollableGridView) v.findViewById(R.id.grid_maze);
        ImageButton refreshButton = (ImageButton) v.findViewById(R.id.grid_refreshbutton);

        pickupsText = (TextView) v.findViewById(R.id.grid_pickups);
        wallsText = (TextView) v.findViewById(R.id.grid_walls);

        maze = new Maze();
        mazeItem = maze.getMazeItem();
        squares = mazeItem.getSquares();

        plotPath(mazeItem.getStart(), 0);
        pickupsText.setText(Integer.toString(pickups) + "/" + maze.getNumberOfPickups());
        wallsText.setText(Integer.toString(walls));

        adapter = new GridAdapter(getActivity(), squares);
        gridView.setAdapter(adapter);

        gridView.setExpanded(true);

        gridView.setNumColumns(mazeItem.getWidth());

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refresh();
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapter.getItem(i).getValue() != END
                        && adapter.getItem(i).getValue() != START
                        && adapter.getItem(i).getValue() != WALL) {
                    adapter.getItem(i).setValue(WALL);

                    walls++;
                    wallsText.setText(Integer.toString(walls));

                    for (MazeSquare s : squares)
                        if (s.getValue() == PATH) s.setValue(TRAVERSABLE_PASSAGE_WAY);

                    pickups = 0;
                    plotPath(mazeItem.getStart(), 0);
                    pickupsText.setText(Integer.toString(pickups) + "/" + maze.getNumberOfPickups());

                    adapter.notifyDataSetChanged();
                } else if (adapter.getItem(i).getValue() != END
                        && adapter.getItem(i).getValue() != START
                        && adapter.getItem(i).getValue() != TRAVERSABLE_PASSAGE_WAY) {
                    adapter.getItem(i).setValue(TRAVERSABLE_PASSAGE_WAY);

                    walls--;
                    wallsText.setText(Integer.toString(walls));

                    for (MazeSquare s : squares)
                        if (s.getValue() == PATH) s.setValue(TRAVERSABLE_PASSAGE_WAY);

                    pickups = 0;
                    plotPath(mazeItem.getStart(), 0);
                    pickupsText.setText(Integer.toString(pickups) + "/" + maze.getNumberOfPickups());

                    adapter.notifyDataSetChanged();
                }
            }
        });

        /*gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {


                return true;
            }
        });*/

        return v;
    }

    private void incrementPickups(int y, int x) {

        int index = MazeItem.getMazeSquareIndexWithCoord(y, x, mazeItem.getWidth());

        if (squares.get(index).isPickup()) pickups++;
    }

    private static final int NORTH = 1;
    private static final int SOUTH = 2;
    private static final int EAST = 3;
    private static final int WEST = 4;

    private boolean plotPath(int[] current, int previous) {

        Log.d("DEBUG", current[1] + " " + current[0]);

        if (Arrays.equals(mazeItem.getEnd(), current)) {
            mazeItem.setSquareValue(current[0], current[1], END);

            // Set the direction of the path
            updatePathDirection(current, previous, NO_PATH);

            incrementPickups(current[0], current[1]);
            Log.d("DEBUG", current[1] + " " + current[0]);
            return true;

        } else {

            if (mazeItem.getStart()[0] > mazeItem.getEnd()[0]) {
                if (checkNorth(current, previous)) return true;
                if (checkSouth(current, previous)) return true;

            } else {
                if (checkSouth(current, previous)) return true;
                if (checkNorth(current, previous)) return true;
            }

            if (mazeItem.getStart()[1] < mazeItem.getEnd()[1]) {
                if (checkEast(current, previous)) return true;
                if (checkWest(current, previous)) return true;

            } else {
                if (checkWest(current, previous)) return true;
                if (checkEast(current, previous)) return true;
            }

            return false;
        }
    }

    private void updatePathDirection(int[] current, int path_direction1, int path_direction2) {
        int direction = NO_PATH;

        if (path_direction1 == NORTH) {
            switch (path_direction2) {
                case NORTH:
                    direction = VERT;
                    break;
                case EAST:
                    direction = UP_RIGHT;
                    break;
                case WEST:
                    direction = UP_LEFT;
                    break;
                default:
                    direction = NO_PATH;
                    break;
            }

        } else if (path_direction1 == SOUTH) {
            switch (path_direction2) {
                case SOUTH:
                    direction = VERT;
                    break;
                case EAST:
                    direction = DOWN_RIGHT;
                    break;
                case WEST:
                    direction = DOWN_LEFT;
                    break;
                default:
                    direction = NO_PATH;
                    break;
            }

        } else if (path_direction1 == EAST) {
            switch (path_direction2) {
                case NORTH:
                    direction = DOWN_LEFT;
                    break;
                case EAST:
                    direction = HORZ;
                    break;
                case SOUTH:
                    direction = UP_LEFT;
                    break;
                default:
                    direction = NO_PATH;
                    break;
            }

        } else if (path_direction1 == WEST) {
            switch (path_direction2) {
                case NORTH:
                    direction = DOWN_RIGHT;
                    break;
                case SOUTH:
                    direction = UP_RIGHT;
                    break;
                case WEST:
                    direction = HORZ;
                    break;
                default:
                    direction = NO_PATH;
                    break;
            }

        }

        mazeItem.setSquaresPathDirection(current[0], current[1], direction);
    }

    private boolean checkNorth(int[] current, int previous) {
        // If not at the top edge, and did not just move South...
        if (current[0] - 1 >= 0 && previous != SOUTH) {

            // Check North for Traversable Passage Way. If passage way is traversable...
            if (mazeItem.getSquareValue(current[0] - 1, current[1]) != WALL
                    && mazeItem.getSquareValue(current[0] - 1, current[1]) != POSSIBLE_PATH
                    && mazeItem.getSquareValue(current[0] - 1, current[1]) != START) {
                int y = current[0] - 1;

                // Set Square Value to POSSIBLE_PATH
                mazeItem.setSquareValue(current[0], current[1], POSSIBLE_PATH);

                // Recursively call "plotPath" to return true if passage reaches the "end" coordinates
                if (plotPath(new int[]{y, current[1]}, NORTH)) {
                    int value = Arrays.equals(current, mazeItem.getStart()) ? START : PATH;
                    mazeItem.setSquareValue(current[0], current[1], value);

                    // Set the direction of the path
                    updatePathDirection(current, previous, NORTH);

                    incrementPickups(current[0], current[1]);
                    Log.d("DEBUG", current[1] + " " + current[0]);
                    return true;

                } else {
                    mazeItem.setSquareValue(current[0], current[1], TRAVERSABLE_PASSAGE_WAY);
                    updatePathDirection(current, NO_PATH, NO_PATH);
                }
            }
        }

        return false;
    }

    private boolean checkSouth(int[] current, int previous) {
        if (current[0] + 1 < mazeItem.getHeight() && previous != NORTH) {

            // Check South for Traversable Passage Way. If passage way is traversable...
            if (mazeItem.getSquareValue(current[0] + 1, current[1]) != WALL
                    && mazeItem.getSquareValue(current[0] + 1, current[1]) != POSSIBLE_PATH
                    && mazeItem.getSquareValue(current[0] + 1, current[1]) != START) {
                int y = current[0] + 1;

                // Set Square Value to POSSIBLE_PATH
                mazeItem.setSquareValue(current[0], current[1], POSSIBLE_PATH);

                // Recursively call "plotPath" to return true if passage reaches the "end" coordinates
                if (plotPath(new int[]{y, current[1]}, SOUTH)) {
                    int value = Arrays.equals(current, mazeItem.getStart()) ? START : PATH;
                    mazeItem.setSquareValue(current[0], current[1], value);

                    // Set the direction of the path
                    updatePathDirection(current, previous, SOUTH);

                    incrementPickups(current[0], current[1]);
                    Log.d("DEBUG", current[1] + " " + current[0]);
                    return true;

                } else {
                    mazeItem.setSquareValue(current[0], current[1], TRAVERSABLE_PASSAGE_WAY);
                    updatePathDirection(current, NO_PATH, NO_PATH);
                }
            }
        }

        return false;
    }

    private boolean checkEast(int[] current, int previous) {
        // If not at the right edge, and did not just move West...
        if (current[1] + 1 < mazeItem.getWidth() && previous != WEST) {

            // Check East for Traversable Passage Way. If passage way is traversable...
            if (mazeItem.getSquareValue(current[0], current[1] + 1) != WALL
                    && mazeItem.getSquareValue(current[0], current[1] + 1) != POSSIBLE_PATH
                    && mazeItem.getSquareValue(current[0], current[1] + 1) != START) {
                int x = current[1] + 1;

                // Set Square Value to POSSIBLE_PATH
                mazeItem.setSquareValue(current[0], current[1], POSSIBLE_PATH);

                // Recursively call "plotPath" to return true if passage reaches the "end" coordinates
                if (plotPath(new int[]{current[0], x}, EAST)) {
                    int value = Arrays.equals(current, mazeItem.getStart()) ? START : PATH;
                    mazeItem.setSquareValue(current[0], current[1], value);

                    // Set the direction of the path
                    updatePathDirection(current, previous, EAST);

                    incrementPickups(current[0], current[1]);
                    Log.d("DEBUG", current[1] + " " + current[0]);
                    return true;

                } else {
                    mazeItem.setSquareValue(current[0], current[1], TRAVERSABLE_PASSAGE_WAY);
                    updatePathDirection(current, NO_PATH, NO_PATH);
                }
            }
        }

        return false;
    }

    private boolean checkWest(int[] current, int previous) {
        // If not at the left edge, and did not just move East...
        if (current[1] - 1 >= 0 && previous != EAST) {

            // Check West for Traversable Passage Way. If passage way is traversable...
            if (mazeItem.getSquareValue(current[0], current[1] - 1) != WALL
                    && mazeItem.getSquareValue(current[0], current[1] - 1) != POSSIBLE_PATH
                    && mazeItem.getSquareValue(current[0], current[1] - 1) != START) {
                int x = current[1] - 1;

                // Set Square Value to POSSIBLE_PATH
                mazeItem.setSquareValue(current[0], current[1], POSSIBLE_PATH);

                // Recursively call "plotPath" to return true if passage reaches the "end" coordinates
                if (plotPath(new int[]{current[0], x}, WEST)) {
                    int value = Arrays.equals(current, mazeItem.getStart()) ? START : PATH;
                    mazeItem.setSquareValue(current[0], current[1], value);

                    // Set the direction of the path
                    updatePathDirection(current, previous, WEST);

                    incrementPickups(current[0], current[1]);
                    Log.d("DEBUG", current[1] + " " + current[0]);
                    return true;

                } else {
                    // No path so remove Direction and set as TRAVERSABLE_PASSAGE_WAY
                    mazeItem.setSquareValue(current[0], current[1], TRAVERSABLE_PASSAGE_WAY);
                    updatePathDirection(current, NO_PATH, NO_PATH);
                }
            }
        }

        return false;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void refresh() {
        if (mListener != null) {
            mListener.onRefresh();
        }
    }

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
        void onRefresh();
    }
}
