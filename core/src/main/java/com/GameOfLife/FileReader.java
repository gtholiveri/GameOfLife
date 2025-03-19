package com.GameOfLife;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.util.ArrayList;

public class FileReader {

    public static boolean[][] readFile(String filename, boolean centered, int rows, int cols) {
        if (centered) {
            boolean[][] centeredBoard = center(readFile(filename));

            // center it again because of resizeRad quirks
            return center(resizeRad(centeredBoard, rows, cols));
        } else {
            boolean[][] board = readFile(filename);
            return resizeRect(board, rows, cols);
        }
    }

    public static boolean[][] readFile(String filename, boolean centered) {
        if (centered) {
            return center(readFile(filename));
        } else {
            return (readFile(filename));
        }
    }

    public static boolean[][] readFile(String filename) {

        FileHandle config = Gdx.files.internal("patterns/" + filename + ".txt");

        ArrayList<ArrayList<Boolean>> raw = new ArrayList<ArrayList<Boolean>>();

        String[] lines = config.readString().split("\n");

        for (int r = 0; r < lines.length; r++) {
            raw.add(new ArrayList<Boolean>());
            for (int c = 0; c < lines[r].length(); c++) {
                if (lines[r].charAt(c) == '*') {
                    raw.get(r).add(true);
                } else if (lines[r].charAt(c) == ' ') {
                    raw.get(r).add(false);
                } else {
                    System.err.println("Invalid configuration file given");
                }
            }


        }

        return listToArr(raw);
    }

    // resizes radially---that is, it cuts off or adds equal amounts of cells on each side of the
    // existing board
    private static boolean[][] resizeRad(boolean[][] board, int newRows, int newCols) {
            int oldRows = board.length;
            int oldCols = board[0].length;

            boolean[][] resizedBoard = new boolean[newRows][newCols];

            int rowOffset = (newRows - oldRows) / 2;
            int colOffset = (newCols - oldCols) / 2;

            for (int i = 0; i < newRows; i++) {
                for (int j = 0; j < newCols; j++) {
                    int translatedRow = i - rowOffset;
                    int translatedCol = j - colOffset;

                    if (translatedRow >= 0 && translatedRow < oldRows && translatedCol >= 0 && translatedCol < oldCols) {
                        // go through copying everything that should be there
                        resizedBoard[i][j] = board[translatedRow][translatedCol];
                    } else {
                        // keep it dead
                    }
                }
            }

            return resizedBoard;
        }

    // resizes rectangularly---that is, it just cuts off copying at rows or greater than the
    // rows and cols given and fills new ein rows/cols only in the positive directions
    private static boolean[][] resizeRect(boolean[][] board, int newRows, int newCols) {

        int oldRows = board.length;
        int oldCols = board[0].length;

        boolean[][] resized = new boolean[newRows][newCols];

        // Copy data, stop at the most constraining point
        for (int i = 0; i < Math.min(oldRows, newRows); i++) {
            for (int j = 0; j < Math.min(oldCols, newCols); j++) {
                resized[i][j] = board[i][j];
            }
        }
        return resized;
    }

    private static boolean[][] center(boolean[][] board) {
        int firstRow = board.length, lastRow = -1, firstCol = board[0].length, lastCol = -1;

        // Find the first and last non-blank rows and columns
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < board[r].length; c++) {
                if (board[r][c]) {
                    if (r < firstRow) firstRow = r;
                    if (r > lastRow) lastRow = r;
                    if (c < firstCol) firstCol = c;
                    if (c > lastCol) lastCol = c;
                }
            }
        }

        // Calculate the dimensions of the pattern
        int patternHeight = lastRow - firstRow + 1;
        int patternWidth = lastCol - firstCol + 1;

        // Calculate the new top-left corner to center the pattern
        int newFirstRow = (board.length - patternHeight) / 2;
        int newFirstCol = (board[0].length - patternWidth) / 2;

        // Create a new board and place the pattern in the center
        boolean[][] centeredBoard = new boolean[board.length][board[0].length];
        for (int r = 0; r < patternHeight; r++) {
            for (int c = 0; c < patternWidth; c++) {
                centeredBoard[newFirstRow + r][newFirstCol + c] = board[firstRow + r][firstCol + c];
            }
        }

        return centeredBoard;

    }

    private static boolean[][] listToArr(ArrayList<ArrayList<Boolean>> twoDList) {
        boolean[][] out = new boolean[twoDList.size()][lenLongestRow(twoDList)];

        for (int r = 0; r < twoDList.size(); r++) {
            for (int c = 0; c < twoDList.get(r).size(); c++) {
                out[r][c] = twoDList.get(r).get(c);
            }
        }

        return out;
    }

    private static int lenLongestRow(ArrayList<ArrayList<Boolean>> raw) {
        int maxLength = 0;
        for (ArrayList<Boolean> row : raw) {
            if (row.size() > maxLength) {
                maxLength = row.size();
            }
        }
        return maxLength;
    }


}
