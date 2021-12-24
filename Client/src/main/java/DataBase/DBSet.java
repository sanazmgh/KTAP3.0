package DataBase;

import java.util.LinkedList;

public interface DBSet<T> {
    T get(long id);
    void add(T t);
    void update(T t);
    long lastID ();
}
