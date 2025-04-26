import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Patient extends User implements Serializable
{
    private int id;
    private String name;
    private String phone;
    private int age;
    private List<Order> orderHistory;
    private List<Prescription> prescriptions;
    private int wallet;
    private Map<Medicine, Integer> cart;

    public Patient() {
        super();
        this.orderHistory = new ArrayList<>();
        this.prescriptions = new ArrayList<>();
    }

    public Patient(int id, String username, String password, String name, String phone, int age, int wallet)
    {
        super(username, password);
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.age = age;
        this.wallet = wallet;
        this.orderHistory = new ArrayList<>();
        this.prescriptions = new ArrayList<>();
    }

    public void addToWallet(int amount)
    {
        wallet += amount;
    }
    
    public void subtractFromWallet(int amount)
    {
        wallet -= amount;
    }

    public void addOrder(Order order)
    {
        orderHistory.add(order);
    }

    public void addPrescription(Prescription prescription)
    {
        prescriptions.add(prescription);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public int getAge() {
        return age;
    }

    public List<Order> getOrderHistory() {
        return orderHistory;
    }

    public List<Prescription> getPrescriptions() {
        return prescriptions;
    }

    public int getWallet() {
        return wallet;
    }

    public Map<Medicine, Integer> getCart() {
        return cart;
    }
}