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

//    private RecyclerView recyclerView;
//    private TextView drip_text;
    
    TextView tvLdr,tvUltra;
    TextView depth_value, bubble_value;
    int ldr=0;
    int ultra=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bubble_value = findViewById(R.id.bubble_value);
        depth_value = findViewById(R.id.depth_value);
        tvLdr=findViewById(R.id.tvLdr);
        tvUltra=findViewById(R.id.tvUltra);

//        recyclerView = findViewById(R.id.recyclerView);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        drip_text = findViewById(R.id.drip_text);

        fetchData();

//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("https://api.thingspeak.com/channels/1352492/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        JSONPlaceHolder jsonPlaceholder = retrofit.create(JSONPlace class);
//        Call<Feeds> call = jsonPlace getFeeds();
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

        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference ref = database.getReference("ldr");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                double d=Double.parseDouble(dataSnapshot.getValue()+"");
                ldr=(int)d;
                tvLdr.setText("Ldr: "+ldr);
                analyseData();
//                r.setLdr((int) d);
                Log.d("readMainLdr:",ldr+"");
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
                ultra=(int) d;
                tvUltra.setText("Ultrasonic: "+ultra);
                analyseData();
//                r.setUltrasonic((int) d);

                Log.d("readMainUlt ",ultra+"");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

//        readings.add(r);
//        Collections.reverse(readings);
////        checkDripActivity(feeds);
//        FieldAdapter fieldAdapter = new FieldAdapter(readings);
//        recyclerView.setAdapter(fieldAdapter);
    }
    private void analyseData() {
        if (ultra >= 800) {
            bubble_value.setText("No Bubble");
        } else {
            bubble_value.setText("Bubbles Formed");
        }
        Log.d("readAdaLdr: ", ldr + "");
        Log.d("readAdaUlt: ", ultra + "");
        switch (ldr) {
            case 12:
                depth_value.setText("100%");
                break;
            case 11:
                depth_value.setText("91.3%");
                break;
            case 10:
                depth_value.setText("83%");
                break;
            case 9:
                depth_value.setText("74.7%");
                break;
            case 8:
                depth_value.setText("66.4%");
                break;
            case 7:
                depth_value.setText("58.1%");
                break;
            case 6:
                depth_value.setText("49.8%");
                break;
            case 5:
                depth_value.setText("41.5%");
                break;
            case 4:
                depth_value.setText("33.2%");
                break;
            case 3:
                depth_value.setText("24.9%");
                break;
            case 2:
                depth_value.setText("16.6%");
                break;
            case 1:
                depth_value.setText("8.3%");
                break;
            case 0:
                depth_value.setText("0%");
                break;
        }
    }

//    private void checkDripActivity(List<Feeds> feeds) {
//        boolean stopped = false;
//
//        int firstMin = Integer.parseInt(feeds.get(0).getCreated_at().substring(14,16));
//        int secondMin = Integer.parseInt(feeds.get(1).getCreated_at().substring(14,16));
//        int firstHour = Integer.parseInt(feeds.get(0).getCreated_at().substring(11,13));
//        int secondHour = Integer.parseInt(feeds.get(1).getCreated_at().substring(11,13));
//
//        Calendar c1 = Calendar.getInstance();
//        Calendar c2 = Calendar.getInstance();
//        c1.set(Calendar.HOUR_OF_DAY, firstHour);
//        c1.set(Calendar.MINUTE, firstMin);
//        c2.set(Calendar.HOUR_OF_DAY, secondHour);
//        c2.set(Calendar.MINUTE, secondMin);
//
//        if((feeds.get(0).getField2() == feeds.get(1).getField2()) && (c1.getTimeInMillis() - c2.getTimeInMillis()) > 300000)
//            stopped = true;
//
//        if (stopped)
//            drip_text.setText("Drip has Stopped");
//        else
//            drip_text.setText("Drip is Flowing");
//    }
}