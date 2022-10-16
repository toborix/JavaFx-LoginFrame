package sample.javafxlogin;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class LoggedInController implements Initializable {

    @FXML
    private Button button_logout;
    @FXML
    private Label label_fav_channel;
    @FXML
    private Label label_welcome;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        button_logout.setOnAction(event -> DBUtils.changeScene(event, "sample.fxml", "Log in!", null, null ));

    }
    public void setUserInformation(String username, String fav_Channel){
        label_welcome.setText("Welcome " + username + "!");
        label_fav_channel.setText("Your favorite YouTube channel is " + "\n" + fav_Channel + "!");
    }
}
