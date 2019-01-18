package controllers.messages;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class LogoutMessage implements Serializable {

    public LogoutMessage(){

    }

    public LogoutMessage(String username){
        this.username = username;
    }

    public String getUsername(){
        return  username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    @JsonProperty("user")
    private String username;
}
