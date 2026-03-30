package com.example.javalearnlab.Topic1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import com.example.javalearnlab.GeneratorTest.FragmentClassAdapter;
import com.example.javalearnlab.R;
import com.example.javalearnlab.Suport.OnAnswerCheckedListener;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import java.util.ArrayList;
import java.util.List;

public class TestsMain extends AppCompatActivity implements OnAnswerCheckedListener {

    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private ImageButton exitButton;
    private Button btnPrevious, btnNext;
    private FragmentClassAdapter adapter;
    private List<Class<? extends Fragment>> fragmentClasses;

    private boolean[] answersStatus;
    private boolean[] answeredStatus;

    private int totalQuestions;
    private int correctAnswers = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tests);

        initViews();
        prepareFragmentList();
        setupViewPager();
        setupTabLayout();
        setupNavigationButtons();
        setupPageChangeListener();

        exitButton.setOnClickListener(v -> finish());
    }

    private void initViews() {
        viewPager = findViewById(R.id.pager_test);
        tabLayout = findViewById(R.id.tabLayout);
        exitButton = findViewById(R.id.button_exit_menu);
        btnPrevious = findViewById(R.id.btn_previous);
        btnNext = findViewById(R.id.btn_next);
    }

    private void prepareFragmentList() {
        fragmentClasses = new ArrayList<>();
        fragmentClasses.add(Test1.class);
        fragmentClasses.add(Test2.class);

        totalQuestions = fragmentClasses.size();
        answersStatus = new boolean[totalQuestions];
        answeredStatus = new boolean[totalQuestions];

        for (int i = 0; i < totalQuestions; i++) {
            answersStatus[i] = false;
            answeredStatus[i] = false;
        }
    }

    private void setupViewPager() {
        adapter = new FragmentClassAdapter(this, fragmentClasses);
        viewPager.setAdapter(adapter);
        viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        viewPager.setOffscreenPageLimit(fragmentClasses.size());
        viewPager.setUserInputEnabled(false);
    }

    private void setupTabLayout() {
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    tab.setText(String.valueOf(position + 1));
                }
        ).attach();
    }

    private void setupNavigationButtons() {
        btnPrevious.setOnClickListener(v -> {
            int currentItem = viewPager.getCurrentItem();
            if (currentItem > 0) {
                viewPager.setCurrentItem(currentItem - 1, true);
            }
        });

        btnNext.setOnClickListener(v -> {
            int currentItem = viewPager.getCurrentItem();

            Fragment currentFragment = getCurrentFragment();

            if (currentFragment != null) {
                if (!answeredStatus[currentItem]) {
                    checkCurrentQuestionAnswer(currentFragment);
                }
            }

            if (currentItem == totalQuestions - 1) {
                showTestResult();
            } else {
                viewPager.setCurrentItem(currentItem + 1, true);
            }
        });

        updateButtonsState(0);
    }

    private void checkCurrentQuestionAnswer(Fragment fragment) {
        if (fragment instanceof Test1) {
            ((Test1) fragment).checkAnswer();
        } else if (fragment instanceof Test2) {
            ((Test2) fragment).checkAnswer();
        }
    }

    private Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentByTag("f" + viewPager.getCurrentItem());
    }

    private void updateButtonsState(int currentPosition) {
        btnPrevious.setEnabled(currentPosition > 0);

        if (currentPosition == totalQuestions - 1) {
            btnNext.setText("Завершить ✓");
        } else {
            btnNext.setText("Следующий ▶");
        }
    }

    private void setupPageChangeListener() {
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updateButtonsState(position);
            }
        });
    }

    @Override
    public void onAnswerChecked(int questionIndex, boolean isCorrect, boolean isAnswered) {
        answersStatus[questionIndex] = isCorrect;
        answeredStatus[questionIndex] = isAnswered;

        correctAnswers = 0;
        for (int i = 0; i < totalQuestions; i++) {
            if (answeredStatus[i] && answersStatus[i]) {
                correctAnswers++;
            }
        }
    }

    private void showTestResult() {
        // Проверяем, все ли вопросы отвечены
        boolean allAnswered = true;
        for (int i = 0; i < totalQuestions; i++) {
            if (!answeredStatus[i]) {
                allAnswered = false;
                break;
            }
        }

        if (!allAnswered) {
            Toast.makeText(this, "Ответьте на все вопросы перед завершением",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // Формируем сообщение с результатом
        String title;
        String message;
        int scorePercent = (correctAnswers * 100) / totalQuestions;

        if (correctAnswers == totalQuestions) {
            title = "Отлично! 🎉";
            message = "Вы правильно ответили на все вопросы!\n\n" +
                    "Результат: " + correctAnswers + " из " + totalQuestions + " (" + scorePercent + "%)\n\n" +
                    "Отличное знание материала!";
        } else if (correctAnswers >= totalQuestions / 2) {
            title = "Хороший результат! 👍";
            message = "Вы правильно ответили на " + correctAnswers + " вопрос из " + totalQuestions + "\n\n" +
                    "Результат: " + scorePercent + "%\n\n" +
                    "Неправильные ответы: " + (totalQuestions - correctAnswers) + "\n\n" +
                    "Повторите материал по вопросам, где были ошибки.";
        } else {
            title = "Нужно повторить материал 📚";
            message = "Вы правильно ответили только на " + correctAnswers + " вопрос из " + totalQuestions + "\n\n" +
                    "Результат: " + scorePercent + "%\n\n" +
                    "Рекомендуем вернуться к теории и пройти тест заново.";
        }

        // Создаем и показываем диалоговое окно
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                    // Возвращаемся к теории
                    finish(); // Закрываем TestsMain и возвращаемся к Theory
                })
                .setCancelable(false) // Запрещаем закрытие окна нажатием вне его
                .show();
    }
}