package agents.types;

import agents.GameAgent;
import game.board.City;

import java.util.ArrayList;

public class ReligiousAgent extends GameAgent {

    @Override
    public ArrayList<City> logic() {

        ArrayList<City> my_new_cities = new ArrayList<>();

        // Distribute money for strategy
        this.moneyToReligion = this.currentMoney / 4;
        this.moneyToAttack = this.moneyToReligion;
        this.moneyToDefenses = this.moneyToReligion;
        this.moneyToUpgrade = this.moneyToReligion;
        this.currentMoney = 0;

        // Defend their cities from religious attacks
        defendReligion();
        this.currentMoney += this.moneyToReligion;

        // Attack opponent cities with religious attacks
        my_new_cities = attackReligion(my_new_cities);
        this.currentMoney += this.moneyToAttack;

        // Upgrade their defenses
        upgradeMyDefenses();
        this.currentMoney += this.moneyToDefenses;

        // Buy empty cities
        this.moneyToBuyEmptyCities = this.moneyToUpgrade / 2;
        this.moneyToUpgrade -= this.moneyToBuyEmptyCities;
        my_new_cities = buyEmptyCities(my_new_cities);
        this.moneyToUpgrade += this.moneyToBuyEmptyCities;

        // Upgrade their cities
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
        if (!this.interactable_cities.isEmpty()) {
            System.out.println("Agent " + getName() + ": Converting opponent cities");
            for (City interacting_city : this.interactable_cities) {
                //TODO atualizar a cidade que estou a interazir com os valores novos de religião
            }
        }
        return my_new_cities;
    }
}
