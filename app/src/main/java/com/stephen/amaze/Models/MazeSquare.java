package com.stephen.amaze.Models;

public class MazeSquare {

    public static final int TRAVERSABLE_PASSAGE_WAY = 0;
    public static final int WALL = 1;
    public static final int START = 2;
    public static final int END = 3;
    public static final int PATH = 4;
    public static final int POSSIBLE_PATH = 5;

    public static final int NO_PATH = 0;
    public static final int VERT = 1;
    public static final int HORZ = 2;
    public static final int UP_RIGHT = 3;
    public static final int UP_LEFT  = 4;
    public static final int DOWN_RIGHT = 5;
    public static final int DOWN_LEFT = 6;

    private int index;
    private int value;
    private boolean pickup;

    private int direction;

    public MazeSquare(int index, int value) {
        this.index = index;
        this.value = value;
        this.pickup = false;
        this.direction = NO_PATH;
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

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }
}
