package com.creative.share.apps.wash_squad_driver.activities_fragments.activity_sign_in;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.creative.share.apps.wash_squad_driver.R;
import com.creative.share.apps.wash_squad_driver.activities_fragments.activity_home.activity.HomeActivity;
import com.creative.share.apps.wash_squad_driver.databinding.FragmentSignInBinding;
import com.creative.share.apps.wash_squad_driver.interfaces.Listeners;
import com.creative.share.apps.wash_squad_driver.models.LoginModel;
import com.creative.share.apps.wash_squad_driver.models.UserModel;
import com.creative.share.apps.wash_squad_driver.preferences.Preferences;
import com.mukesh.countrypicker.CountryPicker;

import java.util.Locale;

import io.paperdb.Paper;

public class Fragment_Sign_In extends Fragment implements Listeners.LoginListener {
    private FragmentSignInBinding binding;
    private SignInActivity activity;
    private String lang;
    private Preferences preferences;
    private CountryPicker countryPicker;
    private LoginModel loginModel;
    private UserModel userModel;

    public static Fragment_Sign_In newInstance() {
        return new Fragment_Sign_In();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_sign_in, container, false);
        View view = binding.getRoot();
        initView();
        return view;
    }

    private void initView() {
        loginModel = new LoginModel();
        activity = (SignInActivity) getActivity();
        preferences = Preferences.newInstance();
        Paper.init(activity);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLoginModel(loginModel);
        binding.setLoginListener(this);





    }





    @Override
    public void checkDataLogin(String user_name, String password) {

        loginModel = new LoginModel(user_name,password);
        binding.setLoginModel(loginModel);

        if (loginModel.isDataValid(activity))
        {
            login(user_name,password);
        }
    }

    private void login(String user_name,String password)
    {

        Intent intent = new Intent(activity, HomeActivity.class);
        startActivity(intent);
        activity.finish();

//        ProgressDialog dialog = Common.createProgressDialog(activity,getString(R.string.wait));
//        dialog.setCancelable(false);
//        dialog.show();
//        try {
//
//            Api.getService(lang)
//                    .login(phone_code,phone,"1",password)
//                    .enqueue(new Callback<UserModel>() {
//                        @Override
//                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {
//                            dialog.dismiss();
//                            if (response.isSuccessful()&&response.body()!=null)
//                            {
//                                Log.e("token",response.body().getToken());
//                                preferences.create_update_userData(activity,response.body());
//                                preferences.createSession(activity, Tags.session_login);
//                                Intent intent = new Intent(activity,HomeActivity.class);
//                                startActivity(intent);
//                                activity.finish();
//
//                            }else
//                            {
//                                if (response.code() == 422) {
//                                    Toast.makeText(activity, getString(R.string.em_exist), Toast.LENGTH_SHORT).show();
//                                } else if (response.code() == 500) {
//                                    Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();
//
//
//                                }else if (response.code()==401)
//                                {
//                                    Toast.makeText(activity, R.string.inc_phone_pas, Toast.LENGTH_SHORT).show();
//
//                                }else
//                                {
//                                    Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
//
//                                    try {
//
//                                        Log.e("error",response.code()+"_"+response.errorBody().string());
//                                    } catch (IOException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<UserModel> call, Throwable t) {
//                            try {
//                                dialog.dismiss();
//                                if (t.getMessage()!=null)
//                                {
//                                    Log.e("error",t.getMessage());
//                                    if (t.getMessage().toLowerCase().contains("failed to connect")||t.getMessage().toLowerCase().contains("unable to resolve host"))
//                                    {
//                                        Toast.makeText(activity,R.string.something, Toast.LENGTH_SHORT).show();
//                                    }else
//                                    {
//                                        Toast.makeText(activity,t.getMessage(), Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//
//                            }catch (Exception e){}
//                        }
//                    });
//        }catch (Exception e){
//            dialog.dismiss();
//
//        }
    }
}
