package com.example.q_learning.smaFx;


import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import javafx.application.Platform;
import javafx.stage.Stage;
public class AgentMaster extends Agent {

    @Override
    protected void setup() {
        DFAgentDescription dfAgentDescription=new DFAgentDescription();
        dfAgentDescription.setName(getAID());
        ServiceDescription serviceDescription=new ServiceDescription();
        serviceDescription.setType("QA");
        serviceDescription.setName("master");
        dfAgentDescription.addServices(serviceDescription);
        try {
            DFService.register(this,dfAgentDescription);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
        addBehaviour(new Behaviour() {
            int i=0;
            @Override
            public void action() {
                ACLMessage aclMessage=blockingReceive();
                i++;
                if(aclMessage!=null){
                    // Extraire le contenu du message (tableau)
                    String contenu = aclMessage.getContent();
                    String[] lignes = contenu.split(",");

                    // Calculer les dimensions du tableau double
                    int nbLignes = lignes.length;
                    int nbColonnes = lignes[0].split(" ").length;

                    // Convertir les chaînes de caractères en tableau double à deux dimensions
                    double[][] tableau = new double[nbLignes][nbColonnes];
                    int index = 0;
                    for (int i = 0; i < nbLignes; i++) {
                        String []ligne= lignes[index].split(" ");
                        for (int j = 0; j < nbColonnes; j++) {
                            tableau[i][j] = Double.parseDouble(ligne[j]);

                        }
                        index++;
                    }


                    Platform.startup(() -> {
                        ApplicationFx applicationFx = new ApplicationFx();
                        applicationFx.setqTable(tableau);
                        applicationFx.start(new Stage());

                    });

                }else {
                    block();
                }

            }

            @Override
            public boolean done() {
                return i==1;
            }
        });
    }

}
