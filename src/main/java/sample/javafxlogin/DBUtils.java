package sample.javafxlogin;


import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import java.sql.*;
import java.io.IOException;


public class DBUtils {
    public static void changeScene(ActionEvent event,
                                   String fxmlFile,
                                   String title,
                                   String username,
                                   String favChannel){
        Parent root = null;

        if(username != null && favChannel != null ){
            try {
                FXMLLoader loader = new FXMLLoader((DBUtils.class.getResource(fxmlFile)));
                root = loader.load();
                LoggedInController loggenInController = loader.getController();
                loggenInController.setUserInformation(username, favChannel);

            } catch(IOException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                root = FXMLLoader.load(DBUtils.class.getResource(fxmlFile));
            } catch(IOException e){
                e.printStackTrace();
            }
        }
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(root, 600,400));
        stage.show();
    }


    public static void singUpUser(ActionEvent event, String username, String password, String favChannel) {
        java.sql.Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement psCheckUserExists = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/JavaFx-app",
                    "root",
                    "braCCe2127");
            psCheckUserExists = connection.prepareStatement("SELECT * FROM users WHERE username = ?");
            psCheckUserExists.setString(1, username );
            resultSet = psCheckUserExists.executeQuery();

            if(resultSet.isBeforeFirst()){
                System.out.println("User already exists!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("You cannot use this username");
                alert.show();
            } else {
                psInsert = connection.prepareStatement("INSERT INTO users (username, password, favChannel) VALUES (?, ?, ?)");
                psInsert.setString(1, username);
                psInsert.setString(2, password);
                psInsert.setString(3, favChannel);
                psInsert.executeUpdate();

                changeScene(event, "logged-in.fxml", "Welcome", username, favChannel);
            }


        } catch(SQLException e) {
            e.printStackTrace();
        } finally {
            if  (resultSet != null){
                try {
                    resultSet.close();
                }   catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if  (psCheckUserExists != null){
                try {
                    psCheckUserExists.close();
                }   catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if  (psInsert != null){
                try {
                    psInsert.close();
                }   catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if  (connection != null){
                try {
                    connection.close();
                }   catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }
    }

    public  static void logInUser(ActionEvent event, String username, String password){
        java.sql.Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            connection = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/JavaFx-app",
                    "root",
                    "braCCe2127");
            preparedStatement = connection.prepareStatement("SELECT password, favChannel FROM users WHERE username = ?");
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();

            if(!resultSet.isBeforeFirst()){
                System.out.println("User not found in the database!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Provided credentials are incorrect!");
                alert.show();
            } else {
                while(resultSet.next()){
                    String retrivedPassword = resultSet.getString("password");
                    String retrivedChannel = resultSet.getString("favChannel");

                    if(retrivedPassword.equals(password)){
                        changeScene(event, "logged-in.fxml", "Welcome", username, retrivedChannel);
                    } else {
                        System.out.println("Passwords did not match!");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("The provided credentials are incorrect!");
                        alert.show();
                    }
                }

            }

        } catch(SQLException e) {
            e.printStackTrace();
        } finally {
            if  (resultSet != null){
                try {
                    resultSet.close();
                }   catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if  (preparedStatement != null){
                try {
                    preparedStatement.close();
                }   catch (SQLException e){
                    e.printStackTrace();
                }
            }
            if  (connection != null){
                try {
                    connection.close();
                }   catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
