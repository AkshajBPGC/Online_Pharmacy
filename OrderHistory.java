import java.io.Serializable;
import java.util.Arrays;

class OrderHistory implements Serializable
{
    private Order[] orderHistory;
    private int numOrders;
    
    public OrderHistory() {
        this.orderHistory = new Order[100]; // Initial capacity
        this.numOrders = 0;
    }
    
    public void addOrder(Order order) {
        if (numOrders >= orderHistory.length) {
            // Expand the array if needed
            Order[] newOrderHistory = new Order[orderHistory.length * 2];
            System.arraycopy(orderHistory, 0, newOrderHistory, 0, orderHistory.length);
            orderHistory = newOrderHistory;
        }
        orderHistory[numOrders++] = order;
    }
    
    public Order[] getOrders() {
        Order[] validOrders = new Order[numOrders];
        System.arraycopy(orderHistory, 0, validOrders, 0, numOrders);
        return validOrders;
    }
    
    public Order getOrderById(int id) {
        for (int i = 0; i < numOrders; i++) {
            if (orderHistory[i].getId() == id) {
                return orderHistory[i];
            }
        }
        return null;
    }
    
    public Order[] getOrdersByPatient(Patient patient) {
        int count = 0;
        for (int i = 0; i < numOrders; i++) {
            if (orderHistory[i].getPatient().getId() == patient.getId()) {
                count++;
            }
        }
        
        Order[] patientOrders = new Order[count];
        int index = 0;
        for (int i = 0; i < numOrders; i++) {
            if (orderHistory[i].getPatient().getId() == patient.getId()) {
                patientOrders[index++] = orderHistory[i];
            }
        }
        
        return patientOrders;
    }
    
    public Order[] getOrdersByStatus(String status) {
        int count = 0;
        for (int i = 0; i < numOrders; i++) {
            if (orderHistory[i].getStatus().equals(status)) {
                count++;
            }
        }
        
        Order[] statusOrders = new Order[count];
        int index = 0;
        for (int i = 0; i < numOrders; i++) {
            if (orderHistory[i].getStatus().equals(status)) {
                statusOrders[index++] = orderHistory[i];
            }
        }
        
        return statusOrders;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Order History:\n");
        for (int i = 0; i < numOrders; i++) {
            sb.append("Order ID: ").append(orderHistory[i].getId())
              .append(", Patient: ").append(orderHistory[i].getPatient().getName())
              .append(", Cost: ").append(orderHistory[i].getTotalCost())
              .append(", Status: ").append(orderHistory[i].getStatus())
              .append("\n");
        }
        return sb.toString();
    }
}
