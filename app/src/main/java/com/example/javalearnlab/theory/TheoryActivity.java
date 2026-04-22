package com.example.javalearnlab.theory;

import static android.content.Intent.getIntent;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.javalearnlab.R;
import com.example.javalearnlab.data.TheoryRepository;
import com.example.javalearnlab.theory.model.Block;
import com.example.javalearnlab.theory.model.Topic;
import com.example.javalearnlab.ui.test.TestActivity;
import com.example.javalearnlab.utils.ProgressManager;

import java.util.List;

public class TheoryActivity extends AppCompatActivity {

    private LinearLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theory);

        container = findViewById(R.id.theoryContainer);

        int topicId = getIntent().getIntExtra("topic_id", -1);

        List<Topic> topics = TheoryRepository.loadTopics(this);

        for (Topic t : topics) {
            if (t.id == topicId) {
                renderBlocks(t);
                ProgressManager.markCompleted(this, t.id);
                break;
            }
        }

        Button startTest = findViewById(R.id.button_start_test);

        startTest.setOnClickListener(v -> {
            Intent intent = new Intent(this, TestActivity.class);
            intent.putExtra("topic_id", topicId);
            startActivity(intent);
        });

    }

    private void renderBlocks(Topic topic) {

        View titleView = getLayoutInflater().inflate(R.layout.item_title, container, false);
        TextView title = titleView.findViewById(R.id.titleContent);
        title.setText(topic.title);
        container.addView(titleView);

        for (Block block : topic.blocks) {

            if (block.type.equals("text")) {

                View v = getLayoutInflater().inflate(R.layout.item_text, container, false);
                TextView tv = v.findViewById(R.id.textContent);
                tv.setText(block.content);
                container.addView(v);

            } else {

                View v = getLayoutInflater().inflate(R.layout.item_code, container, false);
                TextView tv = v.findViewById(R.id.codeContent);
                tv.setText(block.content);
                container.addView(v);
            }
        }
    }
}
