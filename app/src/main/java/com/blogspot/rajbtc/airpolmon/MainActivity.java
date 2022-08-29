package com.blogspot.rajbtc.airpolmon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity {


    boolean fanStateS=false,fanStateF=false;

    GifImageView gifImageView;
    TextView textView;
    Button button;
    DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Data");
    MediaPlayer mp;


    String co="",co2="",fire="",monitor="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gifImageView=findViewById(R.id.imageView);
        textView=findViewById(R.id.outputTv);
        button=findViewById(R.id.switch1);
        mp=MediaPlayer.create(getApplicationContext(),R.raw.alarm);
        load();
        setState();

    }

    public void clickButton(View view) {
        fanStateS=!fanStateS;
        setState();
        reference.child("Button").setValue(fanStateS?"1":"0");

    }

    void setState(){
        button.setText(fanStateS?"Off":"On");
        gifImageView.setImageResource(fanStateF||fanStateS?R.drawable.fanmotion:R.drawable.fan_stop);
        textView.setText("CO: "+co+"\nCO2: "+co2+"\nFire: "+fire+"\n\nMonitor: "+monitor);


    }

    void load(){
        reference.child("CO").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                        co=snapshot.getValue(String.class);
                        setState();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.child("CO2").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                co2=snapshot.getValue(String.class);
                setState();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        reference.child("Fire").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                fire=snapshot.getValue(String.class);


                fire=fire.equals("1")?"Detected!":"Not Detected!";
                setState();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        reference.child("Monitor").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                monitor=snapshot.getValue(String.class);
                setState();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


      reference.child("Fan").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String f=snapshot.getValue(String.class);
                fanStateF=f.contains("1")?true:false;
                if(f.equals("1")){
                    if(!mp.isPlaying()){
                        mp.start();;
                    }
                }else
                    mp.stop();
                setState();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });









    }
}