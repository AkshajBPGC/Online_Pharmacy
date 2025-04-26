package model;

import java.io.Serializable;
import java.util.Objects;

public class Medicine implements Serializable
{
    private int id;
    private String name;
    private int price;
    private boolean prescriptionRequired;

    public Medicine(int id, String name, int price, boolean prescriptionRequired)
    {
        this.id = id;
        this.name = name;
        this.price = price;
        this.prescriptionRequired = prescriptionRequired;
    }

    public void setPrescriptionRequired(boolean required)
    {
        this.prescriptionRequired = required;
    }

    public void setPrice(int price)
    {
        this.price = price;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public int getPrice()
    {
        return price;
    }

    public boolean isPrescriptionRequired()
    {
        return prescriptionRequired;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);  // Generate hash code based on ID
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Medicine medicine = (Medicine) o;
        return id == medicine.id;  // Compare medicines based on their ID
    }

}