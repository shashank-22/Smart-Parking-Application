package com.example.shashank.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanCode extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    ZXingScannerView Scannerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Scannerview=new ZXingScannerView(this);
        setContentView(Scannerview);
    }


    @Override
    public void handleResult(Result result) {

        afterqrresult(result.getText());
        onBackPressed();

    }

    public void afterqrresult(String qrresult)
    {
        int occupiedlo= MainHome_Activity.occupiedlot;
        String qrresultcmp="";

        if(occupiedlo==2)
        {
            qrresultcmp="parkinglot1";
        }
        else if(occupiedlo==5)
        {
            qrresultcmp="parkinglot2";
        }
        else if(occupiedlo==9)
        {
            qrresultcmp="parkinglot3";
        }
        else if(occupiedlo==12)
        {
            qrresultcmp="parkinglot4";
        }

        if(qrresult.equals(qrresultcmp))
        {
            MainHome_Activity.status.setText(qrresult);
            Toast.makeText(this, "Successfully parked", Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(this, "You have parked in wrong slot !!! Change immediately ", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        Scannerview.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Scannerview.setResultHandler(this);
        Scannerview.startCamera();
    }
}
