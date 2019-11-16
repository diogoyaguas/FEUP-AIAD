package agents.types;

import agents.GameAgent;
import game.board.City;

import java.util.ArrayList;

public class MilitaryAgent extends GameAgent {

    @Override
    public ArrayList<City> logic() {
        ArrayList<City> my_new_cities;

        int moneyToBuyDefenses = 3 * (this.current_money / 4);
        upgradeMyDefenses(moneyToBuyDefenses);
        this.current_money -= moneyToBuyDefenses;

        my_new_cities = attackOpponentCities();

        int moneyToBuyEmptyCities = this.current_money;
        my_new_cities=buyEmptyCities(my_new_cities,moneyToBuyEmptyCities);

        return my_new_cities;

    }

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

    private int getTotalOfDefenses() {
        int total = 0;
        for (City my_cities : this.my_cities) {
            total += my_cities.getDefences();
        }
        return total;
    }

    private void redistributeDefenses(int amountOfDefenses) {
        for (City my_cities : this.my_cities) {
            my_cities.addDefences(amountOfDefenses);
        }
    }

}
