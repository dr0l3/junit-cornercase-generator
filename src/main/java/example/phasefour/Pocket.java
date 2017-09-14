package example.phasefour;

public class Pocket {
    private Person owner;
    private double amount;

    public Person getOwner() {
        return owner;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "Pocket{" +
                "owner=" + owner +
                ", amount=" + amount +
                '}';
    }
}
