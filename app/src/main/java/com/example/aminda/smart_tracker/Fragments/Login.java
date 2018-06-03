package com.example.aminda.smart_tracker.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.aminda.smart_tracker.FetchData.UserLogin;
import com.example.aminda.smart_tracker.MainActivity;
import com.example.aminda.smart_tracker.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Login extends Fragment {

    private MainActivity main;
    private EditText username;
    private EditText password;

    public Login() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_login, container, false);
        username = (EditText) view.findViewById(R.id.username);
        password = (EditText) view.findViewById(R.id.password);
        Button btn = (Button) view.findViewById(R.id.signInBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
        return view;
    }

    private void login() {
        UserLogin us = new UserLogin(main);
        Log.d("555555555555",getUsername().getText().toString()+"   "+getPassword().getText().toString());
        us.execute(getUsername().getText().toString(), getPassword().getText().toString());
    }




    public MainActivity getMain() {
        return main;
    }

    public void setMain(MainActivity main) {
        this.main = main;
    }


    public EditText getUsername() {
        return username;
    }

    public EditText getPassword() {
        return password;
    }
}
