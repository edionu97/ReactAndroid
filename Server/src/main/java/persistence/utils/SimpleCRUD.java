package persistence.utils;

import java.util.List;

public interface SimpleCRUD<T> {

    void delete(T element) throws  Exception;
    void update(T element) throws  Exception;


}
