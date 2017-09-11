package example.phaseThree;

public class Samsung extends Smartphone {
    private boolean hasBeenUpdated;

    @Override
    public String displayContents() {
        return this.toString();
    }
}
