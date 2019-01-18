package com.example.eduard.mobile.repository.offline;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import com.example.eduard.mobile.repository.Repository;
import com.example.eduard.mobile.repository.offline.utils.AddContactHandler;
import com.example.eduard.mobile.repository.offline.utils.ChartHandler;
import com.example.eduard.mobile.repository.offline.utils.DeleteContactHandler;
import com.example.eduard.mobile.repository.offline.utils.GetAllContactHandler;
import com.example.eduard.mobile.repository.offline.utils.UpdateContactHandler;
import com.example.eduard.mobile.repository.room.dao.ContactDao;
import com.example.eduard.mobile.repository.room.dao.FetchDao;
import com.example.eduard.mobile.repository.room.dao.UserDao;
import com.example.eduard.mobile.repository.room.database.AppDatabase;
import com.example.eduard.mobile.repository.room.entity.Contact;
import com.example.eduard.mobile.repository.room.entity.Fetch;
import com.example.eduard.mobile.repository.room.entity.utils.OperationType;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.List;

public class LocalRepository implements Repository {

    public LocalRepository(Context context, String authToken, boolean toastNotify) {

        this.userRepository = AppDatabase.getInstance(context).userDao();
        this.contactRepository = AppDatabase.getInstance(context).contactDao();
        this.fetchRepository = AppDatabase.getInstance(context).fetchDao();

        this.username = authToken;
        this.toastNotify = toastNotify;
        this.context = new WeakReference<>(context);
    }

    @Override
    public void getAllContactsForUser() {
        new GetAllContactHandler(context.get(), contactRepository).execute(username);
    }

    @Override
    public void contactAdd(String firstName, String lastName, String phoneNumber) {

        new AddContactHandler(

                context.get(), userRepository, contactRepository, toastNotify,
                () -> {
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
        ).execute(
                username, firstName, lastName, phoneNumber
        );
    }

    @Override
    public void contactUpdate(String firstName, String lastName, String phoneNumber) {
        new UpdateContactHandler(
                context.get(), contactRepository, toastNotify,
                ()->{
                    try {
                        JSONObject object = new JSONObject();
                        object.put("first", firstName);
                        object.put("last", lastName);
                        object.put("phone", phoneNumber);
                        addUnfetched("contact", object.toString(), OperationType.UPDATE.getCode());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
        ).execute(
                username, firstName, lastName, phoneNumber
        );
    }

    @Override
    public void contactDelete(String firstName) {
        new DeleteContactHandler(
                context.get(), contactRepository, toastNotify,
                ()->{
                    try {
                        JSONObject object = new JSONObject();
                        object.put("first", firstName);
                        addUnfetched("contact", object.toString(), OperationType.DELETE.getCode());
                    }catch (Exception ignored){
                    }
                }
        ).execute(
                username, firstName
        );
    }

    @Override
    public void getChartData() {
        new ChartHandler(context.get(), contactRepository).execute(username);
    }

    @Override
    public void addUnfetched(String tableName, String data, int type) {

        fetchRepository.insert(
                new Fetch(
                        username, data, tableName, type
                )
        );
    }

    @Override
    public void fetch(String tableName, String data, int type) {

        Fetch fetch = fetchRepository.getByAllFields(
                username, data, tableName, type
        );

        if (fetch == null) {
            return;
        }

        fetchRepository.delete(fetch);
    }

    @Override
    public List<Fetch> getUnfetchedData() {
        return fetchRepository.getUnfetched(username);
    }


    @Override
    public void syncContactAdd(String firstName, String lastName, String phoneNumber, Runnable onSuccess){
        new AddContactHandler(
                context.get(), userRepository, contactRepository, toastNotify, onSuccess
        ).execute(
                username, firstName, lastName, phoneNumber
        );
    }

    @Override
    public void syncContactUpdate(String firstName, String lastName, String phoneNumber, Runnable onSuccess) {
        new UpdateContactHandler(context.get(), contactRepository, toastNotify, onSuccess).execute(
                username, firstName, lastName, phoneNumber
        );
    }

    @Override
    public void syncContactDelete(String firstName, Runnable onSuccess) {
        new DeleteContactHandler(
                context.get(), contactRepository, toastNotify, onSuccess
        ).execute(
                username, firstName
        );
    }

    private String username;
    private boolean toastNotify;
    private UserDao userRepository;
    private FetchDao fetchRepository;
    private ContactDao contactRepository;
    private WeakReference<Context> context;
}
