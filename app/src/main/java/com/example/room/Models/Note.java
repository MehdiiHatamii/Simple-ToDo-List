package com.example.room.Models;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Note") // Add Data Annotation 'Entity'
public class Note {
    @PrimaryKey(autoGenerate = true) //Generate AutoIncrement Field as PK
    public long id;
    public String noteText;
    public int isDone;

    //Model must have only one constructor
    public Note(String noteText, int isDone) {
        this.noteText = noteText;
        this.isDone = isDone;
        this.id = 0;
    }
}
