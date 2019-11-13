package agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

import java.util.ArrayList;

public abstract class GameAgent extends Agent {

    private ArrayList<int[]> pos = new ArrayList<>();
    private AID controller;

    public void setup()
    {
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setName("GameAgent");
        sd.setType("player");
        dfd.addServices(sd);

        try {
            DFService.register(this, dfd);
        } catch (FIPAException e) {
            e.printStackTrace();
        }

        while (pos == null) {
            ACLMessage msg = blockingReceive();
            controller = msg.getSender();
            String[] params = msg.getContent().split(" ");
            if (params[0].equals("Init")) {
                pos.add(new int[]{Integer.parseInt(params[1]), Integer.parseInt(params[2])});
                break;
            }
        }

        System.out.println("Agent " + getName() + ": Coords " + pos.get(0)[0] + "," + pos.get(0)[1] + "\n");

        addBehaviour(new ReceiveTurn());

    }

    public abstract void start();

    public abstract void takeDown();

    private class ReceiveTurn extends CyclicBehaviour {

        @Override
        public void action() {
            ACLMessage msg = blockingReceive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
            if(msg.getContent().equals("Turn")) {
                System.out.println("Agent " + getName() + ": My turn");
                for (int[] position: pos) {

                    int startX = (position[0] - 1);
                    int startY = (position[1] - 1);
                    int endX =   (position[0] + 1);
                    int endY =   (position[1] + 1);

                    for (int rowNum=startX; rowNum<=endX; rowNum++) {
                        for (int colNum=startY; colNum<=endY; colNum++) {

                             if(position[0] == rowNum && position[1] == colNum){
                                continue;
                            }

                            ACLMessage question = new ACLMessage(ACLMessage.REQUEST);
                            question.addReceiver(controller);
                            question.setContent("Which " + rowNum + " " + colNum);
                            send(question);

                            ACLMessage response = blockingReceive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
                            if(!response.getContent().equals("Out_of_Boundary")) {
                                if(response.getContent().equals("Empty")) {
                                    System.out.println("Agent " + getName() + ": Position - " + rowNum + "," + colNum + " it's empty.");
                                } else {
                                    try {
                                        AID opponent = (AID) response.getContentObject();
                                        System.out.println("Agent " + getName() + ": Position - " + rowNum + "," + colNum + " it's occupied.");
                                    } catch (UnreadableException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }
                        }
                    }

                }
            }
        }
    }
}
