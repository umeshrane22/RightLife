package com.example.rlapp.ui.voicescan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.rlapp.R;

import java.util.Objects;

public class FeelingListFragment extends Fragment {

    private static final String ARG_PAGE_INDEX = "page_index";
    private int pageIndex;

    public static FeelingListFragment newInstance(int pageIndex) {
        FeelingListFragment fragment = new FeelingListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE_INDEX, pageIndex);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pageIndex = getArguments().getInt(ARG_PAGE_INDEX, -1);
        }

        //formViewModel = new ViewModelProvider(requireActivity()).get(FormViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_feelings_list, container, false);

        //formViewModel = new ViewModelProvider(requireActivity()).get(FormViewModel.class);
/*
        ImageView button1 = view.findViewById(R.id.calendarButton);
        dateText = view.findViewById(R.id.dateText);*/

        // Set up listeners and interactions for views specific to this page
        //button1.setOnClickListener(v -> handleButtonClick());
        RelativeLayout rl_main = view.findViewById(R.id.rl_main);

        /*rl_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getActivity() instanceof VoiceScanFromActivity) {
                    ((VoiceScanFromActivity) getActivity()).navigateToNextPage();
                }
            }
        });*/

        RelativeLayout rl_main_feeling_sad = view.findViewById(R.id.rl_main_feeling_sad);
        rl_main_feeling_sad.setOnClickListener(view1 -> ((VoiceScanFromActivity) requireActivity()).navigateToNextPage("Sad"));

        RelativeLayout rl_main_feeling_stressed = view.findViewById(R.id.rl_main_feeling_stressed);
        rl_main_feeling_stressed.setOnClickListener(view1 -> ((VoiceScanFromActivity) requireActivity()).navigateToNextPage("Stressed"));

        RelativeLayout rl_main_feeling_unsure = view.findViewById(R.id.rl_main_feeling_unsure);
        rl_main_feeling_unsure.setOnClickListener(view1 -> ((VoiceScanFromActivity) requireActivity()).navigateToNextPage("Unsure"));

        RelativeLayout rl_main_feeling_relaxed = view.findViewById(R.id.rl_main_feeling_relaxed);
        rl_main_feeling_relaxed.setOnClickListener(view1 -> ((VoiceScanFromActivity) requireActivity()).navigateToNextPage("Relaxed"));

        RelativeLayout rl_main_feeling_happy = view.findViewById(R.id.rl_main_feeling_happy);
        rl_main_feeling_happy.setOnClickListener(view1 -> ((VoiceScanFromActivity) requireActivity()).navigateToNextPage("Happy"));

        return view;
    }
}
