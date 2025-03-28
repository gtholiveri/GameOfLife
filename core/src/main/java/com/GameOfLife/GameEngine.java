package com.GameOfLife;

public class GameEngine {
    private final int totalGens;
    private int currGen;
    private boolean[][] board;

    private boolean isRunning;

    public GameEngine(boolean[][] startBoard, int totalGens) {
        this.board = startBoard;
        this.totalGens = totalGens;
        this.currGen = 0;

        isRunning = false;
    }

    public void advanceGeneration() {
        boolean[][] buffer = new boolean[board.length][board[0].length];

        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[0].length; col++) {

                int liveNeighbors = countLiveNeighbors(row, col);

                if (board[row][col]) {
                    // cell is currently alive
                    if (liveNeighbors == 2 || liveNeighbors == 3) {
                        // stays alive!
                        buffer[row][col] = true;
                    } else {
                        // dies (since we're working in the buffer it just stays dead, do nothing)

                    }
                } else {
                    // cell is currently dead
                    if (liveNeighbors == 3) {
                        // has exactly 3 neighbors: comes alive!
                        buffer[row][col] = true;
                    }
                }
            }
        }
        currGen++;

        board = buffer;
    }

    // counts every live neighbor of a target cell that's within the bounds of the board
    private int countLiveNeighbors(int row, int col) {
        int liveNeighbors = 0;
        int[][] neighborPositions = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};

        for (int[] pair : neighborPositions) {
            int deltaX = pair[0];
            int deltaY = pair[1];

            int absX = row + deltaX;
            int absY = col + deltaY;

            if (inBounds(absX, absY)) {
                // if the neighbor we're checking is within the bounds of the board
                if (board[absX][absY]) {
                    liveNeighbors++;
                }
            }
        }
        // System.out.print("\nlive neighbors for row " + row + ", col " + col + ": " + liveNeighbors);
        return liveNeighbors;
    }

    private boolean inBounds(int row, int col) {
        return row >= 0 && row < board.length && col >= 0 && col < board[0].length;
    }

    public boolean gensRemain() {
        return currGen < totalGens;
    }

    public boolean[][] getBoard() {
        return board;
    }

    public int getTotalGens() {
        return totalGens;
    }

    public int getCurrGen() {
        return currGen;
    }

    public void toggleRunning() {
        isRunning = !isRunning;
    }

    public void run() {
        isRunning = true;
    }

    public void stop() {
        isRunning = false;
    }

    public boolean getIsRunning() {
        return isRunning;
    }


}
