package com.banmo.sweethomeclient.tool;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import androidx.annotation.Nullable;

import com.banmo.sweethomeclient.mvp.singletalk.MsgDateBean;

import java.util.LinkedList;

public class SqLiteTOOLs extends SQLiteOpenHelper {

    private static final String TAG = "SqLiteTOOLs";
    private static final String TABLE_NAME = "MSGTABLE";
    static SqLiteTOOLs sqLiteTOOLs;
    static SQLiteDatabase db;


    public SqLiteTOOLs(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static void init(Context context, int userid) {
        Log.e(TAG, "init: ");
        sqLiteTOOLs = new SqLiteTOOLs(context, "SweetHome_db" + userid, null, 1);
        db = sqLiteTOOLs.getWritableDatabase();

    }

    public static LinkedList<MsgDateBean> select() {
        Cursor cursor = db.query(TABLE_NAME, new String[]{"msgType", "srcUserid", "timeStamp", "content", "recordTime"}, null, null, null, null, null);

        LinkedList<MsgDateBean> msgDateBeans = new LinkedList<>();
        while (cursor.moveToNext()) {
            Bitmap bmp = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888);
            bmp.eraseColor(Color.parseColor("#FFEC808D"));
            int msgType = cursor.getInt(cursor.getColumnIndex("msgType"));
            Integer srcUserid = cursor.getInt(cursor.getColumnIndex("srcUserid"));
            String timeStamp = cursor.getString(cursor.getColumnIndex("timeStamp"));
            byte[] content = cursor.getBlob(cursor.getColumnIndex("content"));
            String recordTime = cursor.getString(cursor.getColumnIndex("recordTime"));
            msgDateBeans.add(new MsgDateBean(content, timeStamp, srcUserid, msgType, recordTime));
        }

        cursor.close();
        Log.e("ss", String.valueOf(msgDateBeans.size()));
        return msgDateBeans;
    }

    public static MsgDateBean selectLastByUserid(int Userid) {
        Cursor cursor = db.query(TABLE_NAME, new String[]{"msgType", "srcUserid", "timeStamp", "content", "recordTime"}, "srcUserid=?", new String[]{String.valueOf(Userid)}, null, null, null);
        if (cursor.moveToLast()) {
            Bitmap bmp = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888);
            bmp.eraseColor(Color.parseColor("#FFEC808D"));
            int msgType = cursor.getInt(cursor.getColumnIndex("msgType"));
            Integer srcUserid = cursor.getInt(cursor.getColumnIndex("srcUserid"));
            String timeStamp = cursor.getString(cursor.getColumnIndex("timeStamp"));
            byte[] content = cursor.getBlob(cursor.getColumnIndex("content"));
            String recordTime = cursor.getString(cursor.getColumnIndex("recordTime"));

            return new MsgDateBean(content, timeStamp, srcUserid, msgType, recordTime);
        }
        cursor.close();
        return null;
    }

    public static LinkedList<MsgDateBean> selectByUserid(int Userid) {
        Cursor cursor = db.query(TABLE_NAME, new String[]{"msgType", "srcUserid", "timeStamp", "content", "recordTime"}, "srcUserid=?", new String[]{String.valueOf(Userid)}, null, null, null);

        LinkedList<MsgDateBean> msgDateBeans = new LinkedList<>();
        while (cursor.moveToNext()) {
            Bitmap bmp = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888);
            bmp.eraseColor(Color.parseColor("#FFEC808D"));
            int msgType = cursor.getInt(cursor.getColumnIndex("msgType"));
            Integer srcUserid = cursor.getInt(cursor.getColumnIndex("srcUserid"));
            String timeStamp = cursor.getString(cursor.getColumnIndex("timeStamp"));
            byte[] content = cursor.getBlob(cursor.getColumnIndex("content"));
            String recordTime = cursor.getString(cursor.getColumnIndex("recordTime"));

            msgDateBeans.add(new MsgDateBean(content, timeStamp, srcUserid, msgType, recordTime));
        }

        cursor.close();
        return msgDateBeans;
    }

    //所有属于和xx的对话都存储在 对应的friendId里面
    public static boolean insert(int msgType, int friendid, String timeStamp, byte[] content, String recordTime) {
        ContentValues values = new ContentValues();
        values.put("msgType", msgType);
        values.put("srcUserid", friendid);
        values.put("timeStamp", timeStamp);
        values.put("content", content);
        values.put("recordTime", recordTime);
        return db.insert(TABLE_NAME, null, values) != -1;
    }

    public static void delete(int srcUserid) {
        //删除条件
        String whereClause = "srcUserid=?";
        //删除条件参数
        String[] whereArgs = {String.valueOf(srcUserid)};
        //执行删除
        db.delete(TABLE_NAME, whereClause, whereArgs);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e(TAG, "onCreate: ");
        String sql = "CREATE TABLE " + TABLE_NAME + " " +
                "(msgType        INT    NOT NULL, " +
                " srcUserid      INT    NOT NULL, " +
                " timeStamp      CHAR(50), " +
                " content        BLOB," +
                " recordTime     CHAR(50))";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
