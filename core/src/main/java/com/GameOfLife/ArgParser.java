package com.GameOfLife;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.util.ArrayList;

public class ArgParser {
    private final String[] args;
    private final String[] validStyles = {"default", "moss", "redstone", "seaLantern"};

    public ArgParser(String[] args) {
        this.args = args;
    }

    public int getRows() {
        // if user passes in size, prioritize size

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-size")) {
                return Integer.parseInt(args[i + 1]);
            }
        }

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-rows")) {
                return Integer.parseInt(args[i + 1]);
            }
        }

        // default: 100
        return 100;
    }

    public int getCols() {
        // if user passes in size, prioritize size
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-size")) {
                return Integer.parseInt(args[i + 1]);
            }
        }

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-columns") || args[i].equals("-cols")) {
                return Integer.parseInt(args[i + 1]);
            }
        }

        // default: 100
        return 100;
    }

    public String getPatternFilename() {

        ArrayList<String> patterns = getValidStartPatterns();
        for (int i = 0; i < args.length; i++) {

            if (patterns.contains(args[i])) {
                return args[i];
            }
        }

        String randomPattern = patterns.get((int) (Math.random() * patterns.size()));
        Gdx.app.log("Args", "No valid start pattern filename found in args, random pattern chosen: " + randomPattern);
        return randomPattern;
    }

    public int getGens() {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-generations") || args[i].equals("gens")) {
                return Integer.parseInt(args[i + 1]);
            }
        }

        // default: go basically forever
        return Integer.MAX_VALUE;
    }

    public boolean getCentered() {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-center")) {
                return true;
            }
        }

        // default: false
        return false;
    }

    public double getGenDelay() {
        // seconds per gen
        return (double) 1 / (double) getSpeed();

    }

    private double getSpeed() {
        // gens per second
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-speed")) {
                return Double.parseDouble(args[i + 1]);
            }
        }

        // default: 10
        return 10;
    }

    public String getStyle() {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-style")) {
                String style = args[i + 1];
                try {
                    assertValidStyle(style);
                    return style;
                } catch (IllegalArgumentException e) {
                    Gdx.app.log("Args", "Invalid style given, default style chosen instead");
                    return  "default";
                }


            }
        }

        return "default";
    }

    public void assertValidStyle(String givenStyle) throws IllegalArgumentException {
        for (String validStyle : validStyles) {
            if (givenStyle.equals(validStyle)) {
                return;
            }
        }

        throw new IllegalArgumentException("Invalid style given: " + givenStyle + "\nStyle must be one of: " + stylesAsString());
    }

    private String stylesAsString() {
        String styles = "";
        for (String validStyle: validStyles) {
            styles += validStyle + "\n";
        }
        return styles;
    }

    private ArrayList<String> getValidStartPatterns() {
        FileHandle file = Gdx.files.internal("assets.txt");
        String str = file.readString();
        String[] assetArr = str.split("\n");

        ArrayList<String> patternList = new ArrayList<>();
        for (String asset : assetArr) {
            if (asset.startsWith("patterns/")) {
                patternList.add(asset.substring(9, asset.length() - 4));
            }
        }

        return patternList;
    }

}
