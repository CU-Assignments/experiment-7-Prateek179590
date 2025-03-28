import java.sql.*;
import java.util.Scanner;

public class ProductCRUD {

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/yourDatabaseName"; 
        String username = "yourUsername"; 
        String password = "yourPassword"; 

        Connection connection = null;

        try {
            connection = DriverManager.getConnection(url, username, password);
            connection.setAutoCommit(false); 

            Scanner scanner = new Scanner(System.in);
            int choice;

            do {
                System.out.println("\nMenu:");
                System.out.println("1. Create (Add New Product)");
                System.out.println("2. Read (View All Products)");
                System.out.println("3. Update (Update Product Details)");
                System.out.println("4. Delete (Remove Product)");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");
                choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        createProduct(connection, scanner);
                        break;
                    case 2:
                        readProducts(connection);
                        break;
                    case 3:
                        updateProduct(connection, scanner);
                        break;
                    case 4:
                        deleteProduct(connection, scanner);
                        break;
                    case 5:
                        System.out.println("Exiting the program...");
                        break;
                    default:
                        System.out.println("Invalid choice! Please try again.");
                }
            } while (choice != 5);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void createProduct(Connection connection, Scanner scanner) {
        try {
            System.out.print("Enter Product Name: ");
            String name = scanner.next();
            System.out.print("Enter Price: ");
            double price = scanner.nextDouble();
            System.out.print("Enter Quantity: ");
            int quantity = scanner.nextInt();

            String sql = "INSERT INTO Product (ProductName, Price, Quantity) VALUES (?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setDouble(2, price);
            pstmt.setInt(3, quantity);

            pstmt.executeUpdate();
            connection.commit();
            System.out.println("Product added successfully!");

        } catch (Exception e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    private static void readProducts(Connection connection) {
        try {
            String sql = "SELECT * FROM Product";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            System.out.println("\nProductID\tProductName\tPrice\tQuantity");
            System.out.println("-------------------------------------------------");
            while (rs.next()) {
                int id = rs.getInt("ProductID");
                String name = rs.getString("ProductName");
                double price = rs.getDouble("Price");
                int quantity = rs.getInt("Quantity");

                System.out.println(id + "\t\t" + name + "\t\t" + price + "\t" + quantity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void updateProduct(Connection connection, Scanner scanner) {
        try {
            System.out.print("Enter Product ID to update: ");
            int id = scanner.nextInt();
            System.out.print("Enter new Product Name: ");
            String name = scanner.next();
            System.out.print("Enter new Price: ");
            double price = scanner.nextDouble();
            System.out.print("Enter new Quantity: ");
            int quantity = scanner.nextInt();

            String sql = "UPDATE Product SET ProductName = ?, Price = ?, Quantity = ? WHERE ProductID = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setDouble(2, price);
            pstmt.setInt(3, quantity);
            pstmt.setInt(4, id);

            pstmt.executeUpdate();
            connection.commit();
            System.out.println("Product updated successfully!");

        } catch (Exception e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
        }
    }

    private static void deleteProduct(Connection connection, Scanner scanner) {
        try {
            System.out.print("Enter Product ID to delete: ");
            int id = scanner.nextInt();

            String sql = "DELETE FROM Product WHERE ProductID = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, id);

            pstmt.executeUpdate();
            connection.commit();
            System.out.println("Product deleted successfully!");

        } catch (Exception e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
        }
    }
}