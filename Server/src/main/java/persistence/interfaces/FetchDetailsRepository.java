package persistence.interfaces;

import models.FetchDetails;
import models.enums.OperationType;
import persistence.utils.SimpleCRUD;

import java.util.List;

public interface FetchDetailsRepository extends SimpleCRUD<FetchDetails> {

    void add(String tableName, OperationType type, String data, String username) throws  Exception;

    FetchDetails findById(int fdid);

    FetchDetails findByAllFields(String tableName, OperationType type, String data, String username);

    List<FetchDetails> getAll();

    List<FetchDetails> getUnfetched(String  username);
}
