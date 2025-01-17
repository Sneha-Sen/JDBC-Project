package Test;
import java.sql.*;
import java.util.Scanner;

public class jdbcProject {
	public static void main(String[] args) {
	
        try{
        	Class.forName("com.mysql.cj.jdbc.Driver");
        	String url = "jdbc:mysql://localhost:3306/my_db";
            String usr = "root";
            String password = "Sneha@1342001";
            Connection connection = DriverManager.getConnection(url, usr, password);
            Scanner sc = new Scanner(System.in);
            System.out.println("Employee Management System");
            String sqlquery="CREATE TABLE employees("
            		+ "id INT PRIMARY KEY,"
            		+ "name VARCHAR(50),"
            		+ "department VARCHAR(100),"
            		+ "salary DOUBLE,"
            		+ "email VARCHAR(100))";
            Statement statement = connection.createStatement();
            statement.executeUpdate(sqlquery);
            System.out.println("Table Created");
            while (true) {
                System.out.println("1. Add Employee");
                System.out.println("2. View Employees");
                System.out.println("3. Update Employee");
                System.out.println("4. Delete Employee");
                System.out.println("5. Search Employee");
                System.out.print("Choose an option: ");
                int choice = sc.nextInt();

                switch (choice) {
                    case 1:
                        addEmployee(connection, sc);
                        break;
                    case 2:
                        viewEmployees(connection);
                        break;
                    case 3:
                        updateEmployee(connection, sc);
                        break;
                    case 4:
                        deleteEmployee(connection, sc);
                        break;
                    case 5:
                        searchEmployee(connection, sc);
                        break;
                    default:
                        System.out.println("Invalid choice!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addEmployee(Connection connection, Scanner sc) throws SQLException {
        System.out.print("Enter Employee ID: ");
        int id = sc.nextInt();
        sc.nextLine(); 
        System.out.print("Enter Employee Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Employee Department: ");
        String department = sc.nextLine();
        System.out.print("Enter Employee Salary: ");
        double salary = sc.nextDouble();
        sc.nextLine(); 
        System.out.print("Enter Employee Email: ");
        String email = sc.nextLine();

        String query = "INSERT INTO employees (id, name, department, salary, email) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, department);
            preparedStatement.setDouble(4, salary);
            preparedStatement.setString(5, email);

            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println(rowsAffected > 0 ? "Employee added successfully!" : "Failed to add employee.");
        }
    }

    public static void viewEmployees(Connection connection) throws SQLException {
        String query = "SELECT * FROM employees";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            System.out.println("Employee Details:");
            while (resultSet.next()) {
                System.out.println("ID: " + resultSet.getInt("id") +
                                   ", Name: " + resultSet.getString("name") +
                                   ", Department: " + resultSet.getString("department") +
                                   ", Salary: " + resultSet.getDouble("salary") +
                                   ", Email: " + resultSet.getString("email"));
            }
        }
    }

    public static void updateEmployee(Connection connection, Scanner sc) throws SQLException {
        System.out.print("Enter Employee ID to update: ");
        int id = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter new Department: ");
        String department = sc.nextLine();
        System.out.print("Enter new Salary: ");
        double salary = sc.nextDouble();

        String query = "UPDATE employees SET department = ?, salary = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, department);
            preparedStatement.setDouble(2, salary);
            preparedStatement.setInt(3, id);

            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println(rowsAffected > 0 ? "Employee updated successfully!" : "Employee not found.");
        }
    }

    public static void deleteEmployee(Connection connection, Scanner sc) throws SQLException {
        System.out.print("Enter Employee ID to delete: ");
        int id = sc.nextInt();

        String query = "DELETE FROM employees WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);

            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println(rowsAffected > 0 ? "Employee deleted successfully!" : "Employee not found.");
        }
    }

    public static void searchEmployee(Connection connection, Scanner sc) throws SQLException {
        System.out.print("Enter Employee ID to search: ");
        int id = sc.nextInt();

        String query = "SELECT * FROM employees WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("ID: " + resultSet.getInt("id") +
                                       ", Name: " + resultSet.getString("name") +
                                       ", Department: " + resultSet.getString("department") +
                                       ", Salary: " + resultSet.getDouble("salary") +
                                       ", Email: " + resultSet.getString("email"));
                } else {
                    System.out.println("Employee not found.");
                }
            }
        }
    }
}
