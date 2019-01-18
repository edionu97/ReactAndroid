package com.example.eduard.mobile.repository.online.utils;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.util.Pair;
import android.widget.Toast;

import com.example.eduard.mobile.repository.online.utils.handlers.AddUnfetchedHandler;
import com.example.eduard.mobile.repository.online.utils.handlers.DeleteHandler;
import com.example.eduard.mobile.repository.online.utils.handlers.FetchHandler;
import com.example.eduard.mobile.repository.online.utils.handlers.GetChartDataHandler;
import com.example.eduard.mobile.repository.online.utils.handlers.GetContactsForUser;
import com.example.eduard.mobile.repository.online.utils.handlers.InsertHandler;
import com.example.eduard.mobile.repository.online.utils.handlers.UpdateHandler;
import com.example.eduard.mobile.repository.room.entity.Contact;
import com.example.eduard.mobile.repository.room.entity.Fetch;
import com.example.eduard.mobile.repository.room.entity.utils.OperationType;
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
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RestCallsHandlers {

    public static void addContactHandler(
            String jwt,
            String first,
            String last,
            String phone,
            Context context,
            boolean notify,
            OnSuccess success) {

        new InsertHandler(context, success, notify).execute(jwt, first, last, phone);
    }

    public static void updateContactHandler(
            String jwt,
            String first,
            String last,
            String phone,
            Context context,
            boolean notify,
            OnSuccess success) {

        new UpdateHandler(context, success, notify).execute(jwt, first, last, phone);
    }

    public static void deleteContactHandler(
            String jwt,
            String first,
            Context context,
            OnSuccess success,
            boolean notify) {

        new DeleteHandler(context, success, notify).execute(jwt, first);
    }

    public static void getContactsForUserHandler(
            String jwt,
            Context context, boolean notify) {

        new GetContactsForUser(context, notify).execute(jwt);
    }

    public static void addUnfetched(
            String jwt,
            String tableName,
            String data,
            int type,
            Context context) {

        new AddUnfetchedHandler(context).execute(jwt, tableName, data, type + "");
    }


    public  static  void getChartData(String jwt, Context context, boolean notify){
        new GetChartDataHandler(context, notify).execute(jwt);
    }

    public static void fetch(String jwtToken, String tableName, String data, int type, Context context) {
        new FetchHandler(context).execute(jwtToken, tableName, data, type+"");
    }


    public static List<Fetch> getUnfetchedData(String jwt, Context context) {
        List<Fetch> list = new ArrayList<>();

        try {

            URL url = new URL(
                    Config.getInstance().getComposed(context, "server-address", "fetching-unfetched-url")
            );

            String header = Config.getInstance().getProperty(context, "jwt-header");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setReadTimeout(5000);
            connection.setConnectTimeout(5000);

            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty(header, jwt);

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                list = getResponse(connection.getInputStream());
            }

        } catch (Exception e) {
            e.printStackTrace();

        }

        return list;
    }

    private static  List<Fetch> getResponse(InputStream inputStream) throws Exception {

        JSONArray response = createJSONFromResponse(inputStream)
                .getJSONArray("unfetched");

        List<Fetch> fetches = new ArrayList<>();

        for (int i = 0; i < response.length(); ++i) {
            JSONObject jsonObject = response.getJSONObject(i);

            Fetch fetch = new Fetch(
                    jsonObject.getString("data"),
                    jsonObject.getString("table"),
                    getNumberFromEnum(jsonObject.getString("type"))
            );

            fetches.add(fetch);
        }

        return fetches;
    }

    private static  int getNumberFromEnum(String data) throws  Exception {

        try{
            return  Integer.parseInt(data);
        }catch (Exception e){
            return OperationType.valueOf(data).getCode();
        }

    }

    private static  JSONObject createJSONFromResponse(InputStream inputStream) throws Exception {

        StringBuilder builder = new StringBuilder();

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;

        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }

        return new JSONObject(builder.toString());
    }

}



