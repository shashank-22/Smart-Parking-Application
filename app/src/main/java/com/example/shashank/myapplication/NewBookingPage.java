package com.example.shashank.myapplication;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class NewBookingPage extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener {


    DatePickerDialog.OnDateSetListener startdatesetlistener,enddatesetlistener;


    private EditText date;
    private EditText time;

    private EditText date2;
    private EditText time2;
    private Spinner spinner;


    String format;
    Calendar currenttime;
    int hour;
    int minute;


    private int hour1,hour2,y1,y2,m1,m2,d1,d2;


    Button proceed;

    String selectedlocation="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_booking_page);



        proceed=findViewById(R.id.proceed);
        proceed.setOnClickListener(this);



        date=findViewById(R.id.date);
        time=findViewById(R.id.time);


        date2=findViewById(R.id.date2);
        time2=findViewById(R.id.time2);


        spinner=findViewById(R.id.locationspinner);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.Locations,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(this);

        //.................................................................................



        // set onclicklistener to start time.....................................................

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timepicker=new TimePickerDialog(NewBookingPage.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        hour1=hourOfDay;
                       hourOfDay= selecttimeformet(hourOfDay);

                        if(hourOfDay<10 && minute==0)
                        {
                            time.setText("0"+hourOfDay+  ":0"+minute+" "+format);
                        }
                        else if(minute==0)
                        {
                            time.setText(hourOfDay+  ":0"+minute+" "+format);
                        }
                        else
                        {
                            Toast.makeText(NewBookingPage.this, "Please select full time like 6:00",Toast.LENGTH_LONG).show();
                        }

                    }
                },hour,minute,true);
                timepicker.show();
            }
        });


        //set onclicklistener to end time..........................................................

        time2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timepicker=new TimePickerDialog(NewBookingPage.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        hour2=hourOfDay;
                        hourOfDay= selecttimeformet(hourOfDay);

                        if(hourOfDay<10 && minute==00)
                        {
                            time2.setText("0"+hourOfDay+  ":0"+minute+" "+format);
                        }
                        else if(minute==00)
                        {
                            time2.setText(hourOfDay+  ":0"+minute+" "+format);
                        }
                        else
                        {
                            Toast.makeText(NewBookingPage.this, "Please select full time like 6:00",Toast.LENGTH_LONG).show();
                        }


                    }
                },hour,minute,true);
                timepicker.show();
            }
        });



// set onclicklistener to startdate...............................



        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal=Calendar.getInstance();
                int year=cal.get(Calendar.YEAR);
                int month=cal.get(Calendar.MONTH);
                int day=cal.get(Calendar.DAY_OF_MONTH);



                DatePickerDialog dilog=new DatePickerDialog(NewBookingPage.this,
                        android.R.style.Theme_DeviceDefault_Dialog_MinWidth,
                        startdatesetlistener
                ,year,month,day);




                dilog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);


                dilog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
                dilog.show();
            }
        });




// set onclicklistener to enddate...............................

        date2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal=Calendar.getInstance();
                int year=cal.get(Calendar.YEAR);
                int month=cal.get(Calendar.MONTH);
                int day=cal.get(Calendar.DAY_OF_MONTH);



                DatePickerDialog dilog=new DatePickerDialog(NewBookingPage.this,
                        android.R.style.Theme_DeviceDefault_Dialog_MinWidth,
                        enddatesetlistener
                        ,year,month,day);



                dilog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);


                dilog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
                dilog.show();
            }
        });


        startdatesetlistener =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                String d =dayOfMonth + "/" + month + "/" + year;

                y1=year;
                m1=month;
                d1=dayOfMonth;
                date.setText(d);
            }
        };

        enddatesetlistener =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                y2=year;
                m2=month;
                d2=dayOfMonth;
                String d =dayOfMonth + "/" + month + "/" + year;
                date2.setText(d);
            }
        };
    }


    public int selecttimeformet(int hour)
    {
        if(hour==0)
        {
            hour=hour+12;
            format="AM";
        }
        else if(hour==12)
        {
            format="PM";
        }
        else if(hour>12)
        {
            hour=hour-12;
            format="PM";
        }
        else
        {
            format="AM";
        }
        return hour;
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

    public boolean validationoftodayhours(){


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


        if(y1==year1 && month1==m1 && date1==d1)
        {
            if(hour1>=curtime+1)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        return true;
    }



    public boolean validationofselecteddateandtime() {
        if (validationoftodayhours() == false) {
            Toast.makeText(NewBookingPage.this, "Inavalid time selection", Toast.LENGTH_LONG).show();
            return false;
        } else {
            if (y2 == y1) {
                if (m2 == m1) {
                    if (d2 == d1) {
                        if (hour2 > hour1) {
                            return true;
                        } else {
                            Toast.makeText(NewBookingPage.this, "Inavalid time selection", Toast.LENGTH_LONG).show();
                            return false;
                        }
                    } else if (d2 < d1) {
                        Toast.makeText(NewBookingPage.this, "Inavalid date selection", Toast.LENGTH_LONG).show();
                        return false;
                    }
                } else if (m2 < m1) {
                    Toast.makeText(NewBookingPage.this, "Inavalid month selection", Toast.LENGTH_LONG).show();
                    return false;
                }
            } else if (y2 < y1) {
                Toast.makeText(NewBookingPage.this, "Inavalid year selection", Toast.LENGTH_LONG).show();
                return false;
            }

            return true;
        }

    }







    @Override
    public void onClick(View v) {
        if(v==proceed)
        {
            boolean validdandt=validationofselecteddateandtime();
            if(!selectedlocation.equals("WTP Parking"))
            {
                Toast.makeText(NewBookingPage.this, "Location is currently unavailable ",Toast.LENGTH_LONG).show();
            }
            else if(validdandt && !selectedlocation.equals("")) {
                startActivity(new Intent(this, Parkinglot.class).putExtra("startdate", date.getText().toString()).putExtra("enddate", date2.getText().toString()).putExtra("starttime", time.getText().toString()).putExtra("endtime", time2.getText().toString()).putExtra("hour1",Integer.toString(hour1)).putExtra("hour2",Integer.toString(hour2)).putExtra("d1",Integer.toString(d1)).putExtra("d2",Integer.toString(d2)).putExtra("m1",Integer.toString(m1)).putExtra("m2",Integer.toString(m2)).putExtra("y1",Integer.toString(y1)).putExtra("y2",Integer.toString(y2)).putExtra("selectedlocation",selectedlocation));

            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedlocation=parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
