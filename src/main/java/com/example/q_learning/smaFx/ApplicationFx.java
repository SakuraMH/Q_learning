package com.example.q_learning.smaFx;


import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ApplicationFx extends Application {
        double[][] qTable ;

        private GridPane gridPane;
        private Label[][] gridLabels;
        int stateI = 0;
        int stateJ = 0;
    List<Integer> StateIL=new ArrayList<>();
    List<Integer> StateJL=new ArrayList<>();


    public void setqTable(double[][] qTable) {
        this.qTable = qTable;
        //System.out.println(this.qTable);

    }

    @Override
        public void start(Stage primaryStage) {

            gridPane = new GridPane();
            gridLabels = new Label[QUtils.GRID_SIZE][QUtils.GRID_SIZE];

            for (int i = 0; i < QUtils.GRID_SIZE; i++) {
                for (int j = 0; j < QUtils.GRID_SIZE; j++) {
                    Label label = new Label();
                    label.setMinSize(70, 70);
                    label.setAlignment(Pos.CENTER);
                    label.setStyle("-fx-border-color: black;");
                    gridLabels[i][j] = label;
                    gridPane.add(label, j, i);
                }
            }

            Button startButton = new Button("Start");
            startButton.setOnAction(event -> {
                //qlearning.runQlearning();
                updateGrid(this.qTable);
                displayOptimalPath();
            });

            HBox buttonBox = new HBox(startButton);
            buttonBox.setAlignment(Pos.CENTER);

            VBox root = new VBox(gridPane, buttonBox);
            root.setAlignment(Pos.CENTER);
            root.setSpacing(20);

            updateGrid(new double[QUtils.GRID_SIZE*QUtils.GRID_SIZE][QUtils.ACTION_SIZE]);

            Scene scene = new Scene(root);
            primaryStage.setTitle("Q-Learning Application SMA");
            primaryStage.setScene(scene);
            primaryStage.show();
        }

        void updateGrid(double[][] qTable) {


            for (int i = 0; i < QUtils.GRID_SIZE; i++) {
                for (int j = 0; j < QUtils.GRID_SIZE; j++) {
                    if(i==stateI && j==stateJ){
                        System.out.println(i+" "+j);
                        gridLabels[i][j].setText("Start");
                        gridLabels[i][j].setStyle("-fx-background-color: yellow; -fx-border-color: black;");
                    }else if (QUtils.grid[i][j] == 1) {
                        gridLabels[i][j].setText("Goal");
                        gridLabels[i][j].setStyle("-fx-background-color: green; -fx-border-color: black;");
                    } else if (QUtils.grid[i][j] == -1) {
                        gridLabels[i][j].setText("Obstacle");
                        gridLabels[i][j].setStyle("-fx-background-color: red; -fx-border-color: black;");
                    } else {
                        int state = i * QUtils.GRID_SIZE + j;
                        double maxQValue = Double.NEGATIVE_INFINITY;
                        int maxAction = 0;
                        for (int k = 0; k < QUtils.ACTION_SIZE; k++) {
                            if (qTable[state][k] > maxQValue) {
                                maxQValue = qTable[state][k];
                                maxAction = k;
                            }
                        }
                        gridLabels[i][j].setText("Q: " + String.format("%.2f", maxQValue) + "\nA: " + maxAction);
                        gridLabels[i][j].setStyle("-fx-background-color: white; -fx-border-color: black;");
                    }
                }
            }
        }

        private void displayOptimalPath() {
            showResult();
            for (int i=0;i<StateIL.size();i++) {
                gridLabels[StateIL.get(i)][StateJL.get(i)].setStyle("-fx-background-color: yellow; -fx-border-color: black;");
            }
        }
    int chooseAction(double eps){
        Random rnd=new Random();
        double bestQ=0;
        int action=0;
        if (rnd.nextDouble()<eps){
            //Exploration
            action=rnd.nextInt(QUtils.ACTION_SIZE);

        }else{
            //Exploitation
            int st=stateI*QUtils.GRID_SIZE+stateJ;
            for(int i=0;i<QUtils.ACTION_SIZE;i++){
                if(qTable[st][i]>bestQ){
                    bestQ=qTable[st][i];
                    action=i;
                }
            }


        }
        return action;
    }
    int executeAction(int act){
        stateI=Math.max(0,Math.min(QUtils.actions[act][0]+stateI,QUtils.GRID_SIZE-1));
        stateJ=Math.max(0,Math.min(QUtils.actions[act][1]+stateJ,QUtils.GRID_SIZE-1));
        return stateI*QUtils.GRID_SIZE+stateJ;

    }
    boolean finished(){
        return QUtils.grid[stateI][stateJ]==1;
    }

    void showResult(){
        stateI=0;
        stateJ=0;
        int i=0;
        while (!finished()){
            int act=chooseAction(0);
            System.out.println(stateI*QUtils.GRID_SIZE+stateJ+" Action: "+act);
            StateIL.add(stateI);
            StateJL.add(stateJ);
            i++;
            executeAction(act);
        }

    }

    }












