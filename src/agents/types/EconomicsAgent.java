package agents.types;

import agents.GameAgent;
import game.board.City;

import java.util.ArrayList;

public class EconomicsAgent extends GameAgent {

    @Override
    public ArrayList<City> logic() {

        ArrayList<City> my_new_cities = new ArrayList<>();

        my_new_cities = buyEmptyCities(my_new_cities, this.current_money);

        int money_for_upgrading = this.current_money / 4;
        int money_for_attacking = money_for_upgrading;
        int money_for_defenses = money_for_upgrading;
        int money_for_religion = money_for_upgrading;
        this.current_money = 0;
        money_for_upgrading += this.defendReligion(money_for_religion);

        my_new_cities = buyOpponentsCities(my_new_cities, money_for_attacking);

        this.current_money += money_for_attacking;
        this.upgradeMyDefenses(money_for_defenses);
        this.current_money += money_for_defenses;

        upgradeCities(money_for_upgrading);

        this.current_money += money_for_upgrading;

        return my_new_cities;
    }

    private ArrayList<City> buyOpponentsCities(ArrayList<City> my_new_cities, int money_for_attacking) {
        for (City city : this.interactable_cities) {
            if (money_for_attacking >= city.getCity_price()) {
                money_for_attacking -= city.getCity_price();
                city.setOwner(this.getAID());
                this.thisCityIsNowMine(city);
                this.my_cities.add(city);
                this.pos.add(city.getMy_cords());
                my_new_cities.add(city);
            }
        }
        return my_new_cities;
    }

}
