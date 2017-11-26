package com.stephen.amaze.Models;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Maze {

    private static final int HEIGHT = 5;
    private static final int WIDTH = 5;

    private int width;
    private int height;
    private ArrayList<MazeSquare> squares;
    private int[] start;
    private int[] end;

    private int numberOfPickups = 0;
    public int getNumberOfPickups() {
        return numberOfPickups;
    }

    public Maze() {
        this.width = WIDTH;
        this.height = HEIGHT;
        this.squares = new ArrayList<>();
        this.start = new int[] {0,0};
        this.end = new int[] {height-1, width-1};
    }

    public void generateMaze() {
        this.squares.clear();

        ArrayList<MazeSquare> temp = new ArrayList<>();

        //int[] start = {5,5};
        //int[] end = {2,7};

        this.start = new int[] {new Random().nextInt(height), new Random().nextInt(width)};
        this.end = new int[] {new Random().nextInt(height), new Random().nextInt(width)};

        int y_diff = end[0] - start[0];
        y_diff = (y_diff < 0) ? (-1 * y_diff) : y_diff;

        int x_diff = end[1] - start[1];
        x_diff = (x_diff < 0) ? (-1 * x_diff) : x_diff;

        while (Arrays.equals(start, end) || y_diff<2 || x_diff<2) {

            int y = new Random().nextInt(height);
            int x = new Random().nextInt(width);

            end = new int[] {y,x};

            y_diff = end[0] - start[0];
            y_diff = (y_diff < 0) ? (-1 * y_diff) : y_diff;

            x_diff = end[1] - start[1];
            x_diff = (x_diff < 0) ? (-1 * x_diff) : x_diff;
        }

        for (int i=0; i<width*height; i++) {

            int value;

            if (i == Maze.getMazeSquareIndexWithCoord(start[0], start[1], width)) {
                value = MazeSquare.START;

            } else if (i == Maze.getMazeSquareIndexWithCoord(end[0], end[1], width)) {
                value = MazeSquare.END;

            } else {
                value = MazeSquare.TRAVERSABLE_PASSAGE_WAY;
            }

            temp.add(new MazeSquare(i, value));
        }

        this.squares.addAll(temp);

        this.numberOfPickups = generatePickUps(height);
    }

    private int generatePickUps(int maxcount) {

        int total_pickups = 2;//new Random().nextInt(maxcount) + 1;

        int count = total_pickups;

        while (count > 0) {

            int pickup_index = new Random().nextInt(squares.size());

            if (setIndexAsPickUp(pickup_index)) count--;
        }

        return total_pickups;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getSquareValue(int y, int x) {
        return squares.get( Maze.getMazeSquareIndexWithCoord(y, x, width) ).getValue();
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
