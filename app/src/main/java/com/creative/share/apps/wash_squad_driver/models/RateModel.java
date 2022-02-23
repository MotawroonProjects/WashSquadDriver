package com.creative.share.apps.wash_squad_driver.models;

import java.io.Serializable;
import java.util.List;

public class RateModel implements Serializable {
    private List<Data> data;

    public List<Data> getData() {
        return data;
    }

    public class Data implements Serializable {
        private double rating;
        private double count_of_orders;
        private double commission_value;
        private double sum_of_commission;

        public double getRating() {
            return rating;
        }

        public double getcount_of_orders() {
            return count_of_orders;
        }

        public double getCommission_value() {
            return commission_value;
        }

        public double getSum_of_commission() {
            return sum_of_commission;
        }
    }


}
