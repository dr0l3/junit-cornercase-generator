package example.phaseThree;

public abstract class Smartphone implements PantsContent{
    private int notifications;

    @Override
    public String toString() {
        return "Smartphone{" +
                "notifications=" + notifications +
                '}';
    }
}
