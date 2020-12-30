package com.abdmmar.todo_list;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddTodoListActivity extends BaseActivity implements View.OnClickListener, EditDialog.EditedTextListener {
    private DatePicker datePicker;
    private Calendar calendar;
    private TextView tv_choosen_date;
    private int year, month, day;
    private String choosenDate;
    private int todoId = 1;
    private ImageButton imgbtn_choose_date,
            imgbtn_addUpcomingTodo,
            imgbtn_submitTodoList;
    private EditText et_addtodo;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    List<Todo> upcomingTodoList = new ArrayList<>();
    DatePickerDialog.OnDateSetListener setDateListener;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo_list);

        tv_choosen_date = findViewById(R.id.tv_choosen_date);
        imgbtn_choose_date = findViewById(R.id.imgbtn_choose_date);
        imgbtn_addUpcomingTodo = findViewById(R.id.imgbtn_addUpcomingTodo);
        imgbtn_submitTodoList = findViewById(R.id.imgbtn_submitTodoList);
        et_addtodo = findViewById(R.id.et_addUpcomingTodo);
        recyclerView = findViewById(R.id.lv_todayTodoList);
        View view = findViewById(R.id.add_todo_view);

        //Calendar
        calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR,1);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        choosenDate = year+"-"+(month+1)+"-"+day;

        //Set Date
        tv_choosen_date.setText(DateFormat
                .getDateInstance(DateFormat.MEDIUM)
                .format(calendar.getTime()));

        //Button onCLick
        imgbtn_choose_date.setOnClickListener(this);
        imgbtn_addUpcomingTodo.setOnClickListener(this);
        imgbtn_submitTodoList.setOnClickListener(this);

        //Calendar SetDate
        setDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                choosenDate = year+"-"+(month+1)+"-"+day;
                Toast.makeText(AddTodoListActivity.this, choosenDate, Toast.LENGTH_SHORT).show();
                tv_choosen_date.setText(DateFormat.getDateInstance(DateFormat.MEDIUM).format(calendar.getTime()));
            }
        };

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        showAddedUpcomingTodoList();

        swiping(view);
    }

    private void showAddedUpcomingTodoList() {
        mAdapter = new TodoAdapter(upcomingTodoList,  this);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imgbtn_choose_date:
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        AddTodoListActivity.this,
                        android.R.style.Theme_DeviceDefault_Dialog_MinWidth,
                        setDateListener, year, month, day);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()+24*60*60*1000);
                datePickerDialog.show();
                break;
            case R.id.imgbtn_addUpcomingTodo:
                String getTitle = et_addtodo.getText().toString();

                Todo upcomingTodo = new Todo(todoId, getTitle, choosenDate, false);
                upcomingTodoList.add(upcomingTodo);
                et_addtodo.setText("");
                todoId++;
                showAddedUpcomingTodoList();
                break;
            case R.id.imgbtn_submitTodoList:
                boolean success = true;
                databaseHelper = new DatabaseHelper(AddTodoListActivity.this,
                        "todolist.db", 1);
                for(Todo todoList : upcomingTodoList){
                    todoList.setDate(choosenDate);
                    success = databaseHelper.addTodo(todoList);
                }
                upcomingTodoList.clear();
                Toast.makeText(this, ""+success, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void swiping(View view){
        view.setOnTouchListener(new OnSwipeTouchListener(AddTodoListActivity.this) {
            @Override
            public void onSwipeLeft() {
                Intent intent = new Intent(AddTodoListActivity.this, UpcomingActivity.class);
                startActivity(intent);
                overridePendingTransitionEnterSwipeLeft();
            }

            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
                Intent intent = new Intent(AddTodoListActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransitionEnterSwipeRight();
            }
        });
    }

    @Override
    public void applyTexts(int position, String editedText, String date, int id, boolean checked) {
        Todo addTodo = new Todo(id, editedText, date, checked);
        upcomingTodoList.set(position, addTodo);
        mAdapter.notifyItemChanged(position);
        showAddedUpcomingTodoList();
    }
}