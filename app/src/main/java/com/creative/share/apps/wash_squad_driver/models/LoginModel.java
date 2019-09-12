package com.creative.share.apps.wash_squad_driver.models;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.creative.share.apps.wash_squad_driver.BR;
import com.creative.share.apps.wash_squad_driver.R;

public class LoginModel extends BaseObservable {

    private String username;
    private String password;
    public ObservableField<String> error_user_name = new ObservableField<>();
    public ObservableField<String> error_password = new ObservableField<>();


    public LoginModel() {
        this.username="";
        this.password="";
    }

    public LoginModel(String username, String password) {
        this.username = username;
        notifyPropertyChanged(BR.username);
        this.password = password;
        notifyPropertyChanged(BR.password);
        


    }


    @Bindable
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        notifyPropertyChanged(BR.username);


    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);

    }

    public boolean isDataValid(Context context)
    {
        if (!TextUtils.isEmpty(username)&&
                password.length()>=6
        )
        {
            error_user_name.set(null);
            error_password.set(null);
           // Log.e("lll","lllll");
            return true;
        }else
            {
                if (username.isEmpty())
                {
                    error_user_name.set(context.getString(R.string.field_req));
                }else
                    {
                        error_user_name.set(null);
                    }



                if (password.isEmpty())
                {
                    error_password.set(context.getString(R.string.field_req));
                }else if (password.length()<6)
                {
                    error_password.set(context.getString(R.string.pass_short));
                }else
                    {
                        error_password.set(null);

                    }
                return false;
            }
    }


}
