package com.stephen.amaze.ViewControllers;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.stephen.amaze.ExtAdapter.GridAdapter;
import com.stephen.amaze.ExtViews.NonScrollableGridView;
import com.stephen.amaze.Models.Maze;
import com.stephen.amaze.Models.MazeItem;
import com.stephen.amaze.Models.MazeSquare;
import com.stephen.amaze.R;

import java.util.ArrayList;
import java.util.Arrays;

import static com.stephen.amaze.Models.MazeSquare.END;
import static com.stephen.amaze.Models.MazeSquare.PATH;
import static com.stephen.amaze.Models.MazeSquare.START;
import static com.stephen.amaze.Models.MazeSquare.TRAVERSABLE_PASSAGE_WAY;

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

    private NonScrollableGridView gridView;

    private ArrayList<MazeSquare> squares;

    private GridAdapter adapter;

    private MazeItem mazeItem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_grid, container, false);

        // Generate empty grid view
        gridView = (NonScrollableGridView) v.findViewById(R.id.grid_maze);

        mazeItem = new Maze().getMazeItem();
        squares = mazeItem.getSquares();
        adapter = new GridAdapter(getActivity(), squares);
        gridView.setAdapter(adapter);

        gridView.setExpanded(true);

        gridView.setNumColumns(mazeItem.getWidth());

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (adapter.getItem(i).getValue() != END
                        && adapter.getItem(i).getValue() != START) {
                    adapter.getItem(i).setValue(MazeSquare.WALL);

                    for (MazeSquare s : squares)
                        if (s.getValue() == PATH) s.setValue(TRAVERSABLE_PASSAGE_WAY);


                    plotPath(mazeItem.getStart(), 0);

                    adapter.notifyDataSetChanged();
                }
            }
        });

        return v;
    }


    private static final int NORTH = 1;
    private static final int SOUTH = 2;
    private static final int EAST = 3;
    private static final int WEST = 4;

    boolean plotPath(int[] current, int previous) {

        Log.d("DEBUG", current[1] + " " + current[0] + " " + previous);

        if (Arrays.equals(mazeItem.getEnd(), current)) {
            mazeItem.setSquareValue(current[0], current[1], END);
            return true;

        } else {

            // If not at the top edge, and did not just move South...
            if (current[0]-1 >= 0 && previous != SOUTH) {

                // Check North for Traversable Passage Way. If passage way is traversable...
                if (mazeItem.getSquareValue(current[0]-1, current[1]) == TRAVERSABLE_PASSAGE_WAY) {
                    int y = current[0] - 1;

                    // Recursively call "plotPath" to return true if passage reaches the "end" coordinates
                    if (plotPath(new int[]{y, current[1]}, NORTH)) {
                        int value = Arrays.equals(current, mazeItem.getStart()) ? START : PATH;
                        mazeItem.setSquareValue(current[0], current[1], value);

                        return true;
                    }
                }
            }

            // If not at the bottom edge, and did not just move North...
            if (current[0]+1 < mazeItem.getHeight() && previous != NORTH) {

                // Check South for Traversable Passage Way. If passage way is traversable...
                if (mazeItem.getSquareValue(current[0]+1, current[1]) == TRAVERSABLE_PASSAGE_WAY) {
                    int y = current[0] + 1;

                    // Recursively call "plotPath" to return true if passage reaches the "end" coordinates
                    if (plotPath(new int[]{y, current[1]}, SOUTH)) {
                        int value = Arrays.equals(current, mazeItem.getStart()) ? START : PATH;
                        mazeItem.setSquareValue(current[0], current[1], value);

                        return true;
                    }
                }
            }

            // If not at the right edge, and did not just move West...
            if (current[1]+1 < mazeItem.getWidth() && previous != WEST) {

                // Check East for Traversable Passage Way. If passage way is traversable...
                if (mazeItem.getSquareValue(current[0], current[1]+1) == TRAVERSABLE_PASSAGE_WAY) {
                    int x = current[1] + 1;

                    // Recursively call "plotPath" to return true if passage reaches the "end" coordinates
                    if (plotPath(new int[]{current[0], x}, EAST)) {
                        int value = Arrays.equals(current, mazeItem.getStart()) ? START : PATH;
                        mazeItem.setSquareValue(current[0], current[1], value);

                        return true;
                    }
                }
            }

            // If not at the left edge, and did not just move East...
            if (current[1]-1 >= 0 && previous != EAST) {

                // Check West for Traversable Passage Way. If passage way is traversable...
                if (mazeItem.getSquareValue(current[0], current[1]-1) == TRAVERSABLE_PASSAGE_WAY) {
                    int x = current[1] - 1;

                    // Recursively call "plotPath" to return true if passage reaches the "end" coordinates
                    if (plotPath(new int[]{current[0], x}, WEST)) {
                        int value = Arrays.equals(current, mazeItem.getStart()) ? START : PATH;
                        mazeItem.setSquareValue(current[0], current[1], value);

                        return true;
                    }
                }
            }

            return false;
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    /*public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
        //void onFragmentInteraction(Uri uri);
    }
}
