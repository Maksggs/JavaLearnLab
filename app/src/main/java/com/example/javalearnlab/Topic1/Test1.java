package com.example.javalearnlab.Topic1;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import com.example.javalearnlab.R;

public class Test1 extends AppCompatActivity {

    ImageButton exitTheory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_type_one);

        exitTheory = findViewById(R.id.button_exit_menu);
        exitTheory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}