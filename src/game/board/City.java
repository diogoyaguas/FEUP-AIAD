package game.board;

import jade.core.AID;
import javafx.util.Pair;
import utils.Coordinate;

import java.util.*;

public class City implements Comparable {

    private AID owner;
    private Coordinate my_cords;

    private static int maximum_level = 10;

    private int current_level;
    private int cost_to_upgrade;
    private int amount_on_upgrade;
    private int amount_money_producing;

    private int convertion_rate; //pode não ser util
    private ArrayList<Pair<AID, Integer>> religion_attacker;

    private int defences;

    private int city_price;

    /**
     * Calculates how much money a percentagem of this religion is worth
     * @param value The percentage I want to calculate
     * @return The amount of money this percentage costs
     */
    public int costOfReligion(int value)
    {
         return ((int) Math.ceil(this.amount_on_upgrade * 2 / 100))*value;
    }

    public void sortReligionAttackers()
    {
        Collections.sort(this.religion_attacker, new Comparator<Pair<AID, Integer>>() {
            @Override
            public int compare(final Pair<AID, Integer> o1, final Pair<AID, Integer> o2) {
                if(o1.getValue() <= o2.getValue())
                    return 1;
                return 0;
            }
        });
    }

    public City(AID owner, Coordinate cord) {
        this.my_cords = cord;
        this.owner = owner;
        this.reset();
    }

    public int addDefences(int amount) {
        this.defences += amount;
        return this.defences;
    }

    public int reduceDefences(int amount) {
        this.defences -= amount;
        if (this.defences < 0) this.defences = 0;
        return this.defences;
    }

    public Boolean upgradeCity() {
        if (current_level + 1 > City.maximum_level)
            return false;
        this.current_level++;
        this.amount_on_upgrade += this.cost_to_upgrade;
        this.cost_to_upgrade *= 2;
        this.amount_money_producing *= 2;
        this.city_price = (int) Math.ceil(this.amount_on_upgrade * 2);
        return true;
    }

    public void reset() {
        current_level = 1;
        cost_to_upgrade = 100;
        amount_money_producing = 5;
        amount_on_upgrade = 0;
        religion_attacker = new ArrayList<>();
        defences = 0;
        city_price = 150;
    }

    public void convertCity(AID new_owner) {
        this.owner = new_owner;
        religion_attacker = new ArrayList<>();
    }


    public int getCurrentLeve() {
        return this.current_level;
    }

    public int getCostUpgrade() {
        return this.cost_to_upgrade;
    }

    public int getMoneyProduced() {
        return this.amount_money_producing;
    }

    public int getDefences() {
        return this.defences;
    }

    public int getCity_price() {
        return city_price;
    }

    public AID getOwner() {
        return this.owner;
    }

    public void setOwner(AID new_owner) {
        this.owner = new_owner;
    }

    public Coordinate getMy_cords() {
        return my_cords;
    }

    public ArrayList<Pair<AID, Integer>> getReligion_attacker() {
        return religion_attacker;
    }


    @Override
    public int compareTo(Object o) {
        if(this.current_level<((City)o).current_level)
            return 1;
        return 0;
    }
}
