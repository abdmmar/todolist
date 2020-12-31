package com.abdmmar.todo_list;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class UpcomingActivity extends BaseActivity implements EditDialog.EditedTextListener {
    DatabaseHelper databaseHelper;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    List<Todo> upcomingTodoList = new ArrayList<>();
    Todo deletedTodo = null;

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

        //Swipe Gesture
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        swiping(view);
    }

    private void showUpcomingTodoList() {
        mAdapter = new TodoAdapter(upcomingTodoList,  this);
        recyclerView.setAdapter(mAdapter);
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            final int position = viewHolder.getAdapterPosition();

            switch (direction){
                case ItemTouchHelper.LEFT:
                    deletedTodo = upcomingTodoList.get(position);
                    String strDeletedTodo = upcomingTodoList.get(position).getTitle();
                    upcomingTodoList.remove(position);
                    databaseHelper.deleteTodoItem(deletedTodo);
                    mAdapter.notifyItemRemoved(position);
                    Snackbar.make(recyclerView, strDeletedTodo+" deleted", Snackbar.LENGTH_LONG)
                            .setAction("Undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    upcomingTodoList.add(position, deletedTodo);
                                    databaseHelper.addTodo(deletedTodo);
                                    mAdapter.notifyItemInserted(position);
                                }
                            }).show();
                    break;
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(UpcomingActivity.this, R.color.bgRedDelete))
                    .addSwipeLeftActionIcon(R.drawable.ic_delete_24)
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

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