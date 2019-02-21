package com.example.muhammadwasi228.carpoolingv2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class OfferRIdeActivity extends AppCompatActivity {
    private static final int FROM_ADDRESS_REQUEST_CODE = 100;
    private static final int TO_ADDRESS_REQUEST_CODE = 101;
    TextView fromAddress;
    TextView toAddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_ride);

        fromAddress= findViewById(R.id.offer_ride_from_address);
        toAddress=findViewById(R.id.offer_ride_to_address);

        fromAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(OfferRIdeActivity.this,SelectLocationActivity.class);
                startActivityForResult(intent,FROM_ADDRESS_REQUEST_CODE);
            }
        });

        toAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(OfferRIdeActivity.this,SelectLocationActivity.class);
                startActivityForResult(intent,TO_ADDRESS_REQUEST_CODE);            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode){
            case FROM_ADDRESS_REQUEST_CODE:
            //set text view of from address given from select location activity
            case TO_ADDRESS_REQUEST_CODE:

        }
    }
}
