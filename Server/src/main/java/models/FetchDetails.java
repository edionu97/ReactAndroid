package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import models.enums.OperationType;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class FetchDetails implements Serializable {

    public FetchDetails(){

    }

    public FetchDetails(String tableName, OperationType type, String data, String username) {
        this.tableName = tableName;
        this.type = type;
        this.data = data;
        this.username = username;
    }

    public int getFdid() {
        return fdid;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public OperationType getType() {
        return type;
    }

    public void setType(OperationType type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }


    public void setFdid(int fdid) {
        this.fdid = fdid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFetched(boolean fetched) {
        this.fetched = fetched;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private int fdid;

    @Column
    @JsonProperty("table")
    private String tableName;

    @Column
    @JsonProperty("type")
    private OperationType type;

    @Column
    @JsonProperty("data")
    private String data;

    @Column
    @JsonIgnore
    private String username;

    public boolean isFetched() {
        return fetched;
    }

    @Column
    @JsonIgnore
    private boolean fetched;
}
