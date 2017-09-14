package example.phasefour;

public class Person {
    private int age;
    private Integer ageInMonths;

    public int getAge() {
        return age;
    }

    public Integer getAgeInMonths() {
        return ageInMonths;
    }

    @Override
    public String toString() {
        return "Person{" +
                "age=" + age +
                ", ageInMonths=" + ageInMonths +
                '}';
    }
}
