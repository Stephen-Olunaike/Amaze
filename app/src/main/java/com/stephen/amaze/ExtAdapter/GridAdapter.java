package com.stephen.amaze.ExtAdapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;

import com.stephen.amaze.Models.MazeSquare;
import com.stephen.amaze.R;

import java.util.ArrayList;

/**
 * Created by stephen on 24/11/2017.
 */

public class GridAdapter extends ArrayAdapter<MazeSquare> {

    private ArrayList<MazeSquare> squares;
    private Activity activity;

    public GridAdapter(@NonNull Activity activity, ArrayList<MazeSquare> squares) {
        super(activity, 0, squares);

        this.activity = activity;
        this.squares = squares;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //return super.getView(position, convertView, parent);

        SquareViewHolder squareViewHolder;

        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(
                    R.layout.itemof_grid, parent, false);

            squareViewHolder = new SquareViewHolder();
            squareViewHolder.background = (FrameLayout) convertView.findViewById(R.id.grid_background);

            convertView.setTag(squareViewHolder);

        } else {
            squareViewHolder = (SquareViewHolder) convertView.getTag();
        }

        MazeSquare mazeSquare = squares.get(position);

        if (squareViewHolder != null) {

            int color;

            switch (mazeSquare.getValue()) {

                case MazeSquare.WALL :
                    color = activity.getColor(R.color.black);
                    break;

                case MazeSquare.START :
                    color = activity.getColor(R.color.colorPrimaryDark);
                    break;

                case MazeSquare.END :
                    color = activity.getColor(R.color.colorPrimaryDark);
                    break;

                case MazeSquare.PATH :
                    color = activity.getColor(R.color.colorPrimary);
                    break;

                default:
                    color = activity.getColor(R.color.white);
                    break;
            }

            squareViewHolder.background.setBackgroundColor(color);
        }

        return convertView;
    }

    static class SquareViewHolder {
        FrameLayout background;
    }
}
