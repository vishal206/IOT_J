package com.undamped.medicare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView drip_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        drip_text = findViewById(R.id.drip_text);

        fetchData();

//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("https://api.thingspeak.com/channels/1352492/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        JSONPlaceHolder jsonPlaceholder = retrofit.create(JSONPlaceHolder.class);
//        Call<Feeds> call = jsonPlaceholder.getFeeds();
//        call.enqueue(new Callback<Feeds>() {
//            @Override
//            public void onResponse(Call<Feeds> call, Response<Feeds> response) {
//                if (!response.isSuccessful()) {
//                    Log.e("Info", String.valueOf(response.code()));
//                    return;
//                }
//
//                List<Feeds> feeds = response.body().getFeeds();
//                Log.e("Info", feeds.get(0).getCreated_at());
//                Collections.reverse(feeds);
//                checkDripActivity(feeds);
//                FieldAdapter fieldAdapter = new FieldAdapter(feeds);
//                recyclerView.setAdapter(fieldAdapter);
//            }
//
//            @Override
//            public void onFailure(Call<Feeds> call, Throwable t) {
//                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
//                Log.e("Error", t.getMessage());
//            }
//        });
    }

    private void fetchData() {

        Feeds feed=new Feeds();
        ArrayList<Feeds> feeds = new ArrayList<>();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference ref = database.getReference("ldr");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                double d=Double.parseDouble(dataSnapshot.getValue()+"");
                feed.setField1((int) d);
                Log.d("onDataChange: ",feed.getField1()+"");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        DatabaseReference ref2 = database.getReference("ultrasonic");
        ref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int d=Integer.parseInt(dataSnapshot.getValue()+"");
                feed.setField2((int) d);
                Log.d("onDataChange2: ",feed.getField2()+"");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        feeds.add(feed);
        Log.e("Info", feeds.toString());
        Collections.reverse(feeds);
//        checkDripActivity(feeds);
        FieldAdapter fieldAdapter = new FieldAdapter(feeds);
        recyclerView.setAdapter(fieldAdapter);
    }

    private void checkDripActivity(List<Feeds> feeds) {
        boolean stopped = false;

        int firstMin = Integer.parseInt(feeds.get(0).getCreated_at().substring(14,16));
        int secondMin = Integer.parseInt(feeds.get(1).getCreated_at().substring(14,16));
        int firstHour = Integer.parseInt(feeds.get(0).getCreated_at().substring(11,13));
        int secondHour = Integer.parseInt(feeds.get(1).getCreated_at().substring(11,13));

        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.set(Calendar.HOUR_OF_DAY, firstHour);
        c1.set(Calendar.MINUTE, firstMin);
        c2.set(Calendar.HOUR_OF_DAY, secondHour);
        c2.set(Calendar.MINUTE, secondMin);

        if((feeds.get(0).getField2() == feeds.get(1).getField2()) && (c1.getTimeInMillis() - c2.getTimeInMillis()) > 300000)
            stopped = true;

        if (stopped)
            drip_text.setText("Drip has Stopped");
        else
            drip_text.setText("Drip is Flowing");
    }
}