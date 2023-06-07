package com.example.q_learning.smaFx;

public class QUtils {
    public static final double ALPHA=0.1;
    public static final double GAMMA=0.9;
    public static final double EPS=0.4;
    public static final int MAX_EPOCH=90000;
    public static final int GRID_SIZE=6;
    public static final int ACTION_SIZE=4;
    public static final int Island_size=3;
    public static final int [][] grid = new int[][]{
        {0, 0,0,0,0,0},
        {0, 0,-1,0,0,0},
        {0, -1,0,0,0,0},
        {0, 0,-1,0,0,0},
        {-1, 0,0,0,0,0},
        {0, 0,0,0,0,1}
    };
    public static final int[][] actions = new int[][]{
            {0, -1},//gauche
            {0, 1},//droit
            {1, 0},//bas
            {-1, 0}//haut
    };
}
