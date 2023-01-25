package edu.itstep.fullstackclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences; // змінна для збереження userName в пам'яті телефону
    private SharedPreferences.Editor editor; // об'єкт для внесення змін у файл (запис даних)
    private String userName; // змінна для збереження даних, завантажених з пам'яті пристрою

    //List<Note> noteList = new ArrayList<>();
    private NoteDataModel noteDataModel;

    // поля - змінні класу, що відповідають активним елементам Activity
    private TextView tvNameLabel;
    private EditText etName;
    private Button btnSave;
    private Button btnNewNote;
    private Button btnAllNotes;
    private ProgressBar pbDownloadsData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView(); // ініціалізація даних
        setListener(); // підключення слухачів
        initData(); // ініціалізація первинних даних
    }

    // ініціалізація даних
    private void initView() {
        // отримання об'єкта класу, що імплементує інтерфейс SharedPreferences
        // sharedPreferences - об'єкт,  який даватиме доступ до файла
        // в параметри приймає:
        // 1 - назву файла, що створюватиметься у пам'яті телефона
        // 2 - (числове значення) режим роботи з файлом
        // - найчастіше застосовується режим MODE_PRIVATE - доступ до файла матиме лише поточний застосунок
        sharedPreferences = getSharedPreferences("settingsUser", MODE_PRIVATE);
        // ініціалізація змінної, що використовуватиметься для внесення змін у вищевказаний файл
        editor = sharedPreferences.edit();


        tvNameLabel = findViewById(R.id.tvNameLabel);
        etName = findViewById(R.id.etName);
        btnSave = findViewById(R.id.btnSave);
        btnNewNote = findViewById(R.id.btnNewNote);
        btnAllNotes = findViewById(R.id.btnAllNotes);
        pbDownloadsData = findViewById(R.id.pbDownloadsData);
    }

    // підключення слухачів
    private void setListener() {

        // Збереження значення userName
        btnSave.setOnClickListener(v -> {
            // приклад запису інф до файлу
            // порядок запису інф подібний до запису до Map -
            // використовується метод putНазваТипуДаних (putInt, putBoolean, ),
            // також можна покласти Set з рядків (унікальна колекція рядків)
            // у параметри приймає - рядок-унікальний ключ та саме значення, що потрібно зберегти
            editor.putString(ConstantsStore.KEY_USER_NAME, etName.getText().toString());

            // використовується алгоритм буферизації, який дозволяє підготувати
            // до запису значну кількість значень, а потім записати усе
            // викликавши один метод apply()
            editor.apply();

            userName = etName.getText().toString();
            //btnSave.setVisibility(View.INVISIBLE);

            // Код для програмного закриття клавіатури (інакше після збереження даних залишається відкритою)
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);

        });

        btnNewNote.setOnClickListener(v -> { // перехід на Актівіті для створення нового елемента
            //btnNewNote.setVisibility(View.INVISIBLE);
            if (userName.equals("")) {
                Toast.makeText(this, "You must enter the user name", Toast.LENGTH_SHORT).show();
            } else {

                Intent intent = new Intent(MainActivity.this, NewNoteActivity.class);
                noteDataModel.setUserName(userName);
                noteDataModel.setNoteId(-1); // обнулення id, що надійшов для редагування елемента (щоб не заважав при переході між іншими Актівіті)
                intent.putExtra(ConstantsStore.KEY_ALL_NOTES, noteDataModel);
                //intent.putExtra(ConstantsStore.KEY_NEW_NOTE, userName);

                startActivity(intent);

                //startActivity(new Intent(this, NewNoteActivity.class));
            }
        });

        btnAllNotes.setOnClickListener(v -> { // перехід до Актівіті демонстрації колекції елементів
            if (userName.equals("")) {
                Toast.makeText(this, "You must enter the user name", Toast.LENGTH_SHORT).show();
            } else {
                noteDataModel.setUserName(userName);
                showAllNotesActivity();
            }
        });

    }

    private void showAllNotesActivity() {
        pbDownloadsData.setVisibility(View.VISIBLE); // прогресбар - видимий

        // Алгоритм отримання колекції елементів
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

                        //etName.setText(noteDataModel.getNoteList().get(0).getNoteAuthor());

                        pbDownloadsData.setVisibility(View.INVISIBLE); // прогресбар - НЕвидимий

                        // Перевірка вмісту колекції - якщо колекція пуста нове Актівіті не запускається
                        if (noteDataModel.getNoteList().size() == 0) {
                            Toast.makeText(MainActivity.this, "There is no data at your request", Toast.LENGTH_SHORT).show();
                        } else {
                            //Toast.makeText(MainActivity.this, "NOT NULL", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(MainActivity.this, AllNotesActivity.class);
                            intent.putExtra(ConstantsStore.KEY_ALL_NOTES, noteDataModel);

                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Note>> call, Throwable t) {
                        pbDownloadsData.setVisibility(View.INVISIBLE); // прогресбар - НЕвидимий
                        Toast.makeText(MainActivity.this, "ERROR: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // ініціалізація первинних даних
    private void initData() {
        // Отримання даних з файлу
        // ініціалізація змінної count значенням, що лежить у відповідному,
        // щойноствореному файлі
        // використовується об'єкт sharedPreferences в якому викликається метод getInt - залежить від типу збережених даних
        // приймає параметри
        // - ключ
        // - значення за замовчуванням - якщо у файлі значення не буде,
        // повернеться саме значення за замовчуванням
        userName = sharedPreferences.getString(ConstantsStore.KEY_USER_NAME, "");

        etName.setText(userName);

        noteDataModel = new NoteDataModel();
    }

}