package example.phaseTwo;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Person {
    private int age;
    private String name;
//    private Map<DayOfWeek,Pants> pantsByWeekday;
    private List<Socks> socks;
    private Set<Underpants> underpants;

    public int getAge() {
        return age;
    }

    public String getName() {
        return name;
    }

    public List<Socks> getSocks() {
        return socks;
    }

    public Set<Underpants> getUnderpants() {
        return underpants;
    }
}
