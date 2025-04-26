package model;

import java.io.Serializable;
import service.DataManager;

public class Admin extends User implements Serializable {
    private int id;
    private String name;
    
    public Admin() {
        super();
    }
    
    public Admin(int id, String username, String password, String name) {
        super(username, password);
        this.id = id;
        this.name = name;
    }
    
    public Patient[] getPatients() {
        return DataManager.loadPatients();
    }
    
    public Doctor[] getDoctors() {
        return DataManager.loadDoctors();
    }
    
    public Inventory getMedicines() {
        return DataManager.loadInventory();
    }
    
    public OrderHistory getOrders() {
        return DataManager.loadOrderHistory();
    }
    
    public void addPatient(Patient patient) {
        Patient[] patients = getPatients();
        if (patients == null) {
            patients = new Patient[1];
            patients[0] = patient;
        } else {
            Patient[] newPatients = new Patient[patients.length + 1];
            System.arraycopy(patients, 0, newPatients, 0, patients.length);
            newPatients[patients.length] = patient;
            patients = newPatients;
        }
        DataManager.savePatients(patients);
    }
    
    public void addDoctor(Doctor doctor) {
        Doctor[] doctors = getDoctors();
        if (doctors == null) {
            doctors = new Doctor[1];
            doctors[0] = doctor;
        } else {
            Doctor[] newDoctors = new Doctor[doctors.length + 1];
            System.arraycopy(doctors, 0, newDoctors, 0, doctors.length);
            newDoctors[doctors.length] = doctor;
            doctors = newDoctors;
        }
        DataManager.saveDoctors(doctors);
    }
    
    public void addMedicine(Medicine medicine, int quantity) {
        Inventory inventory = getMedicines();
        if (inventory == null) {
            inventory = new Inventory();
        }
        for (int i = 0; i < quantity; i++) {
            inventory.addToInventory(medicine);
        }
        DataManager.saveInventory(inventory);
    }
    
    public void updateMedicinePrice(Medicine medicine, int newPrice) {
        medicine.setPrice(newPrice);
        Inventory inventory = getMedicines();
        DataManager.saveInventory(inventory);
    }
    
    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
}