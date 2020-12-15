package com.abdmmar.todo_list;

import java.util.List;

public interface TodoList {
    boolean addTodo(Todo todo);

    boolean deleteTodoItem(Todo todo);

    boolean deleteTodoList(Todo todo);

    boolean updateTodoItem(Todo todo);

    boolean updateTodoListByDate(Todo todo);

    List<Todo> getAllTodo();

    List<Todo> getTodayTodo();

    List<Todo> getHistoryTodo();

    List<Todo> getUpcomingTodo();
}
