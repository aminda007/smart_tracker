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

public class BusAdapter extends ArrayAdapter<String> {

    private final JSONArray buses;

    public BusAdapter(@NonNull Context context, String[] bus,JSONArray buses) {
        super(context, R.layout.bus_row, bus);
        this.buses = buses;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View customView = layoutInflater.inflate(R.layout.bus_row, parent, false);
        String item = getItem(position);
        TextView plateNo = (TextView) customView.findViewById(R.id.plateNo);
        TextView busType = (TextView) customView.findViewById(R.id.busType);
        ImageView busIcon = (ImageView) customView.findViewById(R.id.busIcon);
        JSONObject jo = null;
        try {
            jo = (JSONObject) buses.get(position);
            plateNo.setText((String) jo.get("busNo"));
            busType.setText((String) jo.get("busType"));
            busIcon.setImageResource(R.drawable.ic_directions_bus);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return customView;
    }
}
