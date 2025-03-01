package com.example.rlapp.apimodel.newreportfacescan;

import com.google.gson.annotations.SerializedName;

public class Report {
    public Object identifier;
    public Object ppm;
    public Object bmi;
    public Double snr;
    public Double msi;
    public Double systolic;
    public Double diastolic;
    public Object age;
    public Object breathing;
    public Double healthScore;
    public Double waistToHeight;
    public Double heartRateVariability;
    public Object cardiacWorkload;
    public Double absi;
    public Double cvdRisk;
    public Double strokeRisk;
    public Double heartAttackRisk;
    public Object hypertensionRisk;
    public Object hypertriglyceridemiaRisk;
    public Object hypercholesterolemiaRisk;
    public Object diabetesRisk;
    public int irregularHeartBeats;
    public Object measurementId;
    public Object avgStarRating;
    @SerializedName("BP_DIASTOLIC")
    public Double bpDiastolic;
    @SerializedName("BP_CVD")
    public Double bpCvd;
    @SerializedName("IHB_COUNT")
    public int ihbCount;
    @SerializedName("HEALTH_SCORE")
    public Double healthScore2; // Renamed to avoid duplicate
    @SerializedName("PHYSIO_SCORE")
    public Object physioScore;
    @SerializedName("MENTAL_SCORE")
    public int mentalScore;
    @SerializedName("BP_STROKE")
    public Double bpStroke;
    @SerializedName("BMI_CALC")
    public Double bmiCalc;
    @SerializedName("BP_TAU")
    public Double bpTau;
    @SerializedName("BP_SYSTOLIC")
    public Double bpSystolic;
    @SerializedName("HEIGHT")
    public Object height;
    @SerializedName("MSI")
    public Double msi2; // Renamed to avoid duplicate
    @SerializedName("HPT_RISK_PROB")
    public Double hptRiskProb;
    @SerializedName("WAIST_TO_HEIGHT")
    public Double waistToHeight2; // Renamed to avoid duplicate
    @SerializedName("SNR")
    public Double snr2; // Renamed to avoid duplicate
    @SerializedName("TG_RISK_PROB")
    public Double tgRiskProb;
    @SerializedName("RISKS_SCORE")
    public Object risksScore;
    @SerializedName("HDLTC_RISK_PROB")
    public Double hdtlcRiskProb;
    @SerializedName("VITAL_SCORE")
    public Double vitalScore;
    @SerializedName("BP_RPP")
    public Double bpRpp;
    @SerializedName("AGE")
    public Object age2; // Renamed to avoid duplicate
    @SerializedName("HRV_SDNN")
    public Double hrvSdnn;
    @SerializedName("WEIGHT")
    public Object weight;
    @SerializedName("BP_HEART_ATTACK")
    public Double bpHeartAttack;
    @SerializedName("PHYSICAL_SCORE")
    public Double physicalScore;
    @SerializedName("HR_BPM")
    public Double hrBpm;
    @SerializedName("GENDER")
    public Object gender;
    @SerializedName("BR_BPM")
    public int brBpm;
    @SerializedName("WAIST_CIRCUM")
    public Object waistCircum;
    @SerializedName("DBT_RISK_PROB")
    public Double dbtRiskProb;
    @SerializedName("ABSI")
    public Double absi2; // Renamed to avoid duplicate
    public Object sessionId;
    @SerializedName("SESSION_ID")
    public Object sessionId2; // Renamed to avoid duplicate
    @SerializedName("MEASUREMENT_ID")
    public String measurementId2; // Renamed to avoid duplicate
}
