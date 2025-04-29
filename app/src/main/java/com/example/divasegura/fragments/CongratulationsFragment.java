package com.example.divasegura.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.content.Intent;

import com.example.divasegura.R;
import com.example.divasegura.activities.MainActivity;
import com.example.divasegura.utils.AppPreferences;

public class CongratulationsFragment extends Fragment {
    private Button btnFinish;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_congratulations, container, false);

        btnFinish = view.findViewById(R.id.btnFinish);

        btnFinish.setOnClickListener(v -> {
            // Save all collected data and proceed to main activity
            AppPreferences.setFirstRun(this.getContext(), false);
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            getActivity().finish();
        });

        return view;
    }
}