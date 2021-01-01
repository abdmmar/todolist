package com.abdmmar.todo_list;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class MainActivity extends BaseActivity implements View.OnClickListener, EditDialog.EditedTextListener{

    private static final String TAG = "Todo List App";
    private RecyclerView recyclerView;
    private TextView tv_date;
    private TextView tv_today;
    private Calendar calendar;
    private RecyclerView.Adapter mAdapter;
    private ImageButton imgbtn_addTodoToday;
    private int year, month, day, today;
    private String s_today;
    private EditText et_addtodo;
    private int todoId = 1;
    private String choosenDate;
    private RecyclerView.LayoutManager layoutManager;
    List<Todo> todoList = new ArrayList<>();
    DatePickerDialog.OnDateSetListener setDateListener;
    DatabaseHelper databaseHelper;
    Todo deletedTodo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_date = findViewById(R.id.tv_date);
        tv_today = findViewById(R.id.tv_today);
        imgbtn_addTodoToday = findViewById(R.id.imgbtn_addTodoToday);
        et_addtodo = findViewById(R.id.et_addtodo);
        recyclerView = findViewById(R.id.lv_todo_list);
        View view = findViewById(R.id.main_view);

        //Calendar
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        today = calendar.get(Calendar.DAY_OF_WEEK);
        setChoosenDate(year, month, day);

        getDay();

        //Set Date
        tv_date.setText(DateFormat
                .getDateInstance(DateFormat.MEDIUM)
                .format(calendar.getTime()));

        tv_today.setText(s_today);

        databaseHelper = new DatabaseHelper(MainActivity.this,
                "todolist.db", 1);
        todoList = databaseHelper.getTodayTodo();

        imgbtn_addTodoToday.setOnClickListener(this);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        showTodayTodoList();

        System.out.println(choosenDate);

        //Swipe Gesture
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        swiping(view);
    }

    private void setChoosenDate(int year, int month, int day) {
        String setMonth = ""+(month+1);
        String setDay = ""+day;
        if(month < 10){
            setMonth = "0"+(month+1);
        }

        if (day < 10){
            setDay = "0"+day;
        }

        choosenDate = year+"-"+setMonth+"-"+setDay;
    }

    private void showTodayTodoList() {
        mAdapter = new TodoAdapter(todoList,  this);
        recyclerView.setAdapter(mAdapter);
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            final int position = viewHolder.getAdapterPosition();

            switch (direction){
                case ItemTouchHelper.LEFT:
                    deletedTodo = todoList.get(position);
                    String strDeletedTodo = todoList.get(position).getTitle();
                    todoList.remove(position);
                    databaseHelper.deleteTodoItem(deletedTodo);
                    mAdapter.notifyItemRemoved(position);
                    Snackbar.make(recyclerView, strDeletedTodo+" deleted", Snackbar.LENGTH_LONG)
                            .setAction("Undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    todoList.add(position, deletedTodo);
                                    databaseHelper.addTodo(deletedTodo);
                                    mAdapter.notifyItemInserted(position);
                                }
                            }).show();
                    break;
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.bgRedDelete))
                    .addSwipeLeftActionIcon(R.drawable.ic_delete_24)
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imgbtn_addTodoToday:
                String getTitle = et_addtodo.getText().toString();

                Todo todoToday = new Todo(todoId, getTitle, choosenDate, false);
                todoList.add(todoToday);
                et_addtodo.setText("");
                todoId++;

                databaseHelper = new DatabaseHelper(MainActivity.this,
                        "todolist.db", 1);
                databaseHelper.addTodo(todoToday);

                showTodayTodoList();
                break;
        }
    }

    public void swiping(View view){
        view.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
            @Override
            public void onSwipeLeft() {
                Intent intent = new Intent(MainActivity.this, AddTodoListActivity.class);
                startActivity(intent);
                overridePendingTransitionEnterSwipeLeft();
            }

            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(intent);
                overridePendingTransitionEnterSwipeRight();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void getDay() {
        switch(today){
            case Calendar.MONDAY:
                s_today = "Senin";
                break;
            case Calendar.TUESDAY:
                s_today = "Selasa";
                break;
            case Calendar.WEDNESDAY:
                s_today = "Rabu";
                break;
            case Calendar.THURSDAY:
                s_today = "Kamis";
                break;
            case Calendar.FRIDAY:
                s_today = "Jumat";;
                break;
            case Calendar.SATURDAY:
                s_today = "Sabtu";;
                break;
            case Calendar.SUNDAY:
                s_today = "Ahad";;
                break;
        }
    }

    @Override
    public void applyTexts(int position, String editedText, String date, int id, boolean checked) {
        Todo todayTodo = new Todo(id, editedText, date, checked);
        databaseHelper.updateTodoItem(todayTodo);
        todoList.set(position, todayTodo);
        mAdapter.notifyItemChanged(position);
        showTodayTodoList();
    }
}