package agents.types;

import agents.GameAgent;
import game.board.City;

import java.util.ArrayList;

public class MilitaryAgent extends GameAgent {

    @Override
    public ArrayList<City> logic() {
        ArrayList<City> my_new_cities;

        int moneyToBuyEmptyCities = this.current_money / 4;
        my_new_cities = buyEmptyCities(moneyToBuyEmptyCities);

        return my_new_cities;

    }

}
