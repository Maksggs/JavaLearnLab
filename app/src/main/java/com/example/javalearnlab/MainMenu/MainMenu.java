package com.example.javalearnlab.MainMenu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.javalearnlab.R;

public class MainMenu extends AppCompatActivity {
    Button buttonTopic1;
    Button buttonTopic2;
    Button buttonTopic3;
    Button buttonTopic4;
    Button buttonTopic5;
    Button buttonTopic6;
    Button buttonTopic7;
    Button buttonTopic8;
    Button buttonTopic9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);


        buttonTopic2 = findViewById(R.id.topic_1);
        buttonTopic2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, com.example.javalearnlab.Topic1.Theory.class);
                startActivity(intent);
            }
        });


        buttonTopic2 = findViewById(R.id.topic_2);
        buttonTopic2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, com.example.javalearnlab.Topic2.Theory.class);
                startActivity(intent);
            }
        });

        buttonTopic3 = findViewById(R.id.topic_3);
        buttonTopic3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, com.example.javalearnlab.Topic3.Theory.class);
                startActivity(intent);
            }
        });

        buttonTopic4 = findViewById(R.id.topic_4);
        buttonTopic4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, com.example.javalearnlab.Topic4.Theory.class);
                startActivity(intent);
            }
        });

        buttonTopic5 = findViewById(R.id.topic_5);
        buttonTopic5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, com.example.javalearnlab.Topic5.Theory.class);
                startActivity(intent);
            }
        });

        buttonTopic6 = findViewById(R.id.topic_6);
        buttonTopic6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, com.example.javalearnlab.Topic6.Theory.class);
                startActivity(intent);
            }
        });

        buttonTopic7 = findViewById(R.id.topic_7);
        buttonTopic7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, com.example.javalearnlab.Topic7.Theory.class);
                startActivity(intent);
            }
        });

        buttonTopic8 = findViewById(R.id.topic_8);
        buttonTopic8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, com.example.javalearnlab.Topic8.Theory.class);
                startActivity(intent);
            }
        });

        buttonTopic8 = findViewById(R.id.topic_9);
        buttonTopic8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, com.example.javalearnlab.Topic9.Theory.class);
                startActivity(intent);
            }
        });
    }
}