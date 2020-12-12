package com.abdmmar.todo_list;

public class Todo {
    private int id_list;
    private String name_todo;
    private String date;
    private boolean checked;

    public Todo(int id_list, String name_todo, String date, boolean checked) {
        this.id_list = id_list;
        this.name_todo = name_todo;
        this.date = date;
        this.checked = checked;
    }

    public int getId_list() {
        return id_list;
    }

    public void setId_list(int id_list) {
        this.id_list = id_list;
    }

    public String getName_todo() {
        return name_todo;
    }

    public void setName_todo(String name_todo) {
        this.name_todo = name_todo;
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
                "id_list=" + id_list +
                ", name_todo='" + name_todo + '\'' +
                ", date='" + date + '\'' +
                ", checked=" + checked +
                '}';
    }
}
