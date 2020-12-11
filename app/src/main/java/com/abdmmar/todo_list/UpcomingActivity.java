package com.abdmmar.todo_list;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class UpcomingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming);

        View view = findViewById(R.id.upcoming_view);
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