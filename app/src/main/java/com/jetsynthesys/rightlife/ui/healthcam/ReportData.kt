package com.jetsynthesys.rightlife.ui.healthcam

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ReportData : Serializable {
    @SerializedName("identifier")
    @Expose
    var identifier: Any? = null

    @SerializedName("ppm")
    @Expose
    var ppm: Any? = null

    @SerializedName("bmi")
    @Expose
    var bmi: Any? = null

    @SerializedName("snr")
    @Expose
    var snr: Double? = null

    @SerializedName("MSI")
    @Expose
    var msi: Double? = null

    @SerializedName("systolic")
    @Expose
    var systolic: Double? = null

    @SerializedName("diastolic")
    @Expose
    var diastolic: Double? = null

    @SerializedName("age")
    @Expose
    var age: Any? = null

    @SerializedName("breathing")
    @Expose
    var breathing: Any? = null

    @SerializedName("HEALTH_SCORE")
    @Expose
    var healthScore: Double? = null

    @SerializedName("waistToHeight")
    @Expose
    var waistToHeight: Double? = null

    @SerializedName("heartRateVariability")
    @Expose
    var heartRateVariability: Double? = null

    @SerializedName("cardiacWorkload")
    @Expose
    var cardiacWorkload: Any? = null

    @SerializedName("absi")
    @Expose
    var absi: Double? = null

    @SerializedName("cvdRisk")
    @Expose
    var cvdRisk: Double? = null

    @SerializedName("strokeRisk")
    @Expose
    var strokeRisk: Double? = null

    @SerializedName("heartAttackRisk")
    @Expose
    var heartAttackRisk: Double? = null

    @SerializedName("HypertensionRisk")
    @Expose
    var hypertensionRisk: Any? = null

    @SerializedName("HypertriglyceridemiaRisk")
    @Expose
    var hypertriglyceridemiaRisk: Any? = null

    @SerializedName("HypercholesterolemiaRisk")
    @Expose
    var hypercholesterolemiaRisk: Any? = null

    @SerializedName("DiabetesRisk")
    @Expose
    var diabetesRisk: Any? = null

    @SerializedName("irregularHeartBeats")
    @Expose
    var irregularHeartBeats: Double? = null

    @SerializedName("measurementId")
    @Expose
    var measurementId: Any? = null

    @SerializedName("avgStarRating")
    @Expose
    var avgStarRating: Any? = null

    @SerializedName("BP_DIASTOLIC")
    @Expose
    var bpDiastolic: Double? = null

    @SerializedName("BP_CVD")
    @Expose
    var bpCvd: Double? = null

    @SerializedName("IHB_COUNT")
    @Expose
    var ihbCount: Double? = null

    @SerializedName("HEALTH_SCORE1")
    @Expose
    var healthScore1: Double? = null

    @SerializedName("PHYSIO_SCORE")
    @Expose
    var physioScore: Any? = null

    @SerializedName("MENTAL_SCORE")
    @Expose
    var mentalScore: Double? = null

    @SerializedName("BP_STROKE")
    @Expose
    var bpStroke: Double? = null

    @SerializedName("BMI_CALC")
    @Expose
    var bmiCalc: Double? = null

    @SerializedName("BP_TAU")
    @Expose
    var bpTau: Double? = null

    @SerializedName("BP_SYSTOLIC")
    @Expose
    var bpSystolic: Double? = null

    @SerializedName("HEIGHT")
    @Expose
    var height: Any? = null

    @SerializedName("MSI1")
    @Expose
    var msi1: Double? = null

    @SerializedName("HPT_RISK_PROB")
    @Expose
    var hptRiskProb: Double? = null

    @SerializedName("WAIST_TO_HEIGHT1")
    @Expose
    var waistToHeight1: Double? = null

    @SerializedName("SNR")
    @Expose
    var snr1: Double? = null

    @SerializedName("TG_RISK_PROB")
    @Expose
    var tgRiskProb: Double? = null

    @SerializedName("RISKS_SCORE")
    @Expose
    var risksScore: Any? = null

    @SerializedName("HDLTC_RISK_PROB")
    @Expose
    var hdltcRiskProb: Double? = null

    @SerializedName("VITAL_SCORE")
    @Expose
    var vitalScore: Double? = null

    @SerializedName("BP_RPP")
    @Expose
    var bpRpp: Double? = null

    @SerializedName("AGE")
    @Expose
    var age1: Any? = null

    @SerializedName("HRV_SDNN")
    @Expose
    var hrvSdnn: Double? = null

    @SerializedName("WEIGHT")
    @Expose
    var weight: Any? = null

    @SerializedName("BP_HEART_ATTACK")
    @Expose
    var bpHeartAttack: Any? = null

    @SerializedName("BR_BPM")
    @Expose
    var brBPM: Any? = null

    @SerializedName("HR_BPM")
    @Expose
    var hrBPM: Any? = null
}