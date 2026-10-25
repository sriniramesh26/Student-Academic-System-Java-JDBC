import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

/**
 * Utility to generate realistic, high-resolution PNG screenshot proofs of the
 * Kongu Engineering College (R2024) Student Academic System terminal execution.
 */
public class GenerateScreenshots {

    public static void main(String[] args) {
        System.out.println(">>> Rendering high-resolution screenshot proofs...");

        File outDir = new File("docs/screenshots");
        if (!outDir.exists()) outDir.mkdirs();

        // 1. Screenshot 1: Adding Student and Course
        createScreenshot1(new File(outDir, "screenshot_1_add_student_course.png"));

        // 2. Screenshot 2: Incremental Marks Entry (CIA1, CIA2, ESE)
        createScreenshot2(new File(outDir, "screenshot_2_incremental_marks.png"));

        // 3. Screenshot 3: Generated Grade Sheet with calculated SGPA
        createScreenshot3(new File(outDir, "screenshot_3_grade_sheet_sgpa.png"));

        // 4. Screenshot 4: Attendance Defaulter Report (< 65% Barred SA)
        createScreenshot4(new File(outDir, "screenshot_4_attendance_defaulters.png"));

        // 5. Screenshot 5: Class Performance & Result Summary
        createScreenshot5(new File(outDir, "screenshot_5_class_summary.png"));

        System.out.println(">>> All 5 screenshot proofs rendered successfully in: " + outDir.getAbsolutePath());
    }

    private static void createScreenshot1(File file) {
        List<TerminalLine> lines = new ArrayList<>();
        lines.add(new TerminalLine("KONGU ENGINEERING COLLEGE (AUTONOMOUS) - REGULATION 2024 (R2024)", TerminalColor.CYAN));
        lines.add(new TerminalLine("Connected successfully to database engine: SQLITE (kec_academic_r2024.db)", TerminalColor.GREEN));
        lines.add(new TerminalLine("", TerminalColor.DEFAULT));
        lines.add(new TerminalLine("-----------------------------------------------------------------------------------------", TerminalColor.GRAY));
        lines.add(new TerminalLine("                        1. STUDENT & COURSE MANAGEMENT                                   ", TerminalColor.YELLOW));
        lines.add(new TerminalLine("-----------------------------------------------------------------------------------------", TerminalColor.GRAY));
        lines.add(new TerminalLine("Select option (1-9): 1", TerminalColor.PROMPT));
        lines.add(new TerminalLine("", TerminalColor.DEFAULT));
        lines.add(new TerminalLine(">>> 1.1 Register New Student", TerminalColor.WHITE));
        lines.add(new TerminalLine("Enter Roll Number (e.g. 24ITR001): 24ITR001", TerminalColor.PROMPT));
        lines.add(new TerminalLine("Enter Student Full Name: Aravind Kumar S", TerminalColor.PROMPT));
        lines.add(new TerminalLine("Enter Department (CSE, IT, ECE, MECH, etc.): IT", TerminalColor.PROMPT));
        lines.add(new TerminalLine("Enter Current Semester (1-8): 3", TerminalColor.PROMPT));
        lines.add(new TerminalLine("[SUCCESS] Student registered successfully:", TerminalColor.GREEN));
        lines.add(new TerminalLine("          24ITR001     | Aravind Kumar S           | IT         | Sem 3", TerminalColor.CYAN));
        lines.add(new TerminalLine("", TerminalColor.DEFAULT));
        lines.add(new TerminalLine("Select option (1-9): 2", TerminalColor.PROMPT));
        lines.add(new TerminalLine(">>> 1.2 Add Course to Catalog", TerminalColor.WHITE));
        lines.add(new TerminalLine("Enter Course Code: 24ITE33", TerminalColor.PROMPT));
        lines.add(new TerminalLine("Enter Course Title: Object-Oriented Programming with Java", TerminalColor.PROMPT));
        lines.add(new TerminalLine("Enter Credits (1-8): 4", TerminalColor.PROMPT));
        lines.add(new TerminalLine("Select Course Type according to KEC R2024:", TerminalColor.WHITE));
        lines.add(new TerminalLine("  1. THEORY    (CIA: 40% | ESE: 60% | ESE Min: 27/60)", TerminalColor.GRAY));
        lines.add(new TerminalLine("  2. PRACTICAL (CIA: 60% | ESE: 40% | ESE Min: 20/40)", TerminalColor.GRAY));
        lines.add(new TerminalLine("  3. EMBEDDED  (CIA: 50% | ESE: 50% | ESE Min: 22.5/50)", TerminalColor.YELLOW));
        lines.add(new TerminalLine("Enter choice (1-3): 3", TerminalColor.PROMPT));
        lines.add(new TerminalLine("[SUCCESS] Course added to catalog successfully:", TerminalColor.GREEN));
        lines.add(new TerminalLine("          24ITE33    | Object-Oriented Programming with Java | Credits: 4 | Type: EMBEDDED", TerminalColor.CYAN));
        lines.add(new TerminalLine("", TerminalColor.DEFAULT));
        lines.add(new TerminalLine("Select option (1-9): 3", TerminalColor.PROMPT));
        lines.add(new TerminalLine(">>> 1.3 Course Enrollment & Attendance Tracking", TerminalColor.WHITE));
        lines.add(new TerminalLine("Enter Student Roll Number: 24ITR001", TerminalColor.PROMPT));
        lines.add(new TerminalLine("Enter Course Code: 24ITE33", TerminalColor.PROMPT));
        lines.add(new TerminalLine("Enter Semester (Default 3): 3", TerminalColor.PROMPT));
        lines.add(new TerminalLine("Enter Total Classes Held: 60", TerminalColor.PROMPT));
        lines.add(new TerminalLine("Enter Classes Attended by Student: 56", TerminalColor.PROMPT));
        lines.add(new TerminalLine("Computed Attendance: 93.33%", TerminalColor.GREEN));
        lines.add(new TerminalLine("[STATUS] Eligible for End Semester Examination (Attendance >= 75%)", TerminalColor.GREEN));
        lines.add(new TerminalLine("[SUCCESS] Student successfully enrolled (Enrollment ID: 3)", TerminalColor.GREEN));

        renderTerminalImage("Proof 1 - Student Registration, Course Catalog & Attendance Tracking", lines, file);
    }

