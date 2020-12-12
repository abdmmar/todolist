package com.abdmmar.todo_list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.MyViewHolder> {

    List<Todo> todoList;
    Context context;

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
        holder.c_item.setText(todoList.get(position).getName_todo());

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
                switch (item.getItemId()) {
                    case R.id.item_edit:
                        Toast.makeText(context, "Edit item with id: " + todoList.get(position).getId_list(), Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.item_delete:
                        Toast.makeText(context, "Delete item with id: " + todoList.get(position).getId_list(), Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                        return false;
                }
            }
        };
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

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
