package agents;

import game.board.Board;
import game.board.City;
import game.gui.GameGUI;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.FSMBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.SubscriptionInitiator;
import jade.util.leap.Iterator;

import java.util.LinkedList;
import java.util.Queue;

public class GameController extends Agent {

    private static final String TURN = "TURN";
    private static final String QUERIES = "QUERIES";

    private GameGUI gui;

    private Board board;
    private Queue<AID> turns;

    private long start_time;
    private long finish_time;
    private long game_time = 120000;

    /**
     * Setup game controller.
     */
    public void setup() {
        Object[] args = getArguments();
        int width = (int) args[1];
        int height = (int) args[2];
        turns = new LinkedList<>();
        board = new Board(width, height);

        gui = new GameGUI();
        gui.setBoard(board);

        DFAgentDescription dfd = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType("player");
        dfd.addServices(sd);

        SearchConstraints sc = new SearchConstraints();

        addBehaviour(new SubscriptionInitiator(this, DFService.createSubscriptionMessage(this, getDefaultDF(), dfd, sc)) {

            /**
             * Inform if players joined
             * @param inform message
             */
            protected void handleInform(ACLMessage inform) {
                System.out.println("Agent " + getLocalName() + ": Notification received from DF");
                try {
                    DFAgentDescription[] results = DFService.decodeNotification(inform.getContent());
                    if (results.length > 0) {
                        for (DFAgentDescription dfd : results) {
                            AID player = dfd.getName();
                            System.out.println("--------" + player.getClass());

                            Iterator it = dfd.getAllServices();
                            while (it.hasNext()) {
                                ServiceDescription sd = (ServiceDescription) it.next();
                                if (sd.getType().equals("player")) {
//                                    System.out.println("Agent " + getLocalName() + ": " + player.getName() + " joined");
                                    addActionGUI(player.getName() + " joined");
                                    setupPlayer(player);
                                    break;
                                }
                            }
                        }
                    }
                } catch (FIPAException fe) {
                    fe.printStackTrace();
                }
            }
        });

        this.start_time = System.currentTimeMillis();

        FSMBehaviour fsm = new FSMBehaviour();

        fsm.registerFirstState(new TurnBehaviour(), TURN);
        fsm.registerState(new QueriesBehaviour(), QUERIES);

        fsm.registerDefaultTransition(TURN, QUERIES);
        fsm.registerDefaultTransition(QUERIES, TURN);

        addBehaviour(fsm);

    }

    /**
     * Setup game player.
     *
     * @param player player of the game.
     */
    private void setupPlayer(AID player) {
        int[] pos = board.getRandomAvailable();
        msg(player, "Init " + pos[0] + " " + pos[1], ACLMessage.INFORM);
//        System.out.println("Agent " + getLocalName() + ": " + player.getName() + " - " + pos[0] + "," + pos[1]);
        turns.add(player);
        board.setCityOwner(pos[0], pos[1], player);
        updateBoardGUI();
    }

    /**
     * Create a new message to send.
     *
     * @param player  receiver of the message.
     * @param content content of the message.
     * @param type    type of message.
     */
    private void msg(AID player, String content, int type) {
        ACLMessage msg = new ACLMessage(type);
        msg.addReceiver(player);
        msg.setContent(content);
        send(msg);
    }

    /**
     * Add message to GUI
     *
     * @param msg message to appear on GUI.
     */
    private void addActionGUI(String msg) {
        if (gui != null) gui.addAction(msg);
    }

    /**
     * Update board GUI.
     */
    private void updateBoardGUI() {
        gui.setBoard(board);
    }

    /**
     * Inform which player's turn.
     */
    private class TurnBehaviour extends Behaviour {
        private boolean end = false;

        /**
         * Action to inform player's turn
         */
        @Override
        public void action() {
            if (turns.size() == 0) return;
            if (turns.size() == 1) {
                addActionGUI(turns.peek().getLocalName() + "Won!");
                doDelete();
            }
            finish_time = System.currentTimeMillis();
            if ((finish_time - start_time) > game_time) {
                AID winner = board.getPlayerWithMostCities(turns);
                addActionGUI(winner.getLocalName() + "Won!");
                doDelete();
                takeDown();
            }

            AID p = turns.remove();
//            System.out.println("Agent " + getLocalName() + ": " + p.getName() + " Turn");
            msg(p, "Turn", ACLMessage.INFORM);
//            System.out.println("Agent " + getLocalName() + ": " + "Turn sent to " + p.getName());
            turns.add(p);
            end = true;
        }

        /**
         * When action to inform is done.
         *
         * @return end of action.
         */
        @Override
        public boolean done() {
            if (end) {
                end = false;
                return true;
            }
            return false;
        }
    }

    /**
     * Receives questions of players and answers them.
     */
    private class QueriesBehaviour extends Behaviour {
        private boolean end = false;

        /**
         * Action to receive questions of players and answers them.
         */
        @Override
        public void action() {
            ACLMessage req = receive(MessageTemplate
                    .and(MessageTemplate.not(MessageTemplate.MatchSender(getDefaultDF())),
                            MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.INFORM),
                                    MessageTemplate.MatchPerformative(ACLMessage.REQUEST))));
            if (req == null) return;

            String[] param = req.getContent().split("\\|");
            AID p = req.getSender();
            if (param[0].equals("Update") && req.getPerformative() == ACLMessage.INFORM) {
                for (int i = 1; i < param.length; i++) {
                    String[] coordinates = param[i].split("_");
                    board.setCityOwner(Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1]), p);
                }
                updateBoardGUI();
                end = true;
                return;
            } else if(param[0].equals("Mine") && req.getPerformative() == ACLMessage.INFORM) {
//                System.out.println("Agent " + getLocalName() + ": MINE received from " + req.getSender().getName() + ". Action is " + req.getContent());
                board.getCity(Integer.parseInt(param[1]), Integer.parseInt(param[2])).setOwner(req.getSender());
//                addActionGUI(req.getSender().getLocalName() + " has conquered city at " + Integer.parseInt(param[1]) + "," + Integer.parseInt(param[2]));
                updateBoardGUI();
                return;
            } else if(param[0].equals("Gameover") && req.getPerformative() == ACLMessage.INFORM) {
//                System.out.println("Agent " + getLocalName() + ": Gameover received from " + req.getSender().getName() + ". Action is " + req.getContent());
                addActionGUI(req.getSender().getLocalName() + " Lost!");
                turns.remove(req.getSender());
                end = true;
                return;
            }

            ACLMessage inform = req.createReply();
            inform.setPerformative(ACLMessage.INFORM);
            StringBuilder res = new StringBuilder();

            String[] content = req.getContent().split("\\|");
            for (int i = 1; i < content.length; i++) {
                String[] coords = content[i].split("_");
                int x = Integer.parseInt(coords[0]);
                int y = Integer.parseInt(coords[1]);
                City c = board.getCity(x, y);
                String value;

                if (c == null) value = "Null";

                else {
                    AID owner = c.getOwner();
                    if (owner == null) value = "Empty";
                    else value = owner.toString();
                }

                if (res.length() == 0) res.append(value);
                else res.append("|").append(value);
            }

            inform.setContent(res.toString());

            send(inform);
//            System.out.println("Agent " + getLocalName() + ": INFORM sent to " + req.getSender().getName() + ", " + res);

        }

        /**
         * When action receive questions of players and answers them is done.
         *
         * @return end of action.
         */
        @Override
        public boolean done() {
            if (end) {
                end = false;
                return true;
            }
            return false;
        }
    }

}
