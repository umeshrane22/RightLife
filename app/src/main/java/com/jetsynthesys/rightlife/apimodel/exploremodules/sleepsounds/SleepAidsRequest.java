package com.jetsynthesys.rightlife.apimodel.exploremodules.sleepsounds;
import com.google.gson.annotations.SerializedName;

public class SleepAidsRequest {

    @SerializedName("meditate")
    private boolean meditate;

    @SerializedName("readABook")
    private boolean readABook;

    @SerializedName("writeAJournal")
    private boolean writeAJournal;

    @SerializedName("shower")
    private boolean shower;

    @SerializedName("drinkWater")
    private boolean drinkWater;

    @SerializedName("brushTeeth")
    private boolean brushTeeth;

    @SerializedName("turnOffPhone")
    private boolean turnOffPhone;

    @SerializedName("washHands")
    private boolean washHands;

    @SerializedName("duration")
    private int duration;

    @SerializedName("category")
    private String category;

    @SerializedName("subCategory")
    private String subCategory;

    // Constructor
    public SleepAidsRequest(boolean meditate, boolean readABook, boolean writeAJournal,
                            boolean shower, boolean drinkWater, boolean brushTeeth,
                            boolean turnOffPhone, boolean washHands, int duration,
                            String category, String subCategory) {
        this.meditate = meditate;
        this.readABook = readABook;
        this.writeAJournal = writeAJournal;
        this.shower = shower;
        this.drinkWater = drinkWater;
        this.brushTeeth = brushTeeth;
        this.turnOffPhone = turnOffPhone;
        this.washHands = washHands;
        this.duration = duration;
        this.category = category;
        this.subCategory = subCategory;
    }
}