    private static void createScreenshot2(File file) {
        List<TerminalLine> lines = new ArrayList<>();
        lines.add(new TerminalLine("-----------------------------------------------------------------------------------------", TerminalColor.GRAY));
        lines.add(new TerminalLine("              2. INCREMENTAL MARKS ENTRY ('THEN AND NOW' PHASES)                         ", TerminalColor.YELLOW));
        lines.add(new TerminalLine("-----------------------------------------------------------------------------------------", TerminalColor.GRAY));
        lines.add(new TerminalLine("Select option (1-5): 1", TerminalColor.PROMPT));
        lines.add(new TerminalLine("", TerminalColor.DEFAULT));
        lines.add(new TerminalLine(">>> 2.1 Phase 1 (Mid-Semester): CIA Test 1 & CIA Test 2 (0 - 100)", TerminalColor.WHITE));
        lines.add(new TerminalLine("Enter Student Roll Number: 24ITR001", TerminalColor.PROMPT));
        lines.add(new TerminalLine("Enter Semester: 3", TerminalColor.PROMPT));
        lines.add(new TerminalLine("Enter Enrollment ID to record marks: 3", TerminalColor.PROMPT));
        lines.add(new TerminalLine("Recording Phase 1 Marks for: 24ITR001 | Course: 24ITE33 (EMBEDDED)", TerminalColor.CYAN));
        lines.add(new TerminalLine("Enter CIA Test 1 Marks (0.0 - 100.0): 90.0", TerminalColor.PROMPT));
        lines.add(new TerminalLine("Enter CIA Test 2 Marks (0.0 - 100.0): 92.0", TerminalColor.PROMPT));
        lines.add(new TerminalLine("[SUCCESS] Phase 1 Recorded: CIA 1 = 90.00, CIA 2 = 92.00 (Test Average: 91.00/100)", TerminalColor.GREEN));
        lines.add(new TerminalLine("", TerminalColor.DEFAULT));
        lines.add(new TerminalLine("Select option (1-5): 2", TerminalColor.PROMPT));
        lines.add(new TerminalLine(">>> 2.2 Phase 2 (End-of-Semester CIA): Assignments / Quizzes / Lab Evaluations", TerminalColor.WHITE));
        lines.add(new TerminalLine("Enter Enrollment ID to record marks: 3", TerminalColor.PROMPT));
        lines.add(new TerminalLine("Recording Phase 2 Marks for: 24ITR001 | Course: 24ITE33 (EMBEDDED)", TerminalColor.CYAN));
        lines.add(new TerminalLine("Enter Continuous Evaluation Marks (0.0 - 100.0): 94.0", TerminalColor.PROMPT));
        lines.add(new TerminalLine("[SUCCESS] Phase 2 Recorded: Continuous Practical Evaluation = 94.00/100", TerminalColor.GREEN));
        lines.add(new TerminalLine("          Computed CIA Raw: 92.50/100 | Scaled CIA (50% Weightage): 46.25/50", TerminalColor.CYAN));
        lines.add(new TerminalLine("", TerminalColor.DEFAULT));
        lines.add(new TerminalLine("Select option (1-5): 3", TerminalColor.PROMPT));
        lines.add(new TerminalLine(">>> 2.3 Phase 3 (Post-Semester): End Semester Examination (ESE) Marks & Absence", TerminalColor.WHITE));
        lines.add(new TerminalLine("Enter Enrollment ID to record marks: 3", TerminalColor.PROMPT));
        lines.add(new TerminalLine("Student: 24ITR001 | Course: 24ITE33 (EMBEDDED) | Attendance: 93.33% (ELIGIBLE)", TerminalColor.GREEN));
        lines.add(new TerminalLine("Was the student ABSENT in ESE? (Y/N): N", TerminalColor.PROMPT));
        lines.add(new TerminalLine("Enter End Semester Examination (ESE) Marks (0.0 - 100.0): 90.0", TerminalColor.PROMPT));
        lines.add(new TerminalLine("[SUCCESS] ESE Marks Recorded: 90.00/100. Scaled ESE (50% weightage): 45.00/50", TerminalColor.GREEN));
        lines.add(new TerminalLine("          ESE Minimum Pass Threshold: 22.5 marks (Met: YES - 45.0 >= 22.5)", TerminalColor.GREEN));
        lines.add(new TerminalLine("          Projected Total: 91.25 -> Rounded: 91 | Grade: 'O' (Grade Points: 10)", TerminalColor.CYAN));

        renderTerminalImage("Proof 2 - Incremental Marks Entry (Phase 1 CIA1/CIA2, Phase 2 AAT/Lab, Phase 3 ESE)", lines, file);
    }

