import java.io.File;
import java.util.Map;

class Pharmacy
{
    private String name;
    public Inventory inventory;
    private Patient[] patients;
    private Doctor[] doctors;
    private OrderHistory orderHistory;

    Pharmacy(String name)
    {
        this.name = name;
        this.inventory = DataManager.loadInventory();
        if (inventory == null)
        {
            inventory = new Inventory();
        }
        // this.patients = DataManager.loadPatients();
        // if (patients == null)
        // {
        //     patients = new Patient[1000];
        // }
        // this.doctors = DataManager.loadDoctors();
        // if (doctors == null)
        // {
        //     doctors = new Doctor[1000];
        // }
        // this.orderHistory = DataManager.loadOrderHistory();   
        // if (orderHistory == null)
        // {
        //     orderHistory = new OrderHistory();
        // }
    }
}


class Driver
{
    public static void main(String[] args)
    {
        // Create data folder if it doesn't exist
        File folder = new File("data/");
        if (!folder.exists())
        {
            folder.mkdir();
        }
        // Create inventory file if it doesn't exist
        File inventoryFile = new File("data/inventory.txt");
        if (!inventoryFile.exists())
        {
            try
            {
                inventoryFile.createNewFile();
            }
            catch (Exception e)
            {
                System.out.println("Error creating inventory file");
                e.printStackTrace();
            }
        }
        Pharmacy pharmacy = new Pharmacy("Online Pharmacy");

        Medicine crocin = new Medicine(1, "Crocin", 100, true);
        pharmacy.inventory.addToInventory(crocin);
        pharmacy.inventory.addToInventory(crocin);
        pharmacy.inventory.addToInventory(crocin);
        pharmacy.inventory.addToInventory(crocin);
        // pharmacy.inventory.addToInventory(new Medicine(1, "Crocin", 100, true));
        System.out.println(pharmacy.inventory);

        DataManager.saveInventory(pharmacy.inventory);
    }
}