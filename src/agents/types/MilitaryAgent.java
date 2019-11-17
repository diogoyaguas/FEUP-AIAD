package agents.types;

import agents.GameAgent;
import game.board.City;

import java.util.ArrayList;

public class MilitaryAgent extends GameAgent {

    /**
     * Logic of player's turn.
     *
     * @return list of new cities conquered.
     */
    @Override
    public ArrayList<City> logic() {

        ArrayList<City> my_new_cities;

        // Upgrade their defenses
        this.moneyToDefenses = 3 * (this.currentMoney / 4);
        this.currentMoney -= this.moneyToDefenses;
        upgradeMyDefenses();
        this.currentMoney += this.moneyToDefenses;

        // Attack opponents 'cities by gathering all of their cities' defenses
        // and in the end redistributes all remaining defenses by their cities
        my_new_cities = attackOpponentCities();

        // Buy empty cities
        this.moneyToBuyEmptyCities = this.currentMoney / 2;
        this.currentMoney -= this.moneyToBuyEmptyCities;
        my_new_cities = buyEmptyCities(my_new_cities);
        this.currentMoney += this.moneyToBuyEmptyCities;

        // Upgrade their cities
        this.moneyToUpgrade = this.currentMoney;
        this.currentMoney -= this.moneyToUpgrade;
        upgradeCities();
        this.currentMoney += this.moneyToUpgrade;

        return my_new_cities;

    }

    /**
     * Attack opponents 'cities by gathering all of their cities' defenses
     * and in the end redistributes all remaining defenses by their cities
     *
     * @return cities that managed to win
     */
    private ArrayList<City> attackOpponentCities() {
        ArrayList<City> my_new_cities = new ArrayList<>();
        if (!this.interactable_cities.isEmpty()) {
            System.out.println("Agent " + getName() + ": Attacking opponents");
            int attackers = getTotalOfDefenses() - 10;
            for (City interacting_city : this.interactable_cities) {
                if (attackers >= interacting_city.getDefences()) {
                    System.out.println("Agent " + getName() + ": Getting new city from " + interacting_city.getOwner().getName());
                    attackers -= interacting_city.getDefences();
                    interacting_city.setOwner(this.getAID());
                    this.thisCityIsNowMine(interacting_city);
                    this.my_cities.add(interacting_city);
                    my_new_cities.add(interacting_city);
                }
            }
            attackers += 10;
            redistributeDefenses(attackers);
        }
        return my_new_cities;
    }

    /**
     * Unite all defenses of all cities
     *
     * @return total of defenses
     */
    private int getTotalOfDefenses() {
        int total = 0;
        for (City my_cities : this.my_cities) {
            total += my_cities.getDefences();
        }
        return total;
    }

    /**
     * Redistributes all defenses across cities
     *
     * @param amountOfDefenses Rest of defenses after attacking cities
     */
    private void redistributeDefenses(int amountOfDefenses) {
        for (City my_cities : this.my_cities) {
            my_cities.addDefences(amountOfDefenses);
        }
    }

}
