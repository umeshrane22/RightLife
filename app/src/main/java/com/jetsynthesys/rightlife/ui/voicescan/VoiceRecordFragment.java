package com.jetsynthesys.rightlife.ui.voicescan;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jetsynthesys.rightlife.R;
import com.jetsynthesys.rightlife.ui.healthaudit.HealthAuditFormActivity;
import com.jetsynthesys.rightlife.ui.healthaudit.questionlist.Question;
import com.jetsynthesys.rightlife.ui.utility.DateTimeUtils;

import java.util.ArrayList;

public class VoiceRecordFragment extends Fragment {

    private static final String ARG_PAGE_INDEX = "page_index";
    private int pageIndex;
    private Question question;
    private OnNextVoiceScanFragmentClickListener onNextVoiceScanFragmentClickListener;
    private int selectedPosition = 0;

    private ArrayList<String> voiceScanTopic = new ArrayList<>();
    TextView tvGetDifferentTopics;
    static TextView tvSelectedTopic;
    private RadioButton radioButton;

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

        voiceScanTopic.add("How do you feel today?");
        voiceScanTopic.add("Can you describe your perfect day?");
        voiceScanTopic.add("What inspires you?");
        voiceScanTopic.add("Describe the things that make you happy.");
        voiceScanTopic.add("What advice would you give to someone going through a hard time?");
        voiceScanTopic.add("What makes you feel fulfilled?");
        voiceScanTopic.add("What things are you avoiding dealing with?");
        voiceScanTopic.add("What is your favorite way to spend the day?");
        voiceScanTopic.add("Describe the things that make you smile.");
        voiceScanTopic.add("What couldn’t you imagine living without?");
        voiceScanTopic.add("What do you love about life?");
        voiceScanTopic.add("What can you learn from your biggest mistakes?");
        voiceScanTopic.add("When do you feel most energized?");
        voiceScanTopic.add("How has your week been so far?");
        voiceScanTopic.add("How is your work or school going, and how do you think it will go in the future?");
        voiceScanTopic.add("What would you say are some of your best qualities?");
        voiceScanTopic.add("What are some things that usually put you in a good mood?");
        voiceScanTopic.add("Describe how you’ve been feeling during the past week.");
        voiceScanTopic.add("What are you looking forward to this week?");

        tvGetDifferentTopics = view.findViewById(R.id.tv_get_different_topic);
        tvSelectedTopic = view.findViewById(R.id.txt_describe_day);

        TextView tvDateTime = view.findViewById(R.id.txt_title_date);
        tvDateTime.setText(DateTimeUtils.getDateTime());

        tvSelectedTopic.setText(voiceScanTopic.get(selectedPosition));
        tvGetDifferentTopics.setOnClickListener(view1 -> {
            if (selectedPosition < voiceScanTopic.size() - 1) {
                selectedPosition = selectedPosition + 1;
            } else {
                selectedPosition = 0;
            }
            tvSelectedTopic.setText(voiceScanTopic.get(selectedPosition));
        });

        radioButton = view.findViewById(R.id.radio_use_my_own_topic);

        radioButton.setOnClickListener(view12 -> {
            tvGetDifferentTopics.setEnabled(radioButton.isSelected());
            if (!radioButton.isSelected()) {
                radioButton.setChecked(true);
                radioButton.setSelected(true);
                tvSelectedTopic.setText("My own topic");
            } else {
                radioButton.setChecked(false);
                radioButton.setSelected(false);
                tvSelectedTopic.setText(voiceScanTopic.get(selectedPosition));
            }
        });


        return view;
    }

    public static String getDifferentTopic(){
        return tvSelectedTopic.getText().toString();
    }
}
