package com.abc.daily.Objects;

import java.io.Serializable;

public class NoteObjects implements Serializable {

    public int id;
    public String noteTitle;
    public String noteContent;
    public String reminderDate;
    public String reminderTime;
    public String noteDate;
    public String noteModifyDate;

    public NoteObjects() {

    }

    public NoteObjects(int id, String noteTitle, String noteContent, String reminderDate, String reminderTime, String noteDate, String noteModifyDate) {
        this.id = id;
        this.noteTitle = noteTitle;
        this.noteContent = noteContent;
        this.reminderDate = reminderDate;
        this.reminderTime = reminderTime;
        this.noteDate = noteDate;
        this.noteModifyDate = noteModifyDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNoteContent() {
        return noteContent;
    }

    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }

    public String getReminderDate() {
        return reminderDate;
    }

    public void setReminderDate(String reminderDate) {
        this.reminderDate = reminderDate;
    }

    public String getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(String reminderTime) {
        this.reminderTime = reminderTime;
    }

    public String getNoteDate() {
        return noteDate;
    }

    public void setNoteDate(String noteDate) {
        this.noteDate = noteDate;
    }

    public String getNoteModifyDate() {
        return noteModifyDate;
    }

    public void setNoteModifyDate(String noteModifyDate) {
        this.noteModifyDate = noteModifyDate;
    }

    @Override
    public String toString() {
        return "NoteObjects{" +
                "id=" + id +
                ", noteTitle='" + noteTitle + '\'' +
                ", noteContent='" + noteContent + '\'' +
                ", reminderDate='" + reminderDate + '\'' +
                ", reminderTime='" + reminderTime + '\'' +
                ", noteDate='" + noteDate + '\'' +
                ", noteModifyDate='" + noteModifyDate + '\'' +
                '}';
    }
}
