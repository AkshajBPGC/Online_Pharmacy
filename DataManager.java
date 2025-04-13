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
    
    public static void loadData()
    {

    }
    
    public static void saveData()
    {

    }

    public static Patient[] loadPatients()
    {
        try
        {
            FileInputStream fis = new FileInputStream(patientsFile);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Patient[] patients = (Patient[]) ois.readObject();
            ois.close();
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
            FileInputStream fis = new FileInputStream(doctorsFile);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Doctor[] doctors = (Doctor[]) ois.readObject();
            ois.close();
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
            FileInputStream fis = new FileInputStream(inventoryFile);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Inventory inventory = (Inventory) ois.readObject();
            ois.close();
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
            FileInputStream fis = new FileInputStream(orderHistoryFile);
            ObjectInputStream ois = new ObjectInputStream(fis);
            OrderHistory orderHistory = (OrderHistory) ois.readObject();
            ois.close();
            return orderHistory;
        }
        catch (Exception e)
        {
            System.out.println("Error loading order history");
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
}