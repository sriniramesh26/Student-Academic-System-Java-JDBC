import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Kongu Engineering College (Autonomous) - Regulation 2024
 * DBConnection: JDBC Connection Management.
 * Supports SQLite (Default out-of-the-box embedded) and MySQL.
 */
public class DBConnection {
    private static Properties properties = new Properties();
    private static String dbType = "sqlite";
    private static String url;
    private static String user = "";
    private static String password = "";
    private static boolean initialized = false;

    static {
        loadConfiguration();
    }

    private static void loadConfiguration() {
        File propFile = new File("db.properties");
        if (propFile.exists()) {
            try (InputStream in = new FileInputStream(propFile)) {
                properties.load(in);
                dbType = properties.getProperty("db.type", "sqlite").trim().toLowerCase();
            } catch (Exception e) {
                System.err.println("[DBConnection] Warning: Could not read db.properties, defaulting to SQLite: " + e.getMessage());
                dbType = "sqlite";
            }
        } else {
            dbType = "sqlite";
        }

        if ("mysql".equals(dbType)) {
            String driver = properties.getProperty("mysql.driver", "com.mysql.cj.jdbc.Driver");
            url = properties.getProperty("mysql.url", "jdbc:mysql://localhost:3306/kec_academic_r2024?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true");
            user = properties.getProperty("mysql.user", "root");
            password = properties.getProperty("mysql.password", "root");
            try {
                Class.forName(driver);
            } catch (ClassNotFoundException e) {
                System.err.println("[DBConnection] MySQL driver not found. Falling back to SQLite.");
                dbType = "sqlite";
            }
        }

        if ("sqlite".equals(dbType)) {
            url = properties.getProperty("sqlite.url", "jdbc:sqlite:kec_academic_r2024.db");
            try {
                Class.forName("org.sqlite.JDBC");
            } catch (ClassNotFoundException e) {
                System.err.println("[DBConnection] SQLite driver not found on classpath: " + e.getMessage());
            }
        }
    }

    /**
     * Obtains a new JDBC database connection.
     */
    public static Connection getConnection() throws SQLException {
        Connection conn;
        if ("mysql".equals(dbType)) {
            conn = DriverManager.getConnection(url, user, password);
        } else {
            conn = DriverManager.getConnection(url);
            // Enable foreign key constraints in SQLite
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON;");
            }
        }

        if (!initialized) {
            initializeDatabase(conn);
            initialized = true;
        }

