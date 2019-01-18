package controllers.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import models.enums.OperationType;

import java.io.Serializable;

public class AddUnfetchedMessage implements Serializable {

    public AddUnfetchedMessage() {

    }

    public AddUnfetchedMessage(String tableName, OperationType type, String data) {
        this.tableName = tableName;
        this.type = type;
        this.data = data;
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

    private OperationType type;

    private String data;

    @JsonProperty("table")
    private String tableName;
}
