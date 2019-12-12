package agents;

import game.board.City;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.lang.acl.ACLCodec;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.StringACLCodec;
import utils.Coordinate;
import utils.Pair;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;

public abstract class GameAgent extends Agent {

    protected ArrayList<Coordinate> pos = new ArrayList<>();
    private ArrayList<Coordinate> interactive_coordinates;

    protected ArrayList<City> my_cities;
    protected ArrayList<City> interactive_cities;
    private ArrayList<City> empty_cities;

    private AID controller;

    private int width;
    private int height;

    protected int currentMoney;
    protected int moneyToUpgrade;
    protected int moneyToDefenses;
    protected int moneyToBuyEmptyCities;
    protected int moneyToAttack;
    protected int moneyToReligion;

    /**
     * Creates the agent and receives a coordinate for the starting position
     */
    public void setup() {
        Object[] args = getArguments();
        this.currentMoney = 0;
        this.width = args[0].getClass() == String.class ? Integer.parseInt((String) args[0]) : (int) args[0];
        this.height = args[1].getClass() == String.class ? Integer.parseInt((String) args[1]) : (int) args[1];
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

        setMy_cities();
        setInteractive_coordinates();

        ParallelBehaviour pb = new ParallelBehaviour(ParallelBehaviour.WHEN_ALL);
        pb.addSubBehaviour(new ReceiveTurn());
        pb.addSubBehaviour(new ListenPlayers());

        addBehaviour(pb);
    }

    /**
     * Set interactive coordinates.
     */
    private void setInteractive_coordinates() {
        this.interactive_coordinates = new ArrayList<>();
        for (City c : my_cities) {
            Coordinate cord = c.getCoordinates();
            Coordinate coordinatesToAdd = cord.getTop(this.width, this.height);
            if (coordinatesToAdd != null && !this.interactive_coordinates.contains(coordinatesToAdd))
                this.interactive_coordinates.add(coordinatesToAdd);
            coordinatesToAdd = cord.getButton(this.width, this.height);
            if (coordinatesToAdd != null && !this.interactive_coordinates.contains(coordinatesToAdd))
                this.interactive_coordinates.add(coordinatesToAdd);
            coordinatesToAdd = cord.getLeft(this.width, this.height);
            if (coordinatesToAdd != null && !this.interactive_coordinates.contains(coordinatesToAdd))
                this.interactive_coordinates.add(coordinatesToAdd);
            coordinatesToAdd = cord.getRight(this.width, this.height);
            if (coordinatesToAdd != null && !this.interactive_coordinates.contains(coordinatesToAdd))
                this.interactive_coordinates.add(coordinatesToAdd);
        }
    }

    /**
     * Set new cities.
     */
    private void setMy_cities() {
        this.my_cities = new ArrayList<>();
        for (Coordinate cord : this.pos) {
            this.my_cities.add(new City(this.getAID(), cord));
        }
    }

    /**
     * Get money of all player's cities.
     */
    private void getTurnMoney() {
        for (City city : this.my_cities) {
            this.currentMoney += city.getMoneyProduced();
        }
    }

    /**
     * Logic of player's turn.
     *
     * @return list of new cities conquered.
     */
    public abstract ArrayList<City> logic();

    /**
     * Take down agent.
     */
    public void takeDown() {
        System.out.println("Agent " + getName() + ": Game over");
    }

    /**
     * Inform game controller and opponent that player conquered a new city.
     *
     * @param city city conquered.
     */
    protected void thisCityIsNowMine(City city) {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        if (city.getOwner() != null && !city.getOwner().equals(getAID())) msg.addReceiver(city.getOwner());
        msg.addReceiver(controller);
        msg.setContent("Mine|" + city.getCoordinates().getX() + "|" + city.getCoordinates().getY());
        send(msg);
    }

    /**
     * Buy empty cities with empty cities money.
     *
     * @param new_cities cities that managed to get.
     * @return empty cities that managed to buy.
     */
    protected ArrayList<City> buyEmptyCities(ArrayList<City> new_cities) {
        for (City empty : this.empty_cities) {
            if (this.moneyToBuyEmptyCities >= empty.getCity_price()) {
                this.currentMoney -= empty.getCity_price();
                this.moneyToBuyEmptyCities -= empty.getCity_price();
                empty.reset();
                empty.setOwner(this.getAID());
                this.my_cities.add(empty);
                new_cities.add(empty);
                thisCityIsNowMine(empty);
            }
        }
        return new_cities;
    }

