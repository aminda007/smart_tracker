package com.example.aminda.smart_tracker.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
    private static final String TAG = "BusTimeTable";

    public BusTimeTable() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_bus_time_table, container, false);
        String[] routes = {"00:00 AM - 01:00 AM ",
                "01:00 AM - 02.00 AM",
                "02:00 AM - 03:00 AM",
                "03:00 AM - 04:00 AM",
                "04:00 AM - 05:00 AM",
                "05:00 AM - 06:00 AM",
                "06:00 AM - 07:00 AM",
                "07:00 AM - 08:00 AM",
                "08:00 AM - 09:00 AM",
                "09:00 AM - 10:00 AM",
                "10:00 AM - 11:00 AM",
                "11:00 AM - 12:00 AM",
                "12:00 AM - 01:00 PM",
                "01:00 PM - 02.00 PM",
                "02:00 PM - 03:00 PM",
                "03:00 PM - 04:00 PM",
                "04:00 PM - 05:00 PM",
                "05:00 PM - 06:00 PM",
                "06:00 PM - 07:00 PM",
                "07:00 PM - 08:00 PM",
                "08:00 PM - 09:00 PM",
                "09:00 PM - 10:00 PM",
                "11:00 PM - 12:00 PM"};
        ListAdapter adapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_list_item_1, routes);
        ListView routeList = (ListView)view.findViewById(R.id.timeList);
        routeList.setAdapter(adapter);

        routeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showBusList(String.valueOf(adapterView.getItemAtPosition(i)));
            }
        });

        return view;
    }

    private void showBusList(String time) {
        Log.d(TAG, time);
        time = time.trim();
        String timeHigh="";
        String timeLow="";
        if(time.contains("12:00 AM -")){
            timeLow = "12:00";
            timeHigh = "13:00";
        }
        else if(!time.contains("PM")){
            timeLow = time.substring(0,5);
            timeHigh = time.substring(11, 16);
        }else{
            timeLow = time.substring(0,2);
            timeHigh = time.substring(11, 13);
            int low = Integer.parseInt(timeLow);
            int high = Integer.parseInt(timeHigh);
            low = 12 +low;
            high = 12 +high;
            timeLow = String.valueOf(low)+":00";
            timeHigh = String.valueOf(high)+":00";
        }
        Log.d(TAG, timeLow);
        Log.d(TAG, timeHigh);
        BusList br = new BusList(main);
        main.setBusTimeLow(timeLow);
        main.setBusTimeHigh(timeHigh);
        Log.d(TAG, main.getSelectedBusRoute());
        br.execute(main.getSelectedBusRoute(), timeLow, timeHigh);
    }

    public MainActivity getMain() {
        return main;
    }

    public void setMain(MainActivity main) {
        this.main = main;
    }
}
