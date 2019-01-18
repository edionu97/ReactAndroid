package com.example.eduard.mobile.repository.room.entity.utils;

public enum OperationType {

    INSERT(0),
    UPDATE(1),
    DELETE(2);

    OperationType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static OperationType getFromInt(int type){

        if(type == 0){
            return INSERT;
        }
        if(type == 1){
            return UPDATE;
        }
        if(type == 2){
            return  DELETE;
        }

        return null;
    }

    private int code;



}
