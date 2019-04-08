package me.gyanendrokh.meiteimayek.dictionary.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {WordEntity.class}, version = 1, exportSchema = false)
public abstract class WordDatabase extends RoomDatabase {

  private static WordDatabase mSelf = null;

  public static WordDatabase getInstance(Context context) {
    if (mSelf == null) {
      mSelf = Room.databaseBuilder(context.getApplicationContext(), WordDatabase.class,
        WordDatabase.class.getSimpleName()).build();
    }

    return mSelf;
  }

  public abstract WordDao getWordDao();

}
