package com.example.javalearnlab.ui.test;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.javalearnlab.theory.model.Question;

import java.util.List;

public class TestPagerAdapter extends FragmentStateAdapter {

    public interface AnswerCallback {
        void onAnswer(boolean correct);
    }

    private List<Question> questions;
    private AnswerCallback callback;

    public TestPagerAdapter(@NonNull FragmentActivity fa,
                            List<Question> questions,
                            AnswerCallback callback) {
        super(fa);
        this.questions = questions;
        this.callback = callback;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        Question q = questions.get(position);

        if ("single".equals(q.type)) {
            return TestSingleFragment.newInstance(q, callback);
        } else {
            return TestInputFragment.newInstance(q, callback);
        }
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }
}