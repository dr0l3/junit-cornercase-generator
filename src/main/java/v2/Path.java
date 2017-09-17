package v2;

import io.vavr.collection.List;
import io.vavr.collection.Vector;
import io.vavr.control.Either;

import java.lang.reflect.Field;

public final class Path {
    private Vector<Class> visiting = Vector.empty();
    private List<Either<Class,Field>> path = List.empty();

    public static Path empty(){
        return new Path();
    }

    public Path() {
    }

    public Path(Vector<Class> visiting, List<Either<Class, Field>> path) {
        this.visiting = visiting;
        this.path = path;
    }

    public Path(List<Either<Class, Field>> path) {
        this.path = path;
    }

    public Path(Vector<Class> visiting) {
        this.visiting = visiting;
    }

    public <T> boolean contains(Class<T> clazz){
        return visiting.contains(clazz);
    }

//    public boolean contains(Field field){
//        return path.stream().anyMatch(v -> v.map(field::equals).getOrElse(false));
//    }

    public Path append(Field field){
        return new Path(visiting,path.prepend(Either.right(field)));
    }

    public <T> Path append(Class<T> clazz){
        return new Path(visiting.prepend(clazz), path.prepend(Either.left(clazz)));
    }

    public Path pop(Field field){
        boolean isTop = path.headOption()
                .map(v -> v.map(f -> f.equals(field))
                        .getOrElse(false))
                .getOrElse(false);
        if(isTop){
            return new Path(visiting,path.tail());
        }
        return this;
    }

    public <T> Path pop(Class<T> clazz){
        boolean isTop = path.headOption()
                .map(e -> e.swap().map(c -> c.equals(clazz))
                        .getOrElse(false))
                .getOrElse(false);
        if(isTop){
            return new Path(visiting.remove(clazz), path.tail());
        }
        return new Path(visiting.remove(clazz), path);
    }

    public String getPathAsString(){
        return path.reverse().map(v -> v.fold(c -> "Class: " + c.getName()+". ", f -> "Field: " + f.getName()+ ". "))
                .fold("", (acc, next) -> acc + next);
    }
}
