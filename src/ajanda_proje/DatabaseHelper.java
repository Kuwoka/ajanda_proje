package ajanda_proje;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {
    private static final String URL = "jdbc:mysql://localhost:3306/ajanda_proje";
    private static final String USER = "root";
    private static final String PASSWORD = "qZopRt01PutY";

 
    public static Connection connect() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void addTaskToDatabase(Task task) {
        String query = "INSERT INTO tasks (taskName, tarih, durum) VALUES (?, ?, ?)";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, task.getTaskname());
            stmt.setString(2, task.getTarih());
            stmt.setString(3, task.getDurum());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Task> getAllTasksFromDatabase() {
        List<Task> tasks = new ArrayList<>();
        String query = "SELECT * FROM tasks";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Task task = new Task(
                    rs.getInt("id"),
                    rs.getString("taskName"),
                    rs.getString("tarih"),
                    rs.getString("durum")
                );
                tasks.add(task);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }

    public void updateTaskInDatabase(Task task) {
        String query = "UPDATE tasks SET taskName = ?, tarih = ?, durum = ? WHERE id = ?";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, task.getTaskname());
            stmt.setString(2, task.getTarih());
            stmt.setString(3, task.getDurum());
            stmt.setInt(4, task.getid());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteTaskFromDatabase(int id) {
        String query = "DELETE FROM tasks WHERE id = ?";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public Task findTaskById(int id) {
        String query = "SELECT * FROM tasks WHERE id = ?";
        try (Connection conn = connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Task(
                        rs.getInt("id"),
                        rs.getString("taskName"),
                        rs.getString("tarih"),
                        rs.getString("durum")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
