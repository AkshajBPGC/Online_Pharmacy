import java.util.Scanner;

/**
 * The Menu interface provides a common structure for all menus in the Online Pharmacy system.
 * This includes methods for menu navigation, user input, and display formatting.
 */
public interface Menu {
    /**
     * Shows the menu and handles user interactions
     * @param pharmacy The pharmacy instance
     * @param scanner The scanner for user input
     */
    // abstract void showMenu(Pharmacy pharmacy, Scanner scanner);
    
    /**
     * Clears the console screen (platform independent)
     */
    static void clearScreen() {
        try {
            final String os = System.getProperty("os.name");
            if (os.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            // Fallback if clearing screen fails
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }
    
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
    
    /**
     * Limits a string to a specified maximum length
     * @param input The input string
     * @param maxLength The maximum length allowed
     * @return The truncated string with ellipsis if necessary
     */
    static String limitString(String input, int maxLength) {
        if (input.length() <= maxLength) {
            return input;
        } else {
            return input.substring(0, maxLength - 3) + "...";
        }
    }
}