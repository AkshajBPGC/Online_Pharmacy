package view;

import java.util.Scanner;

/**
 * The Menu interface provides a common structure for all menus in the Online Pharmacy system.
 * This includes methods for menu navigation and user input.
 * It extends the MenuUtils interface for display formatting and screen management utilities.
 */
public interface Menu {
    /**
     * Shows the menu and handles user interactions
     * @param pharmacy The pharmacy instance
     * @param scanner The scanner for user input
     */
    // void showMenu(Pharmacy pharmacy, Scanner scanner);
    
    /**
     * Waits for the user to press Enter
     * @param scanner The scanner for user input
     */
    static void waitForEnter(Scanner scanner) {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    /**
     * Gets integer input from the user with validation
     * @param scanner The scanner for user input
     * @return The validated integer input
     */
    static int getIntInput(Scanner scanner) {
        while (true) {
            try {
                String input = scanner.nextLine();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a number: ");
            }
        }
    }
}