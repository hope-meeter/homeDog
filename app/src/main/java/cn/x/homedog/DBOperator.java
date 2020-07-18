package cn.x.homedog;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOperator extends SQLiteOpenHelper {
    public DBOperator(Context context) {
        super(context, "homedog.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE settings(k TEXT PRIMARY KEY NOT NULL,v TEXT NOT NULL)");
        sqLiteDatabase.execSQL("INSERT INTO settings(k, v) values ('launcherPackageName', 'cn.x.homedog')");
        sqLiteDatabase.execSQL("INSERT INTO settings(k, v) values ('lastestTimePressHome', '" + System.currentTimeMillis() + "')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVer, int newVer) {
    }

    public String getLauncherPackageName() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT v FROM settings WHERE k = 'launcherPackageName'", null);
        String result = null;
        while (cursor.moveToNext()) {
            result = cursor.getString(cursor.getColumnIndex("v"));
        }
        cursor.close();
        db.close();
        if("cn.x.homedog".equals(result)){
            throw new SQLException();
        }
        return result;
    }

    public Long getLastestTimePressHome() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT v FROM settings WHERE k = 'lastestTimePressHome'", null);
        Long result = null;
        while (cursor.moveToNext()) {
            result = Long.parseLong(cursor.getString(cursor.getColumnIndex("v")));
        }
        cursor.close();
        db.close();
        return result;
    }

    public void setLauncherPackageName(String packageName){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE settings SET v = '" + packageName + "' WHERE k = 'launcherPackageName'");
        db.close();
    }

    public void setLastestTimePressHome(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE settings SET v = '" + System.currentTimeMillis() + "' WHERE k = 'lastestTimePressHome'");
        db.close();
    }

    public void set0PressHome(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE settings SET v = '0' WHERE k = 'lastestTimePressHome'");
        db.close();
    }
}
