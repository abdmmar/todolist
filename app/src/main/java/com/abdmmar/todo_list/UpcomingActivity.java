package com.abdmmar.todo_list;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class UpcomingActivity extends BaseActivity implements EditDialog.EditedTextListener {
    DatabaseHelper databaseHelper;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    List<Todo> upcomingTodoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming);

        recyclerView = findViewById(R.id.lv_upcomingTodoList);
        View view = findViewById(R.id.upcoming_view);
        databaseHelper = new DatabaseHelper(UpcomingActivity.this,
                "todolist.db", 1);
        upcomingTodoList = databaseHelper.getUpcomingTodo();

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        showUpcomingTodoList();
        swiping(view);
    }

    private void showUpcomingTodoList() {
        mAdapter = new TodoAdapter(upcomingTodoList,  this);
        recyclerView.setAdapter(mAdapter);
    }

    public void swiping(View view){
        view.setOnTouchListener(new OnSwipeTouchListener(UpcomingActivity.this) {
            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
                Intent intent = new Intent(UpcomingActivity.this, AddTodoListActivity.class);
                startActivity(intent);
                overridePendingTransitionEnterSwipeRight();
                onBackPressed();
            }
        });
    }

    @Override
    public void applyTexts(int position, String editedText, String date, int id, boolean checked) {
        Todo upcomingTodo = new Todo(id, editedText, date, checked);
        databaseHelper.updateTodoItem(upcomingTodo);
        upcomingTodoList.set(position, upcomingTodo);
        mAdapter.notifyItemChanged(position);
        showUpcomingTodoList();
    }
}