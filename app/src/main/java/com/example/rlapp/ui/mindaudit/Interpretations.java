package com.example.rlapp.ui.mindaudit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Interpretations {

@SerializedName("depression")
@Expose
private Depression depression;
@SerializedName("anxiety")
@Expose
private Anxiety anxiety;
@SerializedName("stress")
@Expose
private Stress stress;

public Depression getDepression() {
return depression;
}

public void setDepression(Depression depression) {
this.depression = depression;
}

public Anxiety getAnxiety() {
return anxiety;
}

public void setAnxiety(Anxiety anxiety) {
this.anxiety = anxiety;
}

public Stress getStress() {
return stress;
}

public void setStress(Stress stress) {
this.stress = stress;
}

}