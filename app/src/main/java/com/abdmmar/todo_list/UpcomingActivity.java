package com.abdmmar.todo_list;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class UpcomingActivity extends BaseActivity {
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
        mAdapter = new TodoAdapter(upcomingTodoList,  this);
        recyclerView.setAdapter(mAdapter);
        swiping(view);
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
}