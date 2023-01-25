package edu.itstep.fullstackclient.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import edu.itstep.fullstackclient.R; // ???

public class ContactAdapter extends ArrayAdapter<Note> {

    private int resource;
    private  List<Note> noteList;
    private LayoutInflater inflater;

    public ContactAdapter(@NonNull Context context, int resource, @NonNull List<Note> noteList) {
        super(context, resource, noteList);
        this.resource = resource;
        this.noteList = noteList;

        // Ініціалізація парсера
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // створення елементів списка за допомогою inflater
        // параметри
        // 1 - як саме виглядатиме елемент списка
        // 2 - інформація про батьківська група елементів - parent - контейнер для усіх елементів списка
        // надходить самостійно у параметрах методу
        // 3 - false (параметр вказує чи потрібно доєднувати корньовий елемент до батька-контейнера, зазвичай встановлюється false)
        // item - контейнер для розміщення елементів, що відображатимуться у кількості, що залежить від розміру колекції
        View item = inflater.inflate(resource, parent, false);

        // Отримання елементів xml-шаблона
        TextView tvNoteTitle = item.findViewById(R.id.tvNoteTitle);
        TextView tvNoteText = item.findViewById(R.id.tvNoteText);
        TextView tvNoteAuthor = item.findViewById(R.id.tvNoteAuthor);

        // отримання поточного елемента із загальної колекції за позицією
        Note note = noteList.get(position);

        // Передача потрібних даних для відображення у відповідних полях шаблона
        tvNoteTitle.setText(note.getNoteTitle());
        tvNoteText.setText(note.getNoteText());
        tvNoteAuthor.setText(note.getNoteAuthor());

        return item;
        //return super.getView(position, convertView, parent);
    }
}
