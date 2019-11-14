package agents;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLCodec;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.StringACLCodec;
import utils.Coordinate;

import java.io.StringReader;
import java.util.ArrayList;

public abstract class GameAgent extends Agent {

    private ArrayList<Coordinate> pos = new ArrayList<>();
    private AID controller;

    public void setup() {
        System.out.println("Setting up agent");
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

        ACLMessage msg = blockingReceive();
        controller = msg.getSender();
        String[] params = msg.getContent().split(" ");
        if (params[0].equals("Init")) {
            pos.add(new Coordinate(Integer.parseInt(params[1]), Integer.parseInt(params[2])));
        }

        System.out.println("Agent " + getName() + ": Coords " + pos.get(0).getX() + "," + pos.get(0).getY() + "\n");

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
                for (Coordinate position: pos) {

                    ArrayList<Coordinate> neighbours = position.adjacents();

                    for (Coordinate neighbour : neighbours) {
                        if(pos.contains(neighbour)) continue;

                        int x = neighbour.getX(), y = neighbour.getY();

                        ACLMessage question = new ACLMessage(ACLMessage.REQUEST);
                        question.addReceiver(controller);
                        question.setContent("Which " + x + " " + y);
                        send(question);

                        ACLMessage response = blockingReceive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
                        if(!response.getContent().equals("Out_of_Boundary")) {
                            if(response.getContent().equals("Empty")) {
                                System.out.println("Agent " + getName() + ": Position - " + x + "," + y + " it's empty.");
                            } else {
                                try {
                                    StringACLCodec codec = new StringACLCodec(new StringReader(response.getContent()), null);
                                    AID opponent = codec.decodeAID();
                                    System.out.println("Agent " + getName() + ": Position - " + x + "," + y + " it's occupied.");
                                } catch (ACLCodec.CodecException e) {
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
