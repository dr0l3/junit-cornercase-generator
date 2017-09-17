package example;

import java.util.List;

public class Person {
    private String name;
    private int age;
    private List<Person> family;
    private Jacket jacket;

    public Person(String name) {
        this.name = name;
    }

    public Person(String name, List<Person> family) {
        this.name = name;
        this.family = family;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<Person> getFamily() {
        return family;
    }

    public void setFamily(List<Person> family) {
        this.family = family;
    }

    public Jacket getJacket() {
        return jacket;
    }

    public void setJacket(Jacket jacket) {
        this.jacket = jacket;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", family=" + family +
                ", jacket=" + jacket +
                '}';
    }
}
