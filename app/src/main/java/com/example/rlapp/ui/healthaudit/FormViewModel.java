package com.example.rlapp.ui.healthaudit;

import androidx.lifecycle.ViewModel;

import java.util.HashMap;
import java.util.Map;

public class FormViewModel extends ViewModel {
    private final Map<Integer, FormData> formDataMap = new HashMap<>();

    public void saveFormData(int page, FormData data) {
        formDataMap.put(page, data);
    }

    public FormData getFormData(int page) {
        return formDataMap.get(page);
    }

    public Map<Integer, FormData> getAllFormData() {
        return formDataMap;
    }
}

