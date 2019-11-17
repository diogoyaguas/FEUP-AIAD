package agents.types;

import agents.GameAgent;
import game.board.City;

import java.util.ArrayList;

public class ReligiousAgent extends GameAgent {

    @Override
    public ArrayList<City> logic() {
        ArrayList<City> my_new_cities = new ArrayList<>();

        int moneyForDefenseReligion = this.current_money / 4;
        int moneyForAttackReligion = moneyForDefenseReligion;
        int moneyToBuyDefenses = moneyForDefenseReligion;
        int moneyForUpgrading = moneyForDefenseReligion;
        this.current_money = 0;

        this.current_money += defendReligion(moneyForDefenseReligion);

        my_new_cities = attackReligion(my_new_cities, moneyForAttackReligion);

        upgradeMyDefenses(moneyToBuyDefenses);

        int moneyToBuyEmptyCities = moneyForUpgrading / 2;
        my_new_cities = buyEmptyCities(my_new_cities, moneyToBuyEmptyCities);

        int moneyToUpgradeCities = moneyForUpgrading / 2;
        upgradeCities(moneyToUpgradeCities);

        return my_new_cities;
    }

    private ArrayList<City> attackReligion(ArrayList<City> my_new_cities, int moneyForAttackReligion) {
        if (!this.interactable_cities.isEmpty()) {
            System.out.println("Agent " + getName() + ": Converting opponent cities");
            for (City interacting_city : this.interactable_cities) {

            }
        }
        return my_new_cities;
    }
}
