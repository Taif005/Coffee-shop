// File: Admin.java
// File: Admin.java
public class Admin {
    private static final String PASSWORD = "admin123";
    private Menu menu; public Admin(Menu menu) {
        this.menu = menu;
    }
    public boolean login(String password) {
        return PASSWORD.equals(password);
    }
    public void addMenuItem(MenuItem item) {
        menu.addItem(item);
    }
    public void removeMenuItem(MenuItem item) {
        menu.removeItem(item);
    }
    public void updateMenuItemPrice(MenuItem item, double newPrice) {
        menu.updatePrice(item, newPrice);
    }
}