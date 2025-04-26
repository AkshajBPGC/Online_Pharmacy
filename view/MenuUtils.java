// filepath: /home/akshaj/Documents/bits/oops/Online_Pharmacy/view/MenuUtils.java
package view;

/**
 * The MenuUtils interface provides utility methods for display formatting and screen management
 * used across different menus in the Online Pharmacy system.
 */
public interface MenuUtils {
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