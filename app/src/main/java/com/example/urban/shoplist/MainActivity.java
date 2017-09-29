package com.example.urban.shoplist;

import android.app.DialogFragment;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements NewItemDialog.DialogListener{

    private final String DATABASE_TABLE = "items";
    private SQLiteDatabase db;
    private ListView listView;
    private Cursor cursor;
    private final int DELETE_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // find list view
       listView = (ListView)  findViewById(R.id.listView1);
        // register listView's context menu (to delete row)
        registerForContextMenu(listView);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        // get database instance
        db = (new DatabaseOpenHelper(this)).getWritableDatabase();
        // get data with using own made queryData method
        queryData();


    }

    // query data from database
    public void queryData() {
        // get data with query
        String[] resultColumns = new String[]{"_id","name","count","price"};
        cursor = db.query(DATABASE_TABLE,resultColumns,null,null,null,null,"count DESC",null);

        // add data to adapter
        ListAdapter adapter = new SimpleCursorAdapter(this,
                R.layout.list_item, cursor,
                new String[] {"name", "count","price"},      // from
                new int[] {R.id.name, R.id.count, R.id.price}    // to
                ,0);  // flags
        // show data in listView
        listView.setAdapter(adapter);

        Double result = 0.0;
        if (cursor.moveToFirst()){
            while(!cursor.isAfterLast()){
                Double p = cursor.getDouble(cursor.getColumnIndex("price"));
                Double c = cursor.getDouble(cursor.getColumnIndex("count"));
//
                Double x = p * c;
                result = result + x;
                cursor.moveToNext();
            }
        }
//        cursor.close();

        Toast.makeText(getApplicationContext(), "Total Price: "+ result, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    // Handles item selections
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                NewItemDialog eDialog = new NewItemDialog();
                eDialog.show(getFragmentManager(), "Add new Item");
        }
        return false;
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String name, int count, double price) {
        ContentValues values=new ContentValues(3);
        values.put("name", name);
        values.put("price", price);
        values.put("count", count);
        db.insert("items", null, values);
        // get data again
        queryData();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // TODO Auto-generated method stub

    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(Menu.NONE, DELETE_ID, Menu.NONE, "Delete");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case DELETE_ID:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
                String[] args = {String.valueOf(info.id)};
                db.delete("items", "_id=?", args);
                queryData();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // close cursor and db connection
        cursor.close();
        db.close();
    }

}
