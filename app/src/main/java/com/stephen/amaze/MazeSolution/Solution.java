package com.stephen.amaze.MazeSolution;

import com.stephen.amaze.Models.MazeItem;

import java.util.Arrays;

class Solution {

    private static final int WALL = 1;
    private static final int TRAVERSABLE_PASSAGE_WAY = 0;
    private static final int START = 2;
    private static final int END = 3;
    private static final int PATH = 4;

    private static final String WALL_STR = "#";
    private static final String TRAVERSABLE_PASSAGE_WAY_STR = " ";
    private static final String START_STR = "S";
    private static final String END_STR = "E";
    private static final String PATH_STR = "X";

    private MazeItem mazeItem;

    private boolean solvable;

    Solution(MazeItem mazeItem) {
        this.mazeItem = mazeItem;
    }

    boolean isSolvable() {
        return solvable;
    }



}
