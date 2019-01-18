package controllers.messages;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class DeleteContactMessage implements Serializable {

    public DeleteContactMessage() {
    }

    public DeleteContactMessage(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @JsonProperty("first")
    private String firstName;
}
