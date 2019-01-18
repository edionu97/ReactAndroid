package controllers.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import models.FetchDetails;

import java.io.Serializable;
import java.util.List;

public class UnfetchedMessage implements Serializable {

    public UnfetchedMessage(){

    }

    public UnfetchedMessage(List<FetchDetails> unfetchedData){
        this.unfetchedData = unfetchedData;
    }

    public List<FetchDetails> getUnfetchedData() {
        return unfetchedData;
    }

    public void setUnfetchedData(List<FetchDetails> unfetchedData) {
        this.unfetchedData = unfetchedData;
    }

    @JsonProperty("unfetched")
    private List<FetchDetails> unfetchedData;
}