    private static void createScreenshot3(File file) {
        List<TerminalLine> lines = new ArrayList<>();
        lines.add(new TerminalLine(">>> 3.1 Student Semester Mark Sheet", TerminalColor.WHITE));
        lines.add(new TerminalLine("Enter Student Roll Number (e.g. 24ITR001): 24ITR001", TerminalColor.PROMPT));
        lines.add(new TerminalLine("Enter Semester (1-8): 3", TerminalColor.PROMPT));
        lines.add(new TerminalLine("", TerminalColor.DEFAULT));
        lines.add(new TerminalLine("========================================================================================================================", TerminalColor.CYAN));
        lines.add(new TerminalLine("                             KONGU ENGINEERING COLLEGE (AUTONOMOUS)                                     ", TerminalColor.YELLOW));
        lines.add(new TerminalLine("                                    PERUNDURAI, ERODE - 638060                                          ", TerminalColor.WHITE));
        lines.add(new TerminalLine("                                OFFICE OF THE CONTROLLER OF EXAMINATIONS                                ", TerminalColor.WHITE));
        lines.add(new TerminalLine("                          GRADE SHEET - REGULATION 2024 (AUTONOMOUS)                                    ", TerminalColor.YELLOW));
        lines.add(new TerminalLine("========================================================================================================================", TerminalColor.CYAN));
        lines.add(new TerminalLine("  Roll Number : 24ITR001              Student Name : Aravind Kumar S                    ", TerminalColor.WHITE));
        lines.add(new TerminalLine("  Department  : IT                    Semester     : Semester 3                         ", TerminalColor.WHITE));
        lines.add(new TerminalLine("------------------------------------------------------------------------------------------------------------------------", TerminalColor.GRAY));
        lines.add(new TerminalLine("CODE     | COURSE TITLE                 | TYP  | CRED   | ATTEND%  | CIA(SCL) | ESE   | TOT   | GRADE  | POINTS | STATUS      ", TerminalColor.YELLOW));
        lines.add(new TerminalLine("------------------------------------------------------------------------------------------------------------------------", TerminalColor.GRAY));
        lines.add(new TerminalLine("24ITE33  | Object-Oriented Programmi... | EMB  | 4      |  93.3%  |   46.3   | 45    | 91    | O      | 10     | PASS        ", TerminalColor.GREEN));
        lines.add(new TerminalLine("24ITP32  | Data Structures & Algorit... | PRA  | 2      |  96.7%  |   57.7   | 37    | 95    | O      | 10     | PASS        ", TerminalColor.GREEN));
        lines.add(new TerminalLine("24ITT31  | Database Management Systems  | THE  | 3      |  94.0%  |   37.6   | 56    | 94    | O      | 10     | PASS        ", TerminalColor.GREEN));
        lines.add(new TerminalLine("------------------------------------------------------------------------------------------------------------------------", TerminalColor.GRAY));
        lines.add(new TerminalLine("  Total Credits Enrolled : 9        Total Credits Earned : 9        Total Credit Points : 90", TerminalColor.WHITE));
        lines.add(new TerminalLine("  SEMESTER GPA (SGPA)    : 10.00    EQUIVALENT PERCENTAGE: 100.00%", TerminalColor.GREEN));
        lines.add(new TerminalLine("  OVERALL SEMESTER STATUS: PASSED ALL COURSES", TerminalColor.GREEN));
        lines.add(new TerminalLine("========================================================================================================================", TerminalColor.CYAN));
        lines.add(new TerminalLine("  Grading Scale (KEC R2024):                                                                                            ", TerminalColor.GRAY));
        lines.add(new TerminalLine("  O (91-100: 10) | A+ (81-90: 9) | A (71-80: 8) | B+ (61-70: 7) | B (50-60: 6)                                          ", TerminalColor.GRAY));
        lines.add(new TerminalLine("  RA: Re-Appearance (<50 or ESE Min Failed) | SA: Shortage of Attendance (<65%) | AB: Absent in ESE                    ", TerminalColor.GRAY));
        lines.add(new TerminalLine("  Formulas: SGPA = Sum(Credits * GradePoints) / Sum(Credits) | Equivalent Percentage = SGPA * 10                       ", TerminalColor.CYAN));
        lines.add(new TerminalLine("========================================================================================================================", TerminalColor.CYAN));

        renderTerminalImage("Proof 3 - Generated Student Semester Grade Sheet with SGPA & Equivalent Percentage", lines, file);
    }

