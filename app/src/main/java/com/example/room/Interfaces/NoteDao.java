package com.example.room.Interfaces;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.room.Models.Note;

import java.util.List;
// All Tables has unique 'Dao'
//This 'Dao' is only for NoteModel

@Dao //Data Annotation of 'Dao' interface
public interface NoteDao {
    //This Class is For operations on Model

    @Insert
    void insertNote(Note note); //Insert new Item to DB

    @Update
    void updateNote(Note note); //Update Selected Item

    @Delete
    void deleteNote(Note note); //Delete Selected Item

    @Query("Select * From note")
    List<Note> getAllNotes();   //Run Query after Calling this Method

    @Query("Select * From note Where id = :id")
    Note getNoteById(long id); // Run Query to select a note by Id


}
