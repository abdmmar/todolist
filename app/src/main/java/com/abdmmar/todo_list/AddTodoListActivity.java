package com.abdmmar.todo_list;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;

public class AddTodoListActivity extends BaseActivity {
    private DatePicker datePicker;
    private Calendar calendar;
    private TextView tv_choosen_date;
    private int year, month, day;
    private String choosenDate;
    private ImageButton imgbtn_choose_date;
    DatePickerDialog.OnDateSetListener setDateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo_list);

        tv_choosen_date = findViewById(R.id.tv_choosen_date);
        imgbtn_choose_date = findViewById(R.id.imgbtn_choose_date);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        View view = findViewById(R.id.add_todo_view);
        tv_choosen_date.setText(DateFormat.getDateInstance(DateFormat.MEDIUM).format(calendar.getTime()));

        imgbtn_choose_date.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        AddTodoListActivity.this,
                        android.R.style.Theme_DeviceDefault_Dialog_MinWidth,
                        setDateListener, year, month, day);
                datePickerDialog.show();
            }
        });

        setDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                choosenDate = day+"-"+month+1+"-"+year;
                tv_choosen_date.setText(DateFormat.getDateInstance(DateFormat.MEDIUM).format(calendar.getTime()));
            }
        };

        swiping(view);
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
}