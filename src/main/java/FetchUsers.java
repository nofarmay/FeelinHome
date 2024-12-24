import java.sql.*;

public class FetchUsers {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/SDERUNITY";
        String user = "postgres";
        String password = "3311";

        System.out.println("מתחיל ניסיון התחברות ל-DB...");

        try {
            // בדיקת טעינת הדרייבר
            Class.forName("org.postgresql.Driver");
            System.out.println("הדרייבר נטען בהצלחה");

            // ניסיון התחברות
            System.out.println("מנסה להתחבר לדאטהבייס...");
            Connection connection = DriverManager.getConnection(url, user, password);
            System.out.println("התחבר לדאטהבייס בהצלחה!");

            String query = "SELECT * FROM users LIMIT 2";
            System.out.println("מריץ שאילתא: " + query);

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            boolean hasResults = false;
            while (resultSet.next()) {
                hasResults = true;
                System.out.println("\nנמצאה רשומה:");
                try {
                    int userId = resultSet.getInt("user_id");
                    System.out.println("User ID: " + userId);
                } catch (SQLException e) {
                    System.out.println("שגיאה בקריאת user_id: " + e.getMessage());
                }

                // וכך הלאה עבור שאר השדות...
            }

            if (!hasResults) {
                System.out.println("לא נמצאו תוצאות בשאילתא");
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();
            System.out.println("החיבור נסגר בהצלחה");

        } catch (ClassNotFoundException e) {
            System.out.println("לא נמצא דרייבר PostgreSQL: " + e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("שגיאת SQL: " + e.getMessage());
            System.out.println("קוד שגיאה: " + e.getSQLState());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("שגיאה כללית: " + e.getMessage());
            e.printStackTrace();
        }
    }
}