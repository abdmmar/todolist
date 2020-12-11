package com.abdmmar.todo_list;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HistoryActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        View view = findViewById(R.id.history_view);
        swiping(view);
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
}