package com.example.aminda.smart_tracker.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.aminda.smart_tracker.FetchData.BusList;
import com.example.aminda.smart_tracker.FetchData.BusRoutes;
import com.example.aminda.smart_tracker.MainActivity;
import com.example.aminda.smart_tracker.R;
import com.google.android.gms.maps.MapFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class BusTimeTable extends Fragment {

    private String time;
    private MainActivity main;

    public BusTimeTable() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_bus_time_table, container, false);
        String[] routes = {"0.00 AM - 1.00 AM ", "1.00 AM - 2.00AM", "2.00AM - 3.00AM", "dasdsa", "efas", "dasdsa", "efas", "dasdsa", "efas", "dasdsa", "efas", "dasdsa", "efas", "dasdsa", "efas", "dasdsa", "efas", "dasdsa", "efas", "dasdsa", "efas", "dasdsa", "efas", "dasdsa", "efas"};
        ListAdapter adapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_list_item_1, routes);
        ListView routeList = (ListView)view.findViewById(R.id.timeList);
        routeList.setAdapter(adapter);

        routeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showBusList();
            }
        });

        return view;
    }

    private void showBusList() {
        BusList br = new BusList(main);
        br.execute("776");

    }

    public MainActivity getMain() {
        return main;
    }

    public void setMain(MainActivity main) {
        this.main = main;
    }
}
