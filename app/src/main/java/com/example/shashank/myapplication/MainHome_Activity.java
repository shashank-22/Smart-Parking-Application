package com.example.shashank.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.text.DateFormat;

import java.util.Date;
import java.util.LinkedList;

public class MainHome_Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener , View.OnClickListener{


    private FirebaseAuth firebaseauth;
    private Button initiatebookingbutton;
    private Button scanqr;

    public static int occupiedlot;
    public static TextView status;

    public LinkedList<BookingDetails> bd=new LinkedList();
    public LinkedList<String> userkey=new LinkedList<>();

    private DatabaseReference dbbookinfo;


    public String selectedlocation;
    public String locationstring;
    public int anybookingexists=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home_);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        firebaseauth= FirebaseAuth.getInstance();
        initiatebookingbutton=findViewById(R.id.initiatebookingbutton);

        scanqr=findViewById(R.id.scanqr);
        status=findViewById(R.id.status);
        status.setText("");





        initiatebookingbutton.setOnClickListener(this);

        scanqr.setOnClickListener(this);

        dbbookinfo=FirebaseDatabase.getInstance().getReference("BookingDetails");
        dbbookinfo.addListenerForSingleValueEvent(valueEventListener);




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
                    userkey.add(snapshot.getKey());

                }
            }

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

    public int checkbooking()
    {

        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        String[] temp=currentDateTimeString.split(" ");

        String[] tt=temp[3].split("[:]");

        int curtime=decidehour(Integer.parseInt(tt[0]),temp[4]);


        int year1=Integer.parseInt(temp[2]);
        int date1=Integer.parseInt(temp[1].substring(0,temp[1].length()-1));
        int month1;

        if(temp[0].equals("Apr"))
            month1=4;
        else
            month1=5;

        for(int i=0;i<bd.size();i++)
        {
            if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(userkey.get(i)))
            {
                selectedlocation=bd.get(i).location;
                anybookingexists=1;
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


                if(!( (year1>y2cmp) || (year1==y2cmp && month1>m2cmp) || (year1==y2cmp && month1==m2cmp && date1>d2cmp) || (year1==y2cmp && month1==m2cmp && date1==d2cmp  && curtime>=h2cmp)) && !((year1<y1cmp) || (year1==y1cmp && month1<m1cmp) || (year1==y1cmp && month1==m1cmp && date1<d1cmp) || (year1==y1cmp && month1==m1cmp && date1==d1cmp  && curtime<h1cmp)))
                {
                    occupiedlot=bd.get(i).parkinglotno;
                   return 1;
                }
                else
                {
                    return 0;
                }

            }
        }
        return 0;
    }






    public void onClick(View v) {

        if(v==initiatebookingbutton)
        {

            if(anybookingexists==1)
            {
                Toast.makeText(this, "You have one active booking this time ! can't book another", Toast.LENGTH_LONG).show();
            }
            else {
                startActivity(new Intent(this, NewBookingPage.class));
            }
        }
        else if(v== scanqr)
        {
            int active=checkbooking();
            if(active==1)
            {

                startActivity(new Intent(this,ScanCode.class));

            }
            else {
                Toast.makeText(this, "You have no active Booking available", Toast.LENGTH_LONG).show();
            }


        }


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_home_, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.profile) {
            finish();
            startActivity(new Intent(this,Profile_Activity.class));





        }
        else if (id == R.id.logout)   // Log out from the current session
        {

            firebaseauth.signOut();
            finish();
            startActivity(new Intent(this,LoginActivity.class));

        } else if (id == R.id.nav_share) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:8740003005"));
            startActivity(intent);

        }
        else if (id == R.id.nav_locate) {

            int active2=checkbooking();
            if(selectedlocation.equals("WTP Parking"))
            {
                locationstring="http://maps.google.com/maps?daddr=26.853403,75.805124";
            }

            if(active2==1) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse(locationstring));
                startActivity(intent);
            }
            else
            {
                Toast.makeText(this, "You have no active Booking available", Toast.LENGTH_LONG).show();
            }

        }


        else if (id == R.id.nav_send) {

            startActivity(new Intent(this,livetracking.class));

        }

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }




}
