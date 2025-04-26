import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Doctor extends User implements Serializable
{
    private int id;
    private String name;
    private String phone;
    private int age;
    private int consultationFees;
    private List<Prescription> prescriptionsIssued;
    
    public Doctor() {
        super();
        prescriptionsIssued = new ArrayList<>();
    }
    
    public Doctor(int id, String username, String password, String name, String phone, int age, int consultationFees) {
        super(username, password);
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.age = age;
        this.consultationFees = consultationFees;
        this.prescriptionsIssued = new ArrayList<>();
    }
    
    // Original method that accepts arrays - kept for backward compatibility
    public Prescription prescribe(int prescriptionId, Patient patient, Medicine[] medicines, int[] quantity, 
                                 int refillsAllowed, int refillPeriod, Date issueDate, Date endDate) {
        Prescription prescription = new Prescription(prescriptionId, this, patient, medicines, quantity, 
                                                    refillsAllowed, refillPeriod, issueDate, endDate);
        prescriptionsIssued.add(prescription);
        patient.addPrescription(prescription);
        return prescription;
    }
    
    // New method that accepts a Map of medicine to quantity
    public Prescription prescribe(int prescriptionId, Patient patient, Map<Medicine, Integer> medicines,
                                 int refillsAllowed, int refillPeriod, Date issueDate, Date endDate) {
        Prescription prescription = new Prescription(prescriptionId, this, patient, medicines,
                                                    refillsAllowed, refillPeriod, issueDate, endDate);
        prescriptionsIssued.add(prescription);
        patient.addPrescription(prescription);
        return prescription;
    }
    
    // Convenience method to create a prescription with a single medicine
    public Prescription prescribeSingleMedicine(int prescriptionId, Patient patient, Medicine medicine, int quantity,
                                              int refillsAllowed, int refillPeriod, Date issueDate, Date endDate) {
        Map<Medicine, Integer> medicines = new HashMap<>();
        medicines.put(medicine, quantity);
        return prescribe(prescriptionId, patient, medicines, refillsAllowed, refillPeriod, issueDate, endDate);
    }
    
    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public int getAge() {
        return age;
    }
    
    public int getConsultationFees() {
        return consultationFees;
    }
    
    public List<Prescription> getPrescriptionsIssued() {
        return prescriptionsIssued;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public void setAge(int age) {
        this.age = age;
    }
    
    public void setConsultationFees(int consultationFees) {
        this.consultationFees = consultationFees;
    }
}