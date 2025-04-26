import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

class Prescription implements Serializable
{
    private int id;
    private Doctor doctor;
    private Patient patient;
    private Map<Medicine, Integer> medicines; // Map of medicines and their quantities
    private int refillsAllowed;  // The number of reorders allowed
    private int refillPeriod; // The number of days between refills
    private Date issueDate;
    private Date endDate;

    private int refillsRemaining; // The number of refills remaining
    private Date lastRefillDate; // The date of the last refill

    Prescription(int id, Doctor doctor, Patient patient, Medicine[] medicines, int[] quantity, int refillsAllowed, int refillPeriod, Date issueDate, Date endDate)
    {
        this.id = id;
        this.doctor = doctor;
        this.patient = patient;
        
        // Initialize the medicines map and populate it from the arrays
        this.medicines = new HashMap<>();
        for (int i = 0; i < medicines.length; i++) {
            this.medicines.put(medicines[i], quantity[i]);
        }
        
        this.refillsAllowed = refillsAllowed;
        this.refillPeriod = refillPeriod;
        this.issueDate = issueDate;
        this.endDate = endDate;

        this.refillsRemaining = refillsAllowed;
        this.lastRefillDate = issueDate;
    }
    
    // Additional constructor that directly accepts a Map
    Prescription(int id, Doctor doctor, Patient patient, Map<Medicine, Integer> medicines, 
                int refillsAllowed, int refillPeriod, Date issueDate, Date endDate)
    {
        this.id = id;
        this.doctor = doctor;
        this.patient = patient;
        this.medicines = new HashMap<>(medicines); // Create a copy of the map
        this.refillsAllowed = refillsAllowed;
        this.refillPeriod = refillPeriod;
        this.issueDate = issueDate;
        this.endDate = endDate;

        this.refillsRemaining = refillsAllowed;
        this.lastRefillDate = issueDate;
    }

    public int getId() {
        return id;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    // Updated to return the map of medicines
    public Map<Medicine, Integer> getMedicines() {
        return medicines;
    }
    
    // For backward compatibility - returns just the medicine objects
    public Medicine[] getMedicineArray() {
        return medicines.keySet().toArray(new Medicine[0]);
    }
    
    // For backward compatibility - returns just the quantities
    public int[] getQuantityArray() {
        int[] quantity = new int[medicines.size()];
        int i = 0;
        for (int qty : medicines.values()) {
            quantity[i++] = qty;
        }
        return quantity;
    }

    public int getRefillsAllowed() {
        return refillsAllowed;
    }

    public int getRefillPeriod() {
        return refillPeriod;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public int getRefillsRemaining() {
        return refillsRemaining;
    }

    public Date getLastRefillDate() {
        return lastRefillDate;
    }
    
    public void useRefill() {
        if (refillsRemaining > 0) {
            refillsRemaining--;
            lastRefillDate = new Date(); // Set to current date
        }
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Prescription ID: ").append(id).append("\n");
        sb.append("Doctor: ").append(doctor.getName()).append("\n");
        sb.append("Patient: ").append(patient.getName()).append("\n");
        sb.append("Issue Date: ").append(issueDate).append("\n");
        sb.append("End Date: ").append(endDate).append("\n");
        sb.append("Refills Allowed: ").append(refillsAllowed).append("\n");
        sb.append("Refills Remaining: ").append(refillsRemaining).append("\n");
        sb.append("Medicines:\n");
        
        for (Map.Entry<Medicine, Integer> entry : medicines.entrySet()) {
            sb.append("  ").append(entry.getKey().getName())
              .append(" - Quantity: ").append(entry.getValue());
            if (entry.getKey().isPrescriptionRequired()) {
                sb.append(" (Prescription Required)");
            }
            sb.append("\n");
        }
        
        return sb.toString();
    }
}