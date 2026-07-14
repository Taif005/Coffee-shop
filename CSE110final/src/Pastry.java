// File: Pastry.java
public class Pastry extends MenuItem {
    private String flavor;

    public Pastry(String name, double price, String flavor) {
        super(name, price);
        this.flavor = flavor;
    }

    public String getFlavor() {
        return flavor;
    }

    public void setFlavor(String flavor) {
        this.flavor = flavor;
    }

    @Override
    public String displayDetails() {
        return "Pastry: " + getName() + " (" + flavor + "), Price: $" + String.format("%.2f", getPrice());
    }
}