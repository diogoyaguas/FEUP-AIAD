package game.board;

import agents.GameAgent;
import jade.core.AID;
import utils.Coordinate;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

public class City implements Comparator {

    private AID owner;
    private Coordinate my_cords;

    private static int maximum_level = 10;

    private int current_level;
    private int cost_to_upgrade;
    private int amount_on_upgrade;
    private int amount_money_producing;

    private int convertion_rate; //pode não ser util
    private int my_religion;
    private HashMap<AID, Integer> religion_attacker;

    private int defences;

    private int city_price;

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
        this.city_price = (int) Math.ceil(this.amount_on_upgrade * 1.2);
        return true;
    }

    public void reset() {
        current_level = 1;
        cost_to_upgrade = 100;
        amount_money_producing = 5;
        amount_on_upgrade = 0;
        my_religion = 100;
        religion_attacker = new HashMap<AID, Integer>();
        defences = 0;
        city_price = 150;
    }

    public void convertCity(AID new_owner) {
        this.owner = new_owner;
        my_religion = 100;
        religion_attacker = new HashMap<AID, Integer>();
    }

    public int maximumReligionConvertionCost(AID changer)
    {
        int percent_not_owned=0;
        if(changer==owner)
        {
            percent_not_owned=100-this.my_religion;
            if(percent_not_owned>50)
                percent_not_owned=50;
            return percent_not_owned*5*((int)Math.ceil(this.getCity_price()*0.05));
        }
        else
        {
            percent_not_owned=100-this.religion_attacker.get(changer);
            if(percent_not_owned>50)
                percent_not_owned=50;
            return percent_not_owned*5*((int)Math.ceil(this.getCity_price()*0.1));
        }
    }
    public int maximumReligionConvertionPercentage(AID changer)
    {
        int percent_not_owned=0;
        if(changer==owner)
        {
            percent_not_owned=100-this.my_religion;
        }
        else
        {
            percent_not_owned=100-this.religion_attacker.get(changer);
        }
        if(percent_not_owned>50)
            percent_not_owned=50;
        return percent_not_owned;
    }

    public Boolean changeReligionAmount(AID changer,int percentage)
    {
        if(changer==owner)
        {
            this.my_religion+=percentage;
            if (this.my_religion>=100)
            {
                this.religion_attacker=new HashMap<>();
                this.my_religion=100;
                return true;
            }
            int attackers_quantity=this.religion_attacker.size();
            int amount_to_reduce=percentage;
            Iterator it = this.religion_attacker.entrySet().iterator();
            while(it.hasNext())
            {
                HashMap.Entry pair=(HashMap.Entry)it.next();
                AID id=(AID)pair.getKey();
                int retirar=amount_to_reduce;
                if(this.religion_attacker.get(id)<amount_to_reduce)
                    retirar=this.religion_attacker.get(id);
                amount_to_reduce-=retirar;
                this.religion_attacker.put(id,((int)pair.getValue())-retirar);
                it.remove();
                if(amount_to_reduce<=0)break;
            }
        }
        else
        {
            int new_amount = this.religion_attacker.get(changer) + percentage;
            if(new_amount>=100){
                new_amount=100;
            }
            this.religion_attacker.put(changer,new_amount);
            this.my_religion-=percentage;
            if(new_amount==100)
                return true;
        }
        return false;
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

    public int getMy_religion() {
        return my_religion;
    }

    public AID getOwner() {
        return this.owner;
    }

    public void setOwner(AID new_owner) {
        this.owner = new_owner;
    }


    @Override
    public int compare(Object o, Object t1) {
        return 0;
    }
}
