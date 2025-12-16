package main.java;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbManager {

    private Connection connection;

    /* ===================== CONNECTION ===================== */

    public void connect() {
        try {
            String url = "jdbc:sqlite:identifier.sqlite";
            connection = DriverManager.getConnection(url);
            ensureTables();
            System.out.println("Connected to SQLite");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void ensureTables() {
        String users = """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT UNIQUE,
                email TEXT UNIQUE,
                password_hash TEXT
            );
        """;

        String todos = """
            CREATE TABLE IF NOT EXISTS todolists (
                taskid INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER,
                task TEXT
            );
        """;

        String budgetPlans = """
            CREATE TABLE IF NOT EXISTS budget_plans (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER,
                period_type TEXT,
                income REAL,
                savings REAL,
                total_fixed REAL,
                days INTEGER,
                daily_budget REAL,
                start_date TEXT,
                end_date TEXT,
                created_at TEXT DEFAULT (datetime('now'))
            );
        """;

        String fixedExpenses = """
            CREATE TABLE IF NOT EXISTS fixed_expenses (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                plan_id INTEGER,
                name TEXT,
                amount REAL
            );
        """;

        try (Statement st = connection.createStatement()) {
            st.execute(users);
            st.execute(todos);
            st.execute(budgetPlans);
            st.execute(fixedExpenses);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* ===================== LOGIN ===================== */

    public int loginUser(String username, String passwordHash) {
        if (connection == null) connect();

        String sql = "SELECT id FROM users WHERE username = ? AND password_hash = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, passwordHash);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("id");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /* ===================== REGISTER ===================== */

    public int registerUser(String username, String email, String passwordHash) {
        if (connection == null) connect();

        String sql = "INSERT INTO users (username, email, password_hash) VALUES (?, ?, ?)";

        try (PreparedStatement stmt =
                     connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setString(3, passwordHash);
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /* ===================== TODO ===================== */

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

    public void deleteTask(int taskId) {
        if (connection == null) connect();

        String sql = "DELETE FROM todolists WHERE taskid = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, taskId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /* ===================== BUDGET ===================== */

    public int insertBudgetPlan(int userId, String periodType, double income,
                                double savings, double totalFixed, int days,
                                double dailyBudget, String startDate, String endDate) {

        if (connection == null) connect();

        String sql = """
            INSERT INTO budget_plans
            (user_id, period_type, income, savings, total_fixed, days,
             daily_budget, start_date, end_date)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (PreparedStatement stmt =
                     connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, userId);
            stmt.setString(2, periodType);
            stmt.setDouble(3, income);
            stmt.setDouble(4, savings);
            stmt.setDouble(5, totalFixed);
            stmt.setInt(6, days);
            stmt.setDouble(7, dailyBudget);
            stmt.setString(8, startDate);
            stmt.setString(9, endDate);

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void insertFixedExpense(int planId, String name, double amount) {
        if (connection == null) connect();

        String sql = "INSERT INTO fixed_expenses (plan_id, name, amount) VALUES (?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, planId);
            stmt.setString(2, name);
            stmt.setDouble(3, amount);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<BudgetPlan> getUserBudgetHistory(int userId) {
        if (connection == null) connect();

        List<BudgetPlan> history = new ArrayList<>();
        String sql = "SELECT * FROM budget_plans WHERE user_id = ? ORDER BY created_at DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                history.add(new BudgetPlan(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("period_type"),
                        rs.getDouble("income"),
                        rs.getDouble("savings"),
                        rs.getDouble("total_fixed"),
                        rs.getInt("days"),
                        rs.getDouble("daily_budget"),
                        rs.getString("start_date"),
                        rs.getString("end_date"),
                        rs.getString("created_at")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return history;
    }

    /* ===================== FIXED EXPENSES (RETTEDE) ===================== */

    public List<FixedExpense> getFixedExpenses(int planId) {
        if (connection == null) connect();

        List<FixedExpense> out = new ArrayList<>();
        String sql = "SELECT name, amount FROM fixed_expenses WHERE plan_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, planId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                out.add(new FixedExpense(
                        rs.getString("name"),
                        rs.getDouble("amount")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return out;
    }
}
