package com.sahilda.todoapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import nl.qbusict.cupboard.QueryResultIterable;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class ToDoList extends AppCompatActivity {

    private ArrayList<Item> items;
    private ArrayAdapter<Item> itemsAdapter;
    ListView lvItems;
    private SQLiteDatabase db;
    private final int REQUEST_CODE = 23;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);

        setupDBs();
        lvItems = (ListView) findViewById(R.id.lvItems);
        readItemsDb();
        //readItems();
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
        setupListViewListener();
    }

    private void setupDBs() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        this.db = dbHelper.getWritableDatabase();
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long id) {
                        Item item = items.get(pos);
                        items.remove(pos);
                        itemsAdapter.notifyDataSetChanged();
                        deleteItemDb(item);
                        return true;
                    }
                }
        );

        lvItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View item, int pos, long id) {
                        onClick(pos);
                    }
                }
        );
    }

    private void onClick(int pos) {
        Intent i = new Intent(ToDoList.this, EditItemActivity.class);
        i.putExtra("pos", pos);
        i.putExtra("item", items.get(pos).name);
        startActivityForResult(i, REQUEST_CODE);
    }

    private void readItemsDb() {
        Cursor items = cupboard().withDatabase(db).query(Item.class).getCursor();
        try {
            QueryResultIterable<Item> itr = cupboard().withCursor(items).iterate(Item.class);
            boolean added = false;
            for (Item item : itr) {
                this.items.add(item);
                added = true;
            }
            if (added) {
                itemsAdapter.notifyDataSetChanged();
            } else {
                this.items = new ArrayList<>();
            }
        } finally {
            items.close();
        }
    }

    private void writeItemDb(Item item) {
        cupboard().withDatabase(db).put(item);
    }

    private void deleteItemDb(Item item) {
        cupboard().withDatabase(db).delete(item);
    }

    /*
    private void readItems() {
        File todoFile = new File(getFilesDir(), "todo.txt");
        try {
            items = new ArrayList<String>(FileUtils.readLines(todoFile, Charset.defaultCharset()));
        } catch (IOException e) {
            items = new ArrayList<>();
        }
    }

    private void writeItems() {
        File todoFile = new File(getFilesDir(), "todo.txt");
        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    */

    public void onAddItem(View view) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        Item item = new Item(itemText);
        itemsAdapter.add(item);
        etNewItem.setText("");
        writeItemDb(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            Item item = items.get(data.getExtras().getInt("pos"));
            item.name = data.getExtras().getString("item");
            itemsAdapter.notifyDataSetChanged();
            writeItemDb(item);
        }
    }

}
