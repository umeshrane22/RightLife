package com.jetsynthesys.rightlife.ui.healthaudit;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.jetsynthesys.rightlife.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class HAFormDOBFragment extends Fragment {

    TextView dateText;

    private FormViewModel formViewModel;
    private static final String ARG_PAGE_INDEX = "page_index";
    private int pageIndex;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.page_health_dob, container, false);

        formViewModel = new ViewModelProvider(requireActivity()).get(FormViewModel.class);

        ImageView button1 = view.findViewById(R.id.calendarButton);
        dateText = view.findViewById(R.id.dateText);

        // Set up listeners and interactions for views specific to this page
        button1.setOnClickListener(v -> handleButtonClick());


        return view;
    }

    private void handleButtonClick() {
        // Handle button click logic for Page 1
        Toast.makeText(getContext(), "Button on Page 1 clicked", Toast.LENGTH_SHORT).show();
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
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                        dateText.setText(selectedDate);
                        saveData();
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    // Method to collect data from this fragment
    public Map<String, ArrayList> collectData() {
        ArrayList datalist =  new ArrayList();
        Map<String, ArrayList> data = new HashMap<>();
        // Add values from views in this fragment to the data map
        datalist.add(dateText.getText().toString());
        data.put("fruit",datalist);
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



