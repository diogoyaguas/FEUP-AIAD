package game.board;

import agents.GameAgent;

import java.util.HashMap;

public class City {

    private GameAgent owner;

    private static int maximum_level = 10;

    private int current_level;
    private int cost_to_upgrade;
    private int amount_on_upgrade;
    private int amount_money_producing;

    private int convertion_rate; //pode não ser util
    private int religion_owner;
    private HashMap<GameAgent, Integer> religion_attacker;

    private int defences;

    private int city_price;

    public City(GameAgent owner) {
        this.owner=owner;
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
        this.city_price= (int) Math.ceil(this.amount_on_upgrade*1.2);
        return true;
    }

    public void reset() {
        current_level = 1;
        cost_to_upgrade = 100;
        amount_money_producing = 5;
        amount_on_upgrade = 0;
        religion_owner = 100;
        religion_attacker = new HashMap<GameAgent, Integer>();
        defences = 0;
        city_price=50;
    }

    public void convertCity(GameAgent new_owner)
    {
        this.owner=new_owner;
        religion_owner = 100;
        religion_attacker = new HashMap<GameAgent, Integer>();
        
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

    public GameAgent getOwner()
    {
        return this.owner;
    }

    public void setOwner(GameAgent new_owner)
    {
        this.owner=new_owner;
    }

}
