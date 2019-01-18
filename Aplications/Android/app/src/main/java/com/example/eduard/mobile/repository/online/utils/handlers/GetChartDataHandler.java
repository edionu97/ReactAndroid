package com.example.eduard.mobile.repository.online.utils.handlers;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.util.Pair;
import android.widget.Toast;

import com.example.eduard.mobile.repository.room.entity.Contact;
import com.example.eduard.mobile.utils.configuration.Config;
import com.example.eduard.mobile.view_models.ChartViewModel;
import com.example.eduard.mobile.view_models.ContactViewModel;
import com.example.eduard.mobile.view_models.factories.ChartViewModelFactory;
import com.github.mikephil.charting.charts.Chart;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class GetChartDataHandler extends NetworkHandler<String, Void, Pair<Integer, Map<String,Integer>>> {

    public GetChartDataHandler(Context context, boolean toastNotify){
        this.context = new WeakReference<>(context);
        this.toast = toastNotify;
    }

    @Override
    protected Pair<Integer, Map<String,Integer>> doInBackground(String... strings) {

        jwt = strings[0];

        int responseCode = -1;
        Map<String,Integer> map = new HashMap<>();

        try {

            // prepare connection
            String url = Config.getInstance().getComposed(context.get(), "server-address", "all-contact-url");
            String header = Config.getInstance().getProperty(context.get(), "jwt-header");

            // get response
            HttpURLConnection connection = doGet(url, header, jwt);
            responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                map = getResponse(connection.getInputStream());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new Pair<>(responseCode, map);
    }


    @Override
    protected void onPostExecute(Pair<Integer, Map<String,Integer>> contacts) {

        super.onPostExecute(contacts);

        if (contacts.first != HttpURLConnection.HTTP_OK) {

            if(toast) {
                Toast.makeText(
                        context.get(),
                        "Cannot retrieve contacts",
                        Toast.LENGTH_LONG
                ).show();
            }

            return;
        }

        // update the interface through its model
        FragmentActivity activity = (FragmentActivity) context.get();
        ChartViewModel model = ViewModelProviders.of(
                activity,
                new ChartViewModelFactory(activity, jwt, "")
        ).get(ChartViewModel.class);
        model.setChartData(contacts.second);
    }


    private Map<String,Integer> getResponse(InputStream inputStream) throws Exception {

        JSONArray response = createJSONFromResponse(inputStream)
                .getJSONArray("contacts");

        Map<String, Integer> chart = new TreeMap<>();

        for (int i = 0; i < response.length(); ++i) {

            String fistLetter = Character.toUpperCase(
                    response.getJSONObject(i).getString("firstName").charAt(0)
            )+"";

            chart.putIfAbsent(fistLetter, 0);

            chart.put(
                    fistLetter,
                    chart.get(fistLetter) + 1
            );
        }

        return chart;
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
