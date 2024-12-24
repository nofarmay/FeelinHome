import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.sql.*;
@Component
public class DatabaseInitializer implements CommandLineRunner {

    @Override
    public void run(String... args) {
        String url = "jdbc:postgresql://localhost:5432/SDERUNITY";
        String user = "postgres";
        String password = "3311";

        System.out.println("מתחיל ניסיון התחברות ל-DB...");

        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("הדרייבר נטען בהצלחה");

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
                    String userName = resultSet.getString("user_name");
                    String email = resultSet.getString("email");

                    System.out.println("User ID: " + userId);
                    System.out.println("User Name: " + userName);
                    System.out.println("Email: " + email);
                } catch (SQLException e) {
                    System.out.println("שגיאה בקריאת נתונים: " + e.getMessage());
                }
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
