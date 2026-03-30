package com.example.javalearnlab.Topic9;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.javalearnlab.R;
import com.example.javalearnlab.Suport.OnAnswerCheckedListener;

public class Test1 extends Fragment {
    private static final int CORRECT_ANSWER_ID = R.id.rb_answer_3;
    private OnAnswerCheckedListener listener;
    private RadioGroup radioGroup;
    private boolean isAnswered = false;
    private boolean isCorrect = false;
    private int selectedAnswerId = -1;

    public static Test1 newInstance() {
        return new Test1();
    }

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
        View view = inflater.inflate(R.layout.fragment_test_type_two, container, false);
        initView(view);
        setupRadioGroupListener();
        return view;
    }

    private void initView(View view) {
        TextView layout = view.findViewById(R.id.layout_test_type_two);
        layout.setText("Командор хочет получить данные из 2-й строки и 4-го столбца панели управления. Какое обращение к массиву panel будет верным (помните про индексацию с нуля)?");

        RadioButton rb1, rb2, rb3, rb4;
        rb1 = view.findViewById(R.id.rb_answer_1);
        rb2 = view.findViewById(R.id.rb_answer_2);
        rb3 = view.findViewById(R.id.rb_answer_3);
        rb4 = view.findViewById(R.id.rb_answer_4);

        rb1.setText("panel[2][4]");
        rb2.setText("panel[4][2]");
        rb3.setText("panel[1][3]");
        rb4.setText("panel[2][3]");

        radioGroup = view.findViewById(R.id.rg_answers);

        if (selectedAnswerId != -1) {
            radioGroup.check(selectedAnswerId);
        }
    }

    private void setupRadioGroupListener() {
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId != -1 && !isAnswered) {
                selectedAnswerId = checkedId;
                checkAnswer();
            }
        });
    }

    public boolean checkAnswer() {
        if (selectedAnswerId == -1) {
            return false;
        }

        isAnswered = true;
        isCorrect = (selectedAnswerId == CORRECT_ANSWER_ID);

        if (isCorrect) {
            RadioButton selectedButton = radioGroup.findViewById(selectedAnswerId);
            if (selectedButton != null) {
                selectedButton.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
            }
        } else {
            RadioButton selectedButton = radioGroup.findViewById(selectedAnswerId);
            if (selectedButton != null) {
                selectedButton.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
            }
            RadioButton correctButton = radioGroup.findViewById(CORRECT_ANSWER_ID);
            if (correctButton != null) {
                correctButton.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
            }
        }

        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            radioGroup.getChildAt(i).setEnabled(false);
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

    private int getAdapterPosition() {
        return getArguments() != null ? getArguments().getInt("position", 0) : 0;
    }
}