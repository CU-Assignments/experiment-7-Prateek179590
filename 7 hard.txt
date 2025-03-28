import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


class Student {
    private int studentID;
    private String name;
    private String department;
    private double marks;

  
    public Student(int studentID, String name, String department, double marks) {
        this.studentID = studentID;
        this.name = name;
        this.department = department;
        this.marks = marks;
    }

   
    public int getStudentID() {
        return studentID;
    }

    public void setStudentID(int studentID) {
        this.studentID = studentID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public double getMarks() {
        return marks;
    }

    public void setMarks(double marks) {
        this.marks = marks;
    }
}


class StudentController {

    private Connection connection;

    public StudentController(String url, String username, String password) throws SQLException {
        connection = DriverManager.getConnection(url, username, password);
    }

 
    public void addStudent(Student student) throws SQLException {
        String sql = "INSERT INTO Student (StudentID, Name, Department, Marks) VALUES (?, ?, ?, ?)";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, student.getStudentID());
        pstmt.setString(2, student.getName());
        pstmt.setString(3, student.getDepartment());
        pstmt.setDouble(4, student.getMarks());
        pstmt.executeUpdate();
    }


    public List<Student> getAllStudents() throws SQLException {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM Student";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            Student student = new Student(
                    rs.getInt("StudentID"),
                    rs.getString("Name"),
                    rs.getString("Department"),
                    rs.getDouble("Marks")
            );
            students.add(student);
        }
        return students;
    }


    public void updateStudent(Student student) throws SQLException {
        String sql = "UPDATE Student SET Name = ?, Department = ?, Marks = ? WHERE StudentID = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setString(1, student.getName());
        pstmt.setString(2, student.getDepartment());
        pstmt.setDouble(3, student.getMarks());
        pstmt.setInt(4, student.getStudentID());
        pstmt.executeUpdate();
    }
    public void deleteStudent(int studentID) throws SQLException {
        String sql = "DELETE FROM Student WHERE StudentID = ?";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        pstmt.setInt(1, studentID);
        pstmt.executeUpdate();
    }
}


public class StudentApp {

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/yourDatabaseName"; // Replace with your database name
        String username = "yourUsername";
        String password = "yourPassword";

        try {
            StudentController controller = new StudentController(url, username, password);
            Scanner scanner = new Scanner(System.in);
            int choice;

            do {
                System.out.println("\nMenu:");
                System.out.println("1. Add Student");
                System.out.println("2. View All Students");
                System.out.println("3. Update Student");
                System.out.println("4. Delete Student");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");
                choice = scanner.nextInt();

                switch (choice) {
                    case 1 -> {
                        System.out.print("Enter Student ID: ");
                        int id = scanner.nextInt();
                        System.out.print("Enter Name: ");
                        String name = scanner.next();
                        System.out.print("Enter Department: ");
                        String department = scanner.next();
                        System.out.print("Enter Marks: ");
                        double marks = scanner.nextDouble();

                        Student student = new Student(id, name, department, marks);
                        controller.addStudent(student);
                        System.out.println("Student added successfully.");
                    }
                    case 2 -> {
                        System.out.println("\nStudent List:");
                        for (Student student : controller.getAllStudents()) {
                            System.out.println(student.getStudentID() + " | " +
                                    student.getName() + " | " +
                                    student.getDepartment() + " | " +
                                    student.getMarks());
                        }
                    }
                    case 3 -> {
                        System.out.print("Enter Student ID to update: ");
                        int id = scanner.nextInt();
                        System.out.print("Enter new Name: ");
                        String name = scanner.next();
                        System.out.print("Enter new Department: ");
                        String department = scanner.next();
                        System.out.print("Enter new Marks: ");
                        double marks = scanner.nextDouble();

                        Student student = new Student(id, name, department, marks);
                        controller.updateStudent(student);
                        System.out.println("Student updated successfully.");
                    }
                    case 4 -> {
                        System.out.print("Enter Student ID to delete: ");
                        int id = scanner.nextInt();
                        controller.deleteStudent(id);
                        System.out.println("Student deleted successfully.");
                    }
                    case 5 -> System.out.println("Exiting...");
                    default -> System.out.println("Invalid choice. Try again!");
                }
            } while (choice != 5);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
