package spendingManager.domain;

import java.util.List;
//money spend on lunch or dinner
public class Spending {

    private final long id;
    private final String name; // name of the food
    private final double amount; //in dollars

    public Spending(long id, String name, double amount) {
        this.id = id;
        this.name = name;
        this.amount = amount;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getAmount() {
        return amount;
    }
}