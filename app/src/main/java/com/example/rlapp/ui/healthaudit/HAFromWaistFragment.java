package com.example.rlapp.ui.healthaudit;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.rlapp.R;
import com.example.rlapp.ui.healthaudit.questionlist.Question;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class HAFromWaistFragment extends Fragment {

    private TextView dateText, edtSpinnerWaist;

    private static final String ARG_PAGE_INDEX = "page_index";
    private int pageIndex;

    private EditText edtWaist;
    private Button btnOK;
    private OnNextFragmentClickListener onNextFragmentClickListener;
    private Question question;
    private DecimalFormat decimalFormat = new DecimalFormat("###.##");

    public static HAFromWaistFragment newInstance(int pageIndex, Question question) {
        HAFromWaistFragment fragment = new HAFromWaistFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE_INDEX, pageIndex);
        args.putSerializable(HealthAuditFormActivity.ARG_QUESTION, question);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pageIndex = getArguments().getInt(ARG_PAGE_INDEX, -1);
            question = (Question) getArguments().getSerializable(HealthAuditFormActivity.ARG_QUESTION);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        onNextFragmentClickListener = (OnNextFragmentClickListener) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_health_waist, container, false);

        dateText = view.findViewById(R.id.dateText);

        getViews(view);
        return view;
    }

    private void getViews(View view) {
        edtWaist = view.findViewById(R.id.edt_waist);
        edtSpinnerWaist = view.findViewById(R.id.edt_spinner_waist);
        btnOK = view.findViewById(R.id.btn_ok);

        TextView txtQuestionText = view.findViewById(R.id.dobPrompt);
        txtQuestionText.setText(question.getQuestionTxt());

        edtSpinnerWaist.setOnClickListener(view1 -> {
            openPopupForWaist();
        });

        btnOK.setOnClickListener(view12 -> {
            if (edtWaist.getText().toString().isEmpty()) {
                Toast.makeText(getContext(), requireActivity().getString(R.string.waist_error), Toast.LENGTH_SHORT).show();
            } else {
                ArrayList<String> data = new ArrayList<>();
                data.add(edtWaist.getText().toString());
                onNextFragmentClickListener.getDataFromFragment(question.getQuestion(), data);
                ((HealthAuditFormActivity) requireActivity()).navigateToNextPage();
            }
        });
    }

    private void openPopupForWaist() {
        PopupMenu popupMenu = new PopupMenu(getActivity(), edtSpinnerWaist);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu_waist, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(menuItem -> {
            if (!edtSpinnerWaist.getText().toString().equals(menuItem.toString())) {
                String waist;
                if (requireActivity().getString(R.string.str_inches).equals(menuItem.toString())) {
                    waist = convertInchesToCentimeter(edtWaist.getText().toString());
                } else {
                    waist = convertCentimeterToInches(edtWaist.getText().toString());
                }
                edtWaist.setText(waist);
            }
            edtSpinnerWaist.setText(menuItem.toString());
            return true;
        });
        popupMenu.show();
    }

    private String convertInchesToCentimeter(String inch) {
        try {
            double in = Double.parseDouble(inch);
            double centimeter = in / 2.54;
            return decimalFormat.format(centimeter);
        } catch (Exception e) {
            return "";
        }
    }

    private String convertCentimeterToInches(String centimeter) {
        try {
            double cms = Double.parseDouble(centimeter);
            double inch = cms * 2.54;
            return decimalFormat.format(inch);
        } catch (Exception e) {
            return "";
        }
    }
}



