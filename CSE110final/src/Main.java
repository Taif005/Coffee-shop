// File: CoffeeShopApp.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class Main extends JFrame {
    private Order currentOrder = new Order();
    private Menu menu = Menu.getInstance();
    private Admin admin = new Admin(menu);
    private DefaultListModel<MenuItem> menuListModel = new DefaultListModel<>();
    private DefaultListModel<MenuItem> orderListModel = new DefaultListModel<>();

    private JList<MenuItem> menuList;
    private JList<MenuItem> orderList;
    private JLabel totalLabel;

    // Custom Exception
    class NoItemsInOrderException extends Exception {
        public NoItemsInOrderException(String message) {
            super(message);
        }
    }

    public Main() {
        super("☕ Coffee Shop Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        initializeMenu();

        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        mainPanel.add(createMenuPanel());
        mainPanel.add(createOrderPanel());

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton adminLoginButton = new JButton("Admin Login");
        adminLoginButton.addActionListener(this::showAdminLogin);
        headerPanel.add(adminLoginButton);

        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initializeMenu() {
        menuListModel.clear();
        for (MenuItem item : menu.getAllItems()) {
            menuListModel.addElement(item);
        }
    }

    private JPanel createMenuPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Menu Items"));

        menuList = new JList<>(menuListModel);
        menuList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JButton addToOrderButton = new JButton("Add to Order");
        addToOrderButton.addActionListener(this::addToOrder);

        panel.add(new JScrollPane(menuList), BorderLayout.CENTER);
        panel.add(addToOrderButton, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createOrderPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Current Order"));

        orderList = new JList<>(orderListModel);

        totalLabel = new JLabel("Total: $0.00", JLabel.CENTER);
        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 18));

        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 5, 5));
        JButton removeFromOrderButton = new JButton("Remove");
        JButton clearOrderButton = new JButton("Clear");
        JButton checkoutButton = new JButton("Checkout");

        removeFromOrderButton.addActionListener(this::removeFromOrder);
        clearOrderButton.addActionListener(this::clearOrder);
        checkoutButton.addActionListener(this::checkout);

        buttonPanel.add(removeFromOrderButton);
        buttonPanel.add(clearOrderButton);
        buttonPanel.add(checkoutButton);

        panel.add(totalLabel, BorderLayout.NORTH);
        panel.add(new JScrollPane(orderList), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void showAdminLogin(ActionEvent e) {
        JPasswordField passwordField = new JPasswordField();
        int option = JOptionPane.showConfirmDialog(this, passwordField, "Admin Login", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            String password = new String(passwordField.getPassword());
            if (admin.login(password)) {
                showAdminPanel();
            } else {
                JOptionPane.showMessageDialog(this, "Incorrect password.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showAdminPanel() {
        JDialog adminDialog = new JDialog(this, "Admin Panel", true);
        adminDialog.setLayout(new GridLayout(4, 1, 10, 10));
        adminDialog.setSize(400, 300);

        JButton addMenuItemButton = new JButton("Add New Menu Item");
        JButton removeMenuItemButton = new JButton("Remove Menu Item");
        JButton updatePriceButton = new JButton("Update Item Price");

        addMenuItemButton.addActionListener(e -> showAddItemDialog());
        removeMenuItemButton.addActionListener(e -> showRemoveItemDialog());
        updatePriceButton.addActionListener(e -> showUpdatePriceDialog());

        adminDialog.add(addMenuItemButton);
        adminDialog.add(removeMenuItemButton);
        adminDialog.add(updatePriceButton);
        adminDialog.setLocationRelativeTo(this);
        adminDialog.setVisible(true);
    }

    private void showAddItemDialog() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        JTextField nameField = new JTextField();
        JTextField priceField = new JTextField();
        JComboBox<String> typeField = new JComboBox<>(new String[]{"Drink", "Pastry"});

        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Price:"));
        panel.add(priceField);
        panel.add(new JLabel("Type:"));
        panel.add(typeField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add New Item", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String name = nameField.getText();
                double price = Double.parseDouble(priceField.getText());
                String type = (String) typeField.getSelectedItem();

                if (type.equals("Drink")) {
                    String size = JOptionPane.showInputDialog(this, "Enter size (e.g., Small, Medium, Large):");
                    admin.addMenuItem(new Drink(name, price, size));
                } else {
                    String flavor = JOptionPane.showInputDialog(this, "Enter flavor (e.g., Chocolate, Plain):");
                    admin.addMenuItem(new Pastry(name, price, flavor));
                }

                initializeMenu();
                JOptionPane.showMessageDialog(this, "Item added successfully.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid price format.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showRemoveItemDialog() {
        try {
            MenuItem selectedItem = (MenuItem) JOptionPane.showInputDialog(this,
                    "Select an item to remove:",
                    "Remove Item",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    menu.getAllItems().toArray(),
                    menu.getAllItems().get(0));

            // This line would throw a NullPointerException if selectedItem is null
            admin.removeMenuItem(selectedItem);

            initializeMenu();
            JOptionPane.showMessageDialog(this, "Item removed successfully.");
        } catch (NullPointerException e) {
            JOptionPane.showMessageDialog(this, "No item was selected.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showUpdatePriceDialog() {
        try {
            MenuItem selectedItem = (MenuItem) JOptionPane.showInputDialog(this,
                    "Select an item to update price:",
                    "Update Price",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    menu.getAllItems().toArray(),
                    menu.getAllItems().get(0));

            // This line would throw a NullPointerException if selectedItem is null
            String newPriceString = JOptionPane.showInputDialog(this, "Enter new price for " + selectedItem.getName() + ":");

            double newPrice = Double.parseDouble(newPriceString);
            admin.updateMenuItemPrice(selectedItem, newPrice);
            initializeMenu();
            JOptionPane.showMessageDialog(this, "Price updated successfully.");
        } catch (NullPointerException e) {
            JOptionPane.showMessageDialog(this, "No item was selected or no price was entered.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid price format.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addToOrder(ActionEvent e) {
        try {
            MenuItem selectedItem = menuList.getSelectedValue();
            // This line would throw a NullPointerException if selectedItem is null
            currentOrder.addItem(selectedItem);
            orderListModel.addElement(selectedItem);
            updateTotalLabel();
        } catch (NullPointerException ex) {
            JOptionPane.showMessageDialog(this, "Please select an item to add.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removeFromOrder(ActionEvent e) {
        try {
            MenuItem selectedItem = orderList.getSelectedValue();
            // This line would throw a NullPointerException if selectedItem is null
            currentOrder.removeItem(selectedItem);
            orderListModel.removeElement(selectedItem);
            updateTotalLabel();
        } catch (NullPointerException ex) {
            JOptionPane.showMessageDialog(this, "Please select an item to remove.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearOrder(ActionEvent e) {
        currentOrder = new Order();
        orderListModel.clear();
        updateTotalLabel();
    }

    private void checkout(ActionEvent e) {
        try {
            if (currentOrder.getItems().isEmpty()) {
                throw new NoItemsInOrderException("Cannot checkout with an empty order.");
            }

            // Collect customer details
            JPanel customerPanel = new JPanel(new GridLayout(0, 2, 5, 5));
            JTextField nameField = new JTextField();
            JTextField phoneField = new JTextField();
            JTextField addressField = new JTextField();

            customerPanel.add(new JLabel("Name:"));
            customerPanel.add(nameField);
            customerPanel.add(new JLabel("Phone Number:"));
            customerPanel.add(phoneField);
            customerPanel.add(new JLabel("Address:"));
            customerPanel.add(addressField);

            int result = JOptionPane.showConfirmDialog(this, customerPanel, "Enter Your Details", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                String name = nameField.getText();
                String phone = phoneField.getText();
                String address = addressField.getText();

                // Validate input
                if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please fill in all details.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Confirmation message
                JOptionPane.showMessageDialog(this, "Order confirmed for " + name + "!\nTotal amount: $" + String.format("%.2f", currentOrder.getTotalPrice()), "Order Confirmed", JOptionPane.INFORMATION_MESSAGE);
                clearOrder(e); // Clear the order after successful checkout
            }
        } catch (NoItemsInOrderException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTotalLabel() {
        totalLabel.setText("Total: $" + String.format("%.2f", currentOrder.getTotalPrice()));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main());
    }
}