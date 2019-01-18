package controllers.messages;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class AuthenticationMessage implements Serializable {

    public AuthenticationMessage(){

    }

    public AuthenticationMessage(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @JsonProperty("user")
    private String username;

    @JsonProperty("pass")
    private String password;
}
