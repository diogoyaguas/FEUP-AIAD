package agents.types;

import agents.GameAgent;
import game.board.City;
import jade.core.AID;
import javafx.util.Pair;

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
                int current_my_religion = 0;
                int i = 0;
                boolean exists = false;
                for (Pair<AID, Integer> pair : interacting_city.getReligion_attacker()) {
                    if (pair.getKey().equals(this.getAID())) {
                        current_my_religion = pair.getValue();
                        exists = true;
                        break;
                    }
                    i++;
                }
                if (!exists)
                    i = -1;
                int value_to_attack = 100 - current_my_religion;
                if (value_to_attack > 50) value_to_attack = 50;
                // TODO perguntar o nível da religiao deste gajo ao outro gajo para ser usado em baixo
                int cost_to_attack = interacting_city.costOfReligion(value_to_attack);
                if (this.moneyToAttack >= cost_to_attack) {
                    this.moneyToAttack -= cost_to_attack;
                    if (current_my_religion + value_to_attack >= 100) {
                        interacting_city.resetReligion();
                        this.thisCityIsNowMine(interacting_city);
                        this.my_cities.add(interacting_city);
                        my_new_cities.add(interacting_city);
                        // TODO avisar o outro gajo que a cidade foi convertida para este gajo e que já não lhe pertence
                    } else {
                        interacting_city.setReligionAttacker(i, new Pair<AID, Integer>(this.getAID(), current_my_religion + value_to_attack));
                        //TODO avisar a outra cidade que estou a mudar o valor
                    }
                }
            }
        }
        return my_new_cities;
    }
}
