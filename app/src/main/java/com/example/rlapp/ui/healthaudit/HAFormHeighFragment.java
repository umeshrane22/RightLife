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

public class HAFormHeighFragment extends Fragment {

    TextView dateText,edtSpinner;
    private TextView txtFt, txtInch;
    private EditText edtFt, edtInch, edtCms;
    private Button btnOK;

    private static final String ARG_PAGE_INDEX = "page_index";
    private int pageIndex;
    private OnNextFragmentClickListener onNextFragmentClickListener;
    private Question question;
    private DecimalFormat decimalFormatFtInch = new DecimalFormat("##.##");
    private DecimalFormat decimalFormatCm = new DecimalFormat("###.##");

    public static HAFormHeighFragment newInstance(int pageIndex, Question question) {
        HAFormHeighFragment fragment = new HAFormHeighFragment();
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
        View view = inflater.inflate(R.layout.page_health_height, container, false);

        getViews(view);

        return view;
    }

    private void getViews(View view) {
        txtFt = view.findViewById(R.id.txt_ft);
        txtInch = view.findViewById(R.id.txt_inch);

        edtCms = view.findViewById(R.id.edt_cms);
        edtFt = view.findViewById(R.id.edt_ft);
        edtInch = view.findViewById(R.id.edt_inch);
        edtSpinner = view.findViewById(R.id.edt_spinner);
        btnOK = view.findViewById(R.id.btn_ok);

        TextView txtQuestionText = view.findViewById(R.id.dobPrompt);
        txtQuestionText.setText(question.getQuestionTxt());

        edtSpinner.setOnClickListener(view1 -> {
            openPopUp();
        });

        btnOK.setOnClickListener(view12 -> {
            ArrayList<String> data = new ArrayList<>();
            if (requireActivity().getString(R.string.str_cms).equals(edtSpinner.getText().toString())) {
                if (edtCms.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), requireActivity().getString(R.string.height_error), Toast.LENGTH_SHORT).show();
                } else {
                    data.add(edtCms.getText().toString());
                    navigateNext(data);
                }
            } else {
                if (edtInch.getText().toString().isEmpty() && edtFt.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), requireActivity().getString(R.string.height_error), Toast.LENGTH_SHORT).show();
                } else {
                    data.add(convertFeetToCentimeter(edtFt.getText().toString() + "." + edtInch.getText().toString()));
                    navigateNext(data);
                }
            }
        });
    }

    private void navigateNext(ArrayList<String> data) {
        onNextFragmentClickListener.getDataFromFragment(question.getQuestion(), data);
        ((HealthAuditFormActivity) requireActivity()).navigateToNextPage();
    }

    private void openPopUp() {
        PopupMenu popupMenu = new PopupMenu(getActivity(), edtSpinner);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu_ft_inch_cms, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            if (!edtSpinner.getText().toString().equals(menuItem.toString())) {
                if (requireActivity().getString(R.string.str_cms).equals(menuItem.toString())) {
                    edtCms.setVisibility(View.VISIBLE);
                    edtInch.setVisibility(View.GONE);
                    edtFt.setVisibility(View.GONE);
                    txtInch.setVisibility(View.GONE);
                    txtFt.setVisibility(View.GONE);

                    String ftInch = edtFt.getText().toString() + "." + edtInch.getText().toString();
                    edtCms.setText(convertFeetToCentimeter(ftInch));


                } else {
                    edtCms.setVisibility(View.GONE);
                    edtInch.setVisibility(View.VISIBLE);
                    edtFt.setVisibility(View.VISIBLE);
                    txtInch.setVisibility(View.VISIBLE);
                    txtFt.setVisibility(View.VISIBLE);

                    String cms = convertCentimeterToFtInch(edtCms.getText().toString());
                    String[] strings = cms.split("\\.");
                    edtFt.setText(strings[0]);
                    if (strings.length > 1) {
                        edtInch.setText(strings[1]);
                    }

                }
            }
            edtSpinner.setText(menuItem.toString());
            return true;
        });

        popupMenu.show();
    }

    private String convertFeetToCentimeter(String ftInch) {
        try {
            double feetInch = Double.parseDouble(ftInch);
            double centimeter = feetInch * 30.48;
            return decimalFormatCm.format(centimeter);
        } catch (Exception e) {
            return "";
        }
    }

    private String convertCentimeterToFtInch(String centimeter) {
        try {
            double cms = Double.parseDouble(centimeter);
            double ftInch = cms / 30.48;
            return decimalFormatFtInch.format(ftInch);
        } catch (Exception e) {
            return "";
        }

    }
}



