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

import com.example.aminda.smart_tracker.MainActivity;
import com.example.aminda.smart_tracker.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrainLineFragment extends Fragment {

    private MainActivity main;

    public TrainLineFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_train_line, container, false);

        String[] routes = {"dsa", "dasdsa", "efas", "dasdsa", "efas", "dasdsa", "efas", "dasdsa", "efas", "dasdsa", "efas", "dasdsa", "efas", "dasdsa", "efas", "dasdsa", "efas", "dasdsa", "efas", "dasdsa", "efas", "dasdsa", "efas", "dasdsa", "efas"};
        ListAdapter adapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_list_item_1, routes);
        ListView routeList = (ListView)view.findViewById(R.id.trainLineList);
        routeList.setAdapter(adapter);

        routeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

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
}
