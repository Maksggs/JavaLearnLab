package com.example.javalearnlab.MainMenu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.javalearnlab.R;
import com.example.javalearnlab.Topic1.Theory;

public class MainMenu extends AppCompatActivity {
    Button buttonLvl1;
    Button buttonLvl2;
    Button buttonLvl3;
    Button buttonLvl4;
    Button buttonLvl5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_menu);
        buttonLvl1 = findViewById(R.id.level_1);

        buttonLvl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, Theory.class);
                startActivity(intent);
            }
        });

    }
}