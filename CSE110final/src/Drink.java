// File: Drink.java
public class Drink extends MenuItem {
    private String size;

    public Drink(String name, double price, String size) {
        super(name, price);
        this.size = size;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @Override
    public String displayDetails() {
        return "Drink: " + getName() + " (" + size + "), Price: $" + String.format("%.2f", getPrice());
    }
}