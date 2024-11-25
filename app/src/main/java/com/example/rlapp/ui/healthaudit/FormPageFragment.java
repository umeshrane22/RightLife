package com.example.rlapp.ui.healthaudit;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.rlapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FormPageFragment extends Fragment {

    TextView dateText;

    private FormViewModel formViewModel;
    private static final String ARG_PAGE_INDEX = "page_index";
    private int pageIndex;
    private OnNextFragmentClickListener onNextFragmentClickListener;
    private Button btnOK;

    public static FormPageFragment newInstance(int pageIndex) {
        FormPageFragment fragment = new FormPageFragment();
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

        formViewModel = new ViewModelProvider(requireActivity()).get(FormViewModel.class);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        onNextFragmentClickListener = (OnNextFragmentClickListener) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_health_dob, container, false);

        formViewModel = new ViewModelProvider(requireActivity()).get(FormViewModel.class);

        ImageView button1 = view.findViewById(R.id.calendarButton);
        dateText = view.findViewById(R.id.dateText);

        // Set up listeners and interactions for views specific to this page
        button1.setOnClickListener(v -> handleButtonClick());
        btnOK = view.findViewById(R.id.btn_ok);

        btnOK.setOnClickListener(view1 -> {
            int age = getAge(dateText.getText().toString());
            if (age >= 15){
                ((HealthAuditFormActivity) requireActivity()).navigateToNextPage();
            }else {
                Toast.makeText(getContext(), requireActivity().getString(R.string.dob_error), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void handleButtonClick() {
        // Handle button click logic for Page 1
        showDatePicker();
    }

    private void showDatePicker() {
        if (getContext() == null) {
            return; // Exit if context is null
        }
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                (view, year1, month1, dayOfMonth) -> {
                    String selectedDate = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
                    dateText.setText(selectedDate);
                    ArrayList<String> data = new ArrayList<>();
                    data.add(selectedDate);
                    onNextFragmentClickListener.getDataFromFragment("dob", data);
                    //saveData();
                }, year-15, month, day);
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(year-15, month, day);
        datePickerDialog.getDatePicker().setMaxDate(calendar1.getTimeInMillis());
        datePickerDialog.show();
    }

    private int getAge(String dobString){

        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            date = sdf.parse(dobString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if(date == null) return 0;

        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.setTime(date);

        int year = dob.get(Calendar.YEAR);
        int month = dob.get(Calendar.MONTH);
        int day = dob.get(Calendar.DAY_OF_MONTH);

        dob.set(year, month+1, day);


        return today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
    }

    // Method to collect data from this fragment
    public Map<String, ArrayList> collectData() {
        ArrayList datalist = new ArrayList();
        Map<String, ArrayList> data = new HashMap<>();
        // Add values from views in this fragment to the data map
        datalist.add(dateText.getText().toString());
        data.put("fruit", datalist);
        return data;
    }

    private void saveData() {
        FormData data = new FormData();
        data.setAnswer("User answer");
        data.setSelected(true);

        // Save data in ViewModel with page identifier (e.g., 0 for first page)
        formViewModel.saveFormData(pageIndex, data);
    }
}



