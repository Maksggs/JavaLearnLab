package com.example.javalearnlab.data.local;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "pending_progress")
public class PendingProgress {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public String email;
    public String key;
    public String value; // "true" / "false"
    public long timestamp;

    public PendingProgress(String email, String key, String value) {
        this.email = email;
        this.key = key;
        this.value = value;
        this.timestamp = System.currentTimeMillis();
    }
}
