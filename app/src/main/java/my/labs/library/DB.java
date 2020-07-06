package my.labs.library;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DB {
    DBHelper dbHelper;
    Context context;
    SQLiteDatabase db;

    final static String DB_NAME = "LibDB";
    final static int DB_VERSION = 1;

    public DB(Context context){
        this.context = context;
    }

    public void open(){
        dbHelper = new DBHelper(context, DB_NAME, null, DB_VERSION);
        db = dbHelper.getWritableDatabase();
    }

    public void  close(){
        if(dbHelper != null)
            dbHelper.close();
    }
    public void addData(String name ,String author, String description){
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.COLUMN_NAME, name);
        cv.put(DBHelper.COLUMN_AUTHOR, author);
        cv.put(DBHelper.COLUMN_DESCRIPTION, description);
        db.insert(DBHelper.TABLE_NAME, null, cv);
    }

    public Cursor getAllData(){
        return db.query(DBHelper.TABLE_NAME, null, null, null, null, null, null);
    }

    public Cursor getDataByID(long id){
        return db.query(DBHelper.TABLE_NAME, null, DBHelper.COLUMN_ID +" = " + id, null, null, null, null);
    }

    public void removeByID(long id){
        db.delete(DBHelper.TABLE_NAME, DBHelper.COLUMN_ID + " = " + id, null);
    }

    public void update(long id, String name, String author, String description){
        ContentValues cv = new ContentValues();
        cv.put(DBHelper.COLUMN_NAME, name);
        cv.put(DBHelper.COLUMN_AUTHOR, author);
        cv.put(DBHelper.COLUMN_DESCRIPTION, description);
        db.update(DBHelper.TABLE_NAME, cv, DBHelper.COLUMN_ID + " = " + id, null);
    }
}
