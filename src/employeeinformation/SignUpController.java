package employeeinformation;

import DataBase.Connector;
import GetData.getData;
import java.sql.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SignUpController implements Initializable {

    PreparedStatement ps;
    ResultSet rs;
    Connection connection;

    @FXML
    private TextField txtUserName;
    @FXML
    private TextField txtPassword;
    @FXML
    private Button gotosignInBtn;
    @FXML
    private TextField txtConPass;
    @FXML
    private Button signUpBtn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void gotoDashBordWhenSignUp(ActionEvent event) throws IOException {
        String username = txtUserName.getText();
        String password = txtPassword.getText();
        String conPass = txtConPass.getText();
        String sqlQuery = "SELECT * FROM `admin`";
        String sqlInsert = "INSERT INTO `admin`(`username`, `password`) VALUES (?,?)";

        System.out.println("username = " + username);
        System.out.println("password = " + password);
        System.out.println("c-pass   = " + conPass);

        Alert alert;
        connection = Connector.connection();
        if (username.isEmpty() || password.isEmpty() || conPass.isEmpty()) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
        } else {
            if (!password.equals(conPass)) {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Your confirm password is not correct");
                alert.showAndWait();
            } else {
                try {
                    boolean nonUser = true;
                    ps = connection.prepareStatement(sqlQuery);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        if (username.equals(rs.getString("username")) && password.equals(rs.getString("password"))) {
                            nonUser = false;
//                            alert = new Alert(AlertType.ERROR);
//                            alert.setTitle("Error Message");
//                            alert.setHeaderText(null);
//                            alert.setContentText("Duplicate account");
//                            alert.showAndWait();
                        }
                    }

                    if (nonUser == true) {

                        ps = connection.prepareStatement(sqlInsert);
                        ps.setString(1, username);
                        ps.setString(2, password);
                        int i = ps.executeUpdate();

                        if (i > 0) {
                            alert = new Alert(AlertType.CONFIRMATION);
                            alert.setTitle("Success Message");
                            alert.setHeaderText(null);
                            alert.setContentText("Your Account are created successfuly ");
                            alert.showAndWait();
                            
                            getData.name = txtUserName.getText();
                            
                            gotosignInBtn.getScene().getWindow().hide();

                            Parent route = FXMLLoader.load(getClass().getResource("dashboard.fxml"));

                            Scene scene = new Scene(route);
                            Stage stage = new Stage();
                            stage.setScene(scene);
                            stage.show();
                        } else {
                            alert = new Alert(AlertType.ERROR);
                            alert.setTitle("Error Message");
                            alert.setHeaderText(null);
                            alert.setContentText("Your account can be create");
                            alert.showAndWait();
                        }
                    } else {
                        alert = new Alert(AlertType.ERROR);
                        alert.setTitle("Error Message");
                        alert.setHeaderText(null);
                        alert.setContentText("This account is already uesing by someone");
                        alert.showAndWait();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
//        
    }

    @FXML
    private void gotoSignInForm(ActionEvent event) throws IOException {
        gotosignInBtn.getScene().getWindow().hide();

        Parent route = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));

        Scene scene = new Scene(route);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }

}
