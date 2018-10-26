package com.example.levelforall.leviss.tempiotexample;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    FirebaseDatabase database;
    DatabaseReference myRef;

    ListView listView;
    ArrayList<Integer> arrayList = new ArrayList<>();;
    ArrayAdapter<Integer> arrayAdapter;

    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        listView = (ListView) findViewById(R.id.listview);
        arrayAdapter = new ArrayAdapter<Integer>(MainActivity.this,android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(arrayAdapter);


        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("TempReport");


        ValueEventListener postListener = new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                //Post post = dataSnapshot.getValue(Post.class);
                //Log.d(TAG, " "+dataSnapshot.getValue());
                arrayList.clear();

                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Integer value = postSnapshot.getValue(Integer.class);
                    arrayList.add(value);
                }

                arrayAdapter.notifyDataSetChanged();

                Log.d(TAG, " "+arrayList);


                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        myRef.addValueEventListener(postListener);

    }
}
