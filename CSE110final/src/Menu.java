// File: Menu.java
import java.util.ArrayList;
import java.util.List;

public class Menu {
    private static Menu instance;
    private List<MenuItem> items = new ArrayList<>();

    private Menu() {
        items.add(new Drink("Latte", 4.50, "Medium"));
        items.add(new Drink("Espresso", 3.00, "Small"));
        items.add(new Pastry("Croissant", 2.50, "Plain"));
        items.add(new Pastry("Muffin", 3.25, "Blueberry"));
        items.add(new Drink("Iced Coffee", 4.00, "Large"));

    }

    public static Menu getInstance() {
        if (instance == null) {
            instance = new Menu();
        }
        return instance;
    }

    public List<MenuItem> getAllItems() {
        return items;
    }

    public void addItem(MenuItem item) {
        items.add(item);
    }

    public void removeItem(MenuItem item) {
        items.remove(item);
    }

    public void updatePrice(MenuItem item, double newPrice) {
        for (MenuItem menuItem : items) {
            if (menuItem.equals(item)) {
                menuItem.setPrice(newPrice);
                break;
            }
        }
    }
}