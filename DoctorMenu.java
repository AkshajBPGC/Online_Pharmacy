import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public interface DoctorMenu extends Menu {
    
    static void showMenu(Pharmacy pharmacy, Scanner scanner) {
        Doctor currentDoctor = SessionManager.getCurrentDoctor();
        
        boolean logout = false;
        
        while (!logout) {
            Menu.clearScreen();
            System.out.println("===== DOCTOR MENU =====");
            System.out.println("Welcome, Dr. " + currentDoctor.getName() + "!");
            System.out.println();
            System.out.println("1. View Patients");
            System.out.println("2. Create Prescription");
            System.out.println("3. View My Prescriptions");
            System.out.println("4. Update Personal Information");
            System.out.println("5. Logout");
            System.out.print("Enter your choice: ");
            
            int choice = Menu.getIntInput(scanner);
            
            switch (choice) {
                case 1:
                    viewPatients(pharmacy);
                    break;
                case 2:
                    createPrescription(pharmacy, currentDoctor, scanner);
                    break;
                case 3:
                    viewDoctorPrescriptions(currentDoctor);
                    break;
                case 4:
                    updatePersonalInfo(currentDoctor, pharmacy, scanner);
                    break;
                case 5:
                    logout = true;
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
            
            if (!logout) {
                Menu.waitForEnter(scanner);
            }
        }
    }
    
    static void viewPatients(Pharmacy pharmacy) {
        Menu.clearScreen();
        System.out.println("===== PATIENTS LIST =====");
        
        Patient[] patients = pharmacy.getPatients();
        boolean foundAny = false;
        
        // Table header
        System.out.println("┌────┬──────────────────┬────────────┬─────┐");
        System.out.println("│ ID │ Name             │ Phone      │ Age │");
        System.out.println("├────┼──────────────────┼────────────┼─────┤");
        
        for (Patient patient : patients) {
            if (patient != null) {
                foundAny = true;
                System.out.printf("│ %-2d │ %-16s │ %-10s │ %-3d │%n",
                    patient.getId(),
                    Menu.limitString(patient.getName(), 16),
                    patient.getPhone(),
                    patient.getAge()
                );
            }
        }
        
        // Table footer
        System.out.println("└────┴──────────────────┴────────────┴─────┘");
        
        if (!foundAny) {
            System.out.println("No patients found.");
        }
    }
    
    static void createPrescription(Pharmacy pharmacy, Doctor doctor, Scanner scanner) {
        Menu.clearScreen();
        System.out.println("===== CREATE PRESCRIPTION =====");
        
        // Select a patient
        System.out.println("Select a patient:");
        Patient[] patients = pharmacy.getPatients();
        
        boolean foundAny = false;
        for (Patient patient : patients) {
            if (patient != null) {
                foundAny = true;
                System.out.println(patient.getId() + ": " + patient.getName());
            }
        }
        
        if (!foundAny) {
            System.out.println("No patients found.");
            return;
        }
        
        System.out.print("Enter patient ID (0 to cancel): ");
        int patientId = Menu.getIntInput(scanner);
        
        if (patientId == 0) {
            return;
        }
        
        // Find patient by ID
        Patient selectedPatient = null;
        for (Patient patient : patients) {
            if (patient != null && patient.getId() == patientId) {
                selectedPatient = patient;
                break;
            }
        }
        
        if (selectedPatient == null) {
            System.out.println("Patient not found!");
            return;
        }
        
        // Create a new prescription
        int prescriptionId = generateUniquePrescriptionId();
        
        // Add medicines to prescription
        Map<Medicine, Integer> medicines = new HashMap<>();
        boolean addingMedicines = true;
        
        while (addingMedicines) {
            // Show available medicines
            System.out.println("\nAvailable Medicines:");
            
            // Table header
            System.out.println("┌────┬──────────────────┬─────────┬─────────────────┬──────────┐");
            System.out.println("│ ID │ Name             │ Price   │ Rx Required     │ Quantity │");
            System.out.println("├────┼──────────────────┼─────────┼─────────────────┼──────────┤");
            
            Inventory inventory = pharmacy.getInventory();
            for (Map.Entry<Medicine, Integer> entry : inventory.inventory.entrySet()) {
                Medicine medicine = entry.getKey();
                int quantity = entry.getValue();
                
                System.out.printf("│ %-2d │ %-16s │ %-7d │ %-15s │ %-8d │%n",
                    medicine.getId(),
                    Menu.limitString(medicine.getName(), 16),
                    medicine.getPrice(),
                    (medicine.isPrescriptionRequired() ? "Yes" : "No"),
                    quantity
                );
            }
            
            // Table footer
            System.out.println("└────┴──────────────────┴─────────┴─────────────────┴──────────┘");
            
            System.out.print("\nEnter Medicine ID (0 to finish): ");
            int medicineId = Menu.getIntInput(scanner);
            
            if (medicineId == 0) {
                if (medicines.isEmpty()) {
                    System.out.println("You must add at least one medicine to the prescription.");
                    continue;
                }
                addingMedicines = false;
                continue;
            }
            
            // Find medicine by ID
            Medicine selectedMedicine = null;
            for (Map.Entry<Medicine, Integer> entry : inventory.inventory.entrySet()) {
                if (entry.getKey().getId() == medicineId) {
                    selectedMedicine = entry.getKey();
                    break;
                }
            }
            
            if (selectedMedicine == null) {
                System.out.println("Medicine not found!");
                continue;
            }
            
            System.out.print("Enter quantity: ");
            int quantity = Menu.getIntInput(scanner);
            
            if (quantity <= 0) {
                System.out.println("Invalid quantity!");
                continue;
            }
            
            // Add to prescription
            medicines.put(selectedMedicine, quantity);
            System.out.println(selectedMedicine.getName() + " added to prescription.");
        }
        
        // Set prescription details
        System.out.print("Enter number of refills allowed: ");
        int refillsAllowed = Menu.getIntInput(scanner);
        
        System.out.print("Enter refill period in days: ");
        int refillPeriod = Menu.getIntInput(scanner);
        
        // Set issue date to today
        Date issueDate = new Date();
        
        // Set end date (e.g., 6 months from now)
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(issueDate);
        calendar.add(Calendar.MONTH, 6);
        Date endDate = calendar.getTime();
        
        // Create prescription
        Prescription prescription = doctor.prescribe(prescriptionId, selectedPatient, medicines, 
                                                     refillsAllowed, refillPeriod, issueDate, endDate);
        
        System.out.println("\nPrescription created successfully!");
        System.out.println("Prescription ID: " + prescription.getId());
        System.out.println("Patient: " + selectedPatient.getName());
        System.out.println("Refills Allowed: " + refillsAllowed);
        System.out.println("Valid until: " + endDate);
        
        // Save changes
        pharmacy.saveAllData();
    }
    
    static int generateUniquePrescriptionId() {
        // Generate a simple unique ID based on current time
        return (int) (new Date().getTime() % 100000);
    }
    
    static void viewDoctorPrescriptions(Doctor doctor) {
        Menu.clearScreen();
        System.out.println("===== MY PRESCRIPTIONS =====");
        
        if (doctor.getPrescriptionsIssued() == null || doctor.getPrescriptionsIssued().isEmpty()) {
            System.out.println("You haven't issued any prescriptions yet.");
            return;
        }
        
        for (Prescription prescription : doctor.getPrescriptionsIssued()) {
            // Prescription header
            System.out.println("┌────────────────────────────────────────────────────────┐");
            System.out.printf("│ Prescription #%-40d │%n", prescription.getId());
            System.out.println("├───────────────────┬──────────────────────────────────┤");
            System.out.printf("│ %-15s │ %-30s │%n", "Patient", prescription.getPatient().getName());
            System.out.printf("│ %-15s │ %-30s │%n", "Issue Date", prescription.getIssueDate());
            System.out.printf("│ %-15s │ %-30s │%n", "End Date", prescription.getEndDate());
            System.out.printf("│ %-15s │ %-30d │%n", "Refills Allowed", prescription.getRefillsAllowed());
            System.out.printf("│ %-15s │ %-30d │%n", "Refills Remaining", prescription.getRefillsRemaining());
            System.out.println("└───────────────────┴──────────────────────────────────┘");
            
            // Medicines table
            System.out.println("Medicines:");
            System.out.println("┌──────────────────┬──────────┐");
            System.out.println("│ Medicine         │ Quantity │");
            System.out.println("├──────────────────┼──────────┤");
            
            for (Map.Entry<Medicine, Integer> entry : prescription.getMedicines().entrySet()) {
                System.out.printf("│ %-16s │ %-8d │%n",
                    Menu.limitString(entry.getKey().getName(), 16),
                    entry.getValue()
                );
            }
            System.out.println("└──────────────────┴──────────┘");
            System.out.println();
        }
    }
    
    static void updatePersonalInfo(Doctor doctor, Pharmacy pharmacy, Scanner scanner) {
        Menu.clearScreen();
        System.out.println("===== UPDATE PERSONAL INFORMATION =====");
        System.out.println("Current Information:");
        System.out.println("Name: " + doctor.getName());
        System.out.println("Phone: " + doctor.getPhone());
        System.out.println("Age: " + doctor.getAge());
        System.out.println("Consultation Fee: " + doctor.getConsultationFees());
        
        System.out.println("\nWhat would you like to update?");
        System.out.println("1. Phone Number");
        System.out.println("2. Consultation Fee");
        System.out.println("3. Password");
        System.out.println("4. Cancel");
        System.out.print("Enter your choice: ");
        
        int choice = Menu.getIntInput(scanner);
        
        switch (choice) {
            case 1:
                System.out.print("Enter new phone number: ");
                String newPhone = scanner.nextLine();
                doctor.setPhone(newPhone);
                System.out.println("Phone number updated successfully!");
                break;
            case 2:
                System.out.print("Enter new consultation fee: ");
                int newFee = Menu.getIntInput(scanner);
                if (newFee <= 0) {
                    System.out.println("Invalid fee amount!");
                    return;
                }
                doctor.setConsultationFees(newFee);
                System.out.println("Consultation fee updated successfully!");
                break;
            case 3:
                System.out.print("Enter current password: ");
                String currentPassword = scanner.nextLine();
                
                if (!doctor.authenticate(currentPassword)) {
                    System.out.println("Incorrect password!");
                    return;
                }
                
                System.out.print("Enter new password: ");
                String newPassword = scanner.nextLine();
                System.out.print("Confirm new password: ");
                String confirmPassword = scanner.nextLine();
                
                if (!newPassword.equals(confirmPassword)) {
                    System.out.println("Passwords do not match!");
                    return;
                }
                
                doctor.setPassword(newPassword);
                System.out.println("Password updated successfully!");
                break;
            case 4:
                return;
            default:
                System.out.println("Invalid choice!");
                return;
        }
        
        // Save changes
        pharmacy.saveAllData();
    }
}