package com.example.javalearnlab.Topic1;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import com.example.javalearnlab.R;

public class Theory extends AppCompatActivity {

    private Button startTest;
    private ImageButton exitTheory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theory);

        exitTheory = findViewById(R.id.button_exit_menu);
        exitTheory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

}
