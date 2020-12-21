package com.abdmmar.todo_list;

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

import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.MyViewHolder>{

    List<Todo> todoList;
    Context context;
    private boolean isUndeleted = false;
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
        holder.imgbtn_moreOption.setOnClickListener(moreOption(position));
    }

    private View.OnClickListener moreOption(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //creating a popup menu
                PopupMenu popup = new PopupMenu(context, view);
                //inflating menu from xml resource
                popup.inflate(R.menu.actions);
                //adding click listener
                popup.setOnMenuItemClickListener(geItemtListener(position));
                //displaying the popup
                popup.show();

            }
        };
    }

    private PopupMenu.OnMenuItemClickListener geItemtListener(final int position) {
        return new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                databaseHelper = new DatabaseHelper(context,
                        "todolist.db", 1);

                switch (item.getItemId()) {
                    case R.id.item_edit:
                        openDialog(todoList.get(position).getTitle(), todoList.get(position).getTodoId());
                        return true;
                    case R.id.item_delete:
                        isUndeleted = databaseHelper.deleteTodoItem(todoList.get(position));
                        if (!isUndeleted){
                            todoList.remove(todoList.get(position));
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, todoList.size());
                            Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show();
                        } else{
                            Toast.makeText(context, "Item undeleted", Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    default:
                        return false;
                }
            }
        };
    }

    private void openDialog(String textFromItem, int id) {
        FragmentManager manager = ((AppCompatActivity)context).getSupportFragmentManager();
        EditDialog editDialog = new EditDialog(textFromItem, id);
        editDialog.show(manager, "Edit Dialog");
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

//    @Override
//    public void applyTexts(String editedText, int id) {
//        todoList.get(id).setTitle(editedText);
//    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        CheckBox c_item;
        ImageButton imgbtn_moreOption;
        LinearLayout parentLayout;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            c_item = itemView.findViewById(R.id.c_item);
            imgbtn_moreOption = itemView.findViewById(R.id.imgbtn_moreOption);
            parentLayout = itemView.findViewById(R.id.todo_item_layout);
        }
    }
}
