package com.stephen.amaze.ExtAdapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
            squareViewHolder.outerColor = (RelativeLayout) convertView.findViewById(R.id.square_outercolor);

            squareViewHolder.directionView = (ImageView) convertView.findViewById(R.id.square_direction);
            squareViewHolder.valueView = (ImageView) convertView.findViewById(R.id.square_value);

            convertView.setTag(squareViewHolder);

        } else {
            squareViewHolder = (SquareViewHolder) convertView.getTag();
        }

        MazeSquare mazeSquare = squares.get(position);

        if (squareViewHolder != null) {

            int outer_color = activity.getColor(R.color.white);
            Drawable image = null, valueImage = null;

            switch (mazeSquare.getValue()) {

                case MazeSquare.WALL :
                    outer_color = activity.getColor(R.color.black);
                    break;

                case MazeSquare.START :
                    image = activity.getDrawable(R.drawable.cross);
                    valueImage = activity.getDrawable(R.drawable.point);
                    break;

                case MazeSquare.END :
                    image = activity.getDrawable(R.drawable.cross);
                    valueImage = activity.getDrawable(R.drawable.point);
                    break;

                case MazeSquare.PATH :
                    image = activity.getDrawable(R.drawable.cross);
                    break;

                default:
                    outer_color = activity.getColor(R.color.white);
                    image = null;
                    valueImage = null;
                    break;
            }

            squareViewHolder.directionView.setImageResource(0);
            if (image != null) squareViewHolder.directionView.setImageDrawable(image);

            squareViewHolder.valueView.setImageResource(0);
            if (valueImage != null) squareViewHolder.valueView.setImageDrawable(valueImage);

            squareViewHolder.outerColor.setBackgroundColor(outer_color);

            if (mazeSquare.isPickup()) {
                squareViewHolder.outerColor.setBackgroundColor(activity.getColor(R.color.colorPrimary));
            }
        }

        return convertView;
    }

    static class SquareViewHolder {
        RelativeLayout outerColor;
        ImageView directionView, valueView;
    }
}
