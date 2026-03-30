package com.example.javalearnlab.Topic5;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.javalearnlab.R;
import com.example.javalearnlab.Suport.OnAnswerCheckedListener;

public class Test2 extends Fragment {
    private OnAnswerCheckedListener listener;
    private EditText answer1, answer2;
    private boolean isAnswered = false;
    private boolean isCorrect = false;

    private static final String CORRECT_ANSWER_1 = "boolean";
    private static final String CORRECT_ANSWER_2 = "double";

    @Override
    public void onAttach(@NonNull android.content.Context context) {
        super.onAttach(context);
        if (context instanceof OnAnswerCheckedListener) {
            listener = (OnAnswerCheckedListener) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test_type_three, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        TextView layout = view.findViewById(R.id.layout_test_type_three);
        layout.setText("Объявите переменные для проверки состояния сигнала и массы звезды:");

        TextView askLayout = view.findViewById(R.id.text_job);
        askLayout.setText("________ hasSignal = true;\n\n" +
                "________ massOfStar = 1.989;\n\n" +
                "*(Вставьте подходящие типы данных для логического значения и числа с плавающей точкой)*");

        answer1 = view.findViewById(R.id.answer_1);
        answer2 = view.findViewById(R.id.answer_2);

        // Set hints for answers
        answer1.setHint("тип для логического значения");
        answer2.setHint("тип для числа с плавающей точкой");

        if (getArguments() != null) {
            String savedAnswer1 = getArguments().getString("answer1", "");
            String savedAnswer2 = getArguments().getString("answer2", "");
            answer1.setText(savedAnswer1);
            answer2.setText(savedAnswer2);
        }
    }

    public boolean checkAnswer() {
        String userAnswer1 = answer1.getText().toString().trim();
        String userAnswer2 = answer2.getText().toString().trim();

        if (userAnswer1.isEmpty() || userAnswer2.isEmpty()) {
            android.widget.Toast.makeText(getContext(),
                    "Заполните все поля перед продолжением", android.widget.Toast.LENGTH_SHORT).show();
            return false;
        }

        isAnswered = true;
        isCorrect = userAnswer1.equalsIgnoreCase(CORRECT_ANSWER_1) &&
                userAnswer2.equalsIgnoreCase(CORRECT_ANSWER_2);

        if (isCorrect) {
            answer1.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
            answer2.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
            answer1.setEnabled(false);
            answer2.setEnabled(false);
        } else {
            answer1.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
            answer2.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
            android.widget.Toast.makeText(getContext(),
                    "Правильно: boolean и double", android.widget.Toast.LENGTH_LONG).show();
        }

        if (listener != null) {
            listener.onAnswerChecked(getAdapterPosition(), isCorrect, isAnswered);
        }

        return isCorrect;
    }

    public boolean isAnswered() {
        return isAnswered;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void saveAnswers(Bundle outState) {
        if (answer1 != null && answer2 != null) {
            outState.putString("answer1", answer1.getText().toString());
            outState.putString("answer2", answer2.getText().toString());
        }
    }

    private int getAdapterPosition() {
        return getArguments() != null ? getArguments().getInt("position", 0) : 0;
    }
}