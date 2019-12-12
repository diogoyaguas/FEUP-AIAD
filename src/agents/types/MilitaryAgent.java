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

        my_new_cities = attackOpponentCities();

        // Buy empty cities
        this.moneyToBuyEmptyCities = this.currentMoney / 2;
        my_new_cities = buyEmptyCities(my_new_cities);

        // Upgrade their cities
        this.moneyToUpgrade = this.currentMoney / 2;
        this.currentMoney -= this.moneyToUpgrade;
        upgradeCities();
        this.currentMoney += this.moneyToUpgrade;

        // Upgrade their defenses
        this.moneyToDefenses = this.currentMoney;
        this.currentMoney -= this.moneyToDefenses;
        upgradeMyDefenses();
        this.currentMoney += this.moneyToDefenses;

        // Attack opponents 'cities by gathering all of their cities' defenses
        // and in the end redistributes all remaining defenses by their cities

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
        if (!this.interactive_cities.isEmpty()) {
            int attackers = getTotalOfDefenses() - 10;
            for (City interacting_city : this.interactive_cities) {
                if (interacting_city.getOwner() == getAID()) continue;
                int defenses = requestMessage(interacting_city, "Attack");
                if (attackers >= defenses) {
                    attackers -= defenses;
                    this.thisCityIsNowMine(interacting_city);
                    interacting_city.setOwner(this.getAID());
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
