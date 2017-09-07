package example;

public class Orderline {
    private String item;
    private int amount;
    private double price;
    private Double vat;

    @Override
    public String toString() {
        return "Orderline{" +
                "item='" + item + '\'' +
                ", amount=" + amount +
                ", price=" + price +
                ", vat=" + vat +
                '}';
    }
}
