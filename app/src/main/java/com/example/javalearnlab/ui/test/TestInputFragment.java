package com.example.javalearnlab.ui.test;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.javalearnlab.R;
import com.example.javalearnlab.theory.model.Question;
import com.google.gson.Gson;


public class TestInputFragment extends Fragment {

    private Question question;
    private TestPagerAdapter.AnswerCallback callback;

    public TestInputFragment() {
        super(R.layout.fragment_test_input);
    }

    // 🔥 правильное создание Fragment
    public static TestInputFragment newInstance(Question q,
                                                TestPagerAdapter.AnswerCallback cb) {

        TestInputFragment fragment = new TestInputFragment();

        Bundle args = new Bundle();
        args.putString("question", new Gson().toJson(q));

        fragment.setArguments(args);
        fragment.setCallback(cb);

        return fragment;
    }

    // setter callback
    public void setCallback(TestPagerAdapter.AnswerCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 🔄 восстановление данных
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
        EditText input1 = view.findViewById(R.id.input1);
        EditText input2 = view.findViewById(R.id.input2);
        Button next = view.findViewById(R.id.nextButton);

        text.setText(question.question);

        next.setOnClickListener(v -> {

            if (question.answers == null || question.answers.size() < 2) {
                Log.e("TEST", "Answers are invalid!");
                return;
            }

            String a1 = input1.getText().toString().trim();
            String a2 = input2.getText().toString().trim();

            if (a1.isEmpty() || a2.isEmpty()) {
                Toast.makeText(getContext(), "Введите ответы", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean correct =
                    a1.equalsIgnoreCase(question.answers.get(0)) &&
                            a2.equalsIgnoreCase(question.answers.get(1));

            if (callback != null) {
                callback.onAnswer(correct);
            } else {
                Log.e("TEST", "Callback is null!");
            }
        });
    }
}
