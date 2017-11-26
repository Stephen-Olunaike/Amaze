package com.stephen.amaze.Models;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;

public class MazeItem {

    private int width;
    private int height;
    private ArrayList<MazeSquare> squares;
    private int[] start;
    private int[] end;

    public MazeItem(int width, int height, ArrayList<MazeSquare> squares,
                    int[] start, int[] end) {
        this.width = width;
        this.height = height;
        this.squares = squares;
        this.start = start;
        this.end = end;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getSquareValue(int y, int x) {
        return squares.get( MazeItem.getMazeSquareIndexWithCoord(y, x, width) ).getValue();
    }

    public void setSquareValue(int y, int x, int value) {
        squares.get( getMazeSquareIndexWithCoord(y, x, width) ).setValue(value);
    }

    public static int getMazeSquareIndexWithCoord(int y, int x, int maze_width) {
        if (y == 0) return x;

        return (maze_width*y) + x;
    }

    public int[] getStart() {
        return start;
    }

    public int[] getEnd() {
        return end;
    }

    public ArrayList<MazeSquare> getSquares() {
        return squares;
    }

    public boolean setIndexAsPickUp(int index) {
        if (squares.get(index).isPickup()) return false;

        squares.get(index).setPickup(true);
        return true;
    }

    public void setSquaresFromDirection(int y, int x, int direction) {
        squares.get( getMazeSquareIndexWithCoord(y, x, width) ).setFromDirection(direction);
    }
    public void setSquaresToDirection(int y, int x, int direction) {
        squares.get( getMazeSquareIndexWithCoord(y, x, width) ).setToDirection(direction);
    }
    public void setSquaresPathDirection(int i, Drawable direction) {
        squares.get(i).setDirectionDrawable(direction);
    }

    public void setSquaresFromDirection(int i, int direction) {
        squares.get(i).setFromDirection(direction);
    }
    public void setSquaresToDirection(int i, int direction) {
        squares.get(i).setToDirection(direction);
    }
}