    /**
     * Upgrade cities with upgrade money.
     */
    protected void upgradeCities() {
        Collections.sort(this.my_cities);
        for (City my_city : this.my_cities) {
            if (this.moneyToUpgrade >= my_city.getCostUpgrade()) {
                this.moneyToUpgrade -= my_city.getCostUpgrade();
                my_city.upgradeCity();
            }
        }
    }

    /**
     * Wasted all defenses money upgrading evenly every city you own.
     */
    protected void upgradeMyDefenses() {
        int amountOfDefenses = this.moneyToDefenses / this.my_cities.size();
        if (this.my_cities.size() > this.moneyToDefenses && this.moneyToDefenses != 0) {
            amountOfDefenses = 1;
        }
        for (City c : this.my_cities) {
            this.moneyToDefenses -= amountOfDefenses;
            c.addDefences(amountOfDefenses);
        }
    }

    /**
     * Tries to defend all cities from religion attacks using the money given.
     */
    protected void defendReligion() {
        for (City my_city : this.my_cities) {
            my_city.sortReligionAttackers();
            for (int i = 0; i < my_city.getReligion_attacker().size(); i++) {
                int current_amount = my_city.getReligion_attacker().get(i).getValue();
                if (current_amount >= 50) current_amount = 50;
                int cost = my_city.costOfReligion(current_amount);
                if (this.moneyToReligion >= cost) {
                    this.moneyToReligion -= cost;
                    my_city.getReligion_attacker().set(i, new Pair<>(my_city.getReligion_attacker().get(i).getKey(), my_city.getReligion_attacker().get(i).getValue() - current_amount));
                }
            }
        }
    }

    /**
     * Request to other players.
     *
     * @param city    city to request.
     * @param request message to send.
     * @return value fundamental to play.
     */
    protected int requestMessage(City city, String request) {
        ACLMessage req = new ACLMessage(ACLMessage.REQUEST);
        req.addReceiver(city.getOwner());
        req.setContent(request + "|" + city.getCoordinates().getX() + "|" + city.getCoordinates().getY());
        send(req);

        ACLMessage res = blockingReceive(MessageTemplate.and(
                MessageTemplate.MatchSender(city.getOwner()),
                MessageTemplate.MatchPerformative(ACLMessage.INFORM)));

        return Integer.parseInt(res.getContent());
    }

    /**
     * Player's turn.
     */
    private class ReceiveTurn extends CyclicBehaviour {

        /**
         * Action to play.
         */
        @Override
        public void action() {
            ACLMessage turn = receive(MessageTemplate.and(
                    MessageTemplate.MatchPerformative(ACLMessage.INFORM),
                    MessageTemplate.MatchSender(controller)
            ));
            if (turn == null) return;
            if (turn.getContent().equals("Turn")) {
                handleTurn();
            }
        }

        /**
         * Action to handle player's turn.
         */
        private void handleTurn() {

            if (my_cities.isEmpty()) {
                ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                msg.addReceiver(controller);
                msg.setContent("Gameover");
                send(msg);
                doDelete();
            }

            // Get turn money
            getTurnMoney();

            // Request information about surroundings
            StringBuilder ret = new StringBuilder("Which");
            setInteractive_coordinates();
            for (Coordinate cord : interactive_coordinates) {
                ret.append("|").append(cord);
            }

            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            msg.addReceiver(controller);
            msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
            msg.setContent(ret.toString());
            send(msg);

            // Get information about surroundings
            ACLMessage inform = blockingReceive(MessageTemplate.and(MessageTemplate.and(
                    MessageTemplate.MatchPerformative(ACLMessage.INFORM),
                    MessageTemplate.MatchSender(controller)),
                    MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST)));

            // Process information about surroundings
            String[] content = inform.getContent().split("\\|");
            empty_cities = new ArrayList<>();
            interactive_cities = new ArrayList<>();

            for (int i = 0; i < content.length; i++) {
                int x = interactive_coordinates.get(i).getX();
                int y = interactive_coordinates.get(i).getY();

                // Empty city
                if (content[i].equals("Empty")) {
                    empty_cities.add(new City(null, new Coordinate(x, y)));
                }

                // Invalid coordinates (outside of map)
                else if (content[i].equals("Null")) {
                }
                // Opponent city.
                else {
                    try {
                        StringACLCodec codec = new StringACLCodec(new StringReader(content[i]), null);
                        AID opponent = codec.decodeAID(); // player
                        if (opponent.equals(getAID())) continue;
                        City opponent_city = new City(opponent, new Coordinate(x, y));
                        interactive_cities.add(opponent_city);
                    } catch (ACLCodec.CodecException e) {
                        e.printStackTrace();
                    }
                }
            }

            logic();

            msg = new ACLMessage(ACLMessage.INFORM);
            msg.addReceiver(controller);
            msg.setContent("Update");
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            send(msg);

        }
    }

    /**
     * Players listen to other players and answers their requests.
     */
    private class ListenPlayers extends CyclicBehaviour {

        /**
         * Action to answer request of players.
         */
        @Override
        public void action() {
            ACLMessage msg = receive(MessageTemplate.and(
                    MessageTemplate.or(
                            MessageTemplate.MatchPerformative(ACLMessage.INFORM),
                            MessageTemplate.MatchPerformative(ACLMessage.REQUEST)),
                    MessageTemplate.not(MessageTemplate.MatchSender(controller))
            ));
            if (msg == null) return;
            if (msg.getPerformative() == ACLMessage.REQUEST) {
                String[] content = msg.getContent().split("\\|");
                Coordinate coords = new Coordinate(Integer.parseInt(content[1]), Integer.parseInt(content[2]));
                if (content[0].equals("Price")) {
                    for (City c : my_cities) {
                        if (c.getCoordinates().equals(coords)) {
                            ACLMessage res = msg.createReply();
                            res.setPerformative(ACLMessage.INFORM);
                            res.setContent("" + c.getCity_price());
                            send(res);
                            break;
                        }
                    }
                } else if (content[0].equals("Attack")) {
                    for (City c : my_cities) {
                        if (c.getCoordinates().equals(coords)) {
                            ACLMessage res = msg.createReply();
                            res.setPerformative(ACLMessage.INFORM);
                            res.setContent("" + c.getDefences());
                            send(res);
                            break;
                        }
                    }
                } else if (content[0].equals("Religion")) {
                    for (City c : my_cities) {
                        if (c.getCoordinates().equals(coords)) {
                            ACLMessage res = msg.createReply();
                            res.setPerformative(ACLMessage.INFORM);
                            res.setContent("-1");
                            ArrayList<Pair<AID, Integer>> pairs = c.getReligion_attacker();
                            for (Pair p : pairs) {
                                if (p.getKey() == msg.getSender()) {
                                    res.setContent("" + p.getValue());
                                    break;
                                }
                            }
                            send(res);
                            break;
                        }
                    }
                } else if (content[0].equals("CostToAttack")) {
                    for (City c : my_cities) {
                        if (c.getCoordinates().equals(coords)) {
                            ACLMessage res = msg.createReply();
                            res.setPerformative(ACLMessage.INFORM);
                            res.setContent("" + c.costOfReligion(Integer.parseInt(content[3])));
                            send(res);
                            break;
                        }
                    }
                }
            } else {
                String[] content = msg.getContent().split("\\|");
                Coordinate coords = new Coordinate(Integer.parseInt(content[1]), Integer.parseInt(content[2]));
                if (content[0].equals("Mine")) {
                    City temp = new City(null, coords);
                    my_cities.remove(temp);
                } else if (content[0].equals("ReligionAttack")) {
                    for (City c : my_cities) {
                        if (c.getCoordinates() == coords) {
                            c.setReligionAttacker(Integer.parseInt(content[3]), new Pair<>(msg.getSender(), Integer.parseInt(content[4])));
                            break;
                        }
                    }
                }
            }

        }
    }

}
