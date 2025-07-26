package com.jetsynthesys.rightlife.ui.healthcam;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ReportData implements Serializable {

    @SerializedName("identifier")
    @Expose
    private Object identifier;
    @SerializedName("ppm")
    @Expose
    private Object ppm;
    @SerializedName("bmi")
    @Expose
    private Object bmi;
    @SerializedName("snr")
    @Expose
    private Double snr;
    @SerializedName("MSI")
    @Expose
    private Double msi;
    @SerializedName("systolic")
    @Expose
    private Double systolic;
    @SerializedName("diastolic")
    @Expose
    private Double diastolic;
    @SerializedName("age")
    @Expose
    private Object age;
    @SerializedName("breathing")
    @Expose
    private Object breathing;
    @SerializedName("HEALTH_SCORE")
    @Expose
    private Double healthScore;
    @SerializedName("waistToHeight")
    @Expose
    private Double waistToHeight;
    @SerializedName("heartRateVariability")
    @Expose
    private Double heartRateVariability;
    @SerializedName("cardiacWorkload")
    @Expose
    private Object cardiacWorkload;
    @SerializedName("absi")
    @Expose
    private Double absi;
    @SerializedName("cvdRisk")
    @Expose
    private Double cvdRisk;
    @SerializedName("strokeRisk")
    @Expose
    private Double strokeRisk;
    @SerializedName("heartAttackRisk")
    @Expose
    private Double heartAttackRisk;
    @SerializedName("HypertensionRisk")
    @Expose
    private Object hypertensionRisk;
    @SerializedName("HypertriglyceridemiaRisk")
    @Expose
    private Object hypertriglyceridemiaRisk;
    @SerializedName("HypercholesterolemiaRisk")
    @Expose
    private Object hypercholesterolemiaRisk;
    @SerializedName("DiabetesRisk")
    @Expose
    private Object diabetesRisk;
    @SerializedName("irregularHeartBeats")
    @Expose
    private Double irregularHeartBeats;
    @SerializedName("measurementId")
    @Expose
    private Object measurementId;
    @SerializedName("avgStarRating")
    @Expose
    private Object avgStarRating;
    @SerializedName("BP_DIASTOLIC")
    @Expose
    private Double bpDiastolic;
    @SerializedName("BP_CVD")
    @Expose
    private Double bpCvd;
    @SerializedName("IHB_COUNT")
    @Expose
    private Double ihbCount;
    @SerializedName("HEALTH_SCORE1")
    @Expose
    private Double healthScore1;
    @SerializedName("PHYSIO_SCORE")
    @Expose
    private Object physioScore;
    @SerializedName("MENTAL_SCORE")
    @Expose
    private Double mentalScore;
    @SerializedName("BP_STROKE")
    @Expose
    private Double bpStroke;
    @SerializedName("BMI_CALC")
    @Expose
    private Double bmiCalc;
    @SerializedName("BP_TAU")
    @Expose
    private Double bpTau;
    @SerializedName("BP_SYSTOLIC")
    @Expose
    private Double bpSystolic;
    @SerializedName("HEIGHT")
    @Expose
    private Object height;
    @SerializedName("MSI1")
    @Expose
    private Double msi1;
    @SerializedName("HPT_RISK_PROB")
    @Expose
    private Double hptRiskProb;
    @SerializedName("WAIST_TO_HEIGHT1")
    @Expose
    private Double waistToHeight1;
    @SerializedName("SNR")
    @Expose
    private Double snr1;
    @SerializedName("TG_RISK_PROB")
    @Expose
    private Double tgRiskProb;
    @SerializedName("RISKS_SCORE")
    @Expose
    private Object risksScore;
    @SerializedName("HDLTC_RISK_PROB")
    @Expose
    private Double hdltcRiskProb;
    @SerializedName("VITAL_SCORE")
    @Expose
    private Double vitalScore;
    @SerializedName("BP_RPP")
    @Expose
    private Double bpRpp;
    @SerializedName("AGE")
    @Expose
    private Object age1;
    @SerializedName("HRV_SDNN")
    @Expose
    private Double hrvSdnn;
    @SerializedName("WEIGHT")
    @Expose
    private Object weight;
    @SerializedName("BP_HEART_ATTACK")
    @Expose
    private Object bpHeartAttack;
    @SerializedName("BR_BPM")
    @Expose
    private Object brBPM;
    @SerializedName("HR_BPM")
    @Expose
    private Object hrBPM;

    public Object getBrBPM() {
        return brBPM;
    }

    public void setBrBPM(Object brBPM) {
        this.brBPM = brBPM;
    }

    public Object getHrBPM() {
        return hrBPM;
    }

    public void setHrBPM(Object hrBPM) {
        this.hrBPM = hrBPM;
    }

    public Object getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Object identifier) {
        this.identifier = identifier;
    }

    public Object getPpm() {
        return ppm;
    }

    public void setPpm(Object ppm) {
        this.ppm = ppm;
    }

    public Object getBmi() {
        return bmi;
    }

    public void setBmi(Object bmi) {
        this.bmi = bmi;
    }

    public Double getSnr() {
        return snr;
    }

    public void setSnr(Double snr) {
        this.snr = snr;
    }

    public Double getMsi() {
        return msi;
    }

    public void setMsi(Double msi) {
        this.msi = msi;
    }

    public Double getSystolic() {
        return systolic;
    }

    public void setSystolic(Double systolic) {
        this.systolic = systolic;
    }

    public Double getDiastolic() {
        return diastolic;
    }

    public void setDiastolic(Double diastolic) {
        this.diastolic = diastolic;
    }

    public Object getAge() {
        return age;
    }

    public void setAge(Object age) {
        this.age = age;
    }

    public Object getBreathing() {
        return breathing;
    }

    public void setBreathing(Object breathing) {
        this.breathing = breathing;
    }

    public Double getHealthScore() {
        return healthScore;
    }

    public void setHealthScore(Double healthScore) {
        this.healthScore = healthScore;
    }

    public Double getWaistToHeight() {
        return waistToHeight;
    }

    public void setWaistToHeight(Double waistToHeight) {
        this.waistToHeight = waistToHeight;
    }

    public Double getHeartRateVariability() {
        return heartRateVariability;
    }

    public void setHeartRateVariability(Double heartRateVariability) {
        this.heartRateVariability = heartRateVariability;
    }

    public Object getCardiacWorkload() {
        return cardiacWorkload;
    }

    public void setCardiacWorkload(Object cardiacWorkload) {
        this.cardiacWorkload = cardiacWorkload;
    }

    public Double getAbsi() {
        return absi;
    }

    public void setAbsi(Double absi) {
        this.absi = absi;
    }

    public Double getCvdRisk() {
        return cvdRisk;
    }

    public void setCvdRisk(Double cvdRisk) {
        this.cvdRisk = cvdRisk;
    }

    public Double getStrokeRisk() {
        return strokeRisk;
    }

    public void setStrokeRisk(Double strokeRisk) {
        this.strokeRisk = strokeRisk;
    }

    public Double getHeartAttackRisk() {
        return heartAttackRisk;
    }

    public void setHeartAttackRisk(Double heartAttackRisk) {
        this.heartAttackRisk = heartAttackRisk;
    }

    public Object getHypertensionRisk() {
        return hypertensionRisk;
    }

    public void setHypertensionRisk(Object hypertensionRisk) {
        this.hypertensionRisk = hypertensionRisk;
    }

    public Object getHypertriglyceridemiaRisk() {
        return hypertriglyceridemiaRisk;
    }

    public void setHypertriglyceridemiaRisk(Object hypertriglyceridemiaRisk) {
        this.hypertriglyceridemiaRisk = hypertriglyceridemiaRisk;
    }

    public Object getHypercholesterolemiaRisk() {
        return hypercholesterolemiaRisk;
    }

    public void setHypercholesterolemiaRisk(Object hypercholesterolemiaRisk) {
        this.hypercholesterolemiaRisk = hypercholesterolemiaRisk;
    }

    public Object getDiabetesRisk() {
        return diabetesRisk;
    }

    public void setDiabetesRisk(Object diabetesRisk) {
        this.diabetesRisk = diabetesRisk;
    }

    public Double getIrregularHeartBeats() {
        return irregularHeartBeats;
    }

    public void setIrregularHeartBeats(Double irregularHeartBeats) {
        this.irregularHeartBeats = irregularHeartBeats;
    }

    public Object getMeasurementId() {
        return measurementId;
    }

    public void setMeasurementId(Object measurementId) {
        this.measurementId = measurementId;
    }

    public Object getAvgStarRating() {
        return avgStarRating;
    }

    public void setAvgStarRating(Object avgStarRating) {
        this.avgStarRating = avgStarRating;
    }

    public Double getBpDiastolic() {
        return bpDiastolic;
    }

    public void setBpDiastolic(Double bpDiastolic) {
        this.bpDiastolic = bpDiastolic;
    }

    public Double getBpCvd() {
        return bpCvd;
    }

    public void setBpCvd(Double bpCvd) {
        this.bpCvd = bpCvd;
    }

    public Double getIhbCount() {
        return ihbCount;
    }

    public void setIhbCount(Double ihbCount) {
        this.ihbCount = ihbCount;
    }

    public Double getHealthScore1() {
        return healthScore1;
    }

    public void setHealthScore1(Double healthScore1) {
        this.healthScore1 = healthScore1;
    }

    public Object getPhysioScore() {
        return physioScore;
    }

    public void setPhysioScore(Object physioScore) {
        this.physioScore = physioScore;
    }

    public Double getMentalScore() {
        return mentalScore;
    }

    public void setMentalScore(Double mentalScore) {
        this.mentalScore = mentalScore;
    }

    public Double getBpStroke() {
        return bpStroke;
    }

    public void setBpStroke(Double bpStroke) {
        this.bpStroke = bpStroke;
    }

    public Double getBmiCalc() {
        return bmiCalc;
    }

    public void setBmiCalc(Double bmiCalc) {
        this.bmiCalc = bmiCalc;
    }

    public Double getBpTau() {
        return bpTau;
    }

    public void setBpTau(Double bpTau) {
        this.bpTau = bpTau;
    }

    public Double getBpSystolic() {
        return bpSystolic;
    }

    public void setBpSystolic(Double bpSystolic) {
        this.bpSystolic = bpSystolic;
    }

    public Object getHeight() {
        return height;
    }

    public void setHeight(Object height) {
        this.height = height;
    }

    public Double getMsi1() {
        return msi1;
    }

    public void setMsi1(Double msi1) {
        this.msi1 = msi1;
    }

    public Double getHptRiskProb() {
        return hptRiskProb;
    }

    public void setHptRiskProb(Double hptRiskProb) {
        this.hptRiskProb = hptRiskProb;
    }

    public Double getWaistToHeight1() {
        return waistToHeight1;
    }

    public void setWaistToHeight1(Double waistToHeight1) {
        this.waistToHeight1 = waistToHeight1;
    }

    public Double getSnr1() {
        return snr1;
    }

    public void setSnr1(Double snr1) {
        this.snr1 = snr1;
    }

    public Double getTgRiskProb() {
        return tgRiskProb;
    }

    public void setTgRiskProb(Double tgRiskProb) {
        this.tgRiskProb = tgRiskProb;
    }

    public Object getRisksScore() {
        return risksScore;
    }

    public void setRisksScore(Object risksScore) {
        this.risksScore = risksScore;
    }

    public Double getHdltcRiskProb() {
        return hdltcRiskProb;
    }

    public void setHdltcRiskProb(Double hdltcRiskProb) {
        this.hdltcRiskProb = hdltcRiskProb;
    }

    public Double getVitalScore() {
        return vitalScore;
    }

    public void setVitalScore(Double vitalScore) {
        this.vitalScore = vitalScore;
    }

    public Double getBpRpp() {
        return bpRpp;
    }

    public void setBpRpp(Double bpRpp) {
        this.bpRpp = bpRpp;
    }

    public Object getAge1() {
        return age1;
    }

    public void setAge1(Object age1) {
        this.age1 = age1;
    }

    public Double getHrvSdnn() {
        return hrvSdnn;
    }

    public void setHrvSdnn(Double hrvSdnn) {
        this.hrvSdnn = hrvSdnn;
    }

    public Object getWeight() {
        return weight;
    }

    public void setWeight(Object weight) {
        this.weight = weight;
    }

    public Object getBpHeartAttack() {
        return bpHeartAttack;
    }

    public void setBpHeartAttack(Object bpHeartAttack) {
        this.bpHeartAttack = bpHeartAttack;
    }

}