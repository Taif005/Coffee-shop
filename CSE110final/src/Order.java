// File: Order.java
import java.util.ArrayList;
import java.util.List;

public class Order {
    private List<MenuItem> items;
    private double totalPrice;

    public Order() {
        this.items = new ArrayList<>();
        this.totalPrice = 0.0;
    }

    public List<MenuItem> getItems() {
        return items;
    }

    // Method Overloading
    public void addItem(MenuItem item) {
        this.items.add(item);
        this.totalPrice += item.getPrice();
    }

    public void removeItem(MenuItem item) {
        if (this.items.remove(item)) {
            this.totalPrice -= item.getPrice();
        }
    }

    public double getTotalPrice() {
        return totalPrice;
    }
}