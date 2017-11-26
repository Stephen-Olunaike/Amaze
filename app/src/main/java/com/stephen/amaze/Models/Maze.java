package com.stephen.amaze.Models;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Maze {

    private static final int HEIGHT = 10;
    private static final int WIDTH = 10;

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
        this.end = new int[] {HEIGHT-1, WIDTH-1};
    }

    public ArrayList<MazeSquare> generateMaze() {
        this.squares.clear();

        ArrayList<MazeSquare> temp = new ArrayList<>();

        //int[] start = {5,5};
        //int[] end = {2,7};

        this.start = new int[] {new Random().nextInt(HEIGHT), new Random().nextInt(WIDTH)};
        this.end = new int[] {new Random().nextInt(HEIGHT), new Random().nextInt(WIDTH)};


        while (Arrays.equals(start, end)) {
            int y = new Random().nextInt(HEIGHT);
            int x = new Random().nextInt(WIDTH);

            end = new int[] {y,x};
        }

        for (int i=0; i<100; i++) {

            int value;

            if (i == Maze.getMazeSquareIndexWithCoord(start[0], start[1], WIDTH)) {
                value = MazeSquare.START;

            } else if (i == Maze.getMazeSquareIndexWithCoord(end[0], end[1], WIDTH)) {
                value = MazeSquare.END;

            } else {
                value = MazeSquare.TRAVERSABLE_PASSAGE_WAY;
            }

            temp.add(new MazeSquare(i, value));
        }

        this.squares.addAll(temp);

        this.numberOfPickups = generatePickUps(HEIGHT);

        return squares;
    }

    private int generatePickUps(int maxcount) {

        int total_pickups = new Random().nextInt(maxcount) + 1;

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
