package agents.types;

import agents.GameAgent;
import game.board.City;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.ArrayList;

public class EconomicsAgent extends GameAgent {

    /**
     * Logic of player's turn.
     *
     * @return list of new cities conquered.
     */
    @Override
    public ArrayList<City> logic() {

        ArrayList<City> my_new_cities = new ArrayList<>();

        // Buy empty cities
        this.moneyToBuyEmptyCities = this.currentMoney;
        my_new_cities = buyEmptyCities(my_new_cities);

        // Defend their cities from religious attacks
        this.moneyToReligion = this.currentMoney / 4;
        this.currentMoney -= this.moneyToReligion;
        defendReligion();
        this.currentMoney += this.moneyToReligion;


        // Buy opponents cities
        this.moneyToAttack = this.currentMoney / 4;
        this.currentMoney -= this.moneyToAttack;
        my_new_cities = buyOpponentsCities(my_new_cities);
        this.currentMoney += this.moneyToAttack;

        // Upgrade their defenses
        this.moneyToDefenses = this.currentMoney / 4;
        this.currentMoney -= this.moneyToDefenses;
        this.upgradeMyDefenses();
        this.currentMoney += this.moneyToDefenses;

        // Upgrade their cities
        this.moneyToUpgrade = this.currentMoney / 4;
        this.currentMoney -= this.moneyToUpgrade;
        upgradeCities();
        this.currentMoney += this.moneyToUpgrade;

        return my_new_cities;
    }

    /**
     * Try to buy opponents cities.
     *
     * @param my_new_cities cities that managed to buy
     * @return new cities that managed to buy
     */
    private ArrayList<City> buyOpponentsCities(ArrayList<City> my_new_cities) {
        for (City city : this.interactive_cities) {
            int cityPrice = requestCityPrice(city);

            if (this.moneyToAttack >= cityPrice) {
                this.moneyToAttack -= cityPrice;
                this.thisCityIsNowMine(city);
                city.setOwner(this.getAID());
                this.my_cities.add(city);
                this.pos.add(city.getCoordinates());
                my_new_cities.add(city);
            }
        }
        return my_new_cities;
    }

    private int requestCityPrice(City city) {
        ACLMessage req = new ACLMessage(ACLMessage.REQUEST);
        req.addReceiver(city.getOwner());
        req.setContent("Price|" + city.getCoordinates().getX() + "|" + city.getCoordinates().getY());
        send(req);

        ACLMessage res = blockingReceive(MessageTemplate.and(
                MessageTemplate.MatchSender(city.getOwner()),
                MessageTemplate.MatchPerformative(ACLMessage.INFORM)));

        return Integer.parseInt(res.getContent());
    }

}
