package com.example.aminda.smart_tracker.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.aminda.smart_tracker.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SchoolFragment extends Fragment {


    public SchoolFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_school, container, false);
        String[] routes = {"Colombo to Badulla", "Peradeniya Junction to Matale", "Ragama to Noor Nagar", "Kelani Valley Railway Line", "Northern Line", "Mannar Line", "Trincomalee Line", "Batticaloa Line", "Coastal Line"};
        ListAdapter adapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_list_item_1, routes);
        ListView routeList = (ListView)view.findViewById(R.id.schoolList);
        routeList.setAdapter(adapter);
        return  view;
    }

}
