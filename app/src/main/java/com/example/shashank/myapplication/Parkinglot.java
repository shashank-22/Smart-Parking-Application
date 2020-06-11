package com.example.shashank.myapplication;

import android.annotation.SuppressLint;
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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Parkinglot extends AppCompatActivity implements View.OnClickListener {
    ViewGroup layout;

    public LinkedList<BookingDetails> bd=new LinkedList();



    private DatabaseReference dbbookinfo;

    int selectedlotno;

    String seats = "_A__A_/"
            + "_A__A_/";

    String typeofvehicle="_C__C_/"
            + "_C__C_/";

    List<TextView> seatViewList = new ArrayList<>();
    int seatSize = 100;
    int seatGaping = 10;

    int STATUS_AVAILABLE = 1;
    int STATUS_BOOKED = 2;
    int selectedIds =-1;
    Button confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parkinglot);

        layout = findViewById(R.id.layoutSeat);
        confirm=findViewById(R.id.confirm);
        confirm.setOnClickListener(this);



        dbbookinfo=FirebaseDatabase.getInstance().getReference("BookingDetails");
        dbbookinfo.addListenerForSingleValueEvent(valueEventListener);

        seats = "/" + seats;
        typeofvehicle="/"+typeofvehicle;

    }




    public void drawlotarrangement()
    {
        LinearLayout layoutSeat = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutSeat.setOrientation(LinearLayout.VERTICAL);
        layoutSeat.setLayoutParams(params);
        layoutSeat.setPadding(8 * seatGaping, 8 * seatGaping, 8 * seatGaping, 8 * seatGaping);
        layout.addView(layoutSeat);

        LinearLayout layout = null;

        int count = 0;

        for (int index = 0; index < seats.length(); index++) {


            if (seats.charAt(index) == '/') {
                count++;
                layout = new LinearLayout(this);
                layout.setOrientation(LinearLayout.HORIZONTAL);
                layoutSeat.addView(layout);
            }

            else if (seats.charAt(index) == 'U') {
                count++;
                TextView view = new TextView(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(seatSize, seatSize);
                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping);
                view.setLayoutParams(layoutParams);
                view.setPadding(0, 0, 0, 2 * seatGaping);
                view.setId(count);
                view.setGravity(Gravity.CENTER);
                if(typeofvehicle.charAt(index)=='B')
                    view.setBackgroundResource(R.drawable.reservedbikespot);
                else if(typeofvehicle.charAt(index)=='C')
                    view.setBackgroundResource(R.drawable.reservedcarspot);
                else if(typeofvehicle.charAt(index)=='S')
                    view.setBackgroundResource(R.drawable.reservedsuvspot);


                view.setTag(STATUS_BOOKED);

                layout.addView(view);
                seatViewList.add(view);
                view.setOnClickListener(this);
            }


            else if (seats.charAt(index) == 'A') {
                count++;
                TextView view = new TextView(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(seatSize, seatSize);
                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping);
                view.setLayoutParams(layoutParams);
                view.setPadding(0, 0, 0, 2 * seatGaping);
                view.setId(count);
                view.setGravity(Gravity.CENTER);
                if(typeofvehicle.charAt(index)=='B')
                    view.setBackgroundResource(R.drawable.emptybikespot);
                else if(typeofvehicle.charAt(index)=='C')
                    view.setBackgroundResource(R.drawable.emptycarspot);
                else if(typeofvehicle.charAt(index)=='S')
                    view.setBackgroundResource(R.drawable.emptysuvspot);

                view.setTag(STATUS_AVAILABLE);
                layout.addView(view);
                seatViewList.add(view);
                view.setOnClickListener(this);
            }

            else if (seats.charAt(index) == '_') {
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


    ValueEventListener valueEventListener=new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

           bd.clear();
            if(dataSnapshot.exists())
            {
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    BookingDetails bi=snapshot.getValue(BookingDetails.class);
                    bd.add(bi);
                }
            }
            decideslot();

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };


    public int decidehour(int hour , String ap)
    {
        if(hour==12 && ap.equals("PM"))
        {
            return 12;
        }
        if(hour==12 && ap.equals("AM"))
        {
            return 0;
        }
        if(ap.equals("PM"))
        {
            return (hour+12);
        }
        if(ap.equals("AM"))
        {
            return hour;
        }
        return -1;
    }



    public void decideslot()
    {
        String sdarray[]=getIntent().getStringExtra("startdate").split("[/]");
        String sdarray2[]=getIntent().getStringExtra("enddate").split("[/]");

        int hour1=decidehour(Integer.parseInt(getIntent().getStringExtra("starttime").substring(0,2)),getIntent().getStringExtra("starttime").substring(6,8));
        int hour2=decidehour(Integer.parseInt(getIntent().getStringExtra("endtime").substring(0,2)),getIntent().getStringExtra("endtime").substring(6,8));


        int d1=Integer.parseInt(sdarray[0]);
        int d2=Integer.parseInt(sdarray2[0]);
        int m1=Integer.parseInt(sdarray[1]);
        int m2=Integer.parseInt(sdarray2[1]);
        int y1=Integer.parseInt(sdarray[2]);
        int y2=Integer.parseInt(sdarray2[2]);




        for(int i=0;i<bd.size();i++)
        {
            int h1cmp=decidehour(Integer.parseInt(bd.get(i).timefrom.substring(0,2)),bd.get(i).timefrom.substring(6,8));
            int h2cmp=decidehour(Integer.parseInt(bd.get(i).timeto.substring(0,2)),bd.get(i).timeto.substring(6,8));

            String d1array[]=bd.get(i).datefrom.split("[/]");
            int d1cmp=Integer.parseInt(d1array[0]);
            int m1cmp=Integer.parseInt(d1array[1]);
            int y1cmp=Integer.parseInt(d1array[2]);



            String d2array[]=bd.get(i).dateto.split("[/]");
            int d2cmp=Integer.parseInt(d2array[0]);
            int m2cmp=Integer.parseInt(d2array[1]);
            int y2cmp=Integer.parseInt(d2array[2]);



            if(!(  (y1>y2cmp) || (y1==y2cmp && m1>m2cmp) || (y1==y2cmp && m1==m2cmp && d1>d2cmp) || (y1==y2cmp && m1==m2cmp && d1==d2cmp  && hour1>=h2cmp) ) && !((y2<y1cmp) || (y2==y1cmp && m2<m1cmp) || (y2==y1cmp && m2==m1cmp && d2<d1cmp) || (y2==y1cmp && m2==m1cmp && d2==d1cmp  && hour2<=h1cmp)))
            {
                seats=seats.substring(0,bd.get(i).parkinglotno)+"U"+seats.substring(bd.get(i).parkinglotno+1,seats.length());
            }

        }
        drawlotarrangement();

    }

    public void confirmbooking()
    {

        String date=getIntent().getStringExtra("startdate");
        String time= getIntent().getStringExtra("starttime");

        String date2=getIntent().getStringExtra("enddate");
        String time2= getIntent().getStringExtra("endtime");

        String selectedlocation=getIntent().getStringExtra("selectedlocation");


        startActivity(new Intent(this, BookingInformation.class).putExtra("startdate", date).putExtra("enddate", date2).putExtra("starttime", time).putExtra("endtime", time2).putExtra("selectedlotno",Integer.toString(selectedlotno)).putExtra("selectedlocation",selectedlocation));

    }

    @SuppressLint("ResourceType")
    @Override
    public void onClick(View view) {

        if (view == confirm) {

            if(selectedIds!=-1) {

                confirmbooking();
            }
            else
            {
                Toast.makeText(Parkinglot.this, "Select atleast one parking spot", Toast.LENGTH_SHORT).show();
            }

        } else {
            if ((int) view.getTag() == STATUS_AVAILABLE) {
                if (selectedIds!=-1) {

                    if(selectedIds==view.getId()) {
                        int ind = view.getId() - 1;
                        if (typeofvehicle.charAt(ind) == 'B')
                            view.setBackgroundResource(R.drawable.emptybikespot);
                        else if (typeofvehicle.charAt(ind) == 'C')
                            view.setBackgroundResource(R.drawable.emptycarspot);
                        else if (typeofvehicle.charAt(ind) == 'S')
                            view.setBackgroundResource(R.drawable.emptysuvspot);

                        selectedIds=-1;
                    }

                    else {
                        Toast.makeText(Parkinglot.this, "You can select one spot only", Toast.LENGTH_SHORT).show();
                    }


                    }

                else
                    {
                    selectedlotno=view.getId() - 1;
                    int ind = view.getId() - 1;
                    selectedIds=view.getId();

                    if (typeofvehicle.charAt(ind) == 'B')
                        view.setBackgroundResource(R.drawable.selectedbikespot);
                    else if (typeofvehicle.charAt(ind) == 'C')
                        view.setBackgroundResource(R.drawable.selectedcarspot);
                    else if (typeofvehicle.charAt(ind) == 'S')
                        view.setBackgroundResource(R.drawable.selectedsuvspot);

                }
            } else if ((int) view.getTag() == STATUS_BOOKED) {
                Toast.makeText(this, "Seat " + view.getId() + " is Booked", Toast.LENGTH_SHORT).show();

            }


        }
    }
}
