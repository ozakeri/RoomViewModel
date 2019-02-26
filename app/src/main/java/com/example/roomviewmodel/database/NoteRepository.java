package com.example.roomviewmodel.database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.roomviewmodel.dao.NoteDao;
import com.example.roomviewmodel.model.Note;

import java.util.List;

public class NoteRepository {

    private NoteDao noteDao;
    private LiveData<List<Note>> allNote;

    public NoteRepository(Application application) {
        NoteDatabase database = NoteDatabase.getInstance(application.getApplicationContext());
        noteDao = database.noteDao();
        allNote = noteDao.getAllNote();
    }

    public void insert(Note note) {
        new InsertNoteAsyncTask(noteDao).execute(note);
    }

    public void update(Note note) {
        new UpdateNoteAsyncTask(noteDao).execute(note);
    }

    public void delete(Note note) {
        new DeleteNoteAsyncTask(noteDao).execute(note);
    }

    public void deleteAllNote() {
        new DeleteAllNotes(noteDao).execute();
    }

    public LiveData<List<Note>> getAllNote() {
        return allNote;
    }

    public class InsertNoteAsyncTask extends AsyncTask<Note, Void, Void> {

        NoteDao noteDao;

        public InsertNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.insert(notes[0]);
            return null;
        }
    }

    public class UpdateNoteAsyncTask extends AsyncTask<Note, Void, Void> {

        NoteDao noteDao;

        public UpdateNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.update(notes[0]);
            return null;
        }
    }

    public class DeleteNoteAsyncTask extends AsyncTask<Note, Void, Void> {

        NoteDao noteDao;

        public DeleteNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.delete(notes[0]);
            return null;
        }
    }

    public class DeleteAllNotes extends AsyncTask<Void, Void, Void> {

        NoteDao noteDao;

        public DeleteAllNotes(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.deleteAllNote();
            return null;
        }
    }
}
