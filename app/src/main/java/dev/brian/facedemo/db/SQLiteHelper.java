package dev.brian.facedemo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 作者: jiayi.zhang
 * 时间: 2017/11/6
 * 描述: 数据库
 */

public class SQLiteHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "FaceData.db";
    private static final int VERSION = 1;
    private static SQLiteHelper dataBaseHelper;

    public SQLiteHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    private final String createTb = "CREATE TABLE User (_id INTEGER PRIMARY KEY AUTOINCREMENT,name VARCHAR2,avatar BLOB)";

    /**
     * 单例模式
     *
     * @param context 上下文
     * @return
     */
    public static SQLiteHelper getInstance(Context context) {
        if (dataBaseHelper == null) {
            synchronized (SQLiteHelper.class) {
                if (dataBaseHelper == null) {
                    dataBaseHelper = new SQLiteHelper(context);
                }
            }
        }
        return dataBaseHelper;
    }

    /**
     * 创建数据库表
     *
     * @param db 数据库表
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createTb);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS User");
        onCreate(db);
    }
}
