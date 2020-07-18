package cn.x.homedog;

public class App {
    String packageName, appName;

    public App(String packageName, String appName){
        this.packageName = packageName;
        this.appName = appName;
    }

    @Override
    public String toString() {
        return appName;
    }
}
