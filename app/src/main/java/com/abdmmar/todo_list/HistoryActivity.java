package com.abdmmar.todo_list;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends BaseActivity implements EditDialog.EditedTextListener {
    DatabaseHelper databaseHelper;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    List<Todo> historyTodoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        recyclerView = findViewById(R.id.lv_historyTodoList);
        View view = findViewById(R.id.history_view);
        databaseHelper = new DatabaseHelper(HistoryActivity.this,
                "todolist.db", 1);
        historyTodoList = databaseHelper.getHistoryTodo();

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        showList();
        swiping(view);
    }

    private void showList() {
        mAdapter = new TodoAdapter(historyTodoList,  this);
        recyclerView.setAdapter(mAdapter);
    }

    public void swiping(View view){
        view.setOnTouchListener(new OnSwipeTouchListener(HistoryActivity.this) {
            @Override
            public void onSwipeLeft() {
                Intent intent = new Intent(HistoryActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransitionEnterSwipeLeft();
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransitionExitSwipeLeft();
    }

    @Override
    public void applyTexts(String editedText, int id) {
        Toast.makeText(this, ""+id, Toast.LENGTH_SHORT).show();
        Todo historyTodo = new Todo(id, editedText, "", false);
        databaseHelper.updateTodoItem(historyTodo);
        mAdapter.notifyItemChanged(3);
        showList();
    }
}