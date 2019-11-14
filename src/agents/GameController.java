package agents;

import game.board.Board;
import game.board.City;
import game.gui.GameGUI;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.SubscriptionInitiator;
import jade.util.leap.Iterator;

import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.Queue;

public class GameController extends Agent {

    private static final String TURN = "TURN";
    private static final String QUERIES = "QUERIES";

    private GameGUI gui;

    private Board board;
    private Queue<AID> turns;

    public void setup() {
        turns = new LinkedList<>();
        board = new Board(10, 10);

        gui = new GameGUI();
        gui.setBoard(board);

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
                        for (DFAgentDescription dfd : results) {
                            AID player = dfd.getName();

                            Iterator it = dfd.getAllServices();
                            while (it.hasNext()) {
                                ServiceDescription sd = (ServiceDescription) it.next();
                                if (sd.getType().equals("player")) {
                                    System.out.println("Agent " + getLocalName() + ": " + player.getName() + " joined");
                                    addActionGUI(player.getName() + " joined");
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

        FSMBehaviour fsm = new FSMBehaviour();

        fsm.registerFirstState(new TurnBehaviour(), TURN);
        fsm.registerState(new QueriesBehaviour(), QUERIES);

        fsm.registerDefaultTransition(TURN, QUERIES);
        fsm.registerDefaultTransition(QUERIES, TURN);

        addBehaviour(fsm);

    }

    private void setupPlayer(AID player) {
        int[] pos = board.getRandomAvailable();
        msg(player, "Init " + pos[0] + " " + pos[1], ACLMessage.INFORM);
        System.out.println("Agent " + getLocalName() + ": " + player.getName() + " - " + pos[0] + "," + pos[1]);
        turns.add(player);
        board.setCityOwner(pos[0], pos[1], player);
        updateBoardGUI();
    }

    private void msg(AID player, String content, int type) {
        ACLMessage msg = new ACLMessage(type);
        msg.addReceiver(player);
        msg.setContent(content);
        send(msg);
    }

    private void addActionGUI(String msg) { if(gui != null) gui.addAction(msg); }

    private void updateBoardGUI() {
        gui.setBoard(board);
    }

    public void takedown() {

    }

    private class TurnBehaviour extends Behaviour {
        private boolean end = false;

        @Override
        public void action() {
            if(turns.size() < 2) return;
            AID p = turns.remove();
            System.out.println("Agent " + getLocalName() + ": " + p.getName() + " Turn");
            addActionGUI(p.getName() + " Turn");
            msg(p, "Turn", ACLMessage.INFORM);
            System.out.println("Agent " + getLocalName() + ": " + "Turn sent to " + p.getName());
            turns.add(p);
            end = true;
        }

        @Override
        public boolean done() {
            return end;
        }
    }

    private class QueriesBehaviour extends Behaviour {
        private boolean end = false;

        @Override
        public void action() {
            ACLMessage req = receive(MessageTemplate
                    .and(MessageTemplate.not(MessageTemplate.MatchSender(getDefaultDF())),
                            MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.INFORM),
                                    MessageTemplate.MatchPerformative(ACLMessage.REQUEST)))
            );
            if(req == null) return;

            String[] content = req.getContent().split(" ");
            AID p = req.getSender();
            if(content[0].equals("Update") && req.getPerformative() == ACLMessage.INFORM) {
                end = true;
            } else if (content[0].equals("Which") && req.getPerformative() == ACLMessage.REQUEST){
                City c = board.getCity(Integer.parseInt(content[1]), Integer.parseInt(content[2]));
                if(c == null) {
                    msg(p, "Out_of_Boundary", ACLMessage.INFORM);
                    return;
                }
                AID owner = c.getOwner();
                if(owner == null) {
                    msg(p, "Empty", ACLMessage.INFORM);
                } else {
                    msg(p, owner.toString(), ACLMessage.INFORM);
                }
            }
        }

        @Override
        public boolean done() {
            return end;
        }
    }

}
