package com.example.shashank.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class livetracking extends AppCompatActivity implements View.OnClickListener{
    ViewGroup layout;

    public LinkedList<String > bd = new LinkedList();


    private DatabaseReference dbbookinfo;


    String seats = "_A__A_/"
            + "_A__A_/";

    String typeofvehicle = "_C__C_/"
            + "_C__C_/";

    List<TextView> seatViewList = new ArrayList<>();
    int seatSize = 100;
    int seatGaping = 10;

    int STATUS_AVAILABLE = 1;
    int STATUS_BOOKED = 2;
    Button exit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parkinglot);

        layout = findViewById(R.id.layoutSeat);
        exit=findViewById(R.id.confirm);
        exit.setText("Exit");
        exit.setOnClickListener(this);

        dbbookinfo = FirebaseDatabase.getInstance().getReference("slotstatus");
        dbbookinfo.addValueEventListener(valueEventListener);

        seats = "/" + seats;
        typeofvehicle = "/" + typeofvehicle;

    }


    public void drawlotarrangement() {
        LinearLayout layoutSeat = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutSeat.setOrientation(LinearLayout.VERTICAL);
        layoutSeat.setLayoutParams(params);
        layoutSeat.setPadding(8 * seatGaping, 8 * seatGaping, 8 * seatGaping, 8 * seatGaping);


        layout.removeAllViews();
        layout.addView(layoutSeat);

        LinearLayout layout = null;

        int count = 0;

        for (int index = 0; index < seats.length(); index++) {


            if (seats.charAt(index) == '/') {
                count++;
                layout = new LinearLayout(this);
                layout.setOrientation(LinearLayout.HORIZONTAL);
                layoutSeat.addView(layout);
            } else if (seats.charAt(index) == 'U') {
                count++;
                TextView view = new TextView(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(seatSize, seatSize);
                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping);
                view.setLayoutParams(layoutParams);
                view.setPadding(0, 0, 0, 2 * seatGaping);
                view.setId(count);
                view.setGravity(Gravity.CENTER);
                if (typeofvehicle.charAt(index) == 'B')
                    view.setBackgroundResource(R.drawable.reservedbikespot);
                else if (typeofvehicle.charAt(index) == 'C')
                    view.setBackgroundResource(R.drawable.reservedcarspot);
                else if (typeofvehicle.charAt(index) == 'S')
                    view.setBackgroundResource(R.drawable.reservedsuvspot);


                view.setTag(STATUS_BOOKED);

                layout.addView(view);
                seatViewList.add(view);

            } else if (seats.charAt(index) == 'A') {
                count++;
                TextView view = new TextView(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(seatSize, seatSize);
                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping);
                view.setLayoutParams(layoutParams);
                view.setPadding(0, 0, 0, 2 * seatGaping);
                view.setId(count);
                view.setGravity(Gravity.CENTER);
                if (typeofvehicle.charAt(index) == 'B')
                    view.setBackgroundResource(R.drawable.emptybikespot);
                else if (typeofvehicle.charAt(index) == 'C')
                    view.setBackgroundResource(R.drawable.emptycarspot);
                else if (typeofvehicle.charAt(index) == 'S')
                    view.setBackgroundResource(R.drawable.emptysuvspot);

                view.setTag(STATUS_AVAILABLE);
                layout.addView(view);
                seatViewList.add(view);

            } else if (seats.charAt(index) == '_') {
                count++;
                TextView view = new TextView(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(seatSize, seatSize);
                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping);
                view.setLayoutParams(layoutParams);
                view.setBackgroundColor(Color.TRANSPARENT);
                layout.addView(view);

            }
        }


    }


    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            bd.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String bi = snapshot.getValue().toString();
                    //Toast.makeText(livetracking.this, bi, Toast.LENGTH_SHORT).show();
                    bd.add(bi);
                }
            }
            decideslot();

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };


    public void decideslot() {


        if(bd.get(0).equals("0\r\n")) {
            seats = seats.substring(0, 2) + "A" + seats.substring(3, seats.length());
        }
        else if(bd.get(0).equals("1\r\n"))
        {
            seats = seats.substring(0, 2) + "U" + seats.substring(3, seats.length());
        }


        if(bd.get(1).equals("0\r\n")) {
            seats = seats.substring(0, 5) + "A" + seats.substring(6, seats.length());
        }
        else if(bd.get(1).equals("1\r\n"))
        {
            seats = seats.substring(0, 5) + "U" + seats.substring(6, seats.length());
        }

        if(bd.get(2).equals("0\r\n")) {
            seats = seats.substring(0, 9) + "A" + seats.substring(10, seats.length());
        }

        else if(bd.get(2).equals("1\r\n"))
        {
            seats = seats.substring(0, 9) + "U" + seats.substring(10, seats.length());
        }

        if(bd.get(3).equals("0\r\n")) {
            seats = seats.substring(0, 12) + "A" + seats.substring(13, seats.length());
        }
        else if(bd.get(3).equals("1\r\n"))
        {
            seats = seats.substring(0, 12) + "U" + seats.substring(13, seats.length());
        }

        drawlotarrangement();

    }


    @Override
    public void onClick(View v) {
        if(v==exit)
        {
            finish();
            startActivity(new Intent(this,MainHome_Activity.class));
        }
    }
}
