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
            ensureTables();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void ensureTables() {
        // Beholder jeres eksisterende tabeller (users/todolists/budget) som I allerede har.
        // Opretter nye tabeller til budget-plan + faste udgifter:
        String createBudgetPlans = """
            CREATE TABLE IF NOT EXISTS budget_plans (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER NOT NULL,
                period_type TEXT NOT NULL,
                income REAL NOT NULL,
                savings REAL NOT NULL,
                total_fixed REAL NOT NULL,
                days INTEGER NOT NULL,
                daily_budget REAL NOT NULL,
                start_date TEXT NOT NULL,
                end_date TEXT NOT NULL,
                created_at TEXT DEFAULT (datetime('now'))
            );
        """;

        String createFixedExpenses = """
            CREATE TABLE IF NOT EXISTS fixed_expenses (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                plan_id INTEGER NOT NULL,
                name TEXT NOT NULL,
                amount REAL NOT NULL,
                FOREIGN KEY(plan_id) REFERENCES budget_plans(id) ON DELETE CASCADE
            );
        """;

        try (Statement st = connection.createStatement()) {
            st.execute(createBudgetPlans);
            st.execute(createFixedExpenses);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    // ---------- USERS ----------
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
            if (rs.next()) return rs.getInt(1);

        } catch (SQLException e) {
            if (e.getMessage() != null && e.getMessage().contains("UNIQUE")) {
                System.out.println("Username or email already exists");
            } else {
                e.printStackTrace();
            }
        }
        return -1;
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
            if (rs.next()) return rs.getInt("id");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // ---------- TODO ----------
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

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, taskId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ---------- OLD BUDGET ENTRIES (deposit/withdraw) ----------
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

    public void insertBudgetEntry(int userId, double amount, double balanceAfter) {
        if (connection == null) connect();

        String sql = "INSERT INTO budget (user_id, amount, balance_after) VALUES ( ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setDouble(2, amount);
            stmt.setDouble(3, balanceAfter);
            stmt.executeUpdate();
            System.out.println("Budget entry saved.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ---------- NEW: BUDGET PLAN + FIXED EXPENSES ----------
    public int insertBudgetPlan(int userId, String periodType, double income, double savings,
                                double totalFixed, int days, double dailyBudget,
                                String startDate, String endDate) {
        if (connection == null) connect();

        String sql = """
            INSERT INTO budget_plans
            (user_id, period_type, income, savings, total_fixed, days, daily_budget, start_date, end_date)
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

    public BudgetPlan getLatestBudgetPlan(int userId) {
        if (connection == null) connect();

        String sql = """
            SELECT *
            FROM budget_plans
            WHERE user_id = ?
            ORDER BY datetime(created_at) DESC
            LIMIT 1
        """;

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new BudgetPlan(
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
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<FixedExpense> getFixedExpenses(int planId) {
        if (connection == null) connect();

        List<FixedExpense> out = new ArrayList<>();
        String sql = "SELECT id, plan_id, name, amount FROM fixed_expenses WHERE plan_id = ? ORDER BY id DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, planId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                out.add(new FixedExpense(
                        rs.getInt("id"),
                        rs.getInt("plan_id"),
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
