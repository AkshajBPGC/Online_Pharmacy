package service;

import model.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public interface DataManager 
{
    String dataFolder = "data/";
    String patientsFile = dataFolder + "patients.txt";
    String doctorsFile = dataFolder + "doctors.txt";
    String inventoryFile = dataFolder + "inventory.txt";
    String orderHistoryFile = dataFolder + "orderHistory.txt";
    String adminsFile = dataFolder + "admins.txt";
    
    public static void loadData()
    {
        // Create data folder if it doesn't exist
        File folder = new File(dataFolder);
        if (!folder.exists()) {
            folder.mkdir();
        }
        
        // Ensure all data files exist
        createFileIfNotExists(patientsFile);
        createFileIfNotExists(doctorsFile);
        createFileIfNotExists(inventoryFile);
        createFileIfNotExists(orderHistoryFile);
        createFileIfNotExists(adminsFile);
        
        // Load all data (will be used by specific components as needed)
        loadPatients();
        loadDoctors();
        loadInventory();
        loadOrderHistory();
        loadAdmins();
    }
    
    public static void saveData()
    {
        // This method would be called to save all system data at once
        // Usually called before exiting the system
        // Each component will call its specific save method when needed
    }
    
    private static void createFileIfNotExists(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                System.out.println("Error creating file: " + filePath);
                e.printStackTrace();
            }
        }
    }

    public static Patient[] loadPatients()
    {
        try
        {
            File file = new File(patientsFile);
            if (file.length() == 0) {
                System.out.println("No patient data found. Creating new patients array.");
                return null; // Return null for empty files
            }
            
            FileInputStream fis = new FileInputStream(patientsFile);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Patient[] patients = (Patient[]) ois.readObject();
            ois.close();
            System.out.println("Successfully loaded " + (patients != null ? patients.length : 0) + " patients.");
            return patients;
        }
        catch (Exception e)
        {
            System.out.println("Error loading patients");
            e.printStackTrace();
        }

        return null;
    }

    public static Doctor[] loadDoctors()
    {
        try
        {
            File file = new File(doctorsFile);
            if (file.length() == 0) {
                System.out.println("No doctor data found. Creating new doctors array.");
                return null; // Return null for empty files
            }
            
            FileInputStream fis = new FileInputStream(doctorsFile);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Doctor[] doctors = (Doctor[]) ois.readObject();
            ois.close();
            System.out.println("Successfully loaded " + (doctors != null ? doctors.length : 0) + " doctors.");
            return doctors;
        }
        catch (Exception e)
        {
            System.out.println("Error loading doctors");
            e.printStackTrace();
        }

        return null;
    }

    public static Inventory loadInventory()
    {
        try
        {
            File file = new File(inventoryFile);
            if (file.length() == 0) {
                System.out.println("No inventory data found. Creating new inventory.");
                return null; // Return null for empty files
            }
            
            FileInputStream fis = new FileInputStream(inventoryFile);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Inventory inventory = (Inventory) ois.readObject();
            ois.close();
            System.out.println("Successfully loaded inventory.");
            return inventory;
        }
        catch (Exception e)
        {
            System.out.println("Error loading inventory");
            e.printStackTrace();
        }
        
        return null;
    }

    public static OrderHistory loadOrderHistory()
    {
        try
        {
            File file = new File(orderHistoryFile);
            if (file.length() == 0) {
                System.out.println("No order history found. Creating new order history.");
                return null; // Return null for empty files
            }
            
            FileInputStream fis = new FileInputStream(orderHistoryFile);
            ObjectInputStream ois = new ObjectInputStream(fis);
            OrderHistory orderHistory = (OrderHistory) ois.readObject();
            ois.close();
            System.out.println("Successfully loaded order history.");
            return orderHistory;
        }
        catch (Exception e)
        {
            System.out.println("Error loading order history");
            e.printStackTrace();
        }

        return null;
    }
    
    public static Admin[] loadAdmins() {
        try {
            File file = new File(adminsFile);
            if (file.length() == 0) {
                System.out.println("No admin data found. Creating new admins array.");
                return null; // Return null for empty files
            }
            
            FileInputStream fis = new FileInputStream(adminsFile);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Admin[] admins = (Admin[]) ois.readObject();
            ois.close();
            System.out.println("Successfully loaded " + (admins != null ? admins.length : 0) + " admins.");
            return admins;
        } catch (Exception e) {
            System.out.println("Error loading admins");
            e.printStackTrace();
        }
        
        return null;
    }

    public static void savePatients(Patient[] patients)
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(patientsFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(patients);
            oos.close();
        }
        catch (Exception e)
        {
            System.out.println("Error saving patients");
            e.printStackTrace();
        }
    }

    public static void saveDoctors(Doctor[] doctors)
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(doctorsFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(doctors);
            oos.close();
        }
        catch (Exception e)
        {
            System.out.println("Error saving doctors");
            e.printStackTrace();
        }
    }
    
    public static void saveInventory(Inventory inventory)
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(inventoryFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(inventory);
            oos.close();
        }
        catch (Exception e)
        {
            System.out.println("Error saving inventory");
            e.printStackTrace();
        }
    }

    public static void saveOrderHistory(OrderHistory orderHistory)
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(orderHistoryFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(orderHistory);
            oos.close();
        }
        catch (Exception e)
        {
            System.out.println("Error saving order history");
            e.printStackTrace();
        }
    }
    
    public static void saveAdmins(Admin[] admins) {
        try {
            FileOutputStream fos = new FileOutputStream(adminsFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(admins);
            oos.close();
        } catch (Exception e) {
            System.out.println("Error saving admins");
            e.printStackTrace();
        }
    }
}