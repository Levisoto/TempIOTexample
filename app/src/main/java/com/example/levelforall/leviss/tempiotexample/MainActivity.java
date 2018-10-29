package com.example.levelforall.leviss.tempiotexample;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    FirebaseDatabase database;
    DatabaseReference myRef;

    ListView listView;
    ArrayList<Integer> arrayList = new ArrayList<>();
    ArrayAdapter<Integer> arrayAdapter;

    GraphView graph;

    private final Handler mHandler = new Handler();
    private Runnable mTimer1;
    private Runnable mTimer2;
    private LineGraphSeries<DataPoint> mSeries1;
    private double graph2LastXValue = 5d;


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


        graph = (GraphView) findViewById(R.id.graph);
        initGraph(graph);

        mSeries1 = new LineGraphSeries<>(generateFromArrayList(arrayList));
        graph.addSeries(mSeries1);


    }

    @Override
    protected void onResume() {
        super.onResume();
        mTimer1 = new Runnable() {
            @Override
            public void run() {
                mSeries1.resetData(generateFromArrayList(arrayList));
                mHandler.postDelayed(this, 100);
            }
        };
        mHandler.postDelayed(mTimer1, 100);

    }

    private DataPoint[] generateFromArrayList(ArrayList<Integer> arrayList) {
        DataPoint[] values = new DataPoint[arrayList.size()];
        for(int i=0; i<arrayList.size();i++){
            arrayList.get(i);
            double x = i;
            double y = arrayList.get(i);
            DataPoint v = new DataPoint(x, y);
            values[i] = v;
        }

        return values;
    }


    private void initGraph(GraphView graph) {
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(-200);
        graph.getViewport().setMaxY(200);

        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(50);

        graph.getGridLabelRenderer().setLabelVerticalWidth(15);

        // first mSeries is a line
        mSeries1 = new LineGraphSeries<>();
        mSeries1.setDrawDataPoints(true);
        mSeries1.setDrawBackground(true);
        graph.addSeries(mSeries1);
    }
}
