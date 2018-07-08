package com.android.skygym;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker.IntentBuilder;

import static com.google.android.gms.location.places.ui.PlacePicker.getPlace;

public class PlacePicker extends AppCompatActivity {

    int PLACE_PICKER_REQUEST = 1;
    TextView tvPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_picker);
        tvPlace = findViewById(R.id.tvPlace);
    }

    public void goPlacePicker(View view){
        IntentBuilder builder = new IntentBuilder();
        try{
            startActivityForResult(builder.build(PlacePicker.this),PLACE_PICKER_REQUEST);
        }
        catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == PLACE_PICKER_REQUEST){
            if(resultCode == RESULT_OK){
                Place place = getPlace(PlacePicker.this, data);
                tvPlace.setText(place.getAddress());
            }
        }
    }
}
