package com.abdmmar.todo_list;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AddTodoListActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_todo_list);

        View view = findViewById(R.id.add_todo_view);
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