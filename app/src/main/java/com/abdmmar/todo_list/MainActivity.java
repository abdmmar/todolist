package com.abdmmar.todo_list;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends BaseActivity implements PopupMenu.OnMenuItemClickListener {

    private static final String TAG = "Todo List App";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    List<Todo> todoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fillTodoList();
        Log.d(TAG, "onCreate: " + todoList.toString());
        recyclerView = findViewById(R.id.lv_todo_list);
        View view = findViewById(R.id.main_view);
        swiping(view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        mAdapter = new TodoAdapter(todoList,  this);
        recyclerView.setAdapter(mAdapter);

    }

    private void fillTodoList() {
        Todo t0 = new Todo(0, "Sleep", "12-12-2020", false);
        Todo t1 = new Todo(1, "Play", "12-12-2020", false);
        Todo t2 = new Todo(2, "Read Books", "13-12-2020", false);

        todoList.addAll(Arrays.asList(t0, t1, t2));
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void showPopup(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.actions);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.item_edit:
                Toast.makeText(this, "Edit Pressed", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.item_delete:
                Toast.makeText(this, "Delete Pressed", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return false;
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
}