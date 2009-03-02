package com.android.AndroBuntu;

import android.app.ListActivity;
import android.database.Cursor;
import android.provider.Contacts.People;
import android.widget.ListAdapter;
import android.widget.SimpleCursorAdapter;
import android.os.Bundle;

public class ScriptListActivity extends ListActivity {
	 
    @Override
    protected void onCreate(Bundle icicle){
        super.onCreate(icicle);

        // We'll define a custom screen layout here (the one shown above), but
        // typically, you could just use the standard ListActivity layout.
        setContentView(android.R.layout.simple_list_item_1);

        // Query for all people contacts using the Contacts.People convenience class.
        // Put a managed wrapper around the retrieved cursor so we don't have to worry about
        // requerying or closing it as the activity changes state.

        Cursor mCursor = People.queryGroups(this.getContentResolver(), 10);	// FIXME - SECOND ARG?
        startManagingCursor(mCursor);

        // Now create a new list adapter bound to the cursor. 
        // SimpleListAdapter is designed for binding to a Cursor.
        ListAdapter adapter = new SimpleCursorAdapter(
                this, // Context.
                android.R.layout.two_line_list_item,  // Specify the row template to use (here, two columns bound to the two retrieved cursor rows).
                mCursor,                                    // Pass in the cursor to bind to.
                new String[] {People.NAME, People.NAME}, // Array of cursor columns to bind to.
                new int[] {}								// Parallel array of which template objects to bind to those columns.
        );

        // Bind to our new adapter.
        setListAdapter(adapter);
    }
}

