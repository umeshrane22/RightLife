package com.example.rlapp.ui.voicescan;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.rlapp.R;

public class VoiceRecordFragment extends Fragment {

    private static final String ARG_PAGE_INDEX = "page_index";
    private int pageIndex;

    public static VoiceRecordFragment newInstance(int pageIndex) {
        VoiceRecordFragment fragment = new VoiceRecordFragment();
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
        View view = inflater.inflate(R.layout.page_feelings_record, container, false);

        //formViewModel = new ViewModelProvider(requireActivity()).get(FormViewModel.class);
/*
        ImageView button1 = view.findViewById(R.id.calendarButton);
        dateText = view.findViewById(R.id.dateText);*/

        // Set up listeners and interactions for views specific to this page
        //button1.setOnClickListener(v -> handleButtonClick());


        return view;
    }
}
