package edu.itstep.fullstackclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

import edu.itstep.fullstackclient.models.ConstantsStore;
import edu.itstep.fullstackclient.models.NetworkService;
import edu.itstep.fullstackclient.models.Note;
import edu.itstep.fullstackclient.models.NoteDataModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewNoteActivity extends AppCompatActivity {

    private NoteDataModel noteDataModel;
    //private String userName;
    private Note note;

    // поля - змінні класу, що відповідають активним елементам Activity
    private TextView tvNoteAuthor;
    private EditText etNoteTitle;
    private EditText etNoteText;
    private Button btnSave;
    private Button btnClear;
    private Button btnNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        initView(); // ініціалізація даних
        setListener(); // ініціалізація даних
        initData(); // ініціалізація первинних даних
    }

    // ініціалізація даних
    private void initView() {
        tvNoteAuthor = findViewById(R.id.tvNoteAuthor);
        etNoteTitle = findViewById(R.id.etNoteTitle);
        etNoteText = findViewById(R.id.etNoteText);
        btnSave = findViewById(R.id.btnSave);
        btnClear = findViewById(R.id.btnClear);
        btnNotes = findViewById(R.id.btnNotes);
    }

    // ініціалізація даних
    private void setListener() {
        btnSave.setOnClickListener(v -> {
            // Збереження новоствореного елемента
            // TODO
            String title = etNoteTitle.getText().toString().length() > 0 ? etNoteTitle.getText().toString() : etNoteTitle.getHint().toString();
            String text = etNoteText.getText().toString().length() > 0 ? etNoteText.getText().toString() : etNoteText.getHint().toString();

            note.setNoteTitle(title);
            note.setNoteText(text);
            //note.setNoteAuthor(userName);
            note.setNoteAuthor(noteDataModel.getUserName());

            // Алгоритм збереження / оновлення елемента (залежить від відсутності / наявності id)
            NetworkService
                    .getInstance()
                    .getNoteApi()
                    .saveNote(note)
                    .enqueue(new Callback<Note>() {
                @Override
                public void onResponse(Call<Note> call, Response<Note> response) {
                    note = response.body();

                    Toast.makeText(NewNoteActivity.this, "Note was saved", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<Note> call, Throwable t) {
                    Toast.makeText(NewNoteActivity.this, "ERROR: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        // Очищення текстових полів (окрім автора)
        btnClear.setOnClickListener(v -> {
            note = new Note();
            etNoteTitle.setText("");
            etNoteText.setText("");
        });

        // Запуск Актівіті для відображення колекції елементів, попередньо отримується колекція елементів
        btnNotes.setOnClickListener(v -> {

            NetworkService
                    .getInstance()
                    .getNoteApi()
                    .getAllNotes()
                    .enqueue(new Callback<List<Note>>() {
                        @Override
                        public void onResponse(Call<List<Note>> call, Response<List<Note>> response) {
                            // Отримання колекції з відповіді на запит
                            noteDataModel.setNoteList(response.body());
                            // Реверс колекції елементів - для відображення від найновішої до старіших (найновіший додається в колекцію останнім)
                            Collections.reverse(noteDataModel.getNoteList());

                            // Перевірка вмісту колекції - якщо колекція пуста нове Актівіті не запускається
                            if (noteDataModel.getNoteList().size() == 0) {
                                Toast.makeText(NewNoteActivity.this, "There is no data at your request", Toast.LENGTH_SHORT).show();
                            } else {
                                //Toast.makeText(MainActivity.this, "NOT NULL", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(NewNoteActivity.this, AllNotesActivity.class);
                                intent.putExtra(ConstantsStore.KEY_ALL_NOTES, noteDataModel);

                                startActivity(intent);
                            }

                        }

                        @Override
                        public void onFailure(Call<List<Note>> call, Throwable t) {
                            Toast.makeText(NewNoteActivity.this, "ERROR: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        });
    }

    // ініціалізація первинних даних
    private void initData() {
        note = new Note();

        // Завантаження даних з попереднього Актівіті - ім'я користувача та колекція елементів
        Intent intent = getIntent();
        noteDataModel = (NoteDataModel) intent.getSerializableExtra(ConstantsStore.KEY_ALL_NOTES);
        //userName = noteDataModel.getUserName();
        //userName = intent.getStringExtra(ConstantsStore.KEY_NEW_NOTE);

        tvNoteAuthor.setText(noteDataModel.getUserName());
        //tvNoteAuthor.setText(userName);
        //Toast.makeText(this, "user " + userName, Toast.LENGTH_SHORT).show();

        if(!(noteDataModel.getNoteId() < 0)){
            //Toast.makeText(this, "noteId = " + noteDataModel.getNoteId(), Toast.LENGTH_SHORT).show();
            note = noteDataModel.getNoteList().get(noteDataModel.getNoteId());
            etNoteTitle.setText(note.getNoteTitle());
            etNoteText.setText(note.getNoteText());
        }
        noteDataModel.setNoteId(-1); // обнулення id, що надійшов для редагування елемента (щоб не заважав при переході між іншими Актівіті)
    }
}