package cn.x.homedog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

public class MainActivity extends Activity {
    LinkedList<App> appList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent rebootServiceIntent = new Intent(getApplicationContext(), ForegroundService.class);
        startService(rebootServiceIntent);

        getAppList(getApplicationContext());
        setListView();
    }

    private void getAppList(Context context) {
        appList = new LinkedList<>();
        try {
            PackageManager pm = context.getPackageManager();
            List<PackageInfo> packageInfos = pm.getInstalledPackages(0);
            for (PackageInfo info : packageInfos) {
                ApplicationInfo appInfo = pm.getApplicationInfo(info.applicationInfo.packageName, PackageManager.GET_META_DATA);
                if(((String) pm.getApplicationLabel(appInfo)).contains("auncher") || ((String) pm.getApplicationLabel(appInfo)).contains("桌面")){
                    appList.addFirst(new App(info.applicationInfo.packageName, (String) pm.getApplicationLabel(appInfo)));
                }else{
                    appList.addLast(new App(info.applicationInfo.packageName, (String) pm.getApplicationLabel(appInfo)));
                }
            }
        } catch (Exception e) {
            Toast.makeText(context, "获取软件列表发生错误！", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void setListView() {
        ArrayAdapter<App> adapter = new ArrayAdapter<>
                (getApplication(), android.R.layout.simple_expandable_list_item_1, appList);
        final ListView listView = findViewById(R.id.list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long indexLong) {
                DBOperator dbo = new DBOperator(getApplicationContext());
                dbo.setLauncherPackageName(appList.get(index).packageName);
                Toast.makeText(getApplicationContext(), appList.get(index).appName + "已看家！", Toast.LENGTH_LONG).show();
            }
        });
        listView.setAdapter(adapter);
    }
}