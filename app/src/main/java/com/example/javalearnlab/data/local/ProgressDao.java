package com.example.javalearnlab.data.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ProgressDao {
    @Insert
    long insert(PendingProgress progress);

    @Query("SELECT * FROM pending_progress ORDER BY timestamp ASC")
    List<PendingProgress> getAll();

    @Delete
    void delete(PendingProgress progress);

    @Query("DELETE FROM pending_progress WHERE id = :id")
    void deleteById(long id);
}