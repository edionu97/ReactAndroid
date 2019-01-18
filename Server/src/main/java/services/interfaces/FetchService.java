package services.interfaces;

import models.FetchDetails;
import models.enums.OperationType;

import java.util.List;

public interface FetchService {

    List<FetchDetails> unfetched(String username);

    void add(String tableName, OperationType type, String data, String username) throws  Exception;

    void fetch(String tableName, OperationType type, String data, String username) throws  Exception;
}
