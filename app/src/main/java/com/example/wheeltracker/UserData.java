package com.example.wheeltracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;

import java.util.HashMap;

//singleton class
//only one instance is created every time
public class UserData {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static UserData userData;
    private GoogleSignInClient m_googleSignInClient;
    private UserData(Context m_context)       //private constructor
    {
        sharedPreferences = m_context.getSharedPreferences("UserData", m_context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
    public static UserData getInstance(Context context)
    {
        if (userData == null)
            userData = new UserData(context);

        return userData;
    }
    void copyData(HashMap userdata)
    {
        try
        {
//            if(
//                    userdata.get("success")!=null && userdata.get("sensor_id").toString()!=null
//                    && userdata.get("user_id").toString()!=null && userdata.get("name")!=null
//                    && userdata.get("email")!=null && userdata.get("status_src").toString()!=null
//                    && userdata.get("status_dest").toString()!=null
//              ) {
                editor.putString("success",(String)userdata.get("success"));
                editor.putString("sensor_id",userdata.get("sensor_id").toString());
                editor.putString("user_id",userdata.get("user_id").toString());
                editor.putString("name",(String)userdata.get("name"));
                editor.putString("email",(String)userdata.get("email"));
                editor.putString("status_src",userdata.get("status_src").toString());
                editor.putString("status_dest",userdata.get("status_dest").toString());
                editor.commit();
//             }

        }
        catch (Exception ee)
        {
            Log.e("prefsExcep",ee.getMessage());
        }
    }
    Object getValue(String Key)
    {
        if(sharedPreferences.contains(Key))
            return sharedPreferences.getString(Key,null);

        return null;
    }
    void putExtra(String Key,String Value)
    {
        editor.putString(Key,Value);
        editor.commit();
    }
    void clearData()
    {
        editor.clear();
        editor.commit();
    }
    void del()
    {
        m_googleSignInClient = null;
        userData = null;
    }
    void googleSignIn(GoogleSignInClient googleSignInClient)
    {
        m_googleSignInClient = googleSignInClient;
    }
    Boolean isGoogleSignIn()
    {
        if(m_googleSignInClient == null)
            return false;
        else
            return true;
    }
    GoogleSignInClient getGoogleSignIn()
    {
        if(isGoogleSignIn())
            return m_googleSignInClient;
        else
            return null;
    }
}
