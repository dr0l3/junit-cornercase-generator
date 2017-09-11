package example.phaseThree;

public class Dime implements PantsContent {
    private int value;

    @Override
    public String displayContents() {
        return this.toString();
    }
}
