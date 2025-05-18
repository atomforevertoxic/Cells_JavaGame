package org.example;

import Scripts.Game.GameManager;
import Scripts.Game.Level;
import java.awt.Point;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {


        int rows = 6;
        int cols = 8;

        List<Point> walls = Arrays.asList(
                new Point(1,1),
                new Point(2,2),
                new Point(3,1)
        );


        List<Point> keys = Arrays.asList(
                new Point(0,3),
                new Point(4,2)
        );


        Point start = new Point(0,0);


        Point exit = new Point(6,4);


        GameManager gm = new GameManager();

        new Level(rows, cols, walls, keys, start, exit, gm);
    }
}