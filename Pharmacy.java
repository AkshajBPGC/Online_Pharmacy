import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class Pharmacy implements Serializable
{
    private String name;
    public Inventory inventory;
    private Patient[] patients;
    private Doctor[] doctors;
    private OrderHistory orderHistory;

    Pharmacy(String name)
    {
        this.name = name;
        
        // Initialize data folder and files
        File folder = new File("data/");
        if (!folder.exists())
        {
            folder.mkdir();
        }
        
        // Load all system data
        DataManager.loadData();
        
        this.inventory = DataManager.loadInventory();
        if (inventory == null)
        {
            inventory = new Inventory();
        }
        
        this.patients = DataManager.loadPatients();
        if (patients == null)
        {
            patients = new Patient[1000];
        }
        
        this.doctors = DataManager.loadDoctors();
        if (doctors == null)
        {
            doctors = new Doctor[1000];
        }
        
        this.orderHistory = DataManager.loadOrderHistory();   
        if (orderHistory == null)
        {
            orderHistory = new OrderHistory();
        }
    }
    
    public static void main(String[] args) {
        // Initialize pharmacy
        Pharmacy pharmacy = new Pharmacy("Online Pharmacy");
        
        // If first run, add some sample data
        // initializeSampleData(pharmacy);
        
        // Create and start the main menu
        MainMenu mainMenu = new MainMenu();
        Scanner scanner = new Scanner(System.in);
        mainMenu.showMenu(pharmacy, scanner);
    }
    
    private static void initializeSampleData(Pharmacy pharmacy) {
        // Check if data already exists
        if (pharmacy.getPatients()[0] != null || pharmacy.getDoctors()[0] != null) {
            return; // Data already initialized
        }
        
        // Sample data initialization (same as in Driver class)
        // Create a sample doctor
        Doctor doctor = new Doctor(1, "drjohn", "password", "Dr. John Smith", "1234567890", 45, 500);
        pharmacy.addDoctor(doctor);
        
        // Create a sample patient
        Patient patient = new Patient(1, "patient1", "password", "John Doe", "9876543210", 35, 5000);
        pharmacy.addPatient(patient);
        
        // Create a sample admin
        Admin admin = new Admin(1, "admin", "admin123", "Admin User");
        Admin[] admins = new Admin[1];
        admins[0] = admin;
        DataManager.saveAdmins(admins);
        
        // Add some medicines to inventory
        Medicine crocin = new Medicine(1, "Crocin", 100, true);
        Medicine paracetamol = new Medicine(2, "Paracetamol", 50, false);
        Medicine insulin = new Medicine(3, "Insulin", 500, true);
        
        pharmacy.inventory.addToInventory(crocin);
        pharmacy.inventory.addToInventory(crocin);
        pharmacy.inventory.addToInventory(paracetamol);
        pharmacy.inventory.addToInventory(paracetamol);
        pharmacy.inventory.addToInventory(paracetamol);
        pharmacy.inventory.addToInventory(insulin);
        
        // Save all data
        pharmacy.saveAllData();
    }
    
    public String getName() {
        return name;
    }
    
    public Inventory getInventory() {
        return inventory;
    }
    
    public Patient[] getPatients() {
        return patients;
    }
    
    public Doctor[] getDoctors() {
        return doctors;
    }
    
    public OrderHistory getOrderHistory() {
        return orderHistory;
    }
    
    public void addPatient(Patient patient) {
        // Find the first null slot
        for (int i = 0; i < patients.length; i++) {
            if (patients[i] == null) {
                patients[i] = patient;
                DataManager.savePatients(patients);
                return;
            }
        }
        
        // If we reach here, the array is full, so we need to expand it
        Patient[] newPatients = new Patient[patients.length * 2];
        System.arraycopy(patients, 0, newPatients, 0, patients.length);
        newPatients[patients.length] = patient;
        patients = newPatients;
        DataManager.savePatients(patients);
    }
    
    public void addDoctor(Doctor doctor) {
        // Find the first null slot
        for (int i = 0; i < doctors.length; i++) {
            if (doctors[i] == null) {
                doctors[i] = doctor;
                DataManager.saveDoctors(doctors);
                return;
            }
        }
        
        // If we reach here, the array is full, so we need to expand it
        Doctor[] newDoctors = new Doctor[doctors.length * 2];
        System.arraycopy(doctors, 0, newDoctors, 0, doctors.length);
        newDoctors[doctors.length] = doctor;
        doctors = newDoctors;
        DataManager.saveDoctors(doctors);
    }
    
    public void addOrder(Order order) {
        orderHistory.addOrder(order);
        DataManager.saveOrderHistory(orderHistory);
    }
    
    public void saveAllData() {
        DataManager.savePatients(patients);
        DataManager.saveDoctors(doctors);
        DataManager.saveInventory(inventory);
        DataManager.saveOrderHistory(orderHistory);
    }
}



class Driver
{
    public static void main(String[] args)
    {
        // Create data folder if it doesn't exist
        File folder = new File("data/");
        if (!folder.exists())
        {
            folder.mkdir();
        }
        
        // Create inventory file if it doesn't exist
        File inventoryFile = new File("data/inventory.txt");
        if (!inventoryFile.exists())
        {
            try
            {
                inventoryFile.createNewFile();
            }
            catch (Exception e)
            {
                System.out.println("Error creating inventory file");
                e.printStackTrace();
            }
        }
        
        Pharmacy pharmacy = new Pharmacy("Online Pharmacy");

        // Create sample doctors
        Doctor doctor1 = new Doctor(1, "drjohn", "password", "Dr. John Smith", "1234567890", 45, 500);
        Doctor doctor2 = new Doctor(2, "drsarah", "password", "Dr. Sarah Johnson", "9876543210", 38, 750);
        Doctor doctor3 = new Doctor(3, "drpatel", "password", "Dr. Rajiv Patel", "5551234567", 52, 600);
        
        pharmacy.addDoctor(doctor1);
        pharmacy.addDoctor(doctor2);
        pharmacy.addDoctor(doctor3);
        
        // Create sample patients
        Patient patient1 = new Patient(1, "patient1", "password", "John Doe", "9876543210", 35, 5000);
        Patient patient2 = new Patient(2, "patient2", "password", "Jane Smith", "8765432109", 28, 3000);
        Patient patient3 = new Patient(3, "patient3", "password", "Michael Johnson", "7654321098", 42, 4500);
        Patient patient4 = new Patient(4, "patient4", "password", "Emily Davis", "6543210987", 31, 2500);
        Patient patient5 = new Patient(5, "patient5", "password", "Robert Wilson", "5432109876", 56, 6000);
        
        pharmacy.addPatient(patient1);
        pharmacy.addPatient(patient2);
        pharmacy.addPatient(patient3);
        pharmacy.addPatient(patient4);
        pharmacy.addPatient(patient5);
        
        // Create admin
        Admin admin = new Admin(1, "admin", "admin123", "Admin User");
        Admin[] admins = new Admin[1];
        admins[0] = admin;
        DataManager.saveAdmins(admins);
        
        // Add medicines to inventory
        Medicine crocin = new Medicine(1, "Crocin", 100, true);
        Medicine paracetamol = new Medicine(2, "Paracetamol", 50, false);
        Medicine insulin = new Medicine(3, "Insulin", 500, true);
        Medicine aspirin = new Medicine(4, "Aspirin", 75, false);
        Medicine lisinopril = new Medicine(5, "Lisinopril", 200, true);
        Medicine metformin = new Medicine(6, "Metformin", 150, true);
        Medicine amoxicillin = new Medicine(7, "Amoxicillin", 180, true);
        Medicine ibuprofen = new Medicine(8, "Ibuprofen", 60, false);
        Medicine omeprazole = new Medicine(9, "Omeprazole", 120, false);
        Medicine atorvastatin = new Medicine(10, "Atorvastatin", 250, true);
        
        // Add multiple quantities of each medicine
        for (int i = 0; i < 10; i++) pharmacy.inventory.addToInventory(crocin);
        for (int i = 0; i < 15; i++) pharmacy.inventory.addToInventory(paracetamol);
        for (int i = 0; i < 5; i++) pharmacy.inventory.addToInventory(insulin);
        for (int i = 0; i < 12; i++) pharmacy.inventory.addToInventory(aspirin);
        for (int i = 0; i < 8; i++) pharmacy.inventory.addToInventory(lisinopril);
        for (int i = 0; i < 7; i++) pharmacy.inventory.addToInventory(metformin);
        for (int i = 0; i < 9; i++) pharmacy.inventory.addToInventory(amoxicillin);
        for (int i = 0; i < 20; i++) pharmacy.inventory.addToInventory(ibuprofen);
        for (int i = 0; i < 10; i++) pharmacy.inventory.addToInventory(omeprazole);
        for (int i = 0; i < 6; i++) pharmacy.inventory.addToInventory(atorvastatin);
        
        // Create some sample prescriptions
        Date now = new Date();
        
        // Create end date 3 months from now
        Date endDate = new Date(now.getTime() + (90L * 24 * 60 * 60 * 1000));
        
        // Prescription for patient1 from doctor1
        Map<Medicine, Integer> prescription1Meds = new HashMap<>();
        prescription1Meds.put(insulin, 2);
        prescription1Meds.put(metformin, 1);
        doctor1.prescribe(1001, patient1, prescription1Meds, 3, 30, now, endDate);
        
        // Prescription for patient2 from doctor2
        Map<Medicine, Integer> prescription2Meds = new HashMap<>();
        prescription2Meds.put(lisinopril, 1);
        prescription2Meds.put(atorvastatin, 1);
        doctor2.prescribe(1002, patient2, prescription2Meds, 2, 30, now, endDate);
        
        // Prescription for patient3 from doctor3
        Map<Medicine, Integer> prescription3Meds = new HashMap<>();
        prescription3Meds.put(amoxicillin, 3);
        doctor3.prescribe(1003, patient3, prescription3Meds, 0, 0, now, endDate); // No refills
        
        // Prescription for patient4 from doctor1
        Map<Medicine, Integer> prescription4Meds = new HashMap<>();
        prescription4Meds.put(crocin, 2);
        prescription4Meds.put(insulin, 1);
        doctor1.prescribe(1004, patient4, prescription4Meds, 5, 30, now, endDate);
        
        System.out.println(pharmacy.inventory);

        // Save all data
        pharmacy.saveAllData();
        
        System.out.println("Online Pharmacy initialized successfully with sample data!");
        System.out.println("Sample Accounts Created:");
        System.out.println("-------------------------");
        System.out.println("Admin: username=admin, password=admin123");
        System.out.println("Doctors:");
        System.out.println("  1. username=drjohn, password=password");
        System.out.println("  2. username=drsarah, password=password");
        System.out.println("  3. username=drpatel, password=password");
        System.out.println("Patients:");
        System.out.println("  1. username=patient1, password=password");
        System.out.println("  2. username=patient2, password=password");
        System.out.println("  3. username=patient3, password=password");
        System.out.println("  4. username=patient4, password=password");
        System.out.println("  5. username=patient5, password=password");
        System.out.println("-------------------------");
        System.out.println("Several prescriptions have been created for patients 1-4.");
        System.out.println("A variety of 10 different medicines have been added to inventory.");
    }
}