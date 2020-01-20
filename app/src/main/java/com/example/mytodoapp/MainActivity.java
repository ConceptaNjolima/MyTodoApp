package com.example.mytodoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String KEY_ITEM_TEXT="item_text";
    public static final String KEY_ITEM_POSITION="item_position";
    public static final int EDIT_TEXT_CODE=20;

    List<String> items;

    Button button_add;
    EditText add_item;
    RecyclerView item_list;
    ItemsAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_add=findViewById(R.id.button_add);
        add_item=findViewById(R.id.add_text);
        item_list=findViewById(R.id.etItem);


        loadItems();

        ItemsAdapter.onLongClickListener onLongClickListener =new ItemsAdapter.onLongClickListener(){
            @Override
            public void OnItemLongClicked(int position) {
                //delete item from model
                items.remove(position);
                //notify adapter the position at which we deleted the item
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(),"Item was deleted",Toast.LENGTH_SHORT).show();
                saveItems();

            }
        };
        ItemsAdapter.OnClickListener clickListener=new ItemsAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                Log.d("MainActivity","Single click detected" + position);
                //Create new activity
                Intent i=new Intent(MainActivity.this,EditActivity.class);
                //Pass data
                i.putExtra(KEY_ITEM_TEXT,items.get(position));
                i.putExtra(KEY_ITEM_POSITION,position);

                //display edited activity
                startActivityForResult(i,EDIT_TEXT_CODE);
            }
        };
        itemsAdapter=new ItemsAdapter(items, onLongClickListener,clickListener);
        item_list.setAdapter(itemsAdapter);
        item_list.setLayoutManager(new LinearLayoutManager(this));
        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String todoItem= add_item.getText().toString();
                //Add item to model
                items.add(todoItem);
                //Notify adapter about inserting an item
                itemsAdapter.notifyItemInserted(items.size()-1);
                add_item.setText("");
                Toast.makeText(getApplicationContext(),"Item was added",Toast.LENGTH_SHORT).show();
                saveItems();

            }
        });
    }
    private File getDataFile()
    {
        return new File(getFilesDir(),"data.txt");
    }
    //Function to read dat.txt
    private void loadItems(){
        try {
            items=new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity","Error reading item",e);
            items=new ArrayList<>();
        }
    }
    //Write the file
    private void saveItems(){
        try {
            FileUtils.writeLines(getDataFile(),items);
        } catch (IOException e) {
            Log.e("MainActivity","Error writing item",e);
        }
    }
}




