package com.example.aminda.smart_tracker.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.aminda.smart_tracker.Adapters.BusAdapter;
import com.example.aminda.smart_tracker.MainActivity;
import com.example.aminda.smart_tracker.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BusFragment extends Fragment {

    private MainActivity main;
    private static final String TAG = "BusFragment";

    public BusFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bus, container, false);
//        String[] routes = {"dsa", "dasdsa", "efas", "dasdsa", "efas", "dasdsa", "efas", "dasdsa", "efas", "dasdsa", "efas", "dasdsa", "efas", "dasdsa", "efas", "dasdsa", "efas", "dasdsa", "efas", "dasdsa", "efas", "dasdsa", "efas", "dasdsa", "efas"};
//        ListAdapter adapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_list_item_1, routes);
        JSONArray ja = main.getBusArray();
        String[] st= new String[ja.length()];

        try {
            for(int i = 0; i< ja.length(); i++) {
                JSONObject jo = (JSONObject) ja.get(i);
                String username = (String ) jo.get("username");
                st[i] = username;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ListAdapter adapter = new BusAdapter(getContext(),st, ja);
        ListView routeList = (ListView)view.findViewById(R.id.busList);
        routeList.setAdapter(adapter);

        routeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("5555555555555555555555", String.valueOf(adapterView.getItemAtPosition(i)));
                if(checkActive(i)){
                    main.setSelectedDriver(String.valueOf(adapterView.getItemAtPosition(i)));
                    main.setSelectedDriverIndex(i);
                    showBus();
                }else{
                    Toast.makeText(main, "Journey Stopped!", Toast.LENGTH_SHORT).show();
                }

            }
        });
        return view;
    }

    private void showBus() {
        main.subscribeToDriver();
        main.showMapFragment();
        main.showFindIcons();
    }

    private boolean checkActive(int i){
        boolean connected = false;
        try {
            JSONArray a = main.getBusArray();
            JSONObject o = (JSONObject) a.get(i);
            connected = o.getBoolean("connected");
            Log.d(TAG, "jjjjjjjjjjjjjjjjjjjjjjjj"+connected);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return connected;
    }

    public MainActivity getMain() {
        return main;
    }

    public void setMain(MainActivity main) {
        this.main = main;
    }
}
