package com.example.rlapp.ui.voicescan;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.rlapp.R;
import com.example.rlapp.ui.healthaudit.HealthAuditFormActivity;
import com.example.rlapp.ui.healthaudit.questionlist.Question;

public class VoiceRecordFragment extends Fragment {

    private static final String ARG_PAGE_INDEX = "page_index";
    private int pageIndex;
    private Question question;
    private OnNextVoiceScanFragmentClickListener onNextVoiceScanFragmentClickListener;

    public static VoiceRecordFragment newInstance(int pageIndex, Question question) {
        VoiceRecordFragment fragment = new VoiceRecordFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE_INDEX, pageIndex);
        args.putSerializable(HealthAuditFormActivity.ARG_QUESTION, question);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        onNextVoiceScanFragmentClickListener = (OnNextVoiceScanFragmentClickListener) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pageIndex = getArguments().getInt(ARG_PAGE_INDEX, -1);
            question = (Question) getArguments().getSerializable(HealthAuditFormActivity.ARG_QUESTION);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_feelings_record, container, false);

        return view;
    }
}
