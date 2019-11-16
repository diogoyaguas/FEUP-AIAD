package agents.types;

import agents.GameAgent;
import game.board.City;

import java.util.ArrayList;

public class EconomicsAgent extends GameAgent {

    @Override
    public ArrayList<City> logic() {
        ArrayList<City> my_new_cities = new ArrayList<>();
        for (City empty : this.empty_cities) {
            if (this.current_money >= empty.getCity_price()) {
                this.current_money -= empty.getCity_price();
                empty.setOwner(this.getAID());
                empty.reset();
                this.my_cities.add(empty);
                my_new_cities.add(empty);
            }
        }
        for (City my_city : this.my_cities) {
            if (my_city.getMy_religion() != 100) {
                int cost = my_city.maximumReligionConvertionCost(this.getAID());
                if (this.current_money >= cost) {
                    this.current_money -= cost;
                    my_city.changeReligionAmount(this.getAID(), my_city.maximumReligionConvertionPercentage(this.getAID()));
                }
            }
        }
        int money_for_upgrading = this.current_money / 3;
        int money_for_attacking = this.current_money / 3;
        int money_for_defenses = this.current_money / 3;
        for (City interracting_city : this.interactable_cities) {
            int cost = interracting_city.getCity_price();
            if (money_for_attacking >= cost) {
                this.current_money -= cost;
                money_for_attacking -= cost;
                interracting_city.setOwner(this.getAID());
                this.thisCityIsNowMine(interracting_city);
                this.my_cities.add(interracting_city);
            }
        }
        money_for_upgrading += money_for_attacking;
        for (City my_city : this.my_cities) {

        }
        return my_new_cities;
    }

}
