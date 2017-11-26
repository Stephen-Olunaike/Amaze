package com.stephen.amaze.Models;

import android.graphics.drawable.Drawable;

public class MazeSquare {

    public static final int TRAVERSABLE_PASSAGE_WAY = 0;
    public static final int WALL = 1;
    public static final int START = 2;
    public static final int END = 3;
    public static final int PATH = 4;
    public static final int POSSIBLE_PATH = 5;

    public static final int NO_PATH = 0;
    /*public static final int VERT = 1;
    public static final int HORZ = 2;
    public static final int UP_RIGHT = 3;
    public static final int UP_LEFT  = 4;
    public static final int DOWN_RIGHT = 5;
    public static final int DOWN_LEFT = 6;*/

    private int index;
    private int value;
    private boolean pickup;

    private int fromDirection, toDirection;

    private Drawable directionDrawable;

    public MazeSquare(int index, int value) {
        this.index = index;
        this.value = value;
        this.pickup = false;
        this.fromDirection = NO_PATH;
        this.toDirection = NO_PATH;
        this.directionDrawable = null;
    }

    public int getIndex() {
        return index;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isPickup() {
        return pickup;
    }

    void setPickup(boolean pickup) {
        this.pickup = pickup;
    }

    public int getFromDirection() {
        return fromDirection;
    }

    public int getToDirection() {
        return toDirection;
    }

    public void setFromDirection(int fromDirection) {
        this.fromDirection = fromDirection;
    }

    public void setToDirection(int toDirection) {
        this.toDirection = toDirection;
    }

    public Drawable getDirectionDrawable() {
        return directionDrawable;
    }

    public void setDirectionDrawable(Drawable directionDrawable) {
        this.directionDrawable = directionDrawable;
    }
}
