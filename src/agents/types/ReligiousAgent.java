package agents.types;

import agents.GameAgent;
import game.board.City;

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

        // Defend their cities from religious attacks
        this.moneyToReligion = this.currentMoney / 4;
        this.currentMoney -= this.moneyToReligion;
        defendReligion();
        this.currentMoney += this.moneyToReligion;

        // Attack opponent cities with religious attacks
        this.moneyToAttack += this.currentMoney / 4;
        this.currentMoney -= this.moneyToAttack;
        my_new_cities = attackReligion(my_new_cities);
        this.currentMoney += this.moneyToAttack;

        // Upgrade their defenses
        this.moneyToDefenses = this.currentMoney / 4;
        this.currentMoney -= this.moneyToDefenses;
        upgradeMyDefenses();
        this.currentMoney += this.moneyToDefenses;

        // Buy empty cities
        this.moneyToBuyEmptyCities = this.currentMoney / 4;
        this.currentMoney -= this.moneyToBuyEmptyCities;
        my_new_cities = buyEmptyCities(my_new_cities);
        this.moneyToUpgrade += this.moneyToBuyEmptyCities;

        // Upgrade their cities
        this.moneyToUpgrade = this.currentMoney / 4;
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
            System.out.println("Agent " + getName() + ": Converting opponent cities");
            for (City interacting_city : this.interactive_cities) {
                //TODO atualizar a cidade que estou a interazir com os valores novos de religião
            }
        }
        return my_new_cities;
    }
}
