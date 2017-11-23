package com.stephen.amaze;

import com.stephen.amaze.Models.MazeItem;

import java.util.Arrays;

class Solution {
    /**
     * Constants to represent the most recent direction in the "plotPath()" method
     */
    private static final int NORTH = 1;
    private static final int SOUTH = 2;
    private static final int EAST = 3;
    private static final int WEST = 4;

    /**
     * Constants to represent items in the maze until they are converted to String
     */
    private static final char WALL = '1';
    private static final char TRAVERSABLE_PASSAGE_WAY = '0';
    private static final char START = '2';
    private static final char END = '3';
    private static final char PATH = '4';

    /**
     * Constants to represent the items in the maze when they are output to the console
     */
    private static final String WALL_STR = "#";
    private static final String TRAVERSABLE_PASSAGE_WAY_STR = " ";
    private static final String START_STR = "S";
    private static final String END_STR = "E";
    private static final String PATH_STR = "X";

    /**
     * MazeItem object to contain details of the maze about to be solved
     */
    private MazeItem mazeItem;

    /**
     * Will be "true" if the maze is solved, and "false" if the maze cannot be solved
     */
    private boolean solvable;

    /**
     * Constructor fot the Solution class. Takes in a MazeItem object to initialize the "mazeItem" and "solvable"
     * instance variables. Calls the "plotPath()" method to solve the maze and initialise the "solvable"
     * instance variable.
     * @param mazeItem an object containing the maze to be solved
     */
    Solution(MazeItem mazeItem) {
        this.mazeItem = mazeItem;
        this.solvable = plotPath(mazeItem.getStart(), 0);
    }

    /**
     * Getter for the "solvable" instance variable
     * @return true if the array has been solved, and false if it is not solvable
     */
    boolean isSolvable() {
        return solvable;
    }


    /**
     * A recursive method that calls itself when there is a traversable passage way to the North, South, East or West
     * @param current the current coordinates in the plotting of a path from start to end
     * @param previous the most recent direction (North, South, East or West. Used to check that the coordinates
     *                 do not infinitely go back and forth across 2 traversable coordinates
     * @return true if the the current coordinates are the "end" coordinates, and false if there are no
     * traversable passage ways to the North, South, East or West that lead to the "end" coordinates.
     */
    boolean plotPath(int[] current, int previous) {

        if (Arrays.equals(mazeItem.getEnd(), current)) {
            mazeItem.getMaze().get(current[0])[current[1]] = END;
            return true;

        } else {

            // If not at the top edge, and did not just move South...
            if (current[0]-1 >= 0 && previous != SOUTH) {

                // Check North for Traversable Passage Way. If passage way is traversable...
                if (mazeItem.getMaze().get(current[0] - 1)[current[1]] == TRAVERSABLE_PASSAGE_WAY) {
                    int y = current[0] - 1;

                    // Recursively call "plotPath" to return true if passage reaches the "end" coordinates
                    if (plotPath(new int[]{y, current[1]}, NORTH)) {
                        mazeItem.getMaze().get(current[0])[current[1]] =
                                Arrays.equals(current, mazeItem.getStart()) ? START : PATH;
                        return true;
                    }
                }
            }

            // If not at the bottom edge, and did not just move North...
            if (current[0]+1 < mazeItem.getHeight() && previous != NORTH) {

                // Check South for Traversable Passage Way. If passage way is traversable...
                if (mazeItem.getMaze().get(current[0] + 1)[current[1]] == TRAVERSABLE_PASSAGE_WAY) {
                    int y = current[0] + 1;

                    // Recursively call "plotPath" to return true if passage reaches the "end" coordinates
                    if (plotPath(new int[]{y, current[1]}, SOUTH)) {
                        mazeItem.getMaze().get(current[0])[current[1]] =
                                Arrays.equals(current, mazeItem.getStart()) ? START : PATH;
                        return true;
                    }
                }
            }

            // If not at the right edge, and did not just move West...
            if (current[1]+1 < mazeItem.getWidth() && previous != WEST) {

                // Check East for Traversable Passage Way. If passage way is traversable...
                if (mazeItem.getMaze().get(current[0])[current[1] + 1] == TRAVERSABLE_PASSAGE_WAY) {
                    int x = current[1] + 1;

                    // Recursively call "plotPath" to return true if passage reaches the "end" coordinates
                    if (plotPath(new int[]{current[0], x}, EAST)) {
                        mazeItem.getMaze().get(current[0])[current[1]] =
                                Arrays.equals(current, mazeItem.getStart()) ? START : PATH;
                        return true;
                    }
                }
            }

            // If not at the left edge, and did not just move East...
            if (current[1]-1 >= 0 && previous != EAST) {

                // Check West for Traversable Passage Way. If passage way is traversable...
                if (mazeItem.getMaze().get(current[0])[current[1]-1] == TRAVERSABLE_PASSAGE_WAY) {
                    int x = current[1] - 1;

                    // Recursively call "plotPath" to return true if passage reaches the "end" coordinates
                    if (plotPath(new int[]{current[0], x}, WEST)) {
                        mazeItem.getMaze().get(current[0])[current[1]] =
                                Arrays.equals(current, mazeItem.getStart()) ? START : PATH;
                        return true;
                    }
                }
            }

            return false;
        }
    }


    /**
     * Converts the Output using the static constants to output the solution to the maze according to the
     * instructions in the README.txt file.
     * @return a string with the solution to the maze the according to the instructions in the README.txt file
     */
    String getMazeOutput() {

        String mazeOutput = "";

        for (int i=0; i<mazeItem.getHeight(); i++) {

            for (int j=0; j<mazeItem.getWidth(); j++) {

                int gridItem = mazeItem.getMaze().get(i)[j];
                String rep;

                switch (gridItem) {
                    case WALL:
                        rep = WALL_STR;
                        break;
                    case START:
                        rep = START_STR;
                        break;
                    case END:
                        rep = END_STR;
                        break;
                    case PATH:
                        rep = PATH_STR;
                        break;
                    default:
                        rep = TRAVERSABLE_PASSAGE_WAY_STR;
                        break;
                }

                mazeOutput = mazeOutput.concat(rep);

                if (j == mazeItem.getWidth()-1
                        && i != mazeItem.getHeight()-1)
                    mazeOutput = mazeOutput.concat("\n");
            }

        }

        return mazeOutput;
    }

}
