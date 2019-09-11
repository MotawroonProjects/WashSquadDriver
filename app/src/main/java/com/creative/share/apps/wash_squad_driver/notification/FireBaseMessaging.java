package com.creative.share.apps.wash_squad_driver.notification;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.creative.share.apps.wash_squad_driver.models.UserModel;
import com.creative.share.apps.wash_squad_driver.preferences.Preferences;
import com.creative.share.apps.wash_squad_driver.tags.Tags;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class FireBaseMessaging extends FirebaseMessagingService {

    private Preferences preferences = Preferences.newInstance();
    private Map<String,String> map;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getData().size()>0)
        {
             map= remoteMessage.getData();
            for (String key : map.keySet())
            {
                Log.e("Key :"+key,"__ value :"+map.get(key));
            }

            if (getSession().equals(Tags.session_login))
            {


            }
        }

    }

    private void manageNotification() {
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            sendNotificationNew();
        }else
            {
                sendNotificationOld();
            }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendNotificationNew()
    {





    }

    private void sendNotificationOld()
    {

    }

    private UserModel getUserData()
    {
        return preferences.getUserData(this);
    }

    private String getSession()
    {
        return preferences.getSession(this);
    }
}
