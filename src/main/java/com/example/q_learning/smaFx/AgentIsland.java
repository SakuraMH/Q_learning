package com.example.q_learning.smaFx;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

import java.util.Random;

public class AgentIsland extends Agent {

    private double[][] qTable = new double[QUtils.GRID_SIZE * QUtils.GRID_SIZE][QUtils.ACTION_SIZE];
    private int stateI;
    private int stateJ;

    @Override
    protected void setup() {

        SequentialBehaviour sequentialBehaviour = new SequentialBehaviour();

        sequentialBehaviour.addSubBehaviour(new Behaviour() {
            int it = 0;
            int currentState;
            int nextState;
            int act, act1;

            @Override
            public void action() {
                resetState();
                while (!finished()) {
                    currentState = stateI * QUtils.GRID_SIZE + stateJ;
                    //System.out.println(currentState);
                    act = chooseAction(0.4);
                    nextState = executeAction(act);
                    act1 = chooseAction(0);

                    qTable[currentState][act] = qTable[currentState][act] + QUtils.ALPHA * (QUtils.grid[stateI][stateJ] + QUtils.GAMMA * qTable[nextState][act1] - qTable[currentState][act]);
                    //System.out.println(qTable[currentState][act]);


                }
                it++;
            }

            @Override
            public boolean done() {
                return it > QUtils.MAX_EPOCH || it == QUtils.MAX_EPOCH;
            }
        });
        sequentialBehaviour.addSubBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {
                DFAgentDescription dfAgentDescription = new DFAgentDescription();
                ServiceDescription serviceDescription = new ServiceDescription();
                serviceDescription.setType("QA");
                dfAgentDescription.addServices(serviceDescription);
                DFAgentDescription[] dfAgentDescriptions;
                try {
                    dfAgentDescriptions = DFService.search(getAgent(), dfAgentDescription);
                } catch (FIPAException e) {
                    throw new RuntimeException(e);
                }
                ACLMessage aclMessage = new ACLMessage(ACLMessage.INFORM);
                aclMessage.addReceiver(dfAgentDescriptions[0].getName());
                //// Convertir le tableau en une chaîne de caractères
                StringBuilder contenuBuilder = new StringBuilder();
                for (double[] ligne : qTable) {
                    for (double valeur : ligne) {
                        contenuBuilder.append(valeur);
                        contenuBuilder.append(" ");
                    }
                    contenuBuilder.append(",");
                }
                contenuBuilder.deleteCharAt(contenuBuilder.length() - 1);
                String contenu = contenuBuilder.toString();
                //System.out.println(contenu);

                // Ajouter le contenu au message
                aclMessage.setContent(contenu);
                //aclMessage.setContent(showResult());
                send(aclMessage);
            }
        });
        addBehaviour(sequentialBehaviour);
    }

    private void resetState() {
        stateI = 0;
        stateJ = 0;
    }

    private int chooseAction(double eps) {
        Random rnd = new Random();
        double bestQ = 0;
        int action = 0;
        if (rnd.nextDouble() < eps) {
            //Exploration
            action = rnd.nextInt(QUtils.ACTION_SIZE);

        } else {
            //Exploitation
            int st = stateI * QUtils.GRID_SIZE + stateJ;
            for (int i = 0; i < QUtils.ACTION_SIZE; i++) {
                if (qTable[st][i] > bestQ) {
                    bestQ = qTable[st][i];
                    action = i;
                }
            }


        }
        return action;
    }

    private int executeAction(int act) {
        stateI = Math.max(0, Math.min(QUtils.actions[act][0] + stateI, QUtils.GRID_SIZE-1));
        stateJ = Math.max(0, Math.min(QUtils.actions[act][1] + stateJ, QUtils.GRID_SIZE-1));
        return stateI * QUtils.GRID_SIZE + stateJ;

    }

    private boolean finished() {
        return QUtils.grid[stateI][stateJ] == 1;
    }

    /*private String showResult() {
        String result = "";
        resetState();
        while (!finished()) {
            int act = chooseAction(0);
            //System.out.println(stateI+" "+stateJ+" Action: "+act);
            result = result + stateI + " " + stateJ + " Action: " + act + "\n";
            executeAction(act);
        }
        result = result + "Final state :" + stateI + " " + stateJ;
        return result;

    }*/


}