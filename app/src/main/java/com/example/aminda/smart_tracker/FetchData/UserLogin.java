package com.example.aminda.smart_tracker.FetchData;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.aminda.smart_tracker.MainActivity;
import com.example.aminda.smart_tracker.R;

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

public class UserLogin extends AsyncTask<String,Void,String []> {

    private MainActivity main;
    private ProgressDialog dialog;

    public UserLogin(MainActivity main){
        this.main =main;
    }


    @Override
    protected String[] doInBackground(String... params) {

        try {
            String user_name = params[0];
            String password = params[1];

            URL url = new URL(main.getApplicationContext().getResources().getString(R.string.IP_ADDRESS )+ main.getApplicationContext().getResources().getString(R.string.AUTHENTICATE));
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data= URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(user_name,"UTF-8")+"&"+URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8")  ;
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
            JSONObject obj = new JSONObject(result[0]);

            if(obj.getInt("status")==200){
                String username=result[1];
                Log.d("efs1111111111111a",username);
                main.setActive(true);
                main.showMapFragment();
                main.setInHome(true);
                main.setUsername(username);
                main.saveUser(username);
                main.startDriverClient();
                Toast.makeText(main.getApplicationContext().getApplicationContext(),"Login is Successful!",Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(main.getApplicationContext().getApplicationContext(),"Login is not Successful!",Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }



    }

}


