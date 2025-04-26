import java.util.Map;
import java.util.Scanner;

public interface AdminMenu extends Menu {
    static void showMenu(Pharmacy pharmacy, Scanner scanner) {
        Admin currentAdmin = SessionManager.getCurrentAdmin();
        
        boolean logout = false;
        
        while (!logout) {
            Menu.clearScreen();
            System.out.println("===== ADMIN MENU =====");
            System.out.println("Welcome, " + currentAdmin.getName() + "!");
            System.out.println();
            System.out.println("1. View Inventory");
            System.out.println("2. Add Medicine to Inventory");
            System.out.println("3. Update Medicine Price");
            System.out.println("4. View Patients");
            System.out.println("5. View Doctors");
            System.out.println("6. Add Doctor");
            System.out.println("7. View All Orders");
            System.out.println("8. Logout");
            System.out.print("Enter your choice: ");
            
            int choice = Menu.getIntInput(scanner);
            
            switch (choice) {
                case 1:
                    viewInventory(pharmacy);
                    break;
                case 2:
                    addMedicineToInventory(pharmacy, scanner);
                    break;
                case 3:
                    updateMedicinePrice(pharmacy, scanner);
                    break;
                case 4:
                    viewPatients(pharmacy);
                    break;
                case 5:
                    viewDoctors(pharmacy);
                    break;
                case 6:
                    addDoctor(pharmacy, scanner);
                    break;
                case 7:
                    viewAllOrders(pharmacy);
                    break;
                case 8:
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
    
    static void viewInventory(Pharmacy pharmacy) {
        Menu.clearScreen();
        System.out.println("===== INVENTORY =====");
        Inventory inventory = pharmacy.getInventory();
        
        if (inventory.inventory.isEmpty()) {
            System.out.println("Inventory is empty.");
            return;
        }
        
        System.out.println("ID | Name | Price | Requires Prescription | Available Quantity");
        System.out.println("---------------------------------------------------------");
        
        for (Map.Entry<Medicine, Integer> entry : inventory.inventory.entrySet()) {
            Medicine medicine = entry.getKey();
            int quantity = entry.getValue();
            
            System.out.println(medicine.getId() + " | " + 
                             medicine.getName() + " | " + 
                             medicine.getPrice() + " | " + 
                             (medicine.isPrescriptionRequired() ? "Yes" : "No") + " | " +
                             quantity);
        }
    }
    
    static void addMedicineToInventory(Pharmacy pharmacy, Scanner scanner) {
        Menu.clearScreen();
        System.out.println("===== ADD MEDICINE TO INVENTORY =====");
        
        // Check if we're adding an existing medicine or a new one
        System.out.println("1. Add existing medicine");
        System.out.println("2. Add new medicine");
        System.out.print("Enter your choice: ");
        
        int choice = Menu.getIntInput(scanner);
        
        if (choice == 1) {
            // Show existing medicines
            viewInventory(pharmacy);
            
            System.out.print("Enter Medicine ID (0 to cancel): ");
            int medicineId = Menu.getIntInput(scanner);
            
            if (medicineId == 0) {
                return;
            }
            
            // Find medicine by ID
            Medicine selectedMedicine = null;
            for (Map.Entry<Medicine, Integer> entry : pharmacy.getInventory().inventory.entrySet()) {
                if (entry.getKey().getId() == medicineId) {
                    selectedMedicine = entry.getKey();
                    break;
                }
            }
            
            if (selectedMedicine == null) {
                System.out.println("Medicine not found!");
                return;
            }
            
            System.out.print("Enter quantity to add: ");
            int quantity = Menu.getIntInput(scanner);
            
            if (quantity <= 0) {
                System.out.println("Invalid quantity!");
                return;
            }
            
            // Add to inventory
            for (int i = 0; i < quantity; i++) {
                pharmacy.getInventory().addToInventory(selectedMedicine);
            }
            
            System.out.println(quantity + " units of " + selectedMedicine.getName() + " added to inventory.");
            
        } else if (choice == 2) {
            // Create a new medicine
            System.out.print("Enter medicine name: ");
            String name = scanner.nextLine();
            
            System.out.print("Enter medicine price: ");
            int price = Menu.getIntInput(scanner);
            
            if (price <= 0) {
                System.out.println("Invalid price!");
                return;
            }
            
            System.out.print("Requires prescription? (y/n): ");
            String requiresPrescription = scanner.nextLine().toLowerCase();
            boolean prescriptionRequired = requiresPrescription.startsWith("y");
            
            // Generate a new unique ID
            int id = generateUniqueMedicineId(pharmacy);
            
            // Create the new medicine
            Medicine newMedicine = new Medicine(id, name, price, prescriptionRequired);
            
            System.out.print("Enter initial quantity: ");
            int quantity = Menu.getIntInput(scanner);
            
            if (quantity <= 0) {
                System.out.println("Invalid quantity!");
                return;
            }
            
            // Add to inventory
            for (int i = 0; i < quantity; i++) {
                pharmacy.getInventory().addToInventory(newMedicine);
            }
            
            System.out.println("Medicine added successfully:");
            System.out.println("ID: " + id);
            System.out.println("Name: " + name);
            System.out.println("Price: " + price);
            System.out.println("Requires Prescription: " + (prescriptionRequired ? "Yes" : "No"));
            System.out.println("Quantity: " + quantity);
            
        } else {
            System.out.println("Invalid choice!");
            return;
        }
        
        // Save changes
        pharmacy.saveAllData();
    }
    
    static int generateUniqueMedicineId(Pharmacy pharmacy) {
        int maxId = 0;
        
        for (Map.Entry<Medicine, Integer> entry : pharmacy.getInventory().inventory.entrySet()) {
            if (entry.getKey().getId() > maxId) {
                maxId = entry.getKey().getId();
            }
        }
        
        return maxId + 1;
    }
    
    static void updateMedicinePrice(Pharmacy pharmacy, Scanner scanner) {
        Menu.clearScreen();
        System.out.println("===== UPDATE MEDICINE PRICE =====");
        
        // Show medicines
        viewInventory(pharmacy);
        
        System.out.print("Enter Medicine ID to update (0 to cancel): ");
        int medicineId = Menu.getIntInput(scanner);
        
        if (medicineId == 0) {
            return;
        }
        
        // Find medicine by ID
        Medicine selectedMedicine = null;
        for (Map.Entry<Medicine, Integer> entry : pharmacy.getInventory().inventory.entrySet()) {
            if (entry.getKey().getId() == medicineId) {
                selectedMedicine = entry.getKey();
                break;
            }
        }
        
        if (selectedMedicine == null) {
            System.out.println("Medicine not found!");
            return;
        }
        
        System.out.println("Current price: " + selectedMedicine.getPrice());
        System.out.print("Enter new price: ");
        int newPrice = Menu.getIntInput(scanner);
        
        if (newPrice <= 0) {
            System.out.println("Invalid price!");
            return;
        }
        
        // Update price
        selectedMedicine.setPrice(newPrice);
        System.out.println("Price updated successfully!");
        
        // Save changes
        pharmacy.saveAllData();
    }
    
    static void viewPatients(Pharmacy pharmacy) {
        Menu.clearScreen();
        System.out.println("===== PATIENTS LIST =====");
        
        Patient[] patients = pharmacy.getPatients();
        boolean foundAny = false;
        
        for (Patient patient : patients) {
            if (patient != null) {
                foundAny = true;
                System.out.println("ID: " + patient.getId());
                System.out.println("Username: " + patient.getUsername());
                System.out.println("Name: " + patient.getName());
                System.out.println("Age: " + patient.getAge());
                System.out.println("Phone: " + patient.getPhone());
                System.out.println("Wallet Balance: " + patient.getWallet());
                System.out.println("------------------------------");
            }
        }
        
        if (!foundAny) {
            System.out.println("No patients found.");
        }
    }
    
    static void viewDoctors(Pharmacy pharmacy) {
        Menu.clearScreen();
        System.out.println("===== DOCTORS LIST =====");
        
        Doctor[] doctors = pharmacy.getDoctors();
        boolean foundAny = false;
        
        for (Doctor doctor : doctors) {
            if (doctor != null) {
                foundAny = true;
                System.out.println("ID: " + doctor.getId());
                System.out.println("Username: " + doctor.getUsername());
                System.out.println("Name: " + doctor.getName());
                System.out.println("Age: " + doctor.getAge());
                System.out.println("Phone: " + doctor.getPhone());
                System.out.println("Consultation Fee: " + doctor.getConsultationFees());
                System.out.println("------------------------------");
            }
        }
        
        if (!foundAny) {
            System.out.println("No doctors found.");
        }
    }
    
    static void addDoctor(Pharmacy pharmacy, Scanner scanner) {
        Menu.clearScreen();
        System.out.println("===== ADD DOCTOR =====");
        
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        
        System.out.print("Enter phone number: ");
        String phone = scanner.nextLine();
        
        System.out.print("Enter age: ");
        int age = Menu.getIntInput(scanner);
        
        System.out.print("Enter consultation fee: ");
        int consultationFee = Menu.getIntInput(scanner);
        
        if (consultationFee <= 0) {
            System.out.println("Invalid consultation fee!");
            return;
        }
        
        // Generate a unique ID
        int id = generateUniqueDoctorId(pharmacy);
        
        // Create new doctor
        Doctor newDoctor = new Doctor(id, username, password, name, phone, age, consultationFee);
        
        // Add doctor to pharmacy
        pharmacy.addDoctor(newDoctor);
        
        System.out.println("Doctor added successfully!");
    }
    
    static int generateUniqueDoctorId(Pharmacy pharmacy) {
        Doctor[] doctors = pharmacy.getDoctors();
        int maxId = 0;
        
        for (Doctor doctor : doctors) {
            if (doctor != null && doctor.getId() > maxId) {
                maxId = doctor.getId();
            }
        }
        
        return maxId + 1;
    }
    
    static void viewAllOrders(Pharmacy pharmacy) {
        Menu.clearScreen();
        System.out.println("===== ALL ORDERS =====");
        
        OrderHistory orderHistory = pharmacy.getOrderHistory();
        Order[] orders = orderHistory.getOrders();
        
        if (orders == null || orders.length == 0) {
            System.out.println("No orders found.");
            return;
        }
        
        for (Order order : orders) {
            System.out.println("Order ID: " + order.getId());
            System.out.println("Patient: " + order.getPatient().getName());
            System.out.println("Date: " + order.getDate());
            System.out.println("Status: " + order.getStatus());
            System.out.println("Total Cost: " + order.getTotalCost());
            System.out.println("Medicines:");
            
            for (Map.Entry<Medicine, Integer> entry : order.getMedicines().entrySet()) {
                System.out.println("  - " + entry.getKey().getName() + 
                                 " (Quantity: " + entry.getValue() + 
                                 ", Price: " + entry.getKey().getPrice() + ")");
            }
            
            System.out.println("------------------------------");
        }
    }
}