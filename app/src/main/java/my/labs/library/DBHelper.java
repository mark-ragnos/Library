package my.labs.library;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name,factory,version);
    }

    final static public String TABLE_NAME = "booklist";

    final static public String COLUMN_ID = "_id";
    final static public String COLUMN_NAME = "name";
    final static public String COLUMN_AUTHOR = "author";
    final static public String COLUMN_DESCRIPTION = "description";

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table "+TABLE_NAME+" (" +
                COLUMN_ID +" integer primary key autoincrement, " +
                COLUMN_NAME +" text, " +
                COLUMN_AUTHOR +" text, " +
                COLUMN_DESCRIPTION +" text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
