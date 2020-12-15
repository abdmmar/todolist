package com.abdmmar.todo_list;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper implements TodoList{
    // Table and Column Name
    private static final String TODOLIST_TABLE = "TODOLIST";
    private static final String TODOID = "TODOID";
    private static final String TITLE = "TITLE";
    private static final String DATE = "DATE";
    private static final String CHECKED = "CHECKED";

    public DatabaseHelper(@Nullable Context context, @Nullable String name, int version) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TODOLIST_TABLE +
                " (" + TODOID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "" + TITLE + " TEXT NOT NULL, " + DATE + " TEXT NOT NULL, " +
                "" + CHECKED + " INTEGER NOT NULL)";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
        db.execSQL("DROP TABLE IF EXISTS " + TODOLIST_TABLE);
        onCreate(db);
    }

    public boolean addTodo(Todo todo){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(TITLE, todo.getTitle());
        cv.put(DATE, todo.getDate());
        cv.put(CHECKED, todo.isChecked());

        long insert = db.insert(TODOLIST_TABLE, null, cv);
        return insert != -1;
    }

    public boolean deleteTodoItem(Todo todo){
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + TODOLIST_TABLE +
                " WHERE "+ TODOID + " = " + todo.getTodoId();

        Cursor cursor = db.rawQuery(queryString, null);

        return cursor.moveToFirst();
    }

    public boolean deleteTodoList(Todo todo){
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + TODOLIST_TABLE +
                " WHERE "+ DATE + " = " + todo.getDate();

        Cursor cursor = db.rawQuery(queryString, null);

        return cursor.moveToFirst();
    }

    public boolean updateTodoItem(Todo todo){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        String[] todoId = new String[] { String.valueOf(todo.getTodoId()) };

        cv.put(TITLE, todo.getTitle());

        int result = db.update(TODOLIST_TABLE, cv, TODOID + " = ?", todoId);
        return result == 1;
    }

    public boolean updateTodoListByDate(Todo todo){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        String[] dateUpdate = new String[] { todo.getDate() };

        cv.put(DATE, todo.getDate());

        int result = db.update(TODOLIST_TABLE, cv, DATE + " = ?", dateUpdate);
        return result == 1;
    }

    public List<Todo> getAllTodo(){
        List<Todo> todoList = new ArrayList<>();
        String queryString = "SELECT * FROM " + TODOLIST_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        addTodoToList(todoList, cursor);

        cursor.close();
        db.close();

        return todoList;
    }

    public List<Todo> getTodayTodo(){
        List<Todo> todayTodoList = new ArrayList<>();
        String queryString = "SELECT * FROM " + TODOLIST_TABLE +
                " WHERE " + DATE + " = " + "date('now', 'localtime')";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        addTodoToList(todayTodoList, cursor);

        cursor.close();
        db.close();

        return todayTodoList;
    }

    public List<Todo> getHistoryTodo(){
        List<Todo> historyTodoList = new ArrayList<>();
        String queryString = "SELECT * FROM " + TODOLIST_TABLE +
                " WHERE " + DATE + " < " + "date('now', 'localtime')";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        addTodoToList(historyTodoList, cursor);

        cursor.close();
        db.close();

        return historyTodoList;
    }

    public List<Todo> getUpcomingTodo(){
        List<Todo> upcomingTodoList = new ArrayList<>();
        String queryString = "SELECT * FROM " + TODOLIST_TABLE +
                " WHERE " + DATE + " > " + "date('now', 'localtime')";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        addTodoToList(upcomingTodoList, cursor);

        cursor.close();
        db.close();

        return upcomingTodoList;
    }

    private void addTodoToList(List<Todo> todoList, Cursor cursor) {
        if (cursor.moveToFirst()) {
            do {
                int todoId = cursor.getInt(0);
                String title = cursor.getString(1);
                String date = cursor.getString(2);
                int checked = cursor.getInt(3);
                boolean isChecked = false;
                if (checked == 1) isChecked = true;

                Todo todo = new Todo(todoId, title, date, isChecked);
                todoList.add(todo);

            } while (cursor.moveToNext());
        }
    }
}
