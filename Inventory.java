import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Inventory implements Serializable
{
    public Map<Medicine, Integer> inventory;

    
    Inventory()
    {
        inventory = new HashMap<>();
    }

    // @Override
    // public String toString() 
    // {
    //     System.out.println("Inventory:");
    //     for (Medicine med : inventory.keySet())
    //     {
    //         System.out.println(med.getName() + ": " + inventory.get(med));
    //     }
    //     return null;
    // }

    @Override
    public String toString() 
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Inventory:\n");
        
        for (Medicine med : inventory.keySet()) {
            sb.append(med.getName()).append(": ").append(inventory.get(med)).append("\n");
        }
        
        return sb.toString();
    }


    public void addToInventory(Medicine... m)
    {
        for (Medicine med : m)
        {
            if (inventory.containsKey(med))
            {
                inventory.put(med, inventory.get(med) + 1);
            }
            else
            {
                inventory.put(med, 1);
            }
        }
    }

    public boolean removeFromInventory(Medicine m)
    {        
        if (inventory.containsKey(m))
        {
            if (inventory.get(m) > 0)
            {
                inventory.put(m, inventory.get(m) - 1);
                return true;
            }
            else
            {
                inventory.remove(m);
                return true;
            }
        }

        return false;
    }
}
