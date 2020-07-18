package cn.x.homedog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.widget.Toast;

public class HomeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        DBOperator dbo = new DBOperator(context);
        if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(intent.getAction()) && "recentapps".equals(intent.getStringExtra("reason"))) {
            if(System.currentTimeMillis() - dbo.getLastestTimePressHome() < 1500) {
                dbo.set0PressHome();
                try {
                    Intent i = context.getPackageManager().getLaunchIntentForPackage(dbo.getLauncherPackageName());
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                    Toast.makeText(context, "看家狗：已看家！", Toast.LENGTH_LONG).show();
                } catch (SQLException e){
                    Toast.makeText(context, "看家狗：未设置作为桌面的应用！", Toast.LENGTH_LONG).show();
                } catch (Exception e){
                    Toast.makeText(context, "看家狗：应用没有可显示界面！", Toast.LENGTH_LONG).show();
                }
            }else{
                dbo.setLastestTimePressHome();
            }
        }
    }
}
