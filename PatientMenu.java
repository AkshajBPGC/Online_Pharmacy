import java.util.Date;
import java.util.Map;
import java.util.Scanner;

public interface PatientMenu extends Menu {
    static void showMenu(Pharmacy pharmacy, Scanner scanner) {
        Patient currentPatient = SessionManager.getCurrentPatient();
        Cart cart = new Cart(currentPatient.getId());
        
        boolean logout = false;
        
        while (!logout) {
            Menu.clearScreen();
            System.out.println("===== PATIENT MENU =====");
            System.out.println("Welcome, " + currentPatient.getName() + "!");
            System.out.println("Wallet Balance: " + currentPatient.getWallet());
            System.out.println();
            System.out.println("1. View Available Medicines");
            System.out.println("2. View My Prescriptions");
            System.out.println("3. Add Medicine to Cart");
            System.out.println("4. Add Prescription to Cart");
            System.out.println("5. View Cart");
            System.out.println("6. Checkout");
            System.out.println("7. View Order History");
            System.out.println("8. Add Money to Wallet");
            System.out.println("9. Logout");
            System.out.print("Enter your choice: ");
            
            int choice = Menu.getIntInput(scanner);
            
            switch (choice) {
                case 1:
                    viewMedicines(pharmacy);
                    break;
                case 2:
                    viewPrescriptions(currentPatient);
                    break;
                case 3:
                    addMedicineToCart(pharmacy, cart, scanner);
                    break;
                case 4:
                    addPrescriptionToCart(currentPatient, cart, scanner);
                    break;
                case 5:
                    viewCart(cart);
                    break;
                case 6:
                    checkout(pharmacy, currentPatient, cart);
                    break;
                case 7:
                    viewOrderHistory(currentPatient);
                    break;
                case 8:
                    addMoneyToWallet(currentPatient, scanner);
                    // Save updated patient data
                    pharmacy.saveAllData();
                    break;
                case 9:
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
    
    static void viewMedicines(Pharmacy pharmacy) {
        Menu.clearScreen();
        System.out.println("===== AVAILABLE MEDICINES =====");
        Inventory inventory = pharmacy.getInventory();
        
        if (inventory.inventory.isEmpty()) {
            System.out.println("No medicines available in inventory.");
            return;
        }
        
        // Table header
        System.out.println("┌────┬──────────────────┬─────────┬─────────────────┬──────────┐");
        System.out.println("│ ID │ Name             │ Price   │ Prescript Req   │ Quantity │");
        System.out.println("├────┼──────────────────┼─────────┼─────────────────┼──────────┤");
        
        // Table data
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
    }

    static void addMedicineToCart(Pharmacy pharmacy, Cart cart, Scanner scanner) {
        Menu.clearScreen();
        System.out.println("===== ADD MEDICINE TO CART =====");
        
        // Show available medicines
        viewMedicines(pharmacy);
        
        System.out.print("Enter Medicine ID (0 to cancel): ");
        int medicineId = Menu.getIntInput(scanner);
        
        if (medicineId == 0) {
            return;
        }
        
        // Find medicine by ID
        Medicine selectedMedicine = null;
        int availableQuantity = 0;
        
        for (Map.Entry<Medicine, Integer> entry : pharmacy.getInventory().inventory.entrySet()) {
            if (entry.getKey().getId() == medicineId) {
                selectedMedicine = entry.getKey();
                availableQuantity = entry.getValue();
                break;
            }
        }
        
        if (selectedMedicine == null) {
            System.out.println("Medicine not found!");
            return;
        }
        
        // Check if prescription is required
        if (selectedMedicine.isPrescriptionRequired()) {
            boolean hasPrescription = false;
            
            // Check if patient has a prescription for this medicine
            for (Prescription prescription : SessionManager.getCurrentPatient().getPrescriptions()) {
                for (Map.Entry<Medicine, Integer> entry : prescription.getMedicines().entrySet()) {
                    if (entry.getKey().getId() == selectedMedicine.getId()) {
                        hasPrescription = true;
                        break;
                    }
                }
                if (hasPrescription) break;
            }
            
            if (!hasPrescription) {
                System.out.println("This medicine requires a prescription. Please consult a doctor.");
                return;
            }
        }
        
        System.out.print("Enter quantity (Available: " + availableQuantity + "): ");
        int quantity = Menu.getIntInput(scanner);
        
        if (quantity <= 0) {
            System.out.println("Invalid quantity!");
            return;
        }
        
        if (quantity > availableQuantity) {
            System.out.println("Not enough stock available!");
            return;
        }
        
        // Add to cart
        cart.addToCart(selectedMedicine, quantity);
        System.out.println(quantity + " units of " + selectedMedicine.getName() + " added to cart.");
    }
    
    static void addPrescriptionToCart(Patient patient, Cart cart, Scanner scanner) {
        Menu.clearScreen();
        System.out.println("===== ADD PRESCRIPTION TO CART =====");
        
        if (patient.getPrescriptions() == null || patient.getPrescriptions().isEmpty()) {
            System.out.println("You don't have any prescriptions.");
            return;
        }
        
        // List prescriptions
        viewPrescriptions(patient);
        
        System.out.print("Enter Prescription number (1-" + patient.getPrescriptions().size() + ") or 0 to cancel: ");
        int prescriptionIndex = Menu.getIntInput(scanner);
        
        if (prescriptionIndex == 0) {
            return;
        }
        
        if (prescriptionIndex < 1 || prescriptionIndex > patient.getPrescriptions().size()) {
            System.out.println("Invalid prescription number!");
            return;
        }
        
        Prescription selectedPrescription = patient.getPrescriptions().get(prescriptionIndex - 1);
        
        // Check if prescription is still valid
        Date currentDate = new Date();
        if (currentDate.after(selectedPrescription.getEndDate())) {
            System.out.println("This prescription has expired!");
            return;
        }
        
        if (selectedPrescription.getRefillsRemaining() <= 0) {
            System.out.println("No refills remaining for this prescription!");
            return;
        }
        
        // Add prescription to cart
        cart.addToCart(selectedPrescription);
        selectedPrescription.useRefill();
        
        System.out.println("Prescription added to cart.");
    }
    
    static void viewPrescriptions(Patient patient) {
        Menu.clearScreen();
        System.out.println("===== MY PRESCRIPTIONS =====");
        
        if (patient.getPrescriptions() == null || patient.getPrescriptions().isEmpty()) {
            System.out.println("You don't have any prescriptions.");
            return;
        }
        
        for (int i = 0; i < patient.getPrescriptions().size(); i++) {
            Prescription prescription = patient.getPrescriptions().get(i);
            System.out.println("Prescription #" + (i + 1));
            System.out.println("┌─────────────────┬─────────────────────────────────────┐");
            System.out.printf("│ %-15s │ %-35s │%n", "ID", prescription.getId());
            System.out.printf("│ %-15s │ %-35s │%n", "Doctor", prescription.getDoctor().getName());
            System.out.printf("│ %-15s │ %-35s │%n", "Issue Date", prescription.getIssueDate());
            System.out.printf("│ %-15s │ %-35s │%n", "End Date", prescription.getEndDate());
            System.out.printf("│ %-15s │ %-35s │%n", "Refills Remain", prescription.getRefillsRemaining());
            System.out.println("└─────────────────┴─────────────────────────────────────┘");
            
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

    static void viewCart(Cart cart) {
        Menu.clearScreen();
        System.out.println("===== SHOPPING CART =====");
        
        if (cart.cart.isEmpty()) {
            System.out.println("Your cart is empty.");
            return;
        }
        
        // Table header
        System.out.println("┌──────────────────┬──────────┬─────────┬───────────┐");
        System.out.println("│ Medicine         │ Quantity │ Price   │ Total     │");
        System.out.println("├──────────────────┼──────────┼─────────┼───────────┤");
        
        int totalCost = 0;
        for (Map.Entry<Medicine, Integer> entry : cart.cart.entrySet()) {
            Medicine medicine = entry.getKey();
            int quantity = entry.getValue();
            int unitPrice = medicine.getPrice();
            int itemTotal = unitPrice * quantity;
            
            System.out.printf("│ %-16s │ %-8d │ %-7d │ %-9d │%n",
                Menu.limitString(medicine.getName(), 16),
                quantity,
                unitPrice,
                itemTotal
            );
            
            totalCost += itemTotal;
        }
        
        // Table footer with total
        System.out.println("├──────────────────┴──────────┴─────────┼───────────┤");
        System.out.printf("│ Total                                  │ %-9d │%n", totalCost);
        System.out.println("└──────────────────────────────────────┴───────────┘");
    }
    
    static void checkout(Pharmacy pharmacy, Patient patient, Cart cart) {
        Menu.clearScreen();
        System.out.println("===== CHECKOUT =====");
        
        if (cart.cart.isEmpty()) {
            System.out.println("Your cart is empty.");
            return;
        }
        
        // Create order service
        OrderService orderService = new OrderService(pharmacy);
        
        // Calculate total cost
        int totalCost = cart.calculateTotal();
        
        // Check if patient has enough money
        if (patient.getWallet() < totalCost) {
            System.out.println("Insufficient funds in your wallet!");
            System.out.println("Total cost: " + totalCost);
            System.out.println("Your balance: " + patient.getWallet());
            System.out.println("Please add more money to your wallet.");
            return;
        }
        
        // Place order
        Order order = orderService.placeOrder(patient, cart);
        
        if (order != null) {
            System.out.println("Order placed successfully!");
            System.out.println("Order ID: " + order.getId());
            System.out.println("Total amount: " + order.getTotalCost());
            System.out.println("Remaining balance: " + patient.getWallet());
        } else {
            System.out.println("Failed to place order. Please try again.");
        }
    }
    
    static void viewOrderHistory(Patient patient) {
        Menu.clearScreen();
        System.out.println("===== ORDER HISTORY =====");
        
        if (patient.getOrderHistory() == null || patient.getOrderHistory().isEmpty()) {
            System.out.println("You haven't placed any orders yet.");
            return;
        }
        
        // Main orders table
        System.out.println("┌────────┬────────────────────┬──────────┬───────────┐");
        System.out.println("│ Order  │ Date               │ Status   │ Total     │");
        System.out.println("├────────┼────────────────────┼──────────┼───────────┤");
        
        for (Order order : patient.getOrderHistory()) {
            System.out.printf("│ %-6d │ %-18s │ %-8s │ %-9d │%n",
                order.getId(),
                order.getDate(),
                Menu.limitString(order.getStatus(), 8),
                order.getTotalCost()
            );
        }
        
        System.out.println("└────────┴────────────────────┴──────────┴───────────┘");
        
        // Display detailed view of each order
        for (Order order : patient.getOrderHistory()) {
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
    
    static void addMoneyToWallet(Patient patient, Scanner scanner) {
        Menu.clearScreen();
        System.out.println("===== ADD MONEY TO WALLET =====");
        System.out.println("Current balance: " + patient.getWallet());
        System.out.print("Enter amount to add: ");
        
        int amount = Menu.getIntInput(scanner);
        
        if (amount <= 0) {
            System.out.println("Invalid amount! Please enter a positive number.");
            return;
        }
        
        patient.addToWallet(amount);
        System.out.println("Amount added successfully!");
        System.out.println("New balance: " + patient.getWallet());
    }
}