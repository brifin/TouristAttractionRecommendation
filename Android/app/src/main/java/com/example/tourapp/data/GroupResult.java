package com.example.tourapp.data;

public class GroupResult {
    private Boolean isScatteredGroups;

    public Boolean getScatteredGroups() {
        return isScatteredGroups;
    }

    public void setScatteredGroups(Boolean scatteredGroups) {
        isScatteredGroups = scatteredGroups;
    }

    public GroupResult(Boolean isScatteredGroups) {
        this.isScatteredGroups = isScatteredGroups;
    }
}
