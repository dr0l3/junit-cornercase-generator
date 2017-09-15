package example.phaseThree;

public class IPhone extends Smartphone{
    private boolean overPriced;

    @Override
    public String displayContents() {
        return this.toString();
    }

    @Override
    public String toString() {
        return "IPhone{" +
                "overPriced=" + overPriced +
                '}';
    }
}
