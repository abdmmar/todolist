package com.abdmmar.todo_list;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;
import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.MyViewHolder>{

    List<Todo> todoList;
    Context context;
    private boolean isUndeleted = false;
    private boolean checked = false;
    DatabaseHelper databaseHelper;

    public TodoAdapter(List<Todo> todoList, Context context) {
        this.todoList = todoList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_todo_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.c_item.setText(todoList.get(position).getTitle());
        holder.c_item.setChecked(todoList.get(position).isChecked());

        if (context instanceof  HistoryActivity){
            holder.c_item.setEnabled(false);
        }

        if (context instanceof UpcomingActivity || context instanceof HistoryActivity){
            holder.tv_dateItem.setText(todoList.get(position).getDate());
        }

        if(context instanceof MainActivity){
            holder.c_item.setOnClickListener(clickToCheck(position));
        } else {
            holder.c_item.setOnClickListener(null);
        }

        holder.c_item.setOnLongClickListener(longClickToEdit(position));
    }

    private View.OnLongClickListener longClickToEdit(final int position) {
        return new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                openDialog(position, todoList.get(position).getTitle(), todoList.get(position).getDate(), todoList.get(position).getTodoId(), todoList.get(position).isChecked());
                return false;
            }
        };
    }

    private View.OnClickListener clickToCheck(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseHelper = new DatabaseHelper(context, "todolist.db", 1);
                if (todoList.get(position).isChecked()){
                    todoList.get(position).setChecked(false);
                    checked = databaseHelper.updateTodoItem(todoList.get(position));
                } else {
                    todoList.get(position).setChecked(true);
                    checked = databaseHelper.updateTodoItem(todoList.get(position));
                }
                notifyItemChanged(position);
            }
        };
    }

    private void openDialog(int position, String textFromItem, String date, int id, boolean checked) {
        FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
        EditDialog editDialog = new EditDialog(position, textFromItem, date, id, checked);
        editDialog.show(manager, "Edit Dialog");
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        CheckBox c_item;
        TextView tv_dateItem;
        LinearLayout parentLayout;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            c_item = itemView.findViewById(R.id.c_item);
            tv_dateItem = itemView.findViewById(R.id.tv_dateItem);
            parentLayout = itemView.findViewById(R.id.todo_item_layout);
        }
    }
}
