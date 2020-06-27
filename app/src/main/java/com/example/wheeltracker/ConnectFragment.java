package com.example.wheeltracker;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class ConnectFragment extends Fragment {

    Button connect_device;
    ConnectFragmentListener connectFragmentListener;
    public interface  ConnectFragmentListener
    {
        void startConnecting(Button connect_device);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            connectFragmentListener = (ConnectFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "implement");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_connect, container, false);
        connect_device = view.findViewById(R.id.connect_device);
        connect_device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pass the reference of button to connectactivity
                connectFragmentListener.startConnecting(connect_device);
            }
        });
        return view;
    }
}
