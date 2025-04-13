import java.util.Map;

public class Cart
{
    public int id;
    public Map<Medicine, Integer> cart;

    public void addToCart(Medicine m, int quantity)
    {
        cart.put(m, quantity);
    }

    public void addToCart(Prescription prescription)
    {
        for (Medicine m : prescription.getMedicines())
        {
            cart.put(m, cart.getOrDefault(m, 0) + 1);
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
}
