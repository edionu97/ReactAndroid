package com.example.eduard.mobile.repository.online.utils.handlers;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.util.Pair;
import android.widget.Toast;

import com.example.eduard.mobile.repository.room.entity.Contact;
import com.example.eduard.mobile.utils.configuration.Config;
import com.example.eduard.mobile.view_models.ContactViewModel;
import com.example.eduard.mobile.view_models.factories.ContactViewModelFactory;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class GetContactsForUser extends NetworkHandler<String, Void, Pair<Integer, List<Contact>>> {

    public GetContactsForUser(Context context, boolean toastNotify) {
        this.context = new WeakReference<>(context);
        this.toast = toastNotify;
    }

    @Override
    protected Pair<Integer, List<Contact>> doInBackground(String... strings) {

        jwt = strings[0];

        int responseCode;
        List<Contact> list = new ArrayList<>();

        try {

            // prepare connection
            String url = Config.getInstance().getComposed(context.get(), "server-address", "all-contact-url");
            String header = Config.getInstance().getProperty(context.get(), "jwt-header");

            // get response code
            HttpURLConnection connection = doGet(url, header, jwt);
            responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                list = getResponse(connection.getInputStream());
            }

        } catch (Exception e) {
            e.printStackTrace();
            responseCode = HttpURLConnection.HTTP_CLIENT_TIMEOUT;
        }

        return new Pair<>(responseCode, list);
    }

    @Override
    protected void onPostExecute(Pair<Integer, List<Contact>> integerListPair) {

        super.onPostExecute(integerListPair);

        if (integerListPair.first != HttpURLConnection.HTTP_OK) {

            if(toast) {
                Toast.makeText(
                        context.get(),
                        "Cannot retrieve contacts!",
                        Toast.LENGTH_LONG
                ).show();
            }

            return;
        }

        // update the interface through its model
        FragmentActivity activity = (FragmentActivity) context.get();
        ContactViewModel model = ViewModelProviders.of(
                activity,
                new ContactViewModelFactory(activity, jwt, "")
        ).get(ContactViewModel.class);

        model.setContacts(integerListPair.second);
    }


    private List<Contact> getResponse(InputStream inputStream) throws Exception {

        JSONArray response = createJSONFromResponse(inputStream)
                .getJSONArray("contacts");

        List<Contact> contacts = new ArrayList<>();

        for (int i = 0; i < response.length(); ++i) {
            JSONObject jsonObject = response.getJSONObject(i);
            Contact contact = new Contact(
                    jsonObject.getString("firstName"),
                    jsonObject.getString("lastName"),
                    jsonObject.getString("phoneNumber")
            );
            contacts.add(contact);
        }

        return contacts;
    }

    private JSONObject createJSONFromResponse(InputStream inputStream) throws Exception {

        StringBuilder builder = new StringBuilder();

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;

        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }

        return new JSONObject(builder.toString());
    }


    private String jwt;
    private boolean toast;
    private WeakReference<Context> context;
}