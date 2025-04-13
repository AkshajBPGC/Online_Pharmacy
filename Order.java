import java.io.Serializable;
import java.util.Date;
import java.util.Map;

class Order implements Serializable
{
    private int id; // The ID of the order
    private Patient  patient; // The patient who placed the order
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
}