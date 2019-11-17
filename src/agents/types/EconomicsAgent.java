package agents.types;

import agents.GameAgent;
import game.board.City;

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

        // Distribute the remaining money
        this.moneyToUpgrade = this.currentMoney / 4;
        this.moneyToAttack = this.moneyToUpgrade;
        this.moneyToDefenses = this.moneyToUpgrade;
        this.moneyToReligion = this.moneyToUpgrade;
        this.currentMoney = 0;

        // Defend their cities from religious attacks
        defendReligion();
        this.currentMoney += this.moneyToReligion;

        // Buy opponents cities
        my_new_cities = buyOpponentsCities(my_new_cities);
        this.currentMoney += this.moneyToAttack;

        // Upgrade their defenses
        this.upgradeMyDefenses();
        this.currentMoney += this.moneyToDefenses;

        // Upgrade their cities
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
        for (City city : this.interactable_cities) {
            if (this.moneyToAttack >= city.getCity_price()) {
                this.moneyToAttack -= city.getCity_price();
                city.setOwner(this.getAID());
                this.thisCityIsNowMine(city);
                this.my_cities.add(city);
                this.pos.add(city.getCoordinates());
                my_new_cities.add(city);
            }
        }
        return my_new_cities;
    }

}
