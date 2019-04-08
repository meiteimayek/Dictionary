package me.gyanendrokh.meiteimayek.dictionary.database;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import io.reactivex.Observable;

@Dao
public interface WordDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insert(WordEntity... entity);

  @Query("SELECT COUNT(*) FROM word WHERE lang=:lang")
  int count(String lang);

  @Query("SELECT * FROM word WHERE lang=:lang AND word_id=:wordId")
  WordEntity get(String lang, int wordId);

  @Query("SELECT * FROM word WHERE lang=:lang AND word LIKE :letter LIMIT 1")
  WordEntity getStart(String lang, String letter);

  @Query("SELECT * FROM word WHERE lang=:lang AND word_id >= :start AND word_id <= (:start - 1 + :size) ORDER BY word_id ASC LIMIT :size")
  List<WordEntity> getPositional(String lang, int start, int size);

  @Query("SELECT * FROM word WHERE lang=:lang ORDER BY word_id ASC")
  Observable<List<WordEntity>> getAll(String lang);

  @Query("SELECT * FROM word WHERE is_fav")
  Observable<List<WordEntity>> getFavs();

  @Query("SELECT * FROM word WHERE is_fav & lang=:lang")
  Observable<List<WordEntity>> getFavs(String lang);

  @Update()
  void update(WordEntity... entity);

  @Delete()
  void delete(WordEntity... entity);

  @Query("DELETE FROM word WHERE id=:id")
  void delete(int id);

}
