package ca.ualberta.cs.drivr;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.ArrayList;


public class NotificationReceiver extends BroadcastReceiver {
    private ConnectivityManager connectivityManager;
    private UserManager userManager = UserManager.getInstance();
    private Context context;
//    public Notification(Context context) {
//        this.context = context;
//        this.connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//    }

    public void alarmManagerCallback() {
        Log.i("NotificationReceiver:", "notificationCallback");
        RequestsList requestList = userManager.getRequestsList();
//        requestList.has()
        ArrayList<Request> oldRequestList = userManager.getRequestsList().getRequests();
        ElasticSearch elasticSearch = new ElasticSearch((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        ArrayList<Request> updatedRequests = elasticSearch.getUpdatedRequests();
        for (Request request : updatedRequests) {
            //TODO remove || TRUE
            Log.i("NotificationReceiver:", "updated requests");
            if (requestList.hasProper(request)) {
                requestList.removeProper(request);
                requestList.add(request);
                //There should be a notification right here
                createNotification();
            }
        }
//        createNotification();
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        this.connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        this.context = context;
//        alarmManagerCallback();

    }
    private void createNotification() {
        Log.i("NOTIFICATION: ", "Created notification");
        NotificationManager mNM;
        mNM = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        // Set the icon, scrolling text and timestamp
        NotificationCompat.Builder notification =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_explore_black_24dp)
                        .setContentTitle("Request has updated")
                        .setContentText("something has happened to your request");
//        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNM.notify(1, notification.build());
    }
}
