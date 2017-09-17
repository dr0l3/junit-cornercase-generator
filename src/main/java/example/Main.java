package example;

import com.google.common.collect.Lists;
import io.vavr.collection.Vector;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        final List<Integer> ints = Lists.newArrayList(1,2,3);
        Something p = new Something(ints);
        System.out.println(p);
        dumb(p);
        System.out.println(p);

        System.out.println(ints);
        alsoDumb(ints);
        System.out.println(ints);

        String hello = "HELLO";
        System.out.println(hello);
        lessDumb(hello);
        System.out.println(hello);

        Vector<Integer> vector = Vector.of(1,2,3);
        System.out.println(vector);
        does(vector);
        System.out.println(vector);
    }

    public static void dumb(Something p) {
        p.getInts().add(4);
    }

    public static void alsoDumb(List<Integer> ints){
        ints.add(5);
    }

    public static void does(Vector<Integer> ints){
        ints.prepend(1);
    }

    public static void lessDumb(String hello){
        hello.toLowerCase();
    }

    private static final class Something{
        private final List<Integer> ints;

        private Something(List<Integer> ints) {
            this.ints = ints;
        }

        public List<Integer> getInts() {
            return ints;
        }

        @Override
        public String toString() {
            return "Something{" +
                    "ints=" + ints +
                    '}';
        }
    }
}
