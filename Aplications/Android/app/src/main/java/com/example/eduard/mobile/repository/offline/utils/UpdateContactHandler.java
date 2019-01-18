package com.example.eduard.mobile.repository.offline.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.eduard.mobile.repository.room.dao.ContactDao;
import com.example.eduard.mobile.repository.room.dao.UserDao;
import com.example.eduard.mobile.repository.room.entity.Contact;
import com.example.eduard.mobile.repository.room.entity.User;

import java.lang.ref.WeakReference;

public class UpdateContactHandler extends AsyncTask<String, Void, Boolean> {

    public UpdateContactHandler(Context context, ContactDao contactDao, boolean toast, Runnable onSuccess){
        this.context = new WeakReference<>(context);
        this.contactRepository = contactDao;
        this.toast = toast;
        this.onSuccess = onSuccess;
    }

    @Override
    protected Boolean doInBackground(String... strings) {

        String username = strings[0];
        String firstName = strings[1];
        String lastName = strings[2];
        String phoneNumber = strings[3];

        Contact contact = contactRepository.findContactByFirstNameAndUser(firstName, username);

        if(contact == null){
            return false;
        }

        contact.setLastName(lastName);
        contact.setPhoneNumber(phoneNumber);

        contactRepository.update(
                contact
        );

        return true;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {

        super.onPostExecute(aBoolean);

        if(aBoolean){

            if(toast) {
                Toast.makeText(
                        context.get(),
                        "Contact updated successfully",
                        Toast.LENGTH_LONG
                ).show();
            }

            if(onSuccess != null) {
                new Thread(onSuccess).start();
            }

            return;
        }

        if(toast) {
            Toast.makeText(
                    context.get(),
                    "Error:Contact not found!",
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    private boolean toast;
    private Runnable onSuccess;
    private ContactDao contactRepository;
    private WeakReference<Context> context;
}
