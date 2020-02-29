package com.pratham.assessment.domain;

public class StorageInfo {

    public final String path;
    public final boolean readonly;
    public final boolean removable;
    public final int number;

    public StorageInfo(String path, boolean readonly, boolean removable, int number) {
        this.path = path;
        this.readonly = readonly;
        this.removable = removable;
        this.number = number;
    }

    public String getDisplayName() {
        StringBuilder res = new StringBuilder();
        if (!removable) {
            res.append("Internal SD card");
        } else if (number > 1) {
            res.append("SD card " + number + " " + path);
        } else {
            res.append("USB  " + path);
        }
        if (readonly) {
            res.append(" (Read only)");
        }
        return res.toString();
    }
}