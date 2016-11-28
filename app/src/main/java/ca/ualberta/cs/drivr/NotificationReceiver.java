package ca.ualberta.cs.drivr;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.ArrayList;


public class NotificationReceiver extends BroadcastReceiver{
    private ConnectivityManager connectivityManager;
    private UserManager userManager = UserManager.getInstance();
    private Context context;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
//    public NotificationReceiver() {
//        super("CheckNotifications");
//    }
//    public Notification(Context context) {
//        this.context = context;
//        this.connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//    }


//    @Override
//    protected void onHandleIntent(Intent intent) {
//        Log.i("NotificationReceiver:", "abcd");
//        alarmManagerCallback();
//    }


    public void alarmManagerCallback() {
        Log.i("NotificationReceiver:", "notificationCallback");
        RequestsList requestList = userManager.getRequestsList();
//        requestList.has()
        ArrayList<Request> oldRequestList = userManager.getRequestsList().getRequests();
        ElasticSearch elasticSearch = new ElasticSearch(connectivityManager);
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
//        this.stopSelf();
//        createNotification();
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        this.connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        this.context = context;
        Log.i("NotificationReceiver:", "updsdfsadfsadfs");
        alarmManagerCallback();

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

//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
}
