package com.example.javalearnlab.Topic1;

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

public class Theory extends AppCompatActivity {
    //Код выполняемый, при запуске окна
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Запуск страницы с теорией
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theory);

        //Заголовок страницы
        TextView label = findViewById(R.id.text_topic_title);
        label.setText(R.string.label_topic1);

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
                Intent intent = new Intent(Theory.this, TestsMain.class);
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
                new TheorySection("topic1_theory_text1_1", null),
                new TheorySection( "topic1_theory_text1_2", "topic1_theory_code1"),
                new TheorySection("topic1_theory_text2_1", null),
                new TheorySection("topic1_theory_text2_2", "topic1_theory_code2")
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
