package cn.x.homedog;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;

public class ForegroundService extends Service {
    HomeReceiver homeReceiver;

    @Override
    public void onCreate() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("看家狗 - 监视任务键服务")
                .setContentText("解除服务将使软件功能失效。点击进行关闭操作。")
                .setTicker("看家狗服务开始运行")
                .setContentIntent(getSettinsPendingIntent());
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("homedog", "监视任务键服务", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
            builder.setChannelId("homedog");
        }
        startForeground(1, builder.build());
        super.onCreate();
    }

    private PendingIntent getSettinsPendingIntent() {
        Intent i = new Intent();
        i.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        i.setData(Uri.fromParts("package", this.getPackageName(), null));
        return PendingIntent.getActivity(getApplicationContext(), 2184, i, 0);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        homeReceiver = new HomeReceiver();
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        registerReceiver(homeReceiver, intentFilter);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(homeReceiver);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
