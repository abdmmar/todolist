package com.abdmmar.todo_list;

public class Todo {
    private int todoId;
    private String title;
    private String date;
    private boolean checked = false;

    public Todo(int todoId, String title, String date, boolean checked) {
        this.todoId = todoId;
        this.title = title;
        this.date = date;
        this.checked = checked;
    }

    public int getTodoId() {
        return todoId;
    }

    public void setTodoId(int todoId) {
        this.todoId = todoId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = this.title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public String toString() {
        return "Todo{" +
                "id_list=" + todoId +
                ", name_todo='" + title + '\'' +
                ", date='" + date + '\'' +
                ", checked=" + checked +
                '}';
    }
}
