package com.example.rlapp.apimodel.rlpagemodels.scanresultfacescan;

import java.util.List;

public class FaceScanPastResultResponse {
    private boolean success;
    private int statusCode;
    private List<Data> data;

    // Getters
    public boolean isSuccess() {
        return success;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public List<Data> getData() {
        return data;
    }

}
