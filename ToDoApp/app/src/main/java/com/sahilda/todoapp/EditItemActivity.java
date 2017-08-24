package com.sahilda.todoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        EditText etListItem = (EditText) findViewById(R.id.etListItem);
        String item = getIntent().getStringExtra("item");
        etListItem.setText(item);
    }

    public void saveItem(View view) {
        EditText etListItem = (EditText) findViewById(R.id.etListItem);
        String itemText = etListItem.getText().toString();
        Intent data = new Intent();
        data.putExtra("item", itemText);
        data.putExtra("pos", getIntent().getIntExtra("pos", -1));
        setResult(RESULT_OK, data);
        finish();
    }

}
