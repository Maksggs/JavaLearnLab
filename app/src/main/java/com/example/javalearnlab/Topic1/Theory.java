package com.example.javalearnlab.Topic1;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.javalearnlab.R;

public class Theory extends AppCompatActivity {

    Button startTest;
    ImageButton exitTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_theory);

        startTest = findViewById(R.id.start_test);
        exitTest = findViewById(R.id.exit_test);
        exitTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}