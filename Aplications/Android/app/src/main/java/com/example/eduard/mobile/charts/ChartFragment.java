package com.example.eduard.mobile.charts;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.eduard.mobile.R;
import com.example.eduard.mobile.utils.NetworkVolatile;
import com.example.eduard.mobile.view_models.ChartViewModel;
import com.example.eduard.mobile.view_models.factories.ChartViewModelFactory;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChartFragment extends NetworkVolatile implements Observer<Map<String, Integer>> {

    public static String BROADCAST ="chart-fragment";

    public ChartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle bundle = getArguments();

        if(bundle != null){
            username = bundle.getString("username");
            jwt = bundle.getString("jwt");
        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        if(getActivity() == null){
            return;
        }

        (chartViewModel = ViewModelProviders.of(
                getActivity(), new ChartViewModelFactory(getActivity(), jwt, username)
        ).get(ChartViewModel.class)).createChart().observe(this, this);

        register(getActivity(), BROADCAST);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        pieChart = view.findViewById(R.id.chart);
    }

    @Override
    public void onChanged(@Nullable Map<String, Integer> stringIntegerMap) {

        if(stringIntegerMap == null){
            return;
        }

        // set pie entries
        List<PieEntry> entries = new ArrayList<>();
        stringIntegerMap.forEach((k,v)->
            entries.add(
                    new PieEntry(v,k)
            )
        );

        // create data that will be displayed in chart
        PieData data = new PieData(
                cratePieDataSet(entries)
        );

        data.setValueTextSize(11);
        pieChart.setData(entries.isEmpty() ? null : data);
        pieChart.animateY(1000);
        pieChart.getLegend().setTextSize(15);

        // force redraw
        pieChart.invalidate();
    }

    @Override
    public void connectionLost() {
        chartViewModel.createRepository();
        chartViewModel.refreshView();
    }

    @Override
    public void connectionAvailable() {
        chartViewModel.createRepository();
        chartViewModel.refreshView();
    }

    @Override
    public void broadcastReceived() {
        chartViewModel.refreshView();
    }

    private PieDataSet cratePieDataSet(List<PieEntry> entries){

        PieDataSet dataSet = new PieDataSet(entries, "");

        dataSet.setSliceSpace(3);
        dataSet.setSelectionShift(8);

        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        return dataSet;
    }

    public  static ChartFragment getInstance(){

        if(instance == null){
            synchronized (ChartFragment.class){
                if(instance == null){
                    instance = new ChartFragment();
                }
            }
        }

        return instance;
    }

    @Override
    public void onResume() {
        connectionAvailable();
        super.onResume();
    }


    private String jwt;
    private String username;
    private PieChart pieChart;
    private ChartViewModel chartViewModel;
    private static volatile ChartFragment instance;
}
