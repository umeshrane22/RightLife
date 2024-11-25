package com.example.rlapp.ui.healthaudit;

public class Fruit {
    private String name;
    private boolean isSelected;

    public Fruit(String name, boolean isSelected) {
        this.name = name;
        this.isSelected = isSelected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    // Getters and setters
}