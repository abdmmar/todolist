package com.abdmmar.todo_list;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

public class MainActivity extends BaseActivity implements PopupMenu.OnMenuItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View view = findViewById(R.id.main_view);
        swiping(view);
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