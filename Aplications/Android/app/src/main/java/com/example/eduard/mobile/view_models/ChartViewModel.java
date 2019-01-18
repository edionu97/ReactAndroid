package com.example.eduard.mobile.view_models;

import android.app.Activity;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import com.example.eduard.mobile.repository.Repository;
import com.example.eduard.mobile.repository.RepositoryProvider;
import com.example.eduard.mobile.view_models.utils.ModelView;

import java.lang.ref.WeakReference;
import java.util.Map;

public class ChartViewModel extends ModelView {

    public ChartViewModel(Activity activity, String jwt, String username) {
        super(username, jwt, activity);
        createRepository();
    }

    public MutableLiveData<Map<String, Integer>> createChart() {
        createRepository();
        repository.getChartData();
        return chartData;
    }

    public void setChartData(Map<String, Integer> data){
        chartData.setValue(data);
    }


    private MutableLiveData<Map<String, Integer>> chartData = new MutableLiveData<>();

    public void refreshView() {
        repository.getChartData();
    }


}
