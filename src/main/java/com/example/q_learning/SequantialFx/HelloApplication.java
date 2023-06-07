package com.example.q_learning.SequantialFx;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;

public class HelloApplication extends Application {

        private Qlearning qlearning;

        private GridPane gridPane;
        private Label[][] gridLabels;
        int[][] grid ;

        @Override
        public void start(Stage primaryStage) {
            qlearning = new Qlearning();

            gridPane = new GridPane();
            gridLabels = new Label[qlearning.getGRID_SIZE()][qlearning.getGRID_SIZE()];

            for (int i = 0; i < qlearning.getGRID_SIZE(); i++) {
                for (int j = 0; j < qlearning.getGRID_SIZE(); j++) {
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
                qlearning.runQlearning();
                updateGrid();
                displayOptimalPath();
            });

            HBox buttonBox = new HBox(startButton);
            buttonBox.setAlignment(Pos.CENTER);

            VBox root = new VBox(gridPane, buttonBox);
            root.setAlignment(Pos.CENTER);
            root.setSpacing(20);

            updateGrid();

            Scene scene = new Scene(root);
            primaryStage.setTitle("Q-Learning Application");
            primaryStage.setScene(scene);
            primaryStage.show();
        }

        private void updateGrid() {
            int[][] grid = qlearning.getGrid();
            double[][] qTable = qlearning.getqTable();
            qlearning.resetState();
            int stateIstart=qlearning.getStateI();
            int stateJstart=qlearning.getStateJ();

            for (int i = 0; i < qlearning.getGRID_SIZE(); i++) {
                for (int j = 0; j < qlearning.getGRID_SIZE(); j++) {
                    if(i==stateIstart && j==stateJstart){
                        System.out.println(i+" "+j);
                        gridLabels[i][j].setText("Start");
                        gridLabels[i][j].setStyle("-fx-background-color: yellow; -fx-border-color: black;");
                    }else if (grid[i][j] == 1) {
                        gridLabels[i][j].setText("Goal");
                        gridLabels[i][j].setStyle("-fx-background-color: green; -fx-border-color: black;");
                    } else if (grid[i][j] == -1) {
                        gridLabels[i][j].setText("Obstacle");
                        gridLabels[i][j].setStyle("-fx-background-color: red; -fx-border-color: black;");
                    } else {
                        int state = i * qlearning.getGRID_SIZE() + j;
                        double maxQValue = Double.NEGATIVE_INFINITY;
                        int maxAction = 0;
                        for (int k = 0; k < qlearning.getACTION_SIZE(); k++) {
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
               List<Integer>I=qlearning.StateIL;
               List<Integer>J=qlearning.StateJL;
            for (int i=0;i<I.size();i++) {
                gridLabels[I.get(i)][J.get(i)].setStyle("-fx-background-color: yellow; -fx-border-color: black;");
            }
        }



        public static void main(String[] args) {
            launch(args);
        }
    }












