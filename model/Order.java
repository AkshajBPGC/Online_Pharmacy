package model;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

public class Order implements Serializable
{
    private int id; // The ID of the order
    private Patient patient; // The patient who placed the order
    private Map<Medicine, Integer> medicines; // The medicines in the order
    private int totalCost; // The total cost of the order
    private String status; // The status of the order
    private Date date; // The date the order was placed

    public Order(int id, Patient patient, Map<Medicine, Integer> medicines, int totalCost, String status)
    {
        this.id = id;
        this.patient = patient;
        this.medicines = medicines;
        this.totalCost = totalCost;
        this.status = status;
        this.date = new Date(); // Set current date by default
    }

    public int getId()
    {
        return id;
    }

    public Patient getPatient()
    {
        return patient;
    }

    public Map<Medicine, Integer> getMedicines()
    {
        return medicines;
    }

    public int getTotalCost()
    {
        return totalCost;
    }

    public String getStatus()
    {
        return status;
    }

    public Date getDate()
    {
        return date;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }
    
    public void setDate(Date date)
    {
        this.date = date;
    }
    
    // Nested class for emergency orders
    public static class EmergencyOrder extends Order {
        private static final int EMERGENCY_FEE = 500;
        
        // Constructor with same parameters as parent class
        public EmergencyOrder(int id, Patient patient, Map<Medicine, Integer> medicines, int totalCost, String status) {
            super(id, patient, medicines, totalCost + EMERGENCY_FEE, "Transporting"); // Override status and add fee
        }
        
        // Constructor with varargs for medicines
        public EmergencyOrder(int id, Patient patient, int totalCost, Medicine... medicines) {
            super(id, patient, convertToMap(medicines), totalCost + EMERGENCY_FEE, "Transporting");
        }
        
        // Helper method to convert varargs to Map
        private static Map<Medicine, Integer> convertToMap(Medicine... medicines) {
            Map<Medicine, Integer> medicineMap = new java.util.HashMap<>();
            for (Medicine medicine : medicines) {
                medicineMap.put(medicine, medicineMap.getOrDefault(medicine, 0) + 1);
            }
            return medicineMap;
        }
        
        @Override
        public int getTotalCost() {
            return super.getTotalCost(); // Already includes emergency fee
        }
        
        // Get the emergency fee
        public int getEmergencyFee() {
            return EMERGENCY_FEE;
        }
        
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("*** EMERGENCY ORDER ***\n");
            sb.append(super.toString());
            sb.append("Emergency Fee: ").append(EMERGENCY_FEE).append("\n");
            return sb.toString();
        }
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Order ID: ").append(id).append("\n");
        sb.append("Patient: ").append(patient.getName()).append("\n");
        sb.append("Date: ").append(date).append("\n");
        sb.append("Status: ").append(status).append("\n");
        sb.append("Medicines:\n");
        
        for (Map.Entry<Medicine, Integer> entry : medicines.entrySet()) {
            sb.append("  ").append(entry.getKey().getName())
              .append(" - Quantity: ").append(entry.getValue())
              .append(" - Price: ").append(entry.getKey().getPrice())
              .append(" - Total: ").append(entry.getKey().getPrice() * entry.getValue())
              .append("\n");
        }
        
        sb.append("Total Cost: ").append(totalCost).append("\n");
        return sb.toString();
    }
}