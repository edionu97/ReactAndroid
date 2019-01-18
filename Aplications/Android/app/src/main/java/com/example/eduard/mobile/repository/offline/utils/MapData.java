package com.example.eduard.mobile.repository.offline.utils;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;

import com.example.eduard.mobile.repository.room.entity.Contact;
import com.example.eduard.mobile.view_models.ChartViewModel;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

class MapData extends AsyncTask<Void, Void, Map<String, Integer>> {

    MapData(Context context, List<Contact> contacts){
        this.context = new WeakReference<>(context);
        this.contacts = contacts;
    }

    @Override
    protected Map<String, Integer> doInBackground(Void... voids) {

        Map<String, Integer> chart = new TreeMap<>();

        for (Contact contact : contacts) {

            String fistLetter = Character.toUpperCase(
                 contact.getFirstName().charAt(0)
            )+"";

            chart.putIfAbsent(fistLetter, 0);

            chart.put(
                    fistLetter,
                    chart.get(fistLetter) + 1
            );
        }

        return chart;
    }

    @Override
    protected void onPostExecute(Map<String, Integer> contacts) {

        super.onPostExecute(contacts);

        // update the interface through its model
        FragmentActivity activity = (FragmentActivity) context.get();
        ChartViewModel model = ViewModelProviders.of(activity).get(ChartViewModel.class);
        model.setChartData(contacts);
    }

    private WeakReference<Context> context;
    private List<Contact> contacts;
}