package com.example.shashank.myapplication;

public class BookingDetails {

    public String datefrom;
    public  String timefrom;
    public String dateto;
    public  String timeto;
    public long bookingid;
    public int parkinglotno;
    public String location;

    public BookingDetails()
    {

    }

    public BookingDetails( String datef,String timef,String datet,String timet, long bookingid,int parkinglotno,String loc)
    {
        this.datefrom=datef;
        this.timefrom=timef;
        this.dateto=datet;
        this.timeto=timet;

        this.bookingid=bookingid;
        this.parkinglotno=parkinglotno;
        location=loc;
    }
}
