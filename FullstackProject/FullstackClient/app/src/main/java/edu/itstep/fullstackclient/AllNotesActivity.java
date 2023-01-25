package edu.itstep.fullstackclient;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import edu.itstep.fullstackclient.models.ConstantsStore;
import edu.itstep.fullstackclient.models.ContactAdapter;
import edu.itstep.fullstackclient.models.NetworkService;
import edu.itstep.fullstackclient.models.Note;
import edu.itstep.fullstackclient.models.NoteDataModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllNotesActivity extends AppCompatActivity {

    private NoteDataModel noteDataModel;
    private ContactAdapter adapter; // Створення змінної-адаптера (створений у окремому класі)

    // поля - змінні класу, що відповідають активним елементам Activity
    private ListView lvNotesList;
    private FloatingActionButton fabNewNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_notes);

        initView(); // ініціалізація даних
        setListener(); // підключення слухачів
        initData(); // ініціалізація первинних даних
        initAdapter(); // ініціалізація адаптера
    }

    private void initView() {
        lvNotesList = findViewById(R.id.lvNotesList);
        fabNewNote = findViewById(R.id.fabNewNote);
        //lvNotesList.setAdapter(adapter);
    }

    private void setListener() {
        fabNewNote.setOnClickListener(v -> {
            //Toast.makeText(this, "fabNewNote - pressed", Toast.LENGTH_SHORT).show();
            // Додавання елемента
            Intent intent = new Intent(this, NewNoteActivity.class);
            noteDataModel.setNoteId(-1); // обнулення id, що надійшов для редагування елемента (щоб не заважав при переході між іншими Актівіті)
            intent.putExtra(ConstantsStore.KEY_ALL_NOTES, noteDataModel);
            //intent.putExtra(ConstantsStore.KEY_NEW_NOTE, noteDataModel.getUserName());

            startActivity(intent);

        });

        // редагування елемента
        lvNotesList.setOnItemClickListener((parent, view, position, id) -> {
            //int noteId = getClickedItemId(position); // отримання id обраного елемента
            //noteDataModel.setNoteId(noteId); // передача id обраного елеменента до відповідного поля моделі
            noteDataModel.setNoteId(position); // передача позиції у колекції!!! обраного елеменента до відповідного поля моделі

            Intent intent = new Intent(this, NewNoteActivity.class);
            intent.putExtra(ConstantsStore.KEY_ALL_NOTES, noteDataModel); // приєднання моделі до об'єкта Intent для обміну з наступним Актівіті
            startActivity(intent);
        });

        // видалення елемента
        lvNotesList.setOnItemLongClickListener((parent, view, position, id) -> {
            //int noteId = noteDataModel.getNoteList().get(position).getId(); // отримання id обраного елемента
            int noteId = getClickedItemId(position); // отримання id обраного елемента

            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle("Do you want to delete the selected item?")
                    .setMessage("The element will be removed without the possibility of recovery")
                    .setPositiveButton("YES", (dialog, which) -> {
                        deleteNoteById(noteId, position);
                    })
                    .setNeutralButton("NO", (dialog, which) -> {
                        Toast.makeText(this, "The removal is canceled", Toast.LENGTH_SHORT).show();
                    });
            builder.show();

            //return false;
            return true; // у разі встановлення двох подій OnItemClick та OnItemLongClick - останній має повертати true, інакше при довгому натисканні спрацьовуватимуть обидві події
        });
    }

    private int getClickedItemId(int position) {
        return noteDataModel.getNoteList().get(position).getId();
    }

    // Алгоритм видалення елемента за id
    private void deleteNoteById(int noteId, int position) {
        //Toast.makeText(this, "noteId = " + noteId, Toast.LENGTH_SHORT).show();
        NetworkService
                .getInstance()
                .getNoteApi()
                .deleteNoteById(noteId)
                .enqueue(new Callback<Note>() {
                    @Override
                    public void onResponse(Call<Note> call, Response<Note> response) {
                        Toast.makeText(AllNotesActivity.this, "Note was deleted", Toast.LENGTH_SHORT).show();

                        noteDataModel.getNoteList().remove(position); // видалення елемента (на даний момент з БД він видалений) з колекції у моделі
                        adapter.notifyDataSetChanged(); // команда на оновлення адаптера у зв'язку зі зміною даних (видалено елемент)
                    }

                    @Override
                    public void onFailure(Call<Note> call, Throwable t) {
                        Toast.makeText(AllNotesActivity.this, "ERROR: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void initData() {
        // Завантаження даних з попереднього Актівіті - ім'я користувача та колекція елементів
        Intent intent = getIntent();
        noteDataModel = (NoteDataModel) intent.getSerializableExtra(ConstantsStore.KEY_ALL_NOTES);

        //Toast.makeText(this, "List size = " + noteDataModel.getNoteList().size(), Toast.LENGTH_SHORT).show();
    }

    private void initAdapter() {
        // Ініціалізація адаптера
        adapter = new ContactAdapter(
                this, // контекст
                R.layout.item_list, // шаблон відображення елемента
                noteDataModel.getNoteList() // колекція елементів
        );

        // Встановлення адаптера у список
        lvNotesList.setAdapter(adapter);
    }

}