package com.example.javalearnlab.Topic4;

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

    private static final String CORRECT_ANSWER_1 = "final";
    private static final String CORRECT_ANSWER_2 = "MAX_SPEED";

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
        layout.setText("Создайте константу для хранения максимальной скорости:");

        TextView askLayout = view.findViewById(R.id.text_job);
        askLayout.setText("________ int MAX_SPEED = 300;\n\n" +
                "System.out.println(________);\n\n" +
                "*(Вставьте ключевое слово для константы и её имя для вывода)*");

        answer1 = view.findViewById(R.id.answer_1);
        answer2 = view.findViewById(R.id.answer_2);

        // Set hint for answers
        answer1.setHint("ключевое слово");
        answer2.setHint("имя константы");

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
                    "Правильно: final и MAX_SPEED", android.widget.Toast.LENGTH_LONG).show();
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