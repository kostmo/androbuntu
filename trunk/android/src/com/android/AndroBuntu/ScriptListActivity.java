package com.android.AndroBuntu;

import android.app.ListActivity;
import android.database.Cursor;
import android.provider.Contacts.People;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.os.Bundle;

public class ScriptListActivity extends ListActivity {


    private String[] mStrings = {
            "Abondance", "Ackawi", "Acorn"
    };
    
    @Override
    protected void onCreate(Bundle icicle){
        super.onCreate(icicle);

        // We'll define a custom screen layout here (the one shown above), but
        // typically, you could just use the standard ListActivity layout.
 //       setContentView(android.R.layout.simple_list_item_1);

        
        ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mStrings);
       
        
        setListAdapter(adapter);
    }
}

