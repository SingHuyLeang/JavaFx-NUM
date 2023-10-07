package employeeinformation;

import DataBase.Connector;
import GetData.getData;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.swing.JOptionPane;

public class FXMLDocumentController implements Initializable {

    private Label label;
    @FXML
    private AnchorPane main_form;
    @FXML
    private PasswordField txt_password;
    @FXML
    private TextField txt_username;

    // DATABASE TOOLS
    Connection connection;
    PreparedStatement ps;
    ResultSet rs;

    @FXML
    private Button signInBtn;
    @FXML
    private Button gotoSignUpBtn;

    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        label.setText("Hello World!");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void gotoDashBoard(ActionEvent event) throws IOException {
        String username = txt_username.getText();
        String password = txt_password.getText();
        Alert alert;
        if (username.isEmpty() && password.isEmpty()) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all blank fields");
            alert.showAndWait();
        } else if (username.isEmpty()) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please enter your username");
            alert.showAndWait();
        } else if (password.isEmpty()) {
            alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please enter your password");
            alert.showAndWait();
        } else {
            connection = Connector.connection();
            String sql = "SELECT * FROM `admin` WHERE username=? AND password=?";
            try {
                ps = connection.prepareStatement(sql);
                ps.setString(1, username);
                ps.setString(2, password);
                rs = ps.executeQuery();
                if(rs.next()){
                    
                    getData.name = txt_username.getText();
                    
                    signInBtn.getScene().getWindow().hide();
                    Parent root = FXMLLoader.load(getClass().getResource("dashboard.fxml"));
        
                    Scene scene = new Scene(root);
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.show();
                    // TO DO DISPOSE
                }else{
                    alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Invalid your account");
                    alert.showAndWait();
                }
            } catch (SQLException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    @FXML
    private void gotoSignUpForm() throws IOException {
        gotoSignUpBtn.getScene().getWindow().hide();
        Parent route = FXMLLoader.load(getClass().getResource("signUp.fxml"));
        
        Scene scene = new Scene(route);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }
    
}
