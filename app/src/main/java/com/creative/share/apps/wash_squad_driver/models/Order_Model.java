package com.creative.share.apps.wash_squad_driver.models;

import java.io.Serializable;
import java.util.List;

public class Order_Model implements Serializable {
    private int current_page;
    private List<Data> data;

    public int getCurrent_page() {
        return current_page;
    }

    public List<Data> getData() {
        return data;
    }

    public class Data implements Serializable {
        private int id;
        private String user_phone_code;
        private String user_phone;
        private int order_type;
        private int user_id;
        private String marketer_id;
        private String employee_id;
        private int service_id;
        private int size_id;
        private int type_id;
        private String package_id;
        private String address;
        private double latitude;
        private double longitude;
        private long order_date;
        private int order_time_id;
        private String addons;
        private String order_time;
        private String number_of_cars;
        private String payment_method;
        private int driver_id;
        private String feedback;
        private String start_time_work;
        private String end_time_work;
        private int status;
        private String distributor_employee_id;
        private int cancel_reason;
        private int opinion_des;
        private double rating;
        private String total_price;
        private String coupon_serial;
        private String created_at;
        private String updated_at;
        private String driver_full_name;
        private String user_image;
        private String user_full_name;
        private String service_en_title;
        private String service_ar_title;
        private String service_image;
        private String carSize_en_title;
        private String carSize_ar_title;
        private String carSize_image;
        private String cancel_en_title;
        private String cancel_ar_title;
        private String carType_en_title;
        private String carType__ar_title;
        private String carType__image;
        private String work_time_choosen;
        private String work_time_en_title;
        private String work_time_ar_title;
        private String step;
        private String brand_en_title;
        private String brand__ar_title;
        private List<Services> sub_services;
        private Services service;

        public void setStatus(int status) {
            this.status = status;
        }

        public int getId() {
            return id;
        }

        public String getUser_phone_code() {
            return user_phone_code;
        }

        public String getUser_phone() {
            return user_phone;
        }

        public int getOrder_type() {
            return order_type;
        }

        public int getUser_id() {
            return user_id;
        }

        public String getMarketer_id() {
            return marketer_id;
        }

        public String getEmployee_id() {
            return employee_id;
        }

        public int getService_id() {
            return service_id;
        }

        public int getSize_id() {
            return size_id;
        }

        public int getType_id() {
            return type_id;
        }

        public String getPackage_id() {
            return package_id;
        }

        public String getAddress() {
            return address;
        }

        public double getLatitude() {
            return latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public long getOrder_date() {
            return order_date;
        }

        public int getOrder_time_id() {
            return order_time_id;
        }

        public String getAddons() {
            return addons;
        }

        public String getNumber_of_cars() {
            return number_of_cars;
        }

        public String getPayment_method() {
            return payment_method;
        }

        public int getDriver_id() {
            return driver_id;
        }

        public String getFeedback() {
            return feedback;
        }

        public String getStart_time_work() {
            return start_time_work;
        }

        public String getEnd_time_work() {
            return end_time_work;
        }

        public int getStatus() {
            return status;
        }

        public String getDistributor_employee_id() {
            return distributor_employee_id;
        }

        public int getCancel_reason() {
            return cancel_reason;
        }

        public int getOpinion_des() {
            return opinion_des;
        }

        public double getRating() {
            return rating;
        }

        public String getTotal_price() {
            return total_price;
        }

        public String getCoupon_serial() {
            return coupon_serial;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public String getDriver_full_name() {
            return driver_full_name;
        }

        public String getUser_image() {
            return user_image;
        }

        public String getUser_full_name() {
            return user_full_name;
        }

        public String getService_en_title() {
            return service_en_title;
        }

        public String getService_ar_title() {
            return service_ar_title;
        }

        public String getService_image() {
            return service_image;
        }

        public String getCarSize_en_title() {
            return carSize_en_title;
        }

        public String getCarSize_ar_title() {
            return carSize_ar_title;
        }

        public String getCarSize_image() {
            return carSize_image;
        }

        public String getCancel_en_title() {
            return cancel_en_title;
        }

        public String getCancel_ar_title() {
            return cancel_ar_title;
        }

        public String getCarType_en_title() {
            return carType_en_title;
        }

        public String getCarType__ar_title() {
            return carType__ar_title;
        }

        public String getCarType__image() {
            return carType__image;
        }

        public String getWork_time_choosen() {
            return work_time_choosen;
        }

        public String getWork_time_en_title() {
            return work_time_en_title;
        }

        public String getWork_time_ar_title() {
            return work_time_ar_title;
        }

        public String getStep() {
            return step;
        }

        private List<order_images> order_images;

        public List<Data.order_images> getOrder_images() {
            return order_images;
        }

        public String getBrand_en_title() {
            return brand_en_title;
        }

        public String getBrand__ar_title() {
            return brand__ar_title;
        }

        public List<Services> getSub_service() {
            return sub_services;
        }

        public String getOrder_time() {
            return order_time;
        }

        public Services getService() {
            return service;
        }

        public class order_images implements Serializable {
            private int id;
            private int order_id;
            private String image;
            private String type;

            public int getId() {
                return id;
            }

            public int getOrder_id() {
                return order_id;
            }

            public String getImage() {
                return image;
            }

            public String getType() {
                return type;
            }
        }

        public class Services implements Serializable {
            private String id;
            private String ar_title;
            private String en_title;
            private String ar_des;
            private String en_des;
            private String ar_note;
            private String en_note;
            private String image;
            private String parent_id;
            private String level;
            private String price;
            private String created_at;
            private String updated_at;
            private boolean taked;
            private int timer;

            public String getId() {
                return id;
            }

            public String getAr_title() {
                return ar_title;
            }

            public String getEn_title() {
                return en_title;
            }

            public String getAr_des() {
                return ar_des;
            }

            public String getEn_des() {
                return en_des;
            }

            public String getAr_note() {
                return ar_note;
            }

            public String getEn_note() {
                return en_note;
            }

            public String getImage() {
                return image;
            }

            public String getParent_id() {
                return parent_id;
            }

            public String getLevel() {
                return level;
            }

            public String getPrice() {
                return price;
            }

            public String getCreated_at() {
                return created_at;
            }

            public String getUpdated_at() {
                return updated_at;
            }

            public boolean isTaked() {
                return taked;
            }

            public int getTimer() {
                return timer;
            }
        }

    }

}