package com.zankong.tool.zkapp.v8fn.notify_pck;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.eclipsesource.v8.V8Object;
import com.zankong.tool.zkapp.R;
import com.zankong.tool.zkapp.ZKToolApi;
import com.zankong.tool.zkapp.activity.ZKActivity;
import com.zankong.tool.zkapp.util.Util;
import com.zankong.tool.zkapp.util.V8Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

public class NotifyHelper {
    private static NotificationManager mNotificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
    private static int notifyId = 0;

    private static Context getContext() {
        return ZKToolApi.getInstance().getContext();
    }

    public static void notification(JSONObject message,JSONObject arguments){
        Util.log("aaaaaaaaaaa", "coming");
        try {
            Notification.Builder builder = new Notification.Builder(getContext())
                    .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)
                    .setContentTitle("name")
                    .setContentText("subject")
                    .setSmallIcon(R.drawable.full_star)
                    .setAutoCancel(true);
            RemoteViews remoteViews = new RemoteViews(getContext().getPackageName(), R.layout.v8fn_notify_default);
            Intent intent = new Intent(getContext(), ZKActivity.class);
            Iterator<String> keys = message.keys();
            while (keys.hasNext()) {
                String value = message.opt(keys.next())+ "";
                switch (keys.next()) {
                    case "title":
                        remoteViews.setTextViewText(R.id.title,value);
                        break;
                    case "url":
                        intent.putExtra("page",value);
                        break;
                    case "img":
                        try {
                            remoteViews.setImageViewBitmap(R.id.img, Util.getImageFromAssetsFile(value));
                        } catch (Exception e) {
                            remoteViews.setImageViewResource(R.id.img, R.drawable.splash);
                        }
                        break;
                    case "content":
                        remoteViews.setTextViewText(R.id.content, value);
                        break;
                    case "time":
                        remoteViews.setTextViewText(R.id.time, value);
                        break;
                }
            }
            intent.putExtra("arguments", arguments.toString());
            PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);
            builder.setContent(remoteViews);
            Notification notification = builder.build();
            mNotificationManager.notify(notifyId++, notification);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void notification(V8Object message,V8Object arguments) {
        try {
            notification(V8Utils.V82JSON(message),V8Utils.V82JSON(arguments));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
