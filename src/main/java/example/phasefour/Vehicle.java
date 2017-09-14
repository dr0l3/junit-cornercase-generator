package example.phasefour;

public class Vehicle {
    private Integer wheels;
    private Integer windows;
    private Integer doors;
    private Integer randomNumber;

    public Integer getWheels() {
        return wheels;
    }

    public Integer getWindows() {
        return windows;
    }

    public Integer getDoors() {
        return doors;
    }

    public Integer getRandomNumber() {
        return randomNumber;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "wheels=" + wheels +
                ", windows=" + windows +
                ", doors=" + doors +
                ", randomNumber=" + randomNumber +
                '}';
    }
}
