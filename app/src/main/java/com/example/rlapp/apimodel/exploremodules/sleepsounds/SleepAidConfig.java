package com.example.rlapp.apimodel.exploremodules.sleepsounds;



import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class SleepAidConfig {

    @SerializedName("meditate")
    @Expose
    private Boolean meditate;
    @SerializedName("readABook")
    @Expose
    private Boolean readABook;
    @SerializedName("writeAJournal")
    @Expose
    private Boolean writeAJournal;
    @SerializedName("shower")
    @Expose
    private Boolean shower;
    @SerializedName("drinkWater")
    @Expose
    private Boolean drinkWater;
    @SerializedName("brushTeeth")
    @Expose
    private Boolean brushTeeth;
    @SerializedName("turnOffPhone")
    @Expose
    private Boolean turnOffPhone;
    @SerializedName("washHands")
    @Expose
    private Boolean washHands;
    @SerializedName("duration")
    @Expose
    private Integer duration;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("subCategory")
    @Expose
    private String subCategory;
    @SerializedName("modifiedAt")
    @Expose
    private String modifiedAt;
    @SerializedName("isActive")
    @Expose
    private Boolean isActive;

    public Boolean getMeditate() {
        return meditate;
    }

    public void setMeditate(Boolean meditate) {
        this.meditate = meditate;
    }

    public Boolean getReadABook() {
        return readABook;
    }

    public void setReadABook(Boolean readABook) {
        this.readABook = readABook;
    }

    public Boolean getWriteAJournal() {
        return writeAJournal;
    }

    public void setWriteAJournal(Boolean writeAJournal) {
        this.writeAJournal = writeAJournal;
    }

    public Boolean getShower() {
        return shower;
    }

    public void setShower(Boolean shower) {
        this.shower = shower;
    }

    public Boolean getDrinkWater() {
        return drinkWater;
    }

    public void setDrinkWater(Boolean drinkWater) {
        this.drinkWater = drinkWater;
    }

    public Boolean getBrushTeeth() {
        return brushTeeth;
    }

    public void setBrushTeeth(Boolean brushTeeth) {
        this.brushTeeth = brushTeeth;
    }

    public Boolean getTurnOffPhone() {
        return turnOffPhone;
    }

    public void setTurnOffPhone(Boolean turnOffPhone) {
        this.turnOffPhone = turnOffPhone;
    }

    public Boolean getWashHands() {
        return washHands;
    }

    public void setWashHands(Boolean washHands) {
        this.washHands = washHands;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(String modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

}
