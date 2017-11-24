package com.stephen.amaze.Models;

import java.util.ArrayList;

/**
 * Created by stephen on 24/11/2017.
 */

public class Maze {

    MazeItem mazeItem;

    public Maze() {
        this.mazeItem = generateMaze();
    }

    private MazeItem generateMaze() {
        ArrayList<MazeSquare> mazeSquares = new ArrayList<>();

        int maze_width = 10, maze_height = 10;
        int[] start = {1,1};
        int[] end = {8,8};

        for (int i=0; i<100; i++) {

            int value;

            if (i == MazeItem.getMazeSquareIndexWithCoord(start[0], start[1], maze_width)) {
                value = MazeSquare.START;

            } else if (i == MazeItem.getMazeSquareIndexWithCoord(end[0], end[1], maze_width)) {
                value = MazeSquare.END;

            } else {
                value = MazeSquare.TRAVERSABLE_PASSAGE_WAY;
            }

            mazeSquares.add(new MazeSquare(i, value));
        }

        return new MazeItem(maze_width, maze_height, mazeSquares, start, end);
    }

    public MazeItem getMazeItem() {
        return mazeItem;
    }
}
