package com.example.room.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.room.Interfaces.NoteDao;
import com.example.room.Models.Note;

//1- define information out of class
@Database(entities = {Note.class},version = 1,exportSchema = false)

public abstract class DataSource extends RoomDatabase {
    // 2- define a static variable to store database name
    private static final String DATABASE_NAME = "Note_App";

    //3-Use SingleTone Pattern
    //3-1-create a local static variable of class own kind
    private static DataSource dataSource_instance;

    //4-create a public static synchronized method to get instance of database object and control it
    public static synchronized DataSource getInstance(Context context){
        //4-1 check for incantation of Class
        if (dataSource_instance == null){
            //4-2 Room.databaseBuilder has tow essential method (fallbackToDestructiveMigration & build)
            dataSource_instance = Room.databaseBuilder(context, DataSource.class, DATABASE_NAME)
            .fallbackToDestructiveMigration().build();
        }
        return dataSource_instance;
    }

    // At the end have to Define 'Abstract' Methods that return type is 'Dao' Type
    // All 'Dao' Classes must have declare here
    public abstract NoteDao noteDao();
}
