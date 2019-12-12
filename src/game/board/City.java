package game.board;

import jade.core.AID;
import utils.Coordinate;
import utils.Pair;

import java.util.ArrayList;
import java.util.Objects;

public class City implements Comparable {

    private static int maximum_level = 10;
    private AID owner;
    private Coordinate my_cords;
    private int city_price;
    private int current_level;
    private int cost_to_upgrade;

    private int amount_money_producing;
    private int defences;
    private int amount_on_upgrade;

    private ArrayList<Pair<AID, Integer>> religion_attacker;

    /**
     * Create a new city.
     *
     * @param owner owner of the city
     * @param cord  coordinates of the city.
     */
    public City(AID owner, Coordinate cord) {
        this.my_cords = cord;
        this.owner = owner;
        this.reset();
    }

    /**
     * Calculates how much money a percentage of this religion is worth
     *
     * @param value The percentage I want to calculate
     * @return The amount of money this percentage costs
     */
    public int costOfReligion(int value) {
        return ((int) Math.ceil(this.amount_on_upgrade * 1.5 / 100)) * value;
    }

    /**
     * Sort religious attackers for higher value
     */
    public void sortReligionAttackers() {
        this.religion_attacker.sort((o1, o2) -> {
            if (o1.getValue() < o2.getValue())
                return 1;
            return 0;
        });
    }

    /**
     * Add defences to city.
     *
     * @param amount amount of defenses.
     * @return total of defenses.
     */
    public int addDefences(int amount) {
        this.defences += amount;
        return this.defences;
    }

    /**
     * Reduce defences of a city.
     *
     * @param amount amount of defences.
     * @return total of defenses.
     */
    public int reduceDefences(int amount) {
        this.defences -= amount;
        if (this.defences < 0) this.defences = 0;
        return this.defences;
    }

    /**
     * Upgrade city.
     *
     * @return possibility of upgrade.
     */
    public Boolean upgradeCity() {
        if ((this.current_level + 1) > maximum_level)
            return false;
        this.current_level++;
        this.amount_on_upgrade += this.cost_to_upgrade;
        this.cost_to_upgrade *= 2;
        this.amount_money_producing *= 2;
        this.city_price = (int) Math.ceil(this.amount_on_upgrade * 4);
        return true;
    }

    /**
     * Reset a city to base level.
     */
    public void reset() {
        current_level = 1;
        cost_to_upgrade = 100;
        amount_money_producing = 25;
        amount_on_upgrade = 0;
        religion_attacker = new ArrayList<>();
        defences = 0;
        city_price = 10;
    }

    public void resetReligion() {
        religion_attacker = new ArrayList<>();
    }

    /**
     * Convert city to a new owner.
     *
     * @param new_owner new owner.
     */
    public void convertCity(AID new_owner) {
        this.owner = new_owner;
        religion_attacker = new ArrayList<>();
    }

    /**
     * Get current level of city.
     *
     * @return level of city.
     */
    public int getCurrentLevel() {
        return this.current_level;
    }

    /**
     * Get cost to upgrade city.
     *
     * @return cost to upgrade city.
     */
    public int getCostUpgrade() {
        return this.cost_to_upgrade;
    }

    /**
     * Get money that city produce.
     *
     * @return money that city produce.
     */
    public int getMoneyProduced() {
        return this.amount_money_producing;
    }

    /**
     * Get total of defences.
     *
     * @return total of defences.
     */
    public int getDefences() {
        return this.defences;
    }

    /**
     * Get city price.
     *
     * @return city price.
     */
    public int getCity_price() {
        return city_price;
    }

    /**
     * Get owner of city.
     *
     * @return owner of city.
     */
    public AID getOwner() {
        return this.owner;
    }

    /**
     * Set new owner.
     *
     * @param new_owner new owner.
     */
    public void setOwner(AID new_owner) {
        this.owner = new_owner;
    }

    /**
     * Get coordinates of city.
     *
     * @return coordinates of city.
     */
    public Coordinate getCoordinates() {
        return my_cords;
    }

    /**
     * Get religious attackers.
     *
     * @return religious attackers.
     */
    public ArrayList<Pair<AID, Integer>> getReligion_attacker() {
        return religion_attacker;
    }

    public void setReligionAttacker(int index, Pair<AID, Integer> pair) {
        if (index != -1)
            this.religion_attacker.remove(index);
        this.religion_attacker.add(pair);
    }

    /**
     * Compare two cities.
     *
     * @param o city to compare.
     * @return comparision.
     */
    @Override
    public int compareTo(Object o) {
        if (this.current_level < ((City) o).current_level)
            return 1;
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        City city = (City) o;
        return Objects.equals(my_cords, city.my_cords);
    }

    @Override
    public int hashCode() {

        return Objects.hash(my_cords);
    }
}
