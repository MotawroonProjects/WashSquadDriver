package com.creative.share.apps.wash_squad_driver.models;

import java.io.Serializable;
import java.util.List;

public class ReviewModel implements Serializable {
  private Rate rate1;
    private Rate rate2;
    private Rate rate3;
    private Rate rate4;
    private Rate rate5;

    public Rate getRate1() {
        return rate1;
    }

    public void setRate1(Rate rate1) {
        this.rate1 = rate1;
    }

    public Rate getRate2() {
        return rate2;
    }

    public void setRate2(Rate rate2) {
        this.rate2 = rate2;
    }

    public Rate getRate3() {
        return rate3;
    }

    public void setRate3(Rate rate3) {
        this.rate3 = rate3;
    }

    public Rate getRate4() {
        return rate4;
    }

    public void setRate4(Rate rate4) {
        this.rate4 = rate4;
    }

    public Rate getRate5() {
        return rate5;
    }

    public void setRate5(Rate rate5) {
        this.rate5 = rate5;
    }

    public static class Rate implements Serializable {
        private double rating;
        private double count_of_orders;
        private double commission_value;
        private double sum_of_commission;

        public double getRating() {
            return rating;
        }

        public void setRating(double rating) {
            this.rating = rating;
        }

        public double getCount_of_orders() {
            return count_of_orders;
        }

        public void setCount_of_orders(double count_of_orders) {
            this.count_of_orders = count_of_orders;
        }

        public double getCommission_value() {
            return commission_value;
        }

        public void setCommission_value(double commission_value) {
            this.commission_value = commission_value;
        }

        public double getSum_of_commission() {
            return sum_of_commission;
        }

        public void setSum_of_commission(double sum_of_commission) {
            this.sum_of_commission = sum_of_commission;
        }
    }




}
