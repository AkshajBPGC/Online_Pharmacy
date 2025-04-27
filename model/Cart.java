package model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Cart implements Serializable
{
    public int id;
    public Map<Medicine, Integer> cart;
    
    public Cart() {
        this.cart = new HashMap<>();
    }
    
    public Cart(int id) {
        this.id = id;
        this.cart = new HashMap<>();
    }

    public void addToCart(Medicine m, int quantity)
    {
        cart.put(m, cart.getOrDefault(m, 0) + quantity);
    }

    public void addToCart(Medicine... meds)
    {
        for (Medicine m : meds) {
            cart.put(m, cart.getOrDefault(m, 0) + 1);
        }
    }

    public void addToCart(Prescription prescription)
    {
        for (Map.Entry<Medicine, Integer> entry : prescription.getMedicines().entrySet())
        {
            Medicine medicine = entry.getKey();
            int quantity = entry.getValue();
            cart.put(medicine, cart.getOrDefault(medicine, 0) + quantity);
        }
    }

    public void addToCart(Prescription... prescriptions)
    {
        for (Prescription prescription : prescriptions) {
            this.addToCart(prescription);
        }
    }

    public void addToCart(Map<Medicine, Integer> m)
    {
        cart.putAll(m);
    }

    public void removeFromCart(Medicine m)
    {
        cart.remove(m);
    }

    public void emptyCart()
    {
        cart.clear();
    }

    public void displayCart()
    {
        for (Map.Entry<Medicine, Integer> entry : cart.entrySet())
        {
            System.out.println(entry.getKey().getName() + " : " + entry.getValue());
        }
    }    

    public Map<Medicine, Integer> getCart()
    {
        return cart;
    }
    
    public int calculateTotal() {
        int total = 0;
        for (Map.Entry<Medicine, Integer> entry : cart.entrySet()) {
            total += entry.getKey().getPrice() * entry.getValue();
        }
        return total;
    }
}
