package com.example.di.lab1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by di on 2016-10-11.
 */
public class ChatDatabaseHelper extends SQLiteOpenHelper {

    public static final  String DATABASE_NAME = "Chats.db" ;
    public static final  int VERSION_NUM = 24;
    public static final String TABLE_NAME = "ChatMessages";
    public static final  String KEY_ID = "_id";
    public static final  String KEY_MESSAGE = "message";

    //Database creation sql statement
    private static final String DATABASE_CREATE = "create table " + TABLE_NAME + "( "
            + KEY_ID + " integer primary key autoincrement, " + KEY_MESSAGE
            + " text not null);";

    //Write a constructor that opens a database file “Chats.db”
    public ChatDatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) { //db is the database object
       // Log.i("ChatDatabaseHelper", DATABASE_CREATE);//TEST wether create a table or not
       db.execSQL(DATABASE_CREATE);
        Log.i("ChatDatabaseHelper", "Calling onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer){
        //will destroy all old data
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME );

        this.onCreate(db); //recreating the database

        Log.i("ChatDatabaseHelper", "Calling onUpgrade, oldVersion=" + oldVer + " newVersion=" + newVer);
    }

}
