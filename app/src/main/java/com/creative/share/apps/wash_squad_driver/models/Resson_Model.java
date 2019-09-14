package com.creative.share.apps.wash_squad_driver.models;

import java.io.Serializable;
import java.util.List;

public class Resson_Model implements Serializable {
    private List<Data> data;

    public List<Data> getData() {
        return data;
    }

    public class Data implements Serializable
    {
        private int id;
            private String ar_title;
        private String en_title;

        public int getId() {
            return id;
        }

        public String getAr_title() {
            return ar_title;
        }

        public String getEn_title() {
            return en_title;
        }
    }
}