        return conn;
    }

    /**
     * Initializes tables and constraints if they do not exist.
     */
    public static synchronized void initializeDatabase(Connection conn) {
        try (Statement stmt = conn.createStatement()) {
            if ("sqlite".equals(dbType)) {
                stmt.execute("CREATE TABLE IF NOT EXISTS students (" +
                        "roll_no TEXT PRIMARY KEY, " +
                        "name TEXT NOT NULL, " +
                        "department TEXT NOT NULL, " +
                        "semester INTEGER NOT NULL CHECK (semester >= 1 AND semester <= 8), " +
                        "created_at DATETIME DEFAULT CURRENT_TIMESTAMP);");

                stmt.execute("CREATE TABLE IF NOT EXISTS courses (" +
                        "course_code TEXT PRIMARY KEY, " +
                        "title TEXT NOT NULL, " +
                        "credits INTEGER NOT NULL CHECK (credits > 0 AND credits <= 10), " +
                        "course_type TEXT NOT NULL CHECK (course_type IN ('THEORY', 'PRACTICAL', 'EMBEDDED')), " +
                        "created_at DATETIME DEFAULT CURRENT_TIMESTAMP);");

                stmt.execute("CREATE TABLE IF NOT EXISTS enrollments (" +
                        "enrollment_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "roll_no TEXT NOT NULL, " +
                        "course_code TEXT NOT NULL, " +
                        "semester INTEGER NOT NULL CHECK (semester >= 1 AND semester <= 8), " +
                        "total_classes INTEGER NOT NULL DEFAULT 0 CHECK (total_classes >= 0), " +
                        "attended_classes INTEGER NOT NULL DEFAULT 0 CHECK (attended_classes >= 0), " +
                        "medical_condonation INTEGER NOT NULL DEFAULT 0 CHECK (medical_condonation IN (0, 1)), " +
                        "enrolled_at DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                        "FOREIGN KEY (roll_no) REFERENCES students(roll_no) ON DELETE CASCADE, " +
                        "FOREIGN KEY (course_code) REFERENCES courses(course_code) ON DELETE CASCADE, " +
                        "UNIQUE (roll_no, course_code, semester));");

                stmt.execute("CREATE TABLE IF NOT EXISTS assessment_marks (" +
                        "mark_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "enrollment_id INTEGER NOT NULL UNIQUE, " +
                        "cia_1 REAL DEFAULT NULL, " +
                        "cia_2 REAL DEFAULT NULL, " +
                        "assignment_marks REAL DEFAULT NULL, " +
                        "ese_marks REAL DEFAULT NULL, " +
                        "is_absent_ese INTEGER NOT NULL DEFAULT 0 CHECK (is_absent_ese IN (0, 1)), " +
                        "updated_at DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                        "FOREIGN KEY (enrollment_id) REFERENCES enrollments(enrollment_id) ON DELETE CASCADE);");
            } else {
                // MySQL DDL
                stmt.execute("CREATE TABLE IF NOT EXISTS students (" +
                        "roll_no VARCHAR(20) PRIMARY KEY, " +
                        "name VARCHAR(100) NOT NULL, " +
                        "department VARCHAR(20) NOT NULL, " +
                        "semester INT NOT NULL, " +
                        "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP) ENGINE=InnoDB;");

                stmt.execute("CREATE TABLE IF NOT EXISTS courses (" +
                        "course_code VARCHAR(20) PRIMARY KEY, " +
                        "title VARCHAR(150) NOT NULL, " +
                        "credits INT NOT NULL, " +
                        "course_type VARCHAR(20) NOT NULL, " +
                        "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP) ENGINE=InnoDB;");

                stmt.execute("CREATE TABLE IF NOT EXISTS enrollments (" +
                        "enrollment_id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "roll_no VARCHAR(20) NOT NULL, " +
                        "course_code VARCHAR(20) NOT NULL, " +
                        "semester INT NOT NULL, " +
                        "total_classes INT NOT NULL DEFAULT 0, " +
                        "attended_classes INT NOT NULL DEFAULT 0, " +
                        "medical_condonation INT NOT NULL DEFAULT 0, " +
                        "enrolled_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                        "FOREIGN KEY (roll_no) REFERENCES students(roll_no) ON DELETE CASCADE, " +
                        "FOREIGN KEY (course_code) REFERENCES courses(course_code) ON DELETE CASCADE, " +
                        "UNIQUE KEY uq_stud_course_sem (roll_no, course_code, semester)) ENGINE=InnoDB;");

                stmt.execute("CREATE TABLE IF NOT EXISTS assessment_marks (" +
                        "mark_id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "enrollment_id INT NOT NULL UNIQUE, " +
                        "cia_1 DECIMAL(5,2) DEFAULT NULL, " +
                        "cia_2 DECIMAL(5,2) DEFAULT NULL, " +
                        "assignment_marks DECIMAL(5,2) DEFAULT NULL, " +
                        "ese_marks DECIMAL(5,2) DEFAULT NULL, " +
                        "is_absent_ese INT NOT NULL DEFAULT 0, " +
                        "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, " +
                        "FOREIGN KEY (enrollment_id) REFERENCES enrollments(enrollment_id) ON DELETE CASCADE) ENGINE=InnoDB;");
            }
        } catch (SQLException e) {
            System.err.println("[DBConnection] Schema initialization error: " + e.getMessage());
        }
    }

    /**
     * Resets all tables (Drops and recreates).
     */
    public static void resetDatabase() throws SQLException {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS assessment_marks;");
            stmt.execute("DROP TABLE IF EXISTS enrollments;");
            stmt.execute("DROP TABLE IF EXISTS courses;");
            stmt.execute("DROP TABLE IF EXISTS students;");
            initialized = false;
            initializeDatabase(conn);
        }
    }

    public static String getDatabaseType() {
        return dbType;
    }

    public static String getUrl() {
        return url;
    }

    public static void setDbType(String type) {
        dbType = type;
        loadConfiguration();
        initialized = false;
    }

    public static void close(AutoCloseable... closeables) {
        for (AutoCloseable c : closeables) {
            if (c != null) {
                try {
                    c.close();
                } catch (Exception ignored) {
                }
            }
        }
    }
}
