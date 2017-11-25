package com.stephen.amaze.ExtAdapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

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
            squareViewHolder.outerColor = (RelativeLayout) convertView.findViewById(R.id.grid_outercolor);
            squareViewHolder.innerColor = (RelativeLayout) convertView.findViewById(R.id.grid_innercolor);

            convertView.setTag(squareViewHolder);

        } else {
            squareViewHolder = (SquareViewHolder) convertView.getTag();
        }

        MazeSquare mazeSquare = squares.get(position);

        if (squareViewHolder != null) {

            int outer_color, inner_color;

            switch (mazeSquare.getValue()) {

                case MazeSquare.WALL :
                    outer_color = activity.getColor(R.color.black);
                    inner_color = activity.getColor(R.color.black);
                    break;

                case MazeSquare.START :
                    outer_color = activity.getColor(R.color.white);
                    inner_color = activity.getColor(R.color.colorPrimaryDark);
                    break;

                case MazeSquare.END :
                    outer_color = activity.getColor(R.color.white);
                    inner_color = activity.getColor(R.color.colorPrimaryDark);
                    break;

                case MazeSquare.PATH :
                    outer_color = activity.getColor(R.color.white);
                    inner_color = activity.getColor(R.color.colorAccent);
                    break;

                default:
                    outer_color = activity.getColor(R.color.white);
                    inner_color = activity.getColor(R.color.white);
                    break;
            }

            squareViewHolder.outerColor.setBackgroundColor(outer_color);
            squareViewHolder.innerColor.setBackgroundColor(inner_color);
        }

        if (mazeSquare.isPickup()) {
            squareViewHolder.outerColor.setBackgroundColor(activity.getColor(R.color.colorPrimary));
        }

        return convertView;
    }

    static class SquareViewHolder {
        RelativeLayout outerColor, innerColor;
    }
}
