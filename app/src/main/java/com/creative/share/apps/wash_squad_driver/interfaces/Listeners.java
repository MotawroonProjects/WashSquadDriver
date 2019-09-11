package com.creative.share.apps.wash_squad_driver.interfaces;

public interface Listeners {

    interface LoginListener {
        void checkDataLogin(String user_name, String password);
    }

    interface SkipListener
    {
        void skip();
    }
    interface CreateAccountListener
    {
        void createNewAccount();
    }

    interface ShowCountryDialogListener
    {
        void showDialog();
    }

    interface SignUpListener
    {
        void checkDataSignUp(String name, String phone_code, String phone, String password);

    }

    interface BackListener
    {
        void back();
    }



    interface ContactListener
    {
        void sendContact(String name, String email, String phone_code, String phone, String message);
    }


}
