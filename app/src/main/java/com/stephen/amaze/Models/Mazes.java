package com.stephen.amaze.Models;

import java.io.*;
import java.util.ArrayList;

public class Mazes {

    private static final String MAZE_FOLDER = System.getProperty("user.home")+"/Desktop/MazeFolder";
    private static final String SPACE = " ";
    private static final String NOT_SOLVABLE = "Not Solvable";

    /**
     * Array of mazes, as read from the MazeFolder
     */
    private ArrayList<MazeItem> mazes;

    /**
     * Getter method for "mazes" instance variable
     * @return mazes
     */
    private ArrayList<MazeItem> getMazes() {
        return mazes;
    }

    /**
     * Constructor for the Mazes class that initializes the "mazes" instance variable
     * by calling the "getMazeInputs()" method
     */
    Mazes() {
        this.mazes = getMazeInputs();
    }

    /*public static void main (String[] args) {
        Mazes m = new Mazes();

        for (MazeItem mazeItem : m.getMazes()) {

            System.out.println("\n\n\n" + mazeItem.getName());

            Solution solution = new Solution(mazeItem);

            if (solution.isSolvable()) {
                System.out.println(solution.getMazeOutput());

            } else {
                System.out.println(NOT_SOLVABLE);
            }
        }

    }*/

    /**
     * Get Maze files from MazeFolder on the Desktop. Returns an ArrayList of MazeItem
     * objects representing each ".txt" file containing a maze.
     * @return mazes in the text files as an ArrayList of MazeItem objects
     */
    private ArrayList<MazeItem> getMazeInputs() {
        ArrayList<MazeItem> input_mazes = new ArrayList<>();

        try {
            File folder = new File(MAZE_FOLDER);

            File[] files = folder.listFiles();

            if (files != null) {

                for (File file : files) {

                    if (file.isFile() && file.getName().endsWith(".txt"))
                        input_mazes.add(readFileData(file));
                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return input_mazes;
    }

    /**
     * Read File data into an array and return a MazeItem Object. Each line is read according to the
     * instructions in the "README.txt".
     *
     * @param file the file to be read
     * @return a maze item object containing the details of the maze as specified in the ".txt" file
     * @throws IOException
     */
    private MazeItem readFileData(File file) throws IOException {
        FileReader fileReader = new FileReader(file);
        BufferedReader reader = new BufferedReader(fileReader);

        String line;

        int count = 0;

        int width=0, height=0;
        int[] start = {0,0};
        int[] end = {0,0};
        ArrayList<char[]> mazes = new ArrayList<>();

        while ((line = reader.readLine()) != null) {

            if (count == 0) {
                String[] items = line.split(SPACE);

                width = Integer.parseInt(items[0]);
                height = Integer.parseInt(items[1]);

            } else if (count == 1) {
                String[] items = line.split(SPACE);

                start = new int[] {Integer.parseInt(items[1]), Integer.parseInt(items[0])};

            } else if (count == 2) {
                String[] items = line.split(SPACE);

                end = new int[] {Integer.parseInt(items[1]), Integer.parseInt(items[0])};

            } else {
                line = line.replaceAll("\\s", "");
                mazes.add(line.toCharArray());
            }

            count++;
        }

        return new MazeItem(width, height, mazes, start, end, file.getName());
    }

}
