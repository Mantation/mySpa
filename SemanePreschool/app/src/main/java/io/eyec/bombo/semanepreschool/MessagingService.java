package io.eyec.bombo.semanepreschool;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import menuFragments.GlobalMessages;

public class MessagingService extends FirebaseMessagingService {
    private final String Tag = "FCM Message";

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if(remoteMessage.getNotification() !=null){
            /*Intent appActivityIntent = new Intent(this, main.class);

            PendingIntent contentAppActivityIntent =
                    PendingIntent.getActivity(
                            context,  // calling from Activity
                            0,
                            appActivityIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT);*/



            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();
            //messagingHelper.displayNotification(getApplicationContext(),title,body);
            //String clickAction = remoteMessage.getNotification().getClickAction();
            //Intent intent = new Intent(clickAction);
            Intent intent = new Intent(this, main.class);
            main.setOnNotification(true);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //PendingIntent contentIntent = PendingIntent.getActivity(this, 0,intent,PendingIntent.FLAG_ONE_SHOT);
            PendingIntent contentIntent = PendingIntent.getActivity(this, 0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            if(remoteMessage.getData().isEmpty()) {
                messagingHelper.showNotification(getApplicationContext(), title, body,contentIntent);
            }else{
                messagingHelper.showBackGroundNotification(getApplicationContext(),remoteMessage.getData(),contentIntent);

            }
            //methods.globalMethods.loadFragments(R.id.main, new GlobalMessages(), getApplicationContext());
        }

    }




}
