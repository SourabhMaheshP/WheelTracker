package com.example.wheeltracker;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {

    private TextView sensor_id = null,name = null,phone = null,email = null;
    private UserData userData = null;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        userData = UserData.getInstance(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        sensor_id = view.findViewById(R.id.profile_sensor_id);
        name = view.findViewById(R.id.profile_name);
        phone = view.findViewById(R.id.profile_phone);
        email = view.findViewById(R.id.profile_email);

        if(userData.getValue("sensor_id")!=null)
            sensor_id.setText("ID: "+(String)userData.getValue("sensor_id"));
        if(userData.getValue("name")!=null)
            name.setText((String)userData.getValue("name"));
        if(userData.getValue("phone")!=null)
            phone.setText("(+91) "+(String)userData.getValue("phone"));
        if(userData.getValue("email")!=null)
            email.setText((String)userData.getValue("email"));
        return view;
    }
}
