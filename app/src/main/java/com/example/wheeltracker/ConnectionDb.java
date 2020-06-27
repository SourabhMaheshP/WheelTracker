package com.example.wheeltracker;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

public class ConnectionDb {

   private URL url;
   private String link, data;
   private URLConnection conn;
   private OutputStreamWriter wr;
   private BufferedReader reader;
   private String response;

    //user data from server as response
    HashMap userdata = null,googleUser=null,location=null;
    ConnectionDb(String link, String data) {
        this.link = link;
        this.data = data;
    }
    private void init()
    {
        response = "";
        try {
            url = new URL(link);
            conn = url.openConnection();
            conn.setDoOutput(true);
            wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            response = reader.readLine();
        }catch (Exception ee)
        {
            Log.e("Exception",ee.toString());
        }

    }
    HashMap connectionLogin()
    {        //post request
            userdata = new HashMap<>();
            init();
            try{
                JSONObject jsonobject = new JSONObject(response);
                userdata.put("success",jsonobject.getString("success"));
                userdata.put("sensor_id",jsonobject.getInt("sensor_id"));
                userdata.put("user_id",jsonobject.getInt("user_id"));
                userdata.put("name",jsonobject.getString("name"));
                userdata.put("email",jsonobject.getString("email"));
                userdata.put("status_src",jsonobject.getInt("status_src"));
                userdata.put("status_dest",jsonobject.getInt("status_dest"));
            }catch (JSONException ee)
            {
                userdata.put("JSON",ee.toString());
            }
            return userdata;
    }

    String connectionRegister() {
        init();
        return response;
    }

    HashMap connectionGoogleSignIn() {        //post request
        googleUser = new HashMap<>();
        init();
        try{
            JSONObject jsonobject = new JSONObject(response);
            googleUser.put("success",jsonobject.getString("success"));
            googleUser.put("phone",jsonobject.getString("phone"));

        }catch (JSONException ee)
        {
            googleUser.put("JSON",ee.toString());
        }
        return googleUser;
    }
    String connectToDevice()
    {
        init();
        return response;
    }
    String logout()
    {
        init();
        return response;
    }
    HashMap getLocation()
    {
        location = new HashMap();
        init();
        try{
            JSONObject jsonobject = new JSONObject(response);
            location.put("success",jsonobject.getString("success"));
            Double latitude = jsonobject.getDouble("latitude");
            Double longitude = jsonobject.getDouble("longitude");
            location.put("latitude",latitude.toString());
            location.put("longitude",longitude.toString());
        }catch (JSONException ee)
        {
           location.put("JSON",ee.toString());
           Log.e("JSONError","getlocation");
        }
        return location;
    }
}
