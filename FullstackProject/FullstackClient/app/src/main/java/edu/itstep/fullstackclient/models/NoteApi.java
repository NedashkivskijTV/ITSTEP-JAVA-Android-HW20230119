package edu.itstep.fullstackclient.models;

import java.util.List;

import edu.itstep.fullstackclient.models.Note;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface NoteApi {

    @GET("/notes")
    Call<List<Note>>getAllNotes();

    @GET("/notes/{id}")
    Call<Note>getNoteById(@Path("id") int id);

    @POST("/notes")
    Call<Note>saveNote(@Body Note note);

    @PUT("/notes")
    Call<Note>updateNote(@Body Note note);

    @DELETE("/notes/{id}")
    Call<Note>deleteNoteById(@Path("id") int id);

}
