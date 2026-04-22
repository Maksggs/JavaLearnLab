package com.example.javalearnlab.ui.test;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.javalearnlab.R;
import com.example.javalearnlab.theory.model.Question;
import com.example.javalearnlab.ui.main.MainActivity;
import com.example.javalearnlab.utils.AuthManager;
import com.example.javalearnlab.utils.ProgressManager;
import com.example.javalearnlab.utils.TestRepository;

import java.util.List;

public class TestActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private List<Question> questions;

    private int correctAnswers = 0;
    private int topicId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        topicId = getIntent().getIntExtra("topic_id", -1);

        viewPager = findViewById(R.id.viewPager);

        questions = TestRepository.getQuestions(this, topicId);

        viewPager.setUserInputEnabled(false);

        viewPager.setAdapter(
                new TestPagerAdapter(this, questions, isCorrect -> onAnswer(isCorrect))
        );
    }

    private void onAnswer(boolean isCorrect) {

        if (isCorrect) correctAnswers++;

        int current = viewPager.getCurrentItem();

        if (current < questions.size() - 1) {
            viewPager.setCurrentItem(current + 1);
        } else {
            showResult();
        }
    }

    private void showResult() {

        boolean success = correctAnswers == questions.size();
        if (!AuthManager.isLogged(this)) {
            finish();
            return;
        }
        if (success) {
            ProgressManager.markTestPassed(this, topicId);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Результат");
        builder.setMessage("Правильно: " + correctAnswers + "/" + questions.size());

        if (success) {
            builder.setPositiveButton("Далее", (d, w) -> {

                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(intent);
                finish();
            });

        } else {
            builder.setPositiveButton("К теории", (d, w) -> {
                finish();
            });
        }

        builder.setCancelable(false);
        builder.show();
    }
}
