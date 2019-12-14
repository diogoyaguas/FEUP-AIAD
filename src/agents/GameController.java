package agents;

import game.board.Board;
import game.board.City;
import game.gui.GameGUI;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.Runtime;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.FSMBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.domain.JADEAgentManagement.JADEManagementOntology;
import jade.domain.JADEAgentManagement.ShutdownPlatform;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.proto.SubscriptionInitiator;
import jade.util.leap.Iterator;
import jade.wrapper.ControllerException;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import static java.lang.System.exit;

public class GameController extends Agent {

    private static final String TURN = "TURN";
    private static final String QUERIES = "QUERIES";

    private GameGUI gui;

    private Board board;
    private Queue<AID> turns;
    private int numberOfPlayers;
    private int economicsNumber = 0;
    private int militaryNumber = 0;
    private int religiousNumber = 0;
    private ArrayList<String> types;
    private int successfulAttacks = 0;

    private int turn_counter = 0;
    private int counter=0;

    /**
     * Setup game controller.
     */
    public void setup() {
        Object[] args = getArguments();
        numberOfPlayers = (int) args[0];
        int width = (int) args[1];
        int height = (int) args[2];
        economicsNumber = (int) args[3];
        militaryNumber =(int) args[4];
        religiousNumber =(int) args[5];
        types = (ArrayList<String>) args[6];

        turns = new LinkedList<>();
        board = new Board(width, height);

//        gui = new GameGUI();
//      gui.setBoard(board);

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
                            Iterator it = dfd.getAllServices();
                            while (it.hasNext()) {
                                ServiceDescription sd = (ServiceDescription) it.next();
                                if (sd.getType().equals("player")) {
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
        if(player.getName().split("@")[0].equals("Player0"))
        {
            pos[0]=0;
            pos[1]=0;
        }
        else while (pos[0]==0 &&pos[1]==0)pos = board.getRandomAvailable();
        msg(player, "Init " + pos[0] + " " + pos[1], ACLMessage.INFORM);
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
        if (gui != null) gui.setBoard(board);
    }

    /**
     * Inform which player's turn.
     */
    private class TurnBehaviour extends Behaviour {
        private boolean end = false;
        private boolean first = true;

        /**
         * Action to inform player's turn
         */
        @Override
        public void action() {
            if(first && turns.size() != numberOfPlayers) return;
            first = false;
            if (turns.size() == 0) return;
            if (turns.size() == 1) {
                updateCSV(Integer.parseInt(turns.peek().getLocalName().replaceAll("\\D+","")));
                addActionGUI(turns.peek().getLocalName() + " Won!");
                System.out.println(turns.peek().getLocalName() + " Won!");
                stopInstance();
            }
            if(turn_counter > board.getNumberOfCities() * 2) {
                AID winner = board.getPlayerWithMostCities(turns);
                if(winner != null)
                {
                    updateCSV(Integer.parseInt(winner.getLocalName().replaceAll("\\D+","")));
                    addActionGUI(winner.getLocalName() + " Won!");
                    System.out.println(winner.getName() + " Won!");
                }
                stopInstance();
            }

            AID p = turns.remove();
            msg(p, "Turn", ACLMessage.INFORM);
            if(Integer.parseInt(p.getLocalName().replaceAll("\\D+",""))==0)
                counter++;
            turn_counter++;
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

    private void stopInstance() {
        if(this.gui != null) {
            this.gui.close();
            return;
        }
        exit(0);
    }

    private void updateCSV(int winner) {

        try {
            FileWriter fw = new FileWriter("classification.csv", true);
            String win = "false";
            if(winner == 0)
                ganhou = "true";
            fw.write("\n" + win + "," + types.get(0) + "," + board.getNumberOfCities()
                    + "," + economicsNumber
                    + "," + militaryNumber
                    + "," + religiousNumber);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileWriter fw = new FileWriter("regression.csv", true);
            fw.write("\n" + types.get(0) + "," + board.getNumberOfCities()
                    + "," + economicsNumber
                    + "," + militaryNumber
                    + "," + religiousNumber
                    + "," + Math.ceil(successfulAttacks/counter));
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
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
            } else if (param[0].equals("Mine") && req.getPerformative() == ACLMessage.INFORM) {
                board.getCity(Integer.parseInt(param[1]), Integer.parseInt(param[2])).setOwner(req.getSender());
                if(req.getSender().getLocalName().equals("Player0")) {
                    successfulAttacks++;
                }
                updateBoardGUI();
                return;
            } else if (param[0].equals("Gameover") && req.getPerformative() == ACLMessage.INFORM) {
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
