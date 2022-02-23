package com.creative.share.apps.wash_squad_driver.models;

import java.io.Serializable;
import java.util.List;

public class StaticModel implements Serializable {
    private Data data;

    public Data getData() {
        return data;
    }

    public class Data implements Serializable {
        private double polishing;
        private double wash;
        private double sterilization;
        private double subscription;
        private double total_orders;
        private double commissions;

        public double getPolishing() {
            return polishing;
        }

        public double getWash() {
            return wash;
        }

        public double getSterilization() {
            return sterilization;
        }

        public double getSubscription() {
            return subscription;
        }

        public double getTotal_orders() {
            return total_orders;
        }

        public double getCommissions() {
            return commissions;
        }
    }


}
