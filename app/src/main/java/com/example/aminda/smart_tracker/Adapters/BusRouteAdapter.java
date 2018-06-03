package com.example.aminda.smart_tracker.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aminda.smart_tracker.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class BusRouteAdapter extends ArrayAdapter<String> {

    private final JSONArray routes;

    public BusRouteAdapter(@NonNull Context context, String[] route,JSONArray routes) {
        super(context, R.layout.bus_row, route);
        this.routes=routes;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View customView = layoutInflater.inflate(R.layout.bus_route, parent, false);
//        String item = getItem(position);
        TextView routeNo = (TextView) customView.findViewById(R.id.routeNo);
        TextView routeName = (TextView) customView.findViewById(R.id.routeName);
        JSONObject jo = null;
        try {
            jo = (JSONObject) routes.get(position);
            routeNo.setText((String) jo.get("routeNo"));
            routeName.setText((String) jo.get("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return customView;
    }
}
