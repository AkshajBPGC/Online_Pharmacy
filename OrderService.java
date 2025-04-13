import java.util.Map;
import java.util.UUID;

public class OrderService 
{
    // TODO: add order to main order history
    // TODO: make it thread safe
    // TODO: implement it as an atomic transaction

    public Order placeOrder(Patient patient, Cart cart)
    {
        int totalCost = 0;
        for (Map.Entry<Medicine, Integer> entry : cart.cart.entrySet())
        {
            totalCost += entry.getKey().getPrice() * entry.getValue();
        }

        if (patient.getWallet() < totalCost)
        {
            return null;
        }
        
        int uuid = UUID.randomUUID().hashCode();
        Order order = new Order(uuid, patient, cart.getCart(), totalCost, "Placed");
        patient.subtractFromWallet(totalCost);
        patient.addOrder(order);
        cart.emptyCart();
        return order;
    }
}
