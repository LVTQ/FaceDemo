package dev.brian.facedemo.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import dev.brian.facedemo.R;

/**
 * 作者: jiayi.zhang
 * 时间: 2017/11/6
 * 描述:
 */

public class DBOperate {
    private SQLiteHelper dbHelper;
    private Context context;

    /**
     * 要操作数据库操作实例首先得得到数据库操作实例
     *
     * @param context 上下文
     */
    public DBOperate(Context context) {
        this.context = context;
        this.dbHelper = SQLiteHelper.getInstance(context);
    }

    /**
     *  保存图片
     */
    public void saveImage() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("_id", 1);
        cv.put("avatar", bitmapToBytes(context));
        db.insert("User", null, cv);
        db.close();
    }

    /**
     * 读取图片
     * @return
     */
    public byte[] readImage() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cur = db.query("User", new String[]{"_id", "avatar"}, null, null, null, null, null);
        byte[] imgData = null;
        if (cur.moveToNext()) {
            // 将Blob数据转化为字节数组
            imgData = cur.getBlob(cur.getColumnIndex("avatar"));
        }
        return imgData;
    }

    //图片转为二进制数据
    public byte[] bitmapToBytes(Context context) {
        //将图片转化为位图
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        int size = bitmap.getWidth() * bitmap.getHeight() * 4;
        //创建一个字节数组输出流,流的大小为size
        ByteArrayOutputStream bos = new ByteArrayOutputStream(size);
        try {
            //设置位图的压缩格式，质量为100%，并放入字节数组输出流中
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            //将字节数组输出流转化为字节数组byte[]
            byte[] imageData = bos.toByteArray();
            return imageData;
        } catch (Exception e) {
        } finally {
            try {
                bitmap.recycle();
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new byte[0];
    }
}
