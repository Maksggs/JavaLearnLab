package com.example.javalearnlab.ui.test;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.javalearnlab.R;
import com.example.javalearnlab.theory.model.Question;
import com.google.gson.Gson;

import java.util.List;

public class TestSingleFragment extends Fragment {

    private Question question;
    private TestPagerAdapter.AnswerCallback callback;

    public TestSingleFragment() {
        super(R.layout.fragment_test_single);
    }

    // 🔥 Правильное создание Fragment
    public static TestSingleFragment newInstance(Question q,
                                                 TestPagerAdapter.AnswerCallback cb) {

        TestSingleFragment fragment = new TestSingleFragment();

        Bundle args = new Bundle();
        args.putString("question", new Gson().toJson(q));

        fragment.setArguments(args);
        fragment.setCallback(cb);

        return fragment;
    }

    // setter для callback
    public void setCallback(TestPagerAdapter.AnswerCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 🔄 восстановление question
        if (question == null) {
            Bundle args = getArguments();
            if (args != null) {
                String json = args.getString("question");
                if (json != null) {
                    question = new Gson().fromJson(json, Question.class);
                }
            }
        }

        // ❗ защита от null
        if (question == null) {
            Log.e("TEST", "Question is null!");
            return;
        }

        TextView text = view.findViewById(R.id.questionText);
        RadioGroup group = view.findViewById(R.id.optionsGroup);
        Button next = view.findViewById(R.id.nextButton);

        text.setText(question.question);

        List<String> options = question.options;

        // создаём варианты ответов
        for (int i = 0; i < options.size(); i++) {
            RadioButton rb = new RadioButton(getContext());
            rb.setText(options.get(i));
            rb.setTextAppearance(R.style.TestOptionRadio);
            rb.setId(i);
            group.addView(rb);
        }

        // кнопка далее
        next.setOnClickListener(v -> {

            int selected = group.getCheckedRadioButtonId();

            if (selected == -1) {
                Toast.makeText(getContext(), "Выберите ответ", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean isCorrect = (selected == question.correct);

            if (callback != null) {
                callback.onAnswer(isCorrect);
            } else {
                Log.e("TEST", "Callback is null!");
            }
        });
    }
}