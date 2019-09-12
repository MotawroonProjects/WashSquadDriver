package com.creative.share.apps.wash_squad_driver.models;

import java.io.Serializable;

public class UserModel implements Serializable {
private int id;
        private String name;
    private String phone_code;
    private String phone;
    private String full_name;
    private String logo;
    private int user_type;
    private int is_active;
    private int is_login;
    private int software_type;
    private int  is_confirmed;
    private String confirmation_code;
    private int is_marketer_add;
    private String card_Id;
    private String email;
    private String gender;
    private String logout_time;
    private String email_verified_at;
    private String created_at;
    private String updated_at;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone_code() {
        return phone_code;
    }

    public String getPhone() {
        return phone;
    }

    public String getFull_name() {
        return full_name;
    }

    public String getLogo() {
        return logo;
    }

    public int getUser_type() {
        return user_type;
    }

    public int getIs_active() {
        return is_active;
    }

    public int getIs_login() {
        return is_login;
    }

    public int getSoftware_type() {
        return software_type;
    }

    public int getIs_confirmed() {
        return is_confirmed;
    }

    public String getConfirmation_code() {
        return confirmation_code;
    }

    public int getIs_marketer_add() {
        return is_marketer_add;
    }

    public String getCard_Id() {
        return card_Id;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }

    public String getLogout_time() {
        return logout_time;
    }

    public String getEmail_verified_at() {
        return email_verified_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }
}
