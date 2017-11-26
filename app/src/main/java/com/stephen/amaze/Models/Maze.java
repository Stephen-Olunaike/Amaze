package com.stephen.amaze.Models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by stephen on 24/11/2017.
 */

public class Maze {
    
    private static final int HEIGHT = 10;
    private static final int WIDTH = 10;

    private MazeItem mazeItem;

    private int numberOfPickups = 0;

    public Maze() {
        this.mazeItem = generateMaze();
        this.numberOfPickups = generatePickUp( HEIGHT );
    }

    private MazeItem generateMaze() {
        ArrayList<MazeSquare> mazeSquares = new ArrayList<>();

        //int[] start = {5,5};
        //int[] end = {2,7};

        int[] start = {new Random().nextInt(HEIGHT), new Random().nextInt(WIDTH)};
        int[] end = {new Random().nextInt(HEIGHT), new Random().nextInt(WIDTH)};

        while (Arrays.equals(start, end)) {
            int y = new Random().nextInt(HEIGHT);
            int x = new Random().nextInt(WIDTH);

            end = new int[] {y,x};
        }

        for (int i=0; i<100; i++) {

            int value;

            if (i == MazeItem.getMazeSquareIndexWithCoord(start[0], start[1], WIDTH)) {
                value = MazeSquare.START;

            } else if (i == MazeItem.getMazeSquareIndexWithCoord(end[0], end[1], WIDTH)) {
                value = MazeSquare.END;

            } else {
                value = MazeSquare.TRAVERSABLE_PASSAGE_WAY;
            }

            mazeSquares.add(new MazeSquare(i, value));
        }

        return new MazeItem(WIDTH, HEIGHT, mazeSquares, start, end);
    }

    public MazeItem getMazeItem() {
        return mazeItem;
    }

    private int generatePickUp(int maxcount) {

        int total_pickups = new Random().nextInt(maxcount) + 1;

        int count = total_pickups;

        while (count > 0) {

            int pickup_index = new Random().nextInt(mazeItem.getSquares().size());

            if (mazeItem.setIndexAsPickUp(pickup_index)) count--;
        }

        return total_pickups;
    }

    public int getNumberOfPickups() {
        return numberOfPickups;
    }
}
