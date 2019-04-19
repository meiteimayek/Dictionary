package me.gyanendrokh.meiteimayek.dictionary.database;

import java.util.List;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface WordDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insert(WordEntity... entity);
  
  @Query("SELECT * FROM word WHERE lang=:lang AND word_id=:wordId")
  WordEntity get(String lang, int wordId);

  @Query("SELECT * FROM word WHERE lang=:lang AND word LIKE :letter LIMIT 1")
  WordEntity getStart(String lang, String letter);
  
  @Query("SELECT * FROM word WHERE lang=:lang ORDER BY word_id ASC")
  List<WordEntity> getAll(String lang);

  @Query("SELECT * FROM word WHERE is_fav")
  DataSource.Factory<Integer, WordEntity> getFavs();

  @Query("SELECT * FROM word WHERE is_fav & lang=:lang")
  DataSource.Factory<Integer, WordEntity> getFavs(String lang);

  @Query("SELECT * FROM word WHERE lang=:lang AND word LIKE :query")
  DataSource.Factory<Integer, WordEntity> search(String lang, String query);
  
  @Query("SELECT * FROM word WHERE lang=:lang ORDER BY word_id ASC")
  DataSource.Factory<Integer, WordEntity> getAllPaged(String lang);
  
  @Update()
  void update(WordEntity... entity);

}
