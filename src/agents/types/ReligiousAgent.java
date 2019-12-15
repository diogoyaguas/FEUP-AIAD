package agents.types;

import agents.GameAgent;
import game.board.City;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.ArrayList;


public class ReligiousAgent extends GameAgent {

    /**
     * Logic of player's turn.
     *
     * @return list of new cities conquered.
     */
    @Override
    public ArrayList<City> logic() {

        ArrayList<City> my_new_cities = new ArrayList<>();

        //this.currentMoney += (this.currentMoney * 1000);

        // Buy empty cities
        this.moneyToBuyEmptyCities = this.currentMoney;
        my_new_cities = buyEmptyCities(my_new_cities);


        // Upgrade their cities
        this.moneyToUpgrade = this.currentMoney / 2;
        this.currentMoney -= this.moneyToUpgrade;
        upgradeCities();
        this.currentMoney += this.moneyToUpgrade;


        // Attack opponent cities with religious attacks
        this.moneyToAttack += this.currentMoney / 2;
        this.currentMoney -= this.moneyToAttack;
        my_new_cities = attackReligion(my_new_cities);
        this.currentMoney += this.moneyToAttack;


        // Defend their cities from religious attacks
        this.moneyToReligion = this.currentMoney / 2;
        this.currentMoney -= this.moneyToReligion;
        defendReligion();
        this.currentMoney += this.moneyToReligion;


        // Upgrade their defenses
        this.moneyToDefenses = this.currentMoney / 2;
        this.currentMoney -= this.moneyToDefenses;
        upgradeMyDefenses();
        this.currentMoney += this.moneyToDefenses;


        // Upgrade their cities
        this.moneyToUpgrade = this.currentMoney;
        this.currentMoney -= this.moneyToUpgrade;
        upgradeCities();
        this.currentMoney += this.moneyToUpgrade;

        return my_new_cities;
    }

    /**
     * Attack opponent cities with religious attacks
     *
     * @param my_new_cities cities that managed to buy
     * @return new cities that managed to buy
     */
    private ArrayList<City> attackReligion(ArrayList<City> my_new_cities) {
        if (!this.interactive_cities.isEmpty()) {
            for (City interacting_city : this.interactive_cities) {
                if (interacting_city.getOwner() == getAID()) continue;
                int current_my_religion;
                int i = 0;

                current_my_religion = requestMessage(interacting_city, "Religion");
                if (current_my_religion == -1) {
                    i = -1;
                    current_my_religion = 0;
                }

                int value_to_attack = 100 - current_my_religion;

                int cost_to_attack = requestCostToAttack(interacting_city, value_to_attack);



                if (this.moneyToAttack >= cost_to_attack) {
                    this.moneyToAttack -= cost_to_attack;
                    if (current_my_religion + value_to_attack >= 100) {
                        this.thisCityIsNowMine(interacting_city);
                        interacting_city.resetReligion();
                        interacting_city.setOwner(this.getAID());
                        this.my_cities.add(interacting_city);
                        my_new_cities.add(interacting_city);
                    } else {
                        sendReligionAttack(interacting_city, i, current_my_religion + value_to_attack);
                    }
                }
            }
        }
        return my_new_cities;
    }

    /**
     * Request cost to attack religious.
     *
     * @param city  city to attack.
     * @param value value to attack.
     * @return cost to attack.
     */
    private int requestCostToAttack(City city, int value) {
        ACLMessage req = new ACLMessage(ACLMessage.REQUEST);
        req.addReceiver(city.getOwner());
        req.setContent("CostToAttack|" + city.getCoordinates().getX() + "|" + city.getCoordinates().getY() + "|" + value);
        send(req);

        ACLMessage res = blockingReceive(MessageTemplate.and(
                MessageTemplate.MatchSender(city.getOwner()),
                MessageTemplate.MatchPerformative(ACLMessage.INFORM)));

        System.out.println(res.getContent());

        return Integer.parseInt(res.getContent());
    }

    /**
     * Inform that city will be attacked religious.
     *
     * @param city  city that will be attacked.
     * @param i     religious attacker.
     * @param value value to attack.
     */
    private void sendReligionAttack(City city, int i, int value) {
        ACLMessage req = new ACLMessage(ACLMessage.INFORM);
        req.addReceiver(city.getOwner());
        req.setContent("ReligionAttack|" + city.getCoordinates().getX() + "|" + city.getCoordinates().getY() + "|" + i + "|" + value);
        System.out.println(req.getContent());
        send(req);
    }
}