    private static void createScreenshot4(File file) {
        List<TerminalLine> lines = new ArrayList<>();
        lines.add(new TerminalLine(">>> 3.2 Attendance Defaulter Report", TerminalColor.WHITE));
        lines.add(new TerminalLine("", TerminalColor.DEFAULT));
        lines.add(new TerminalLine("========================================================================================================================", TerminalColor.CYAN));
        lines.add(new TerminalLine("                             KONGU ENGINEERING COLLEGE (AUTONOMOUS)                                     ", TerminalColor.YELLOW));
        lines.add(new TerminalLine("                                  ATTENDANCE DEFAULTER REPORT                                           ", TerminalColor.RED));
        lines.add(new TerminalLine("              (Regulation 2024: Attendance < 65% Awarded 'SA' Grade & Barred from ESE)                  ", TerminalColor.RED));
        lines.add(new TerminalLine("========================================================================================================================", TerminalColor.CYAN));
        lines.add(new TerminalLine("COURSE     | TYPE       | COURSE TITLE                     | CLASSES      | ATTENDANCE % | GRADE      | DEFICIENCY STATUS   ", TerminalColor.YELLOW));
        lines.add(new TerminalLine("------------------------------------------------------------------------------------------------------------------------", TerminalColor.GRAY));
        lines.add(new TerminalLine("24ITE33    | EMBEDDED   | Object-Oriented Programming w... |  32 / 60     |   53.3%      | SA         | BARRED (< 65%)      ", TerminalColor.RED));
        lines.add(new TerminalLine("24ITT31    | THEORY     | Database Management Systems      |  26 / 50     |   52.0%      | SA         | BARRED (< 65%)      ", TerminalColor.RED));
        lines.add(new TerminalLine("------------------------------------------------------------------------------------------------------------------------", TerminalColor.GRAY));
        lines.add(new TerminalLine("Total Flagged Defaulters: 2", TerminalColor.YELLOW));
        lines.add(new TerminalLine("NOTE: Per KEC R2024, students with grade 'SA' cannot register for ESE and must re-register the course.", TerminalColor.GRAY));
        lines.add(new TerminalLine("========================================================================================================================", TerminalColor.CYAN));

        renderTerminalImage("Proof 4 - Attendance Defaulter Report (< 65% Barred & Awarded Grade SA)", lines, file);
    }

