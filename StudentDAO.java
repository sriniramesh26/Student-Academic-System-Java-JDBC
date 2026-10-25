import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Kongu Engineering College (Autonomous) - Regulation 2024
 * Student Data Access Object (StudentDAO)
 * Handles CRUD queries for Students table using JDBC PreparedStatements.
 */
public class StudentDAO {

    /**
     * Registers a new student in the database.
     */
    public boolean addStudent(Student student) throws SQLException {
        String sql = "INSERT INTO students (roll_no, name, department, semester) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, student.getRollNo().trim().toUpperCase());
            pstmt.setString(2, student.getName().trim());
            pstmt.setString(3, student.getDepartment().trim().toUpperCase());
            pstmt.setInt(4, student.getSemester());
            return pstmt.executeUpdate() > 0;
        }
    }

    /**
     * Retrieves a student by their Roll Number.
     */
    public Student getStudentByRollNo(String rollNo) throws SQLException {
        String sql = "SELECT roll_no, name, department, semester FROM students WHERE roll_no = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, rollNo.trim().toUpperCase());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Student(
                            rs.getString("roll_no"),
                            rs.getString("name"),
                            rs.getString("department"),
                            rs.getInt("semester")
                    );
                }
            }
        }
        return null;
    }

    /**
     * Retrieves all registered students.
     */
    public List<Student> getAllStudents() throws SQLException {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT roll_no, name, department, semester FROM students ORDER BY roll_no ASC";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Student(
                        rs.getString("roll_no"),
                        rs.getString("name"),
                        rs.getString("department"),
                        rs.getInt("semester")
                ));
            }
        }
        return list;
    }

    /**
     * Retrieves students belonging to a specific department.
     */
    public List<Student> getStudentsByDepartment(String department) throws SQLException {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT roll_no, name, department, semester FROM students WHERE department = ? ORDER BY roll_no ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, department.trim().toUpperCase());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new Student(
                            rs.getString("roll_no"),
                            rs.getString("name"),
                            rs.getString("department"),
                            rs.getInt("semester")
                    ));
                }
            }
        }
        return list;
    }

    /**
     * Retrieves distinct departments present in students table.
     */
    public List<String> getAllDepartments() throws SQLException {
        List<String> list = new ArrayList<>();
        String sql = "SELECT DISTINCT department FROM students ORDER BY department ASC";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(rs.getString("department"));
            }
        }
        return list;
    }

    /**
     * Deletes a student by Roll Number.
     */
    public boolean deleteStudent(String rollNo) throws SQLException {
        String sql = "DELETE FROM students WHERE roll_no = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, rollNo.trim().toUpperCase());
            return pstmt.executeUpdate() > 0;
        }
    }

    /**
     * Updates an existing student record.
     */
    public boolean updateStudent(Student student) throws SQLException {
        String sql = "UPDATE students SET name = ?, department = ?, semester = ? WHERE roll_no = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, student.getName().trim());
            pstmt.setString(2, student.getDepartment().trim().toUpperCase());
            pstmt.setInt(3, student.getSemester());
            pstmt.setString(4, student.getRollNo().trim().toUpperCase());
            return pstmt.executeUpdate() > 0;
        }
    }

    /**
     * Returns total registered student count.
     */
    public int getStudentCount() throws SQLException {
        String sql = "SELECT COUNT(*) FROM students";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
}
