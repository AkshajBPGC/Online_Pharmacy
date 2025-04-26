package service;

import model.*;
import main.Pharmacy;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class OrderService implements Serializable
{
    private Pharmacy pharmacy;
    private OrderHistory orderHistory;
    
    public OrderService(Pharmacy pharmacy) {
        this.pharmacy = pharmacy;
        this.orderHistory = pharmacy.getOrderHistory();
    }

    public Order placeOrder(Patient patient, Cart cart)
    {
        int totalCost = 0;
        for (Map.Entry<Medicine, Integer> entry : cart.cart.entrySet())
        {
            totalCost += entry.getKey().getPrice() * entry.getValue();
        }

        if (patient.getWallet() < totalCost)
        {
            System.out.println("Insufficient funds in wallet. Order cancelled.");
            return null;
        }
        
        // Check inventory availability
        for (Map.Entry<Medicine, Integer> entry : cart.cart.entrySet()) {
            Medicine medicine = entry.getKey();
            int quantity = entry.getValue();
            int available = 0;
            
            // Check if medicine exists in inventory and has enough quantity
            if (pharmacy.getInventory().inventory.containsKey(medicine)) {
                available = pharmacy.getInventory().inventory.get(medicine);
            }
            
            if (available < quantity) {
                System.out.println("Insufficient stock for " + medicine.getName() + ". Available: " + available + ", Required: " + quantity);
                return null;
            }
        }
        
        // Process the order
        int orderId = UUID.randomUUID().hashCode();
        Order order = new Order(orderId, patient, cart.getCart(), totalCost, "Placed");
        order.setDate(new Date()); // Set current date
        
        // Update patient wallet
        patient.subtractFromWallet(totalCost);
        
        // Update inventory
        for (Map.Entry<Medicine, Integer> entry : cart.cart.entrySet()) {
            Medicine medicine = entry.getKey();
            int quantity = entry.getValue();
            
            for (int i = 0; i < quantity; i++) {
                pharmacy.getInventory().removeFromInventory(medicine);
            }
        }
        
        // Add order to history
        patient.addOrder(order);
        orderHistory.addOrder(order);
        
        // Clear cart
        cart.emptyCart();
        
        // Save changes
        pharmacy.saveAllData();
        
        System.out.println("Order placed successfully! Order ID: " + orderId);
        return order;
    }
    
    public Order getOrderById(int orderId) {
        return orderHistory.getOrderById(orderId);
    }
    
    public Order[] getOrdersByPatient(Patient patient) {
        return orderHistory.getOrdersByPatient(patient);
    }
    
    public Order[] getActiveOrders() {
        return orderHistory.getOrdersByStatus("Placed");
    }
    
    public void updateOrderStatus(int orderId, String status) {
        Order order = orderHistory.getOrderById(orderId);
        if (order != null) {
            order.setStatus(status);
            pharmacy.saveAllData();
        }
    }
    
    public boolean cancelOrder(int orderId) {
        Order order = orderHistory.getOrderById(orderId);
        if (order != null && order.getStatus().equals("Placed")) {
            // Return items to inventory
            for (Map.Entry<Medicine, Integer> entry : order.getMedicines().entrySet()) {
                Medicine medicine = entry.getKey();
                int quantity = entry.getValue();
                
                for (int i = 0; i < quantity; i++) {
                    pharmacy.getInventory().addToInventory(medicine);
                }
            }
            
            // Refund patient
            order.getPatient().addToWallet(order.getTotalCost());
            
            // Update order status
            order.setStatus("Cancelled");
            
            // Save changes
            pharmacy.saveAllData();
            
            System.out.println("Order cancelled successfully! Funds refunded to wallet.");
            return true;
        }
        
        System.out.println("Order not found or cannot be cancelled.");
        return false;
    }
}
