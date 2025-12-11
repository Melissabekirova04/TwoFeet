package main.java;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbManager {

    private Connection connection;

    public void connect() {
        try {
            String url = "jdbc:sqlite:identifier.sqlite";
            connection = DriverManager.getConnection(url);
            System.out.println("Connected to SQLite database.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void addUser(String name) {
        if (connection == null) connect();

        String sql = "INSERT OR IGNORE INTO todolists(username) VALUES (?)"; // OR IGNORE avoids duplicate errors
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.executeUpdate();
            System.out.println("User added: " + name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addTask(String task, String username) {
        if (connection == null) connect();

        Integer userId = getUserId(username);
        if (userId == null) {
            System.out.println("User not found: " + username);
            return;
        }

        String sql = "INSERT INTO todoLists(username, task) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, task);
            stmt.executeUpdate();
            System.out.println("Task added for userId=" + userId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<TaskEntry> getAllTasks() {
        List<TaskEntry> list = new ArrayList<>();

        String sql = "SELECT username, task FROM todolists";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new TaskEntry(
                        rs.getString("username"),
                        rs.getString("task")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<Task> getTasksForUser(String username) {
        if (connection == null) connect();
        List<Task> listForUser = new ArrayList<>();

        String sql = "SELECT * FROM todolists WHERE username = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, username);   // Værdi for ?

            try (ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {
                    listForUser.add(new Task(
                            rs.getInt("taskid"),
                            rs.getString("username"),
                            rs.getString("task")
                    ));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listForUser;
    }


    public record TaskEntry(String username, String task) {}


    public Integer getUserId(String username) {
        if (connection == null) connect();
        String sql = "SELECT taskid FROM todolists WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("taskid");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteTask(int taskId) {
        if (connection == null) connect();
        String sql = "DELETE FROM todolists WHERE taskid = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, taskId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}

