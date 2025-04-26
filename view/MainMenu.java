package view;

import model.*;
import service.DataManager;
import service.SessionManager;
import main.Pharmacy;
import java.util.Map;
import java.util.Scanner;

public class MainMenu implements Menu {
    private static Scanner scanner = new Scanner(System.in);
    private static Pharmacy pharmacy;
    
    // @Override
    public void showMenu(Pharmacy pharmacy, Scanner scanner) {
        MainMenu.pharmacy = pharmacy;
        MainMenu.scanner = scanner;
        
        boolean exit = false;
        
        while (!exit) {
            Menu.clearScreen();
            System.out.println("===== ONLINE PHARMACY SYSTEM =====");
            System.out.println("1. Login");
            System.out.println("2. Register as Patient");
            System.out.println("3. Debug Mode");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            
            int choice = Menu.getIntInput(scanner);
            
            switch (choice) {
                case 1:
                    loginMenu();
                    break;
                case 2:
                    registerPatient();
                    break;
                case 3:
                    debugMode();
                    break;
                case 4:
                    exit = true;
                    System.out.println("Thank you for using Online Pharmacy System. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
                    Menu.waitForEnter(scanner);
            }
        }
    }
    
    private static void loginMenu() {
        Menu.clearScreen();
        System.out.println("===== LOGIN MENU =====");
        System.out.println("Select user type:");
        System.out.println("1. Patient");
        System.out.println("2. Doctor");
        System.out.println("3. Admin");
        System.out.println("4. Back to Main Menu");
        System.out.print("Enter your choice: ");
        
        int choice = Menu.getIntInput(scanner);
        
        if (choice == 4) {
            return;
        }
        
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        
        boolean loginSuccess = SessionManager.login(username, password);
        
        if (loginSuccess) {
            System.out.println("Login successful!");
            Menu.waitForEnter(scanner);
            
            // Redirect to appropriate menu based on user type
            if (SessionManager.isPatient()) {
                PatientMenu.showMenu(pharmacy, scanner);
            } else if (SessionManager.isDoctor()) {
                DoctorMenu.showMenu(pharmacy, scanner);
            } else if (SessionManager.isAdmin()) {
                AdminMenu.showMenu(pharmacy, scanner);
            }
            
            // After logout, the control returns here
            SessionManager.logout();
        } else {
            System.out.println("Login failed! Invalid username or password.");
            Menu.waitForEnter(scanner);
        }
    }
    
    private static void registerPatient() {
        Menu.clearScreen();
        System.out.println("===== PATIENT REGISTRATION =====");
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
        
        // Generate a unique ID for the new patient
        int id = generateUniquePatientId();
        
        // Create new patient
        Patient newPatient = new Patient(id, username, password, name, phone, age, 1000);
        
        // Add patient to pharmacy
        pharmacy.addPatient(newPatient);
        
        System.out.println("Registration successful! Initial wallet balance: 1000");
        System.out.println("You can now login with your credentials.");
        Menu.waitForEnter(scanner);
    }
    
    private static int generateUniquePatientId() {
        Patient[] patients = pharmacy.getPatients();
        int maxId = 0;
        
        for (Patient patient : patients) {
            if (patient != null && patient.getId() > maxId) {
                maxId = patient.getId();
            }
        }
        
        return maxId + 1;
    }
    
    private static void debugMode() {
        boolean backToMain = false;
        
        while (!backToMain) {
            Menu.clearScreen();
            System.out.println("===== DEBUG MODE =====");
            System.out.println("1. View All Patients");
            System.out.println("2. View All Doctors");
            System.out.println("3. View All Admins");
            System.out.println("4. View Inventory");
            System.out.println("5. View Order History");
            System.out.println("6. Back to Main Menu");
            System.out.print("Enter your choice: ");
            
            int choice = Menu.getIntInput(scanner);
            
            switch (choice) {
                case 1:
                    debugViewAllPatients();
                    break;
                case 2:
                    debugViewAllDoctors();
                    break;
                case 3:
                    debugViewAllAdmins();
                    break;
                case 4:
                    debugViewInventory();
                    break;
                case 5:
                    debugViewOrderHistory();
                    break;
                case 6:
                    backToMain = true;
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
            
            if (!backToMain) {
                Menu.waitForEnter(scanner);
            }
        }
    }
    
    private static void debugViewAllPatients() {
        Menu.clearScreen();
        System.out.println("===== DEBUG: ALL PATIENTS =====");
        
        Patient[] patients = DataManager.loadPatients();
        
        if (patients == null || patients.length == 0) {
            System.out.println("No patients loaded from DataManager.");
            return;
        }
        
        int nonNullCount = 0;
        for (Patient patient : patients) {
            if (patient != null) {
                nonNullCount++;
            }
        }
        
        System.out.println("Total patients: " + patients.length + " (Non-null: " + nonNullCount + ")");
        
        // Table header
        System.out.println("┌────┬────────────┬──────────────────┬────────────┬─────┬────────┬──────────────┬─────────┐");
        System.out.println("│ ID │ Username   │ Name             │ Phone      │ Age │ Wallet │ Prescriptions│ Orders  │");
        System.out.println("├────┼────────────┼──────────────────┼────────────┼─────┼────────┼──────────────┼─────────┤");
        
        // Table data
        for (Patient patient : patients) {
            if (patient != null) {
                System.out.printf("│ %-2d │ %-10s │ %-16s │ %-10s │ %-3d │ %-6d │ %-12d │ %-7d │%n",
                    patient.getId(),
                    patient.getUsername(),
                    Menu.limitString(patient.getName(), 16),
                    patient.getPhone(),
                    patient.getAge(),
                    patient.getWallet(),
                    (patient.getPrescriptions() != null ? patient.getPrescriptions().size() : 0),
                    (patient.getOrderHistory() != null ? patient.getOrderHistory().size() : 0)
                );
            }
        }
        
        // Table footer
        System.out.println("└────┴────────────┴──────────────────┴────────────┴─────┴────────┴──────────────┴─────────┘");
    }
    
    private static void debugViewAllDoctors() {
        Menu.clearScreen();
        System.out.println("===== DEBUG: ALL DOCTORS =====");
        
        Doctor[] doctors = DataManager.loadDoctors();
        
        if (doctors == null || doctors.length == 0) {
            System.out.println("No doctors loaded from DataManager.");
            return;
        }
        
        int nonNullCount = 0;
        for (Doctor doctor : doctors) {
            if (doctor != null) {
                nonNullCount++;
            }
        }
        
        System.out.println("Total doctors: " + doctors.length + " (Non-null: " + nonNullCount + ")");
        
        // Table header
        System.out.println("┌────┬────────────┬──────────────────┬────────────┬─────┬────────────┬────────────────┐");
        System.out.println("│ ID │ Username   │ Name             │ Phone      │ Age │ Cons. Fee  │ Prescriptions  │");
        System.out.println("├────┼────────────┼──────────────────┼────────────┼─────┼────────────┼────────────────┤");
        
        // Table data
        for (Doctor doctor : doctors) {
            if (doctor != null) {
                System.out.printf("│ %-2d │ %-10s │ %-16s │ %-10s │ %-3d │ %-10d │ %-14d │%n",
                    doctor.getId(),
                    doctor.getUsername(),
                    Menu.limitString(doctor.getName(), 16),
                    doctor.getPhone(),
                    doctor.getAge(),
                    doctor.getConsultationFees(),
                    (doctor.getPrescriptionsIssued() != null ? doctor.getPrescriptionsIssued().size() : 0)
                );
            }
        }
        
        // Table footer
        System.out.println("└────┴────────────┴──────────────────┴────────────┴─────┴────────────┴────────────────┘");
    }
    
    private static void debugViewAllAdmins() {
        Menu.clearScreen();
        System.out.println("===== DEBUG: ALL ADMINS =====");
        
        Admin[] admins = DataManager.loadAdmins();
        
        if (admins == null || admins.length == 0) {
            System.out.println("No admins loaded from DataManager.");
            return;
        }
        
        int nonNullCount = 0;
        for (Admin admin : admins) {
            if (admin != null) {
                nonNullCount++;
            }
        }
        
        System.out.println("Total admins: " + admins.length + " (Non-null: " + nonNullCount + ")");
        
        // Table header
        System.out.println("┌────┬────────────┬──────────────────┐");
        System.out.println("│ ID │ Username   │ Name             │");
        System.out.println("├────┼────────────┼──────────────────┤");
        
        // Table data
        for (Admin admin : admins) {
            if (admin != null) {
                System.out.printf("│ %-2d │ %-10s │ %-16s │%n",
                    admin.getId(),
                    admin.getUsername(),
                    Menu.limitString(admin.getName(), 16)
                );
            }
        }
        
        // Table footer
        System.out.println("└────┴────────────┴──────────────────┘");
    }
    
    private static void debugViewInventory() {
        Menu.clearScreen();
        System.out.println("===== DEBUG: INVENTORY =====");
        
        Inventory inventory = DataManager.loadInventory();
        
        if (inventory == null || inventory.inventory.isEmpty()) {
            System.out.println("No inventory loaded from DataManager.");
            return;
        }
        
        System.out.println("Total unique medicines: " + inventory.inventory.size());
        int totalQuantity = 0;
        
        // Table header
        System.out.println("┌────┬──────────────────┬─────────┬─────────────────┬──────────┐");
        System.out.println("│ ID │ Name             │ Price   │ Rx Required     │ Quantity │");
        System.out.println("├────┼──────────────────┼─────────┼─────────────────┼──────────┤");
        
        // Table data
        for (Map.Entry<Medicine, Integer> entry : inventory.inventory.entrySet()) {
            Medicine medicine = entry.getKey();
            int quantity = entry.getValue();
            totalQuantity += quantity;
            
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
        System.out.println("Total medicine quantity: " + totalQuantity);
    }
    
    private static void debugViewOrderHistory() {
        Menu.clearScreen();
        System.out.println("===== DEBUG: ORDER HISTORY =====");
        
        OrderHistory orderHistory = DataManager.loadOrderHistory();
        
        if (orderHistory == null) {
            System.out.println("No order history loaded from DataManager.");
            return;
        }
        
        Order[] orders = orderHistory.getOrders();
        
        if (orders == null || orders.length == 0) {
            System.out.println("No orders in the order history.");
            return;
        }
        
        System.out.println("Total orders: " + orders.length);
        
        // Table header
        System.out.println("┌────────┬─────────────────┬────────────────────┬──────────┬───────────┐");
        System.out.println("│ Order  │ Patient         │ Date               │ Status   │ Total     │");
        System.out.println("├────────┼─────────────────┼────────────────────┼──────────┼───────────┤");
        
        // Table data
        for (Order order : orders) {
            if (order != null) {
                System.out.printf("│ %-6d │ %-15s │ %-18s │ %-8s │ %-9d │%n",
                    order.getId(),
                    Menu.limitString(order.getPatient().getName(), 15),
                    order.getDate(),
                    Menu.limitString(order.getStatus(), 8),
                    order.getTotalCost()
                );
            }
        }
        
        // Table footer
        System.out.println("└────────┴─────────────────┴────────────────────┴──────────┴───────────┘");
        
        // Display each order's details
        for (Order order : orders) {
            if (order != null) {
                System.out.println("\nOrder Details - #" + order.getId());
                
                // Medicines table header
                System.out.println("┌──────────────────┬──────────┬─────────┬───────────┐");
                System.out.println("│ Medicine         │ Quantity │ Price   │ Subtotal  │");
                System.out.println("├──────────────────┼──────────┼─────────┼───────────┤");
                
                // Medicines table data
                for (Map.Entry<Medicine, Integer> entry : order.getMedicines().entrySet()) {
                    Medicine medicine = entry.getKey();
                    int quantity = entry.getValue();
                    int price = medicine.getPrice();
                    int subtotal = price * quantity;
                    
                    System.out.printf("│ %-16s │ %-8d │ %-7d │ %-9d │%n",
                        Menu.limitString(medicine.getName(), 16),
                        quantity,
                        price,
                        subtotal
                    );
                }
                
                // Medicines table footer
                System.out.println("└──────────────────┴──────────┴─────────┴───────────┘");
            }
        }
    }
}