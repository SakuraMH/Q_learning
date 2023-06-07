package com.example.q_learning.SequantialFx;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Qlearning {
    private final double ALPHA=0.1;
    private final double GAMMA=0.9;
    private final double EPS=0.4;
    private final int MAX_EPOCH=90000;
    private final int GRID_SIZE=6;
    private final int ACTION_SIZE=4;
    private int [][] grid;
    private double [][]qTable=new double[GRID_SIZE*GRID_SIZE][ACTION_SIZE];
    private int [][] actions;
    private int stateI;
    private int stateJ;
    List<Integer> StateIL=new ArrayList<>();
    List<Integer> StateJL=new ArrayList<>();

    public Qlearning(){
        actions=new int[][]{
                {0,-1},//gauche
                {0,1},//droit
                {1,0},//bas
                {-1,0}//haut
        };
        grid=new int[][]{
                {0, 0,0,0,-1,-1},
                {0, 0,-1,0,0,0},
                {0, -1,0,0,0,0},
                {0, 0,-1,0,0,0},
                {-1, 0,0,0,0,0},
                {0, 0,0,0,0,1}
        };
    }
    void resetState(){
        stateI=0;
        stateJ=0;
    }

    public int getStateI() {
        return stateI;
    }

    public int getStateJ() {
        return stateJ;
    }

    int chooseAction(double eps){
        Random rnd=new Random();
        double bestQ=0;
        int action=0;
        if (rnd.nextDouble()<eps){
            //Exploration
            action=rnd.nextInt(ACTION_SIZE);

        }else{
            //Exploitation
            int st=stateI*GRID_SIZE+stateJ;
            for(int i=0;i<ACTION_SIZE;i++){
                if(qTable[st][i]>bestQ){
                    bestQ=qTable[st][i];
                    action=i;
                }
            }


        }
        return action;
    }
    int executeAction(int act){
        stateI=Math.max(0,Math.min(actions[act][0]+stateI,GRID_SIZE-1));
        stateJ=Math.max(0,Math.min(actions[act][1]+stateJ,GRID_SIZE-1));
        return stateI*GRID_SIZE+stateJ;

    }
    boolean finished(){
        return grid[stateI][stateJ]==1;
    }
    void showResult(){
        for (double []line:qTable){
            System.out.print("[");
            for(double qvalue:line){
                System.out.print(qvalue+",");
            }
           System.out.println("]");

        }
        resetState();
        int i=0;
        while (!finished()){
            int act=chooseAction(0);
            System.out.println(stateI*GRID_SIZE+stateJ+" Action: "+act);
            StateIL.add(stateI);
            StateJL.add(stateJ);
            i++;
            executeAction(act);
        }
        System.out.println("Final state :"+stateI+ " "+stateJ);

    }
    public void runQlearning(){
        int it=0;
        int currentState;
        int nextState;
        int act,act1;
        while (it<MAX_EPOCH){
            resetState();
            while(!finished()){
                currentState=stateI*GRID_SIZE+stateJ;
                //System.out.println(currentState);
                act=chooseAction(0.4);
                nextState=executeAction(act);
                act1=chooseAction(0);

                qTable[currentState][act]=qTable[currentState][act]+ALPHA*(grid[stateI][stateJ]+GAMMA*qTable[nextState][act1]-qTable[currentState][act]);
                //System.out.println(qTable[currentState][act]);


            }
            it++;
        }

        showResult();
    }

    public int getMAX_EPOCH() {
        return MAX_EPOCH;
    }

    public double getEPS() {
        return EPS;
    }

    public double[][] getqTable() {
        return qTable;
    }

    public double getALPHA() {
        return ALPHA;
    }

    public double getGAMMA() {
        return GAMMA;
    }

    public int getGRID_SIZE() {
        return GRID_SIZE;
    }

    public int getACTION_SIZE() {
        return ACTION_SIZE;
    }

    public int[][] getGrid() {
        return grid;
    }

    public int[][] getActions() {
        return actions;
    }
}
