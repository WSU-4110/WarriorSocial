/*package com.example.warriorsocial.ui.discover;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.example.warriorsocial.R;

public class ActMain extends AppCompatActivity {

    CardView csc;
    CardView wsu;
    CardView music;
    CardView anime;
    CardView nba;
    CardView food;
//these are click function for the cards to lead user to a new screen when they click
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        System.out.println("Inside the discover.ActMain");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);//changed act_main to activity_main
        csc = findViewById(R.id.csc);
        wsu = findViewById(R.id.wsu);
        music = findViewById(R.id.music);
        anime = findViewById(R.id.anime);
        nba = findViewById(R.id.nba);
        food = findViewById(R.id.food);

        csc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActMain.this,MainActivity1.class));

            }
        });
        wsu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActMain.this,MainActivity1.class));
            }
        });
        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActMain.this,MainActivity1.class));
            }
        });
        anime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActMain.this,MainActivity1.class));
            }
        });
        nba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActMain.this,MainActivity1.class));
            }
        });
        food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActMain.this,MainActivity1.class));
            }
        });

    }
}

 */