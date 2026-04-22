package com.example.javalearnlab.ui.profile;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.javalearnlab.R;
import com.example.javalearnlab.data.TheoryRepository;
import com.example.javalearnlab.theory.model.Topic;
import com.example.javalearnlab.utils.ProgressManager;

import java.util.List;

public class ProfileFragment extends Fragment {
    public ProfileFragment() { super(R.layout.fragment_main_profile); }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextView progressText = view.findViewById(R.id.progressText);

        List<Topic> topics = TheoryRepository.loadTopics(requireContext());

        int done = ProgressManager.getCompletedCount(requireContext(), topics);

        progressText.setText("Пройдено: " + done + " / " + topics.size());

        Button resetBtn = view.findViewById(R.id.resetButton);

        resetBtn.setOnClickListener(v -> {
            ProgressManager.resetAll(requireContext());
            Toast.makeText(getContext(), "Прогресс сброшен", Toast.LENGTH_SHORT).show();
        });
    }
}