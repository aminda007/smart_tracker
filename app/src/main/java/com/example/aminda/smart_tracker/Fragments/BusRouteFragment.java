package com.example.aminda.smart_tracker.Fragments;


import android.content.Context;
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

import com.example.aminda.smart_tracker.Adapters.BusRouteAdapter;
import com.example.aminda.smart_tracker.MainActivity;
import com.example.aminda.smart_tracker.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class BusRouteFragment extends Fragment {

    private MainActivity main;

    public BusRouteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_bus_route, container, false);
        JSONArray ja = main.getBusRoutesArray();
        String[] st= new String[ja.length()];

        try {
            for(int i = 0; i< ja.length(); i++) {
                JSONObject jo = (JSONObject) ja.get(i);
                int id = (int) jo.get("id");
                st[i] = String.valueOf(id);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        String[] routes = {"dsa", "dasdsa", "efas", "dasdsa", "efas", "dasdsa", "efas", "dasdsa", "efas", "dasdsa", "efas", "dasdsa", "efas", "dasdsa", "efas", "dasdsa", "efas", "dasdsa", "efas", "dasdsa", "efas", "dasdsa", "efas", "dasdsa", "efas"};
//        String[] routes = new String[ja.length()];
//        ListAdapter adapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_list_item_1, routes);
        ListAdapter adapter = new BusRouteAdapter(getContext(),st, ja);
        ListView routeList = (ListView)view.findViewById(R.id.busRouteList);
        routeList.setAdapter(adapter);

        routeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("1111111111111", String.valueOf(adapterView.getItemAtPosition(i)));
                main.setSelectedBusRoute(String.valueOf(adapterView.getItemAtPosition(i)));
                openTimeTable();
            }
        });

        return view;

    }

    public MainActivity getMain() {
        return main;
    }

    public void setMain(MainActivity main) {
        this.main = main;
    }

    public void openTimeTable(){
        BusTimeTable busTimeTable = new BusTimeTable();
        busTimeTable.setMain(main);
        main.showFragment(busTimeTable);
    }
}
