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

    public int registerUser(String username, String email, String passwordHash) {
        if (connection == null) connect();

        String sql = """
        INSERT INTO users (username, email, password_hash)
        VALUES (?, ?, ?)
    """;

        try (PreparedStatement stmt =
                     connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setString(3, passwordHash);
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1); // user_id
            }

        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE")) {
                System.out.println("Username or email already exists");
            } else {
                e.printStackTrace();
            }
        }
        return -1;
    }


    public void addTodo(int userId, String task) {
        if (connection == null) connect();

        String sql = "INSERT INTO todolists (user_id, task) VALUES (?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, task);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Task> getTodos(int userId) {
        if (connection == null) connect();

        List<Task> todos = new ArrayList<>();
        String sql = "SELECT taskid, task FROM todolists WHERE user_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                todos.add(new Task(
                        rs.getInt("taskid"),
                        null,
                        rs.getString("task")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return todos;
    }



    public List<Budget> getBudget(int userId) {
        if (connection == null) connect();

        List<Budget> entries = new ArrayList<>();

        String sql = """
        SELECT amount, balance_after, timestamp
        FROM budget
        WHERE user_id = ?
        ORDER BY timestamp DESC
    """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                entries.add(new Budget(
                        rs.getDouble("amount"),
                        rs.getDouble("balance_after"),
                        rs.getString("timestamp")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return entries;
    }

    public int loginUser(String username, String passwordHash) {
        if (connection == null) connect();

        String sql = """
        SELECT id FROM users
        WHERE username = ? AND password_hash = ?
    """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, passwordHash);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // login failed
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

    public void insertBudgetEntry(int userId, double amount,  double balanceAfter) {
        if (connection == null) connect();

        String sql = "INSERT INTO budget (user_id, amount, balance_after) VALUES ( ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);        // user who is logged in
            stmt.setDouble(2, amount);     // deposit/withdraw amount
            stmt.setDouble(3, balanceAfter); // new balance

            stmt.executeUpdate();
            System.out.println("Budget entry saved.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}
