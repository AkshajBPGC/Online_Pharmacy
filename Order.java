import java.io.Serializable;
import java.util.Date;
import java.util.Map;

class Order implements Serializable
{
    private int id; // The ID of the order
    private Patient patient; // The patient who placed the order
    private Map<Medicine, Integer> medicines; // The medicines in the order
    private int totalCost; // The total cost of the order
    private String status; // The status of the order
    private Date date; // The date the order was placed

    Order(int id, Patient patient, Map<Medicine, Integer> medicines, int totalCost, String status)
    {
        this.id = id;
        this.patient = patient;
        this.medicines = medicines;
        this.totalCost = totalCost;
        this.status = status;
        this.date = new Date(); // Set current date by default
    }

    int getId()
    {
        return id;
    }

    Patient getPatient()
    {
        return patient;
    }

    Map<Medicine, Integer> getMedicines()
    {
        return medicines;
    }

    int getTotalCost()
    {
        return totalCost;
    }

    String getStatus()
    {
        return status;
    }

    Date getDate()
    {
        return date;
    }

    void setStatus(String status)
    {
        this.status = status;
    }
    
    void setDate(Date date)
    {
        this.date = date;
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