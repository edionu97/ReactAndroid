package com.example.eduard.mobile.repository.online;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.eduard.mobile.repository.Repository;
import com.example.eduard.mobile.repository.online.utils.RestCallsHandlers;
import com.example.eduard.mobile.repository.room.entity.Fetch;
import com.example.eduard.mobile.repository.room.entity.utils.OperationType;

import org.json.JSONObject;

import java.util.List;

public class NetworkRepository implements Repository {

    public NetworkRepository(Context context, String authToken, boolean toastNotify) {
        reference = context;
        this.jwtToken = authToken;
        this.notifyUI = toastNotify;
    }

    @Override
    public void contactAdd(String firstName, String lastName, String phoneNumber) {

        RestCallsHandlers.addContactHandler(
                jwtToken,
                firstName,
                lastName,
                phoneNumber,
                reference,
                notifyUI,
                () -> {
                    this.forceUpdate();
                    try {
                        JSONObject object = new JSONObject();
                        object.put("first", firstName);
                        object.put("last", lastName);
                        object.put("phone", phoneNumber);
                        addUnfetched("contact", object.toString(), OperationType.INSERT.getCode());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        );

    }

    @Override
    public void contactUpdate(String firstName, String secondName, String phoneNumber) {

        RestCallsHandlers.updateContactHandler(
                jwtToken,
                firstName,
                secondName,
                phoneNumber,
                reference,
                notifyUI,
                () -> {
                    this.forceUpdate();
                    try {
                        JSONObject object = new JSONObject();
                        object.put("first", firstName);
                        object.put("last", secondName);
                        object.put("phone", phoneNumber);
                        addUnfetched("contact", object.toString(), OperationType.UPDATE.getCode());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        );

    }

    @Override
    public void contactDelete(String firstName) {
        RestCallsHandlers.deleteContactHandler(
                jwtToken,
                firstName,
                reference,
                () -> {
                    this.forceUpdate();
                    try {
                        JSONObject object = new JSONObject();
                        object.put("first", firstName);
                        addUnfetched("contact", object.toString(), OperationType.DELETE.getCode());
                    } catch (Exception ignored) {
                    }
                },
                notifyUI
        );
    }

    @Override
    public void getAllContactsForUser() {

        RestCallsHandlers.getContactsForUserHandler(
                jwtToken,
                reference,
                notifyUI
        );
    }


    @Override
    public void getChartData() {
        RestCallsHandlers.getChartData(jwtToken, reference, notifyUI);
    }


    @Override
    public void addUnfetched(
            String tableName,
            String data,
            int type) {

        RestCallsHandlers.addUnfetched(
                jwtToken,
                tableName,
                data,
                type,
                reference
        );


    }

    @Override
    public void fetch(String tableName, String data, int type) {

        RestCallsHandlers.fetch(
                jwtToken,
                tableName,
                data,
                type,
                reference
        );
    }

    @Override
    public List<Fetch> getUnfetchedData() {

        return RestCallsHandlers.getUnfetchedData(
                jwtToken,
                reference
        );
    }


    @Override
    public void syncContactAdd(String firstName, String lastName, String phoneNumber, @NonNull Runnable onSuccess) {

        RestCallsHandlers.addContactHandler(
                jwtToken,
                firstName,
                lastName,
                phoneNumber,
                reference,
                notifyUI,
                onSuccess::run
        );

    }

    @Override
    public void syncContactUpdate(String firstName, String lastName, String phoneNumber, @NonNull Runnable onSuccess) {

        RestCallsHandlers.updateContactHandler(
                jwtToken,
                firstName,
                lastName,
                phoneNumber,
                reference,
                notifyUI,
                onSuccess::run
        );

    }

    @Override
    public void syncContactDelete(String firstName, @NonNull Runnable onSuccess) {

        RestCallsHandlers.deleteContactHandler(
                jwtToken,
                firstName,
                reference,
                onSuccess::run,
                notifyUI
        );

    }

    private void forceUpdate() {
        getAllContactsForUser();
    }

    private String jwtToken;
    private boolean notifyUI;
    private Context reference;
}
