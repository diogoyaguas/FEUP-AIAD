package agents;

import com.sun.xml.internal.ws.api.model.MEP;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.lang.acl.ACLCodec;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.StringACLCodec;
import jade.proto.AchieveREInitiator;
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

        System.out.println("Agent " + getName() + ": Coords " + pos.get(0).x() + "," + pos.get(0).y() + "\n");

        addBehaviour(new ReceiveTurn());

    }

    public abstract void start();

    public abstract void takeDown();

    private class ReceiveTurn extends CyclicBehaviour {

        @Override
        public void action() {
            ACLMessage turn = receive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
            if(turn == null) return;
            if(turn.getContent().equals("Turn")) {
                handleTurn();
            }
        }

        private void handleTurn() {
            System.out.println("Agent " + getName() + ": My turn");
            ArrayList<Coordinate> coordinates = new ArrayList();
            String ret = "Which";
            for (Coordinate position: pos) {
                ArrayList<Coordinate> neighbours = position.adjacents();
                for(Coordinate n : neighbours) {
                    if(pos.contains(n)) continue;
                    coordinates.add(n);
                    ret += "|" + n;
                }
            }

            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            msg.addReceiver(controller);
            msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
            msg.setContent(ret);
            send(msg);

            System.out.println("Agent " + getAgent().getName() +
                    ": Request Sent, " + msg.getContent());

            ACLMessage inform = blockingReceive(MessageTemplate.and(
                    MessageTemplate.MatchPerformative(ACLMessage.INFORM),
                    MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST)));


            System.out.println("Agent " + getAgent().getName() +
                    ": Inform Received, " + inform.getContent());

            String[] content = inform.getContent().split("\\|");
            for(int i = 0; i < content.length; i++) {
                int x = coordinates.get(i).x();
                int y = coordinates.get(i).y();
                if(content[i].equals("Empty"))
                    System.out.println("Agent " + getAgent().getName() +
                            ": Position - " + x + "," + y + " is empty.");
                else if(content[i].equals("Null"))
                    System.out.println("Agent " + getAgent().getName() +
                            ": Position - " + x + "," + y + " doesn't exist.");
                else {
                    try {
                        StringACLCodec codec = new StringACLCodec(new StringReader(content[i]), null);
                        AID opponent = codec.decodeAID();
                        System.out.println("Agent " + getAgent().getName() +
                                ": Position - " + x + "," + y + " it's occupied by " +
                                opponent.getName());
                    } catch (ACLCodec.CodecException e) {
                        e.printStackTrace();
                    }
                }
            }

            msg = new ACLMessage(ACLMessage.INFORM);
            msg.addReceiver(controller);
            msg.setContent("Update");
            send(msg);

            System.out.println("Agent " + getAgent().getName() +
                    ": Update Sent");

        }
    }
}
