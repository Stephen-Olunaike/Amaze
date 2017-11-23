package com.stephen.amaze.Models;

import java.util.ArrayList;

public class MazeItem {

    /**
     * contains the name of the text file the maze was read from
     */
    private String name;

    /**
     * contains the width of the maze
     */
    private int width;

    /**
     * contains the height of the maze
     */
    private int height;

    /**
     * contains the maze represented as char arrays in an ArrayList
     */
    private ArrayList<char[]> maze;

    /**
     * contains the "start" coordinates
     */
    private int[] start;

    /**
     * contains the "end" coordinates
     */
    private int[] end;

    /**
     * Constructor to initialise instance variables
     * @param width the width of the maze
     * @param height the height of the maze
     * @param maze an ArrayList of char arrays. Indexes of the ArrayList will represent the "y" axis.
     *             Indexes of the char array will represent the "x" axis
     * @param start a two-element array representing starting coordinates – "{y,x}"
     * @param end a two-element array representing ending coordinates – "{y,x}"
     * @param name the name of the ".txt" file the maze details were read from
     */
    MazeItem(int width, int height, ArrayList<char[]> maze, int[] start, int[] end, String name) {
        this.width = width;
        this.height = height;
        this.maze = maze;
        this.start = start;
        this.end = end;
        this.name = name;
    }

    /**
     * Getter for the "width" instance variable
     * @return the value of "width"
     */
    public int getWidth() {
        return width;
    }

    /**
     * Getter for the "height" instance variable
     * @return the value of "height"
     */
    public int getHeight() {
        return height;
    }

    /**
     * Getter for the "maze" instance variable
     * @return the value of "maze"
     */
    public ArrayList<char[]> getMaze() {
        return maze;
    }

    /**
     * Getter for the "start" instance variable
     * @return the value of "start"
     */
    public int[] getStart() {
        return start;
    }

    /**
     * Getter for the "end" instance variable
     * @return the value of "end"
     */
    public int[] getEnd() {
        return end;
    }

    /**
     * Getter for the "name" instance variable
     * @return the value of "name"
     */
    String getName() {
        return name;
    }
}
