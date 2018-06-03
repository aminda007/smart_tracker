package com.example.aminda.smart_tracker.FetchData;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.aminda.smart_tracker.Fragments.BusFragment;
import com.example.aminda.smart_tracker.MainActivity;
import com.example.aminda.smart_tracker.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by uwin5 on 05/20/18.
 */

public class BusList extends AsyncTask<String,Void,String []> {

    private MainActivity main;
    private ProgressDialog dialog;

    public BusList(MainActivity main){
        this.main =main;
    }


    @Override
    protected String[] doInBackground(String... params) {

        try {
            String route_id = params[0];
//            String password = params[1];

            URL url = new URL(main.getApplicationContext().getResources().getString(R.string.IP_ADDRESS )+ main.getApplicationContext().getResources().getString(R.string.BUS_LIST));
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data= URLEncoder.encode("route_id","UTF-8")+"="+URLEncoder.encode(route_id,"UTF-8") ;
            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            String result = "";
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                result += line;

            }

            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();

            return new String[]{result, params[0]};
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            return new String[]{main.getApplicationContext().getResources().getString(R.string.NO_INTERNET)};
        }

        return null;
    }


    @Override
    protected void onPreExecute() {
//        dialog = new ProgressDialog(context);
//        this.dialog.setMessage("Please Wait!!");
//        this.dialog.show();

    }

    @Override
    protected void onPostExecute(String [] result) {
        super.onPostExecute(result);
//        if (dialog.isShowing()) {
//            dialog.dismiss();
//        }
        Log.d("fcad",result[0]);
        try {
//            JSONObject obj = new JSONObject(result[0]);

            JSONArray obj = new JSONArray(result[0]);
            main.setBusArray(obj);
            for (int i = 0; i< obj.length(); i++) {
                JSONObject jo = (JSONObject) obj.get(i);
                String routeNo  = (String) jo.get("busNo");
                String name = (String) jo.get("busType");
//                int id = (int) jo.get("id");
                Log.d("aaaaaaaaaa",routeNo+name);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        BusFragment busFragment = new BusFragment();
        busFragment.setMain(main);
        main.showFragment(busFragment);

    }

}


