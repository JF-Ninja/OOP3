import java.sql.*;
import java.util.Scanner;
public class Main {
    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/postgres?user=postgres&password=123456789";
    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Select number:\n1. Make a reservation\n2. Postpone your reservation\n3. Delete your reservation\n4. Check busy time\nYour choice: ");
            int choice = scanner.nextInt();

            Connection connection = DriverManager.getConnection(JDBC_URL);
            Statement statement = connection.createStatement();

            statement.execute("CREATE TABLE IF NOT EXISTS users (name TEXT, month INT, day INT, hour INT, minute INT)");

            switch (choice) {
                case 1:
                    makeReservation(statement, scanner);
                    break;
                case 2:
                    postponeReservation(statement, scanner);
                    break;
                case 3:
                    deleteReservation(statement, scanner);
                    break;
                case 4:
                    checkBusyTime(statement, scanner);
                    break;
                default:
                    System.out.println("Error. Try again");
            }

            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private static void makeReservation(Statement statement, Scanner scanner) throws SQLException {
        try (Connection connection = DriverManager.getConnection(JDBC_URL)) {
            System.out.print("Your name: ");
            String userName = scanner.next();
            System.out.print("Month: ");
            int userMonth = scanner.nextInt();
            System.out.print("Day: ");
            int userDay = scanner.nextInt();
            System.out.print("Hour: ");
            int userHour = scanner.nextInt();
            System.out.print("Minute: ");
            int userMinute = scanner.nextInt();

            ResultSet resultSet = statement.executeQuery("SELECT * FROM users WHERE month = " + userMonth +
                    " AND day = " + userDay + " AND hour = " + userHour + " AND minute = " + userMinute);
            if (!resultSet.next()) {
                PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO users VALUES (?, ?, ?, ?, ?)");
                insertStatement.setString(1, userName);
                insertStatement.setInt(2, userMonth);
                insertStatement.setInt(3, userDay);
                insertStatement.setInt(4, userHour);
                insertStatement.setInt(5, userMinute);
                insertStatement.executeUpdate();

                System.out.println("Successful reservation");
            } else {
                System.out.println("This time is already reserved");
            }
        }
    }
    private static void postponeReservation(Statement statement, Scanner scanner) throws SQLException {
        try (Connection connection = DriverManager.getConnection(JDBC_URL)) {
            System.out.print("Your name: ");
            String userName = scanner.next();
            System.out.print("Month: ");
            int userMonth = scanner.nextInt();
            System.out.print("Day: ");
            int userDay = scanner.nextInt();
            System.out.print("Hour: ");
            int userHour = scanner.nextInt();
            System.out.print("Minute: ");
            int userMinute = scanner.nextInt();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM users WHERE name = '" + userName +
                    "' AND month = " + userMonth + " AND day = " + userDay +
                    " AND hour = " + userHour + " AND minute = " + userMinute);
            if (!resultSet.next()) {
                System.out.println("Reservation not found");
            } else {
                statement.executeUpdate("DELETE FROM users WHERE name = '" + userName +
                        "' AND month = " + userMonth + " AND day = " + userDay +
                        " AND hour = " + userHour + " AND minute = " + userMinute);
                System.out.print("Enter new month: ");
                int newMonth = scanner.nextInt();
                System.out.print("Enter new day: ");
                int newDay = scanner.nextInt();
                System.out.print("Enter new hour: ");
                int newHour = scanner.nextInt();
                System.out.print("Enter new minute: ");
                int newMinute = scanner.nextInt();
                PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO users VALUES (?, ?, ?, ?, ?)");
                insertStatement.setString(1, userName);
                insertStatement.setInt(2, newMonth);
                insertStatement.setInt(3, newDay);
                insertStatement.setInt(4, newHour);
                insertStatement.setInt(5, newMinute);
                insertStatement.executeUpdate();
                System.out.println("Your reservation has been saved");
            }
        }
    }
    private static void deleteReservation(Statement statement, Scanner scanner) throws SQLException {
        try (Connection connection = DriverManager.getConnection(JDBC_URL)) {
            System.out.print("Your name: ");
            String userName = scanner.next();
            System.out.print("Month: ");
            int userMonth = scanner.nextInt();
            System.out.print("Day: ");
            int userDay = scanner.nextInt();
            System.out.print("Hour: ");
            int userHour = scanner.nextInt();
            System.out.print("Minute: ");
            int userMinute = scanner.nextInt();

            ResultSet resultSet = statement.executeQuery("SELECT * FROM users WHERE name = '" + userName +
                    "' AND month = " + userMonth + " AND day = " + userDay +
                    " AND hour = " + userHour + " AND minute = " + userMinute);
            if (!resultSet.next()) {
                System.out.println("Reservation not found");
            } else {
                statement.executeUpdate("DELETE FROM users WHERE name = '" + userName +
                        "' AND month = " + userMonth + " AND day = " + userDay +
                        " AND hour = " + userHour + " AND minute = " + userMinute);
                System.out.println("Your reservation has been deleted");
            }
        }
    }
    private static void checkBusyTime(Statement statement, Scanner scanner) throws SQLException {
        try (Connection connection = DriverManager.getConnection(JDBC_URL)) {
            System.out.print("Month: ");
            int userMonth = scanner.nextInt();
            ResultSet resultSet = statement.executeQuery("SELECT day, hour, minute FROM users WHERE month = " + userMonth);
            System.out.println("Time worked per month in format day month minute:");
            while (resultSet.next()) {
                int day = resultSet.getInt("day");
                int hour = resultSet.getInt("hour");
                int minute = resultSet.getInt("minute");
                System.out.println(day + " " + hour + " " + minute);
            }
        }
    }
}
