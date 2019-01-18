package services.implementations;

import models.FetchDetails;
import models.enums.OperationType;
import persistence.interfaces.FetchDetailsRepository;
import services.interfaces.FetchService;

import java.util.List;

public class FetchServiceImpl implements FetchService {

    public FetchServiceImpl(FetchDetailsRepository repository) {
        this.fetchRepo = repository;
    }

    @Override
    public List<FetchDetails> unfetched(String username) {
        return fetchRepo.getUnfetched(username);
    }

    @Override
    public void add(String tableName, OperationType type, String data, String username) throws Exception {
        fetchRepo.add(tableName, type, data, username);
    }

    @Override
    public void fetch(String tableName, OperationType type, String data, String username) throws Exception {

        FetchDetails details = fetchRepo.findByAllFields(tableName, type, data, username);

        if(details == null){
            throw new Exception("Fetch does not exist!");
        }

        fetchRepo.delete(details);
    }

    private FetchDetailsRepository fetchRepo;
}
