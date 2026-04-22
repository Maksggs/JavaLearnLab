package com.example.javalearnlab.ui.topics;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.javalearnlab.R;
import com.example.javalearnlab.data.TheoryRepository;
import com.example.javalearnlab.theory.TheoryActivity;
import com.example.javalearnlab.theory.model.Topic;
import com.example.javalearnlab.utils.AuthManager;
import com.example.javalearnlab.utils.ProgressManager;

import java.util.List;

public class TopicsFragment extends Fragment {

    private LinearLayout container;

    public TopicsFragment() {
        super(R.layout.fragment_main_choice_topics);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        container = view.findViewById(R.id.topicsContainer);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadTopics();
    }

    private void loadTopics() {
        container.removeAllViews();

        if (!AuthManager.isLogged(requireContext())) {
            TextView message = new TextView(requireContext());
            message.setText("Войдите в аккаунт, чтобы открыть темы");
            message.setTextColor(getResources().getColor(R.color.black));
            message.setTextSize(18);
            message.setGravity(Gravity.CENTER);
            container.addView(message);
            return;
        }

        List<Topic> topics = TheoryRepository.loadTopics(requireContext());

        for (int i = 0; i < topics.size(); i++) {
            Topic topic = topics.get(i);
            boolean unlocked = ProgressManager.isUnlocked(requireContext(), topics, i);

            int style = unlocked ? R.style.item_manu_topics_button : R.style.item_manu_topics_button_locked;
            Button btn = new Button(requireContext(), null, 0, style);
            btn.setText(topic.title);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 16, 0, 0);
            btn.setLayoutParams(params);

            if (!unlocked) {
                btn.setEnabled(false);
                btn.setAlpha(0.7f);
                btn.setText(topic.title + " (закрыто)");
            } else {
                btn.setOnClickListener(v -> {
                    Intent intent = new Intent(getActivity(), TheoryActivity.class);
                    intent.putExtra("topic_id", topic.id);
                    startActivity(intent);
                });
            }

            container.addView(btn);
        }
    }
}