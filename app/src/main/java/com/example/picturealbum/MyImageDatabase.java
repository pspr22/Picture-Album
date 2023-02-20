package com.example.picturealbum;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {MyImages.class},version = 1)
public abstract class MyImageDatabase extends RoomDatabase {

    private static MyImageDatabase instance;
    public abstract MyImagesDAO myImagesDAO();

    public static synchronized MyImageDatabase getInstance(Context context){

        if(instance == null){

            instance = Room.databaseBuilder(context.getApplicationContext()
            ,MyImageDatabase.class,"my_images_database")
                    .fallbackToDestructiveMigration()
                    .build();

        }

        return instance;

    }

}
