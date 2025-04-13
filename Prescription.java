import java.util.Date;

class Prescription
{
    private int id;
    private Doctor doctor;
    private Patient patient;
    private Medicine[] medicines;
    private int[] quantity; // The number of medicines
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
        this.medicines = medicines;
        this.quantity = quantity;
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

    public Medicine[] getMedicines() {
        return medicines;
    }

    public int[] getQuantity() {
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
}