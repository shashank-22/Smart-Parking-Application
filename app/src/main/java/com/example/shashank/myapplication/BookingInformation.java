package com.example.shashank.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class BookingInformation extends AppCompatActivity implements View.OnClickListener{

    static long  bookingid=1000;
    Button confirm;
    TextView from;
    TextView to;
    TextView lotno;
    TextView amount;
    TextView location;
    Date dt1;
    Date dt2;
    int hour1;
    int hour2;
    int payment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_information);

        confirm=findViewById(R.id.confirm);
        from=findViewById(R.id.from);
        to=findViewById(R.id.to);
        lotno=findViewById(R.id.lotno);
        amount=findViewById(R.id.amount);
        location=findViewById(R.id.location);

        //...................................
        //amount calculation
        determinedate();

        int days= getDifference(dt1,dt2);

        if(days==0)
        {
            payment=(hour2-hour1)*5;
        }
        else
        {
            payment=days*60;
        }





        //................................







        from.setText("From"+"                    "+getIntent().getStringExtra("startdate")+" " + getIntent().getStringExtra("starttime") );
        to.setText("To                      "+getIntent().getStringExtra("enddate") + " " + getIntent().getStringExtra("endtime"));
        amount.setText("amount                      "+payment);
        lotno.setText("Parking Lot                 "+getIntent().getStringExtra("selectedlotno"));
        location.setText("Location                "+getIntent().getStringExtra("selectedlocation"));



        confirm.setOnClickListener(this);

    }




    public void determinedate()
    {
        String sdarray[]=getIntent().getStringExtra("startdate").split("[/]");
        String sdarray2[]=getIntent().getStringExtra("enddate").split("[/]");

        hour1=decidehour(Integer.parseInt(getIntent().getStringExtra("starttime").substring(0,2)),getIntent().getStringExtra("starttime").substring(6,8));
        hour2=decidehour(Integer.parseInt(getIntent().getStringExtra("endtime").substring(0,2)),getIntent().getStringExtra("endtime").substring(6,8));


        int d1=Integer.parseInt(sdarray[0]);
        int d2=Integer.parseInt(sdarray2[0]);
        int m1=Integer.parseInt(sdarray[1]);
        int m2=Integer.parseInt(sdarray2[1]);
        int y1=Integer.parseInt(sdarray[2]);
        int y2=Integer.parseInt(sdarray2[2]);


        dt1=new Date(d1,m1,y1);
        dt2=new Date(d2,m2,y2);
    }

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

    ///..................................................................

    static class Date
    {
        int d, m, y;

        public Date(int d, int m, int y)
        {
            this.d = d;
            this.m = m;
            this.y = y;
        }

    }


    static int monthDays[] = {31, 28, 31, 30, 31, 30,
            31, 31, 30, 31, 30, 31};

    static int countLeapYears(Date d)
    {
        int years = d.y;


        if (d.m <= 2)
        {
            years--;
        }


        return years / 4 - years / 100 + years / 400;
    }


    static int getDifference(Date dt1, Date dt2)
    {

        int n1 = dt1.y * 365 + dt1.d;


        for (int i = 0; i < dt1.m - 1; i++)
        {
            n1 += monthDays[i];
        }


        n1 += countLeapYears(dt1);


        int n2 = dt2.y * 365 + dt2.d;
        for (int i = 0; i < dt2.m - 1; i++)
        {
            n2 += monthDays[i];
        }
        n2 += countLeapYears(dt2);
        return (n2 - n1);
    }

    //..................................................................





    public void savebookingdata()
    {
        String date=getIntent().getStringExtra("startdate");
        String time= getIntent().getStringExtra("starttime");

        String date2=getIntent().getStringExtra("enddate");
        String time2= getIntent().getStringExtra("endtime");
        String selectedlocation=getIntent().getStringExtra("selectedlocation");



        int selectedlotno=Integer.parseInt(getIntent().getStringExtra("selectedlotno"));
        BookingDetails bs=new BookingDetails(date,time,date2,time2,bookingid,selectedlotno,selectedlocation);


        FirebaseDatabase.getInstance().getReference("BookingDetails")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(bs).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isComplete())
                {
                    Toast.makeText(BookingInformation.this, "Parking Booked Successfully ",Toast.LENGTH_LONG).show();
                    bookingid=bookingid+1;

                    startActivity(new Intent(BookingInformation.this,MainHome_Activity.class));

                }
                else
                {
                    //faliure
                }
            }
        });






    }



    @Override
    public void onClick(View v) {

        if(v==confirm)
        {
            savebookingdata();
        }

    }
}
