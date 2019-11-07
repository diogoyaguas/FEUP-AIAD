package agents;

import game.board.Board;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.proto.SubscriptionInitiator;
import jade.util.leap.Iterator;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class GameController extends Agent {

    private Board board;
    private Queue<AID> turns;

    public void setup()
    {
        turns = new LinkedList<>();
        board = new Board(10, 10);

        DFAgentDescription dfd = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType("player");
        dfd.addServices(sd);

        SearchConstraints sc = new SearchConstraints();

        addBehaviour(new SubscriptionInitiator(this, DFService.createSubscriptionMessage(this, getDefaultDF(), dfd, sc)) {
            protected void handleInform(ACLMessage inform) {
                System.out.println("Agent " + getLocalName() + ": Notification received from DF");
                try {
                    DFAgentDescription[] results = DFService.decodeNotification(inform.getContent());
                    if (results.length > 0) {
                        for (int i = 0; i < results.length; ++i) {
                            DFAgentDescription dfd = results[i];
                            AID player = dfd.getName();

                            Iterator it = dfd.getAllServices();
                            while (it.hasNext()) {
                                ServiceDescription sd = (ServiceDescription) it.next();
                                if (sd.getType().equals("player")) {
                                    System.out.println("Agent " + getLocalName() + ": " + player.getName() + " joined");
                                    turns.add(player);
                                    setupPlayer(player);
                                    break;
                                }
                            }
                        }
                    }
                }
                catch (FIPAException fe) {
                    fe.printStackTrace();
                }
            }
        } );
    }

    private void setupPlayer(AID player) {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.addReceiver(player);
        int[] pos = board.getRandomAvailable();
        msg.setContent("Init " + pos[0] + " " + pos[1]);
        System.out.println("Agent " + getLocalName() + ": " + player.getName() + " - " + pos[0] + "," + pos[1]);
        send(msg);
    }

    public void takedown()
    {

    }
}
