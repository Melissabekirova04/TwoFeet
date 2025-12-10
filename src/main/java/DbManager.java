package main.java;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbManager {

    private Connection connection;
    private final String url = "jdbc:sqlite:identifier.sqlite";

    public DbManager() {
        // Lazy connect: connection is established on first use
    }
    private void ensureConnected() {
        if (connection != null) return;
        try {
            connection = DriverManager.getConnection(url);
            System.out.println("Connected to SQLite database. Working dir: " + new java.io.File(".").getAbsolutePath());
        } catch (SQLException e) {
            System.err.println("Failed to connect to DB: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Unable to connect to database", e);
        }
    }


    public Connection getConnection() {
        ensureConnected();
        return connection;
    }

    public void addUser(String name) {
        ensureConnected();

        String sql = "INSERT OR IGNORE INTO todolists(user) VALUES (?)"; // OR IGNORE avoids duplicate errors
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.executeUpdate();
            System.out.println("User added: " + name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addTask(String task, String username) {
ensureConnected();

        Integer userId = getUserId(username);
        if (userId == null) {
            System.out.println("User not found: " + username);
            return;
        }

        String sql = "INSERT INTO todolists(user, task) VALUES (?, ?)";
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
        ensureConnected();
        List<TaskEntry> list = new ArrayList<>();

        String sql = "SELECT user, task FROM todolists";

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
        ensureConnected();
        List<Task> listForUser = new ArrayList<>();

        String sql = "SELECT * FROM todolists WHERE user = ?";

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
        ensureConnected();
        String sql = "SELECT taskid FROM todolists WHERE user = ?";
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
        ensureConnected();
        String sql = "DELETE FROM todolists WHERE taskid = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, taskId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}

