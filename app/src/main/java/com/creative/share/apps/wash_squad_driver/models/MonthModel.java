package com.creative.share.apps.wash_squad_driver.models;

import java.io.Serializable;
import java.util.List;

public class MonthModel implements Serializable {
    private String title;
    private boolean isselected;

    public MonthModel(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public boolean isIsselected() {
        return isselected;
    }

    public void setIsselected(boolean isselected) {
        this.isselected = isselected;
    }
}
