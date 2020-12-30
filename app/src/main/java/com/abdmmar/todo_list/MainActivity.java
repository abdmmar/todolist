package com.abdmmar.todo_list;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener{

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
        choosenDate = year+"-"+(month+1)+"-"+day;

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

        swiping(view);
    }

    private void showTodayTodoList() {
        mAdapter = new TodoAdapter(todoList,  this);
        recyclerView.setAdapter(mAdapter);
    }

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
}