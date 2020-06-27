package com.example.wheeltracker;

import android.content.Context;
import android.content.Intent;

import java.util.HashMap;
import java.util.Map;

public class CallIntent {
    private Context m_context;
    private Class m_cls;
    private Intent new_intent;
    private HashMap<String,String> m_data;
    CallIntent(Context context ,Class cls)
    {
        m_data = new HashMap();
        m_context = context;
        m_cls = cls;
    }
    void callIntent()
    {
        new_intent = new Intent(m_context,m_cls);
        new_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if(!m_data.isEmpty())
        {
            for (HashMap.Entry<String,String> entry : m_data.entrySet())
                new_intent.putExtra(entry.getKey(),entry.getValue());
        }
        m_context.startActivity(new_intent);
    }
    void extraData(String key,String value)
    {
        m_data.put(key,value);
    }
    void callIntentWithFlag(int flags)
    {
        new_intent = new Intent(m_context,m_cls);
        new_intent.setFlags(flags);
        m_context.startActivity(new_intent);
    }

}
