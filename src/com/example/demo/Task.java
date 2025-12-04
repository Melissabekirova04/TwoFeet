package com.example.demo;

public class Task {
    private int id;
    private String username;
    private String task;

    public Task(int id, String username, String task) {
        this.id = id;
        this.username = username;
        this.task = task;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getTask() {
        return task;
    }

    @Override
    public String toString() {
        return username + ": " + task;
    }

}
