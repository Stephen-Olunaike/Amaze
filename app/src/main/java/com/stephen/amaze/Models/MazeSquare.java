package com.stephen.amaze.Models;

public class MazeSquare {

    public static final int TRAVERSABLE_PASSAGE_WAY = 0;
    public static final int WALL = 1;
    public static final int START = 2;
    public static final int END = 3;
    public static final int PATH = 4;
    public static final int POSSIBLE_PATH = 5;

    private int index;
    private int value;

    public MazeSquare(int index, int value) {
        this.index = index;
        this.value = value;
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
}
