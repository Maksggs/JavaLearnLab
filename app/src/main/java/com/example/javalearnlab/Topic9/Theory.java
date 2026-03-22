package com.example.javalearnlab.Topic9;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.javalearnlab.R;
import com.example.javalearnlab.Suport.TheorySection;
import com.example.javalearnlab.Topic1.Test1;

public class Theory extends AppCompatActivity {
    //Код выполняемый, при запуске окна
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Запуск страницы с теорией
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theory);

        //Заголовок страницы
        TextView label = findViewById(R.id.text_topic_title);
        label.setText(R.string.label_topic9);

        //Кнопка выхода из теории
        ImageButton exitButton = findViewById(R.id.button_exit_menu);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Кнопка запуска теста
        Button startButtonTest = findViewById(R.id.button_start_test);
        startButtonTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startButtonTest.setTextAppearance(R.style.main_menu_button);
                Intent intent = new Intent(com.example.javalearnlab.Topic9.Theory.this, Test1.class);
                startActivity(intent);
            }
        });

        //Вызов метода для заполнения экрана информацией
        completionText();
    }

    //Метод для заполнения экрана информацией
    private void completionText(){
        //Контейнер в котором хранится весь тест
        LinearLayout linearLayout = findViewById(R.id.topic_theory_text);

        //Элементы с текстом
        TheorySection[] sections = {
                new TheorySection("topic9_theory_text1_1", null),
                new TheorySection("topic9_theory_text1_2", null),
                new TheorySection("topic9_theory_text1_3", "topic9_theory_code1"),
                new TheorySection("topic9_theory_text1_4", null),
                new TheorySection("topic9_theory_text1_5", "topic9_theory_code2"),
                new TheorySection("topic9_theory_text2_1", null),
                new TheorySection("topic9_theory_text2_2", null),
                new TheorySection("topic9_theory_text2_3", null),
                new TheorySection("topic9_theory_text2_4", null),
                new TheorySection("topic9_theory_text2_5", null),
                new TheorySection("topic9_theory_text2_6", null),
                new TheorySection("topic9_theory_text2_7", null),
                new TheorySection("topic9_theory_text2_8", null),
                new TheorySection("topic9_theory_text2_9", null),
                new TheorySection("topic9_theory_text3_1", "topic9_theory_code3"),
                new TheorySection("topic9_theory_text3_2", "topic9_theory_code4"),
                new TheorySection("topic9_theory_text3_3", "topic9_theory_code5"),
                new TheorySection("topic9_theory_text3_4", "topic9_theory_code6"),
                new TheorySection("topic9_theory_text3_5", "topic9_theory_code7"),
                new TheorySection("topic9_theory_text4_1", "topic9_theory_code8"),
                new TheorySection("topic9_theory_text4_2", "topic9_theory_code9"),
                new TheorySection("topic9_theory_text5_1", "topic9_theory_code10")
        };

        // Заполняем контейнер в цикле
        for (TheorySection section : sections) {
            if (section.textResId != null) {
                //Контейнер для текста
                TextView textView = new TextView(this);
                //Подключение стиля для текста
                textView.setTextAppearance(R.style.theory_text_text);
                // Получаем текстовые данные из ресурсов
                int resId = getResources().getIdentifier(section.textResId, "string", getPackageName());
                String text = getString(resId);
                //Запись текста в контейнер
                textView.setText(text);
                linearLayout.addView(textView);
            }
            if (section.codeResId != null) {
                //Контейнер для кода
                TextView codeView = new TextView(this);
                //Подключение стиля для кода
                codeView.setTextAppearance(R.style.theory_text_code);
                //Подключение рамки для кода
                codeView.setBackgroundResource(R.drawable.background_text_code);
                // Получаем текстовые данные из ресурсов
                int resId = getResources().getIdentifier(section.codeResId, "string", getPackageName());
                String text = getString(resId);
                //Запись текста в контейнер
                codeView.setText(text);
                linearLayout.addView(codeView);
            }
        }
    }
}