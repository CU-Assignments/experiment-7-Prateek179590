import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class FetchEmployeeData {
    private static final String URL = "jdbc:mysql://localhost:3306/your_database_name"; 
    private static final String USER = "your_username"; 
    private static final String PASSWORD = "your_password"; 

    public static void main(String[] args) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to the database successfully!");

            statement = connection.createStatement();
            String sql = "SELECT EmpID, Name, Salary FROM Employee";
            resultSet = statement.executeQuery(sql);

            System.out.println("Employee Records:");
            while (resultSet.next()) {
                int empId = resultSet.getInt("EmpID");
                String name = resultSet.getString("Name");
                double salary = resultSet.getDouble("Salary");
                System.out.printf("EmpID: %d, Name: %s, Salary: %.2f%n", empId, name, salary);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
........................................................................................................................................
