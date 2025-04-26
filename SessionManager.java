import java.io.Serializable;

public class SessionManager implements Serializable {
    private static User currentUser = null;
    
    // Private constructor to prevent instantiation
    private SessionManager() {
    }
    
    public static boolean login(String username, String password) {
        // Try to find user in patients
        Patient[] patients = DataManager.loadPatients();
        if (patients != null) {
            for (Patient patient : patients) {
                if (patient != null && patient.getUsername().equals(username)) {
                    if (patient.authenticate(password)) {
                        currentUser = patient;
                        return true;
                    }
                    return false; // Wrong password
                }
            }
        }
        
        // Try to find user in doctors
        Doctor[] doctors = DataManager.loadDoctors();
        if (doctors != null) {
            for (Doctor doctor : doctors) {
                if (doctor != null && doctor.getUsername().equals(username)) {
                    if (doctor.authenticate(password)) {
                        currentUser = doctor;
                        return true;
                    }
                    return false; // Wrong password
                }
            }
        }
        
        return false; // User not found
    }
    
    public static void logout() {
        currentUser = null;
    }
    
    public static User getCurrentUser() {
        return currentUser;
    }
    
    public static boolean isLoggedIn() {
        return currentUser != null;
    }
    
    public static boolean isPatient() {
        return currentUser instanceof Patient;
    }
    
    public static boolean isDoctor() {
        return currentUser instanceof Doctor;
    }
    
    public static boolean isAdmin() {
        return currentUser instanceof Admin;
    }
    
    public static Patient getCurrentPatient() {
        if (isPatient()) {
            return (Patient) currentUser;
        }
        return null;
    }
    
    public static Doctor getCurrentDoctor() {
        if (isDoctor()) {
            return (Doctor) currentUser;
        }
        return null;
    }
    
    public static Admin getCurrentAdmin() {
        if (isAdmin()) {
            return (Admin) currentUser;
        }
        return null;
    }
}