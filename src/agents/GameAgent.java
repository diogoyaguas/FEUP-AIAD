package agents;

import game.board.City;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.FIPANames;
import jade.lang.acl.ACLCodec;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.StringACLCodec;
import javafx.util.Pair;
import utils.Coordinate;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;

public abstract class GameAgent extends Agent {

    protected ArrayList<Coordinate> pos = new ArrayList<>();
    private AID controller;
    private int width;
    private int height;
    protected ArrayList<City> my_cities;
    protected int current_money;
    private ArrayList<City> empty_cities;
    protected ArrayList<City> interactable_cities;

    private ArrayList<Coordinate> interactable_coordinates;

    /**
     * Creates the agent and receives a coordinate for the starting position
     */
    public void setup() {
        Object[] args = getArguments();
        this.current_money = 0;
        System.out.println("Setting up agent");
        this.width = (int) args[0];
        this.height = (int) args[1];
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

        setInteractable_coordinates();
        setMy_cities();

        addBehaviour(new ReceiveTurn());
    }

    private void setInteractable_coordinates() {
        this.interactable_coordinates = new ArrayList<>();
        for (Coordinate cord : this.pos) {
            Coordinate adicionar = cord.getTop(this.width, this.height);
            if (adicionar != null && !this.interactable_coordinates.contains(adicionar))
                this.interactable_coordinates.add(adicionar);
            adicionar = cord.getButton(this.width, this.height);
            if (adicionar != null && !this.interactable_coordinates.contains(adicionar))
                this.interactable_coordinates.add(adicionar);
            adicionar = cord.getLeft(this.width, this.height);
            if (adicionar != null && !this.interactable_coordinates.contains(adicionar))
                this.interactable_coordinates.add(adicionar);
            adicionar = cord.getRight(this.width, this.height);
            if (adicionar != null && !this.interactable_coordinates.contains(adicionar))
                this.interactable_coordinates.add(adicionar);
        }
    }

    private void setMy_cities() {
        this.my_cities = new ArrayList<>();
        for (Coordinate cord : this.pos) {
            this.my_cities.add(new City(this.getAID(), cord));
        }
    }

    private void getTurnMoney() {
        for (City city : this.my_cities) {
            this.current_money += city.getMoneyProduced();
        }
    }

    public abstract ArrayList<City> logic();

    public void takeDown() {
        System.out.println("Exiting");
    }

    protected void thisCityIsNowMine(City city) {
        //TODO avisar o controller e o agent que comprei a cidade dele
    }

    protected ArrayList<City> buyEmptyCities(ArrayList<City> new_cities, int moneyToSpent) {
        for (City empty : this.empty_cities) {
            if (moneyToSpent >= empty.getCity_price()) {
                System.out.println("Agent " + getName() + ": Getting a new city");
                this.current_money -= empty.getCity_price();
                moneyToSpent-=empty.getCity_price();
                empty.setOwner(this.getAID());
                empty.reset();
                this.my_cities.add(empty);
                new_cities.add(empty);
            }
        }
        return new_cities;
    }

    protected void upgradeCities(int moneyToUpgradeCities) {
        Collections.sort(this.my_cities);
        for (City my_city : this.my_cities) {
            if (moneyToUpgradeCities >= my_city.getCostUpgrade()) {
                moneyToUpgradeCities -= my_city.getCostUpgrade();
                my_city.upgradeCity();
            }
        }
    }

    /**
     * Wasted all money upgrading evenly every city you own
     *
     * @param moneyToDefenses The money I want to waste defending my cities
     */
    protected void upgradeMyDefenses(int moneyToDefenses) {
        int amountOfDefenses = moneyToDefenses / this.my_cities.size();
        System.out.println("Agent " + getName() + ": Increasing defenses");
        for (City my_cities : this.my_cities) {
            my_cities.addDefences(amountOfDefenses);
        }
    }

    /**
     * Tries to defend all cities from religion attacks using the money given
     *
     * @param money_to_waste The money used to defend cities against religion attacks
     * @return The money not wasted
     */
    protected int defendReligion(int money_to_waste) {
        for (City my_city : this.my_cities) {
            my_city.sortReligionAttackers();
            for (int i = 0; i < my_city.getReligion_attacker().size(); i++) {
                int current_amount = my_city.getReligion_attacker().get(i).getValue();
                if (current_amount >= 50) current_amount = 50;
                int cost = my_city.costOfReligion(current_amount);
                if (money_to_waste >= cost) {
                    money_to_waste -= cost;
                    my_city.getReligion_attacker().set(i, new Pair<>(my_city.getReligion_attacker().get(i).getKey(), my_city.getReligion_attacker().get(i).getValue() - current_amount));
                }
            }
        }
        return money_to_waste;
    }

    private class ReceiveTurn extends CyclicBehaviour {

        @Override
        public void action() {
            ACLMessage turn = receive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
            if (turn == null) return;
            if (turn.getContent().equals("Turn")) {
                handleTurn();
            }
        }

        private void handleTurn() {
            getTurnMoney();
            System.out.println("Agent " + getName() + ": My turn");
            StringBuilder ret = new StringBuilder("Which");
            setInteractable_coordinates();
            for (Coordinate cord : interactable_coordinates) {
                ret.append("|").append(cord);
            }

            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            msg.addReceiver(controller);
            msg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
            msg.setContent(ret.toString());
            send(msg);

            System.out.println("Agent " + getAgent().getName() +
                    ": Request Sent, " + msg.getContent());

            ACLMessage inform = blockingReceive(MessageTemplate.and(
                    MessageTemplate.MatchPerformative(ACLMessage.INFORM),
                    MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST)));


            System.out.println("Agent " + getAgent().getName() +
                    ": Inform Received, " + inform.getContent());

            String[] content = inform.getContent().split("\\|");
            empty_cities = new ArrayList<>();
            interactable_cities = new ArrayList<>();
            for (int i = 0; i < content.length; i++) {
                int x = interactable_coordinates.get(i).getX();
                int y = interactable_coordinates.get(i).getY();
                // Espaço vazio
                if (content[i].equals("Empty")) {
                    System.out.println("Agent " + getAgent().getName() +
                            ": Position - " + x + "," + y + " is empty.");
                    empty_cities.add(new City(null, new Coordinate(x, y)));
                }
                // Espaço inválido
                else if (content[i].equals("Null"))
                    System.out.println("Agent " + getAgent().getName() +
                            ": Position - " + x + "," + y + " doesn't exist.");
                    // Existe um jogador
                else {
                    try {
                        StringACLCodec codec = new StringACLCodec(new StringReader(content[i]), null);
                        AID opponent = codec.decodeAID(); //jogador
                        System.out.println("Agent " + getAgent().getName() +
                                ": Position - " + x + "," + y + " it's occupied by " +
                                opponent.getName());
                        City opponent_city = new City(opponent, new Coordinate(x, y));
                        //TODO é preciso mais informação sobre a cidade, costo da cidade, religião que eu tenho agora
                        interactable_cities.add(opponent_city);
                    } catch (ACLCodec.CodecException e) {
                        e.printStackTrace();
                    }
                }
            }

            // TODO: implementar lógica especifica a cada tipo de jogador
            // é possivel que se tenha de altera qualquer coisa porque o economist vai
            // precisar de perguntar ao controller o preço da cidade
            ArrayList<City> new_cities = logic();

            msg = new ACLMessage(ACLMessage.INFORM);
            msg.addReceiver(controller);
            msg.setContent("Update");
            send(msg);

            System.out.println("Agent " + getAgent().getName() +
                    ": Update Sent");

        }
    }
}