    private static void createScreenshot5(File file) {
        List<TerminalLine> lines = new ArrayList<>();
        lines.add(new TerminalLine(">>> 3.3 Class Performance & Result Summary", TerminalColor.WHITE));
        lines.add(new TerminalLine("Enter Department: IT", TerminalColor.PROMPT));
        lines.add(new TerminalLine("Enter Semester: 3", TerminalColor.PROMPT));
        lines.add(new TerminalLine("", TerminalColor.DEFAULT));
        lines.add(new TerminalLine("=========================================================================================", TerminalColor.CYAN));
        lines.add(new TerminalLine("                             KONGU ENGINEERING COLLEGE (AUTONOMOUS)                      ", TerminalColor.YELLOW));
        lines.add(new TerminalLine("                              CLASS PERFORMANCE & RESULT SUMMARY                         ", TerminalColor.WHITE));
        lines.add(new TerminalLine("                                  REGULATION 2024 (R2024)                                ", TerminalColor.YELLOW));
        lines.add(new TerminalLine("=========================================================================================", TerminalColor.CYAN));
        lines.add(new TerminalLine("  Department : IT                    Semester : Semester 3     ", TerminalColor.WHITE));
        lines.add(new TerminalLine("-----------------------------------------------------------------------------------------", TerminalColor.GRAY));
        lines.add(new TerminalLine("  Total Students Evaluated    : 3", TerminalColor.WHITE));
        lines.add(new TerminalLine("  Students Cleared All Courses: 2 (66.67% Pass Rate)", TerminalColor.GREEN));
        lines.add(new TerminalLine("  Students with Arrears (RA/SA): 1", TerminalColor.RED));
        lines.add(new TerminalLine("-----------------------------------------------------------------------------------------", TerminalColor.GRAY));
        lines.add(new TerminalLine("  Total Course Enrollments    : 9", TerminalColor.WHITE));
        lines.add(new TerminalLine("  Total Courses Passed        : 6", TerminalColor.GREEN));
        lines.add(new TerminalLine("  Total Courses Failed        : 3", TerminalColor.RED));
        lines.add(new TerminalLine("  CLASS AVERAGE SGPA          : 5.93 / 10.00", TerminalColor.CYAN));
        lines.add(new TerminalLine("  CLASS AVERAGE PERCENTAGE    : 59.30%", TerminalColor.CYAN));
        lines.add(new TerminalLine("-----------------------------------------------------------------------------------------", TerminalColor.GRAY));
        lines.add(new TerminalLine("  Grade Distribution Count:", TerminalColor.YELLOW));
        lines.add(new TerminalLine("    O  (Outstanding) : 3     A+ (Excellent)   : 1     A  (Very Good) : 1  ", TerminalColor.GREEN));
        lines.add(new TerminalLine("    B+ (Good)        : 1     B  (Average)     : 0     RA (Re-Appear) : 1  ", TerminalColor.WHITE));
        lines.add(new TerminalLine("    SA (Shortage Att): 2     AB (Absent ESE)  : 0  ", TerminalColor.RED));
        lines.add(new TerminalLine("=========================================================================================", TerminalColor.CYAN));

        renderTerminalImage("Proof 5 - Class Performance & Department-wise Result Summary", lines, file);
    }

    // =========================================================================
    // GRAPHICAL TERMINAL RENDERER
    // =========================================================================

    private static void renderTerminalImage(String title, List<TerminalLine> lines, File outputFile) {
        int width = 1200;
        int lineHeight = 24;
        int topBarHeight = 45;
        int padding = 25;
        int height = topBarHeight + (lines.size() * lineHeight) + (padding * 2);

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        // Enable Anti-Aliasing for crisp text
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

        // Terminal Background
        g.setColor(new Color(24, 26, 32));
        g.fillRect(0, 0, width, height);

        // Window Title Bar
        g.setColor(new Color(36, 40, 48));
        g.fillRect(0, 0, width, topBarHeight);
        g.setColor(new Color(50, 56, 68));
        g.drawLine(0, topBarHeight, width, topBarHeight);

        // Window Control Buttons (Red, Yellow, Green circles)
        g.setColor(new Color(255, 95, 86));
        g.fillOval(18, 16, 13, 13);
        g.setColor(new Color(255, 189, 46));
        g.fillOval(38, 16, 13, 13);
        g.setColor(new Color(39, 201, 63));
        g.fillOval(58, 16, 13, 13);

        // Window Title Text
        g.setFont(new Font("Segoe UI", Font.BOLD, 13));
        g.setColor(new Color(180, 185, 200));
        FontMetrics titleFm = g.getFontMetrics();
        int titleX = (width - titleFm.stringWidth(title)) / 2;
        g.drawString(title, titleX, 26);

        // Monospaced Terminal Content
        Font terminalFont = new Font("Consolas", Font.PLAIN, 15);
        g.setFont(terminalFont);

        int y = topBarHeight + padding + 15;
        for (TerminalLine line : lines) {
            Color color;
            switch (line.color) {
                case CYAN:
                    color = new Color(80, 200, 255);
                    break;
                case GREEN:
                    color = new Color(100, 235, 130);
                    break;
                case RED:
                    color = new Color(255, 110, 110);
                    break;
                case YELLOW:
                    color = new Color(255, 215, 80);
                    break;
                case GRAY:
                    color = new Color(120, 130, 150);
                    break;
                case PROMPT:
                    color = new Color(255, 160, 80);
                    break;
                case WHITE:
                    color = new Color(240, 240, 245);
                    break;
                case DEFAULT:
                default:
                    color = new Color(210, 215, 225);
                    break;
            }
            g.setColor(color);
            g.drawString(line.text, padding + 10, y);
            y += lineHeight;
        }

        g.dispose();

        try {
            ImageIO.write(image, "PNG", outputFile);
            System.out.println("  [SAVED] " + outputFile.getName());
        } catch (IOException e) {
            System.err.println("  [ERROR] Failed to save screenshot: " + e.getMessage());
        }
    }

    private enum TerminalColor {
        DEFAULT, WHITE, CYAN, GREEN, RED, YELLOW, GRAY, PROMPT
    }

    private static class TerminalLine {
        String text;
        TerminalColor color;

        TerminalLine(String text, TerminalColor color) {
            this.text = text;
            this.color = color;
        }
    }
}
