package employeeinformation;

import DataBase.Connector;
import GetData.getData;
import Model.EmployeeModel;
import java.awt.Desktop;
import java.io.File;
import javafx.scene.image.Image;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

public class DashboardController implements Initializable {

    @FXML
    private Label username;
    @FXML
    private Button joinHomeBtn;
    @FXML
    private Button joinAddEMPBtn;
    @FXML
    private Button joinSalaryBtn;
    @FXML
    private Button signOutBtn;
    @FXML
    private AnchorPane panelHome;
    @FXML
    private BarChart<?, ?> home_chart;
    @FXML
    private Label home_totalEmp;
    @FXML
    private Label home_totalPresents;
    @FXML
    private Label home_totalInactive;
    @FXML
    private AnchorPane panelAddEmployee;
    @FXML
    private TextField addEmp_txtFirstName;
    @FXML
    private TextField addEmp_txtLastName;
    @FXML
    private ComboBox<?> addEmp_comGender;
    @FXML
    private TextField addEmp_txtPhone;
    @FXML
    private ComboBox<?> addEmp_comPosition;
    @FXML
    private ImageView addEmp_image;
    @FXML
    private Button addEmp_chooseImgBtn;
    @FXML
    private Button addEmp_addBtn;
    @FXML
    private Button addEmp_updateBtn;
    @FXML
    private Button addEmp_deleteBtn;
    @FXML
    private Button addEmp_clearBtn;
    @FXML
    private TableView<EmployeeModel> addEmp_tableViews;
    @FXML
    private TableColumn<EmployeeModel, String> addEmp_cul_empID;
    @FXML
    private TableColumn<EmployeeModel, String> addEmp_cul_firstName;
    @FXML
    private TableColumn<EmployeeModel, String> addEmp_cul_lastName;
    @FXML
    private TableColumn<EmployeeModel, String> addEmp_cul_gender;
    @FXML
    private TableColumn<EmployeeModel, String> addEmp_cul_phone;
    @FXML
    private TableColumn<EmployeeModel, String> addEmp_cul_position;
    @FXML
    private TableColumn<EmployeeModel, String> addEmp_cul_dateMember;
    @FXML
    private TextField addEmp_search;
    @FXML
    private AnchorPane panelSalary;
    @FXML
    private Label salary_employeeID;
    @FXML
    private TextField salary_employeeSalary;
    @FXML
    private Label salary_employeeFirstName;
    @FXML
    private Label salary_employeeLastName;
    @FXML
    private Label salary_employeePosition;
    @FXML
    private Button salary_updateBtn;
    @FXML
    private Button salary_clearBtn;
    @FXML
    private TableView<EmployeeModel> salary_tableViews;
    @FXML
    private TableColumn<EmployeeModel, String> salary_cul_employeeID;
    @FXML
    private TableColumn<EmployeeModel, String> salary_cul_firstName;
    @FXML
    private TableColumn<EmployeeModel, String> salary_cul_lastName;
    @FXML
    private TableColumn<EmployeeModel, String> salary_cul_position;
    @FXML
    private TableColumn<EmployeeModel, String> salary_cul_salary;
    @FXML
    private AnchorPane mainForm;
    @FXML
    private TextField addEmp_txtId;

    private Connection connection;
    private PreparedStatement ps;
    private ResultSet rs;
    private Image image;

    // add employee add data
    @FXML
    public void addEmployeeAdd() {

        connection = Connector.connection();
        try {
            Alert alert;
            if (addEmp_txtFirstName.getText().isEmpty()
                    || addEmp_txtLastName.getText().isEmpty()
                    || addEmp_comGender.getSelectionModel().getSelectedItem() == null
                    || addEmp_txtPhone.getText().isEmpty()
                    || addEmp_comPosition.getSelectionModel().getSelectedItem() == null) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            } else {
                String checkUser = "SELECT `first_name`, `last_name` FROM `employee_data` WHERE first_name=? AND last_name=?";

                ps = connection.prepareStatement(checkUser);
                ps.setString(1, addEmp_txtFirstName.getText());
                ps.setString(2, addEmp_txtLastName.getText());
                rs = ps.executeQuery();
                if (rs.next()) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText(addEmp_txtFirstName.getText() + " " + addEmp_txtLastName.getText() + " has already exits!");
                    alert.showAndWait();
                } else {
                    String sql = "INSERT INTO `employee_data`(`first_name`, `last_name`, `gender`, `phone`, `position`, `image`) VALUES (?,?,?,?,?,?)";
                    ps = connection.prepareStatement(sql);
                    ps.setString(1, addEmp_txtFirstName.getText());
                    ps.setString(2, addEmp_txtLastName.getText());
                    ps.setString(3, (String) addEmp_comGender.getSelectionModel().getSelectedItem());
                    ps.setString(4, addEmp_txtPhone.getText());
                    ps.setString(5, (String) addEmp_comPosition.getSelectionModel().getSelectedItem());

                    String uri = getData.path;
                    uri = uri.replace("\\", "\\\\");
                    
                    ps.setString(6, uri);
                    int i = ps.executeUpdate();
                    
                    String salaryinfo = "INSERT INTO `employee_salary`(`first_name`, `last_name`, `position`, `salary`) VALUES (?,?,?,?)";
                    ps = connection.prepareStatement(salaryinfo);
                    ps.setString(1, addEmp_txtFirstName.getText());
                    ps.setString(2, addEmp_txtLastName.getText());
                    ps.setString(3, (String) addEmp_comPosition.getSelectionModel().getSelectedItem());
                    ps.setFloat(4,(float) 0.0);
                    int j = ps.executeUpdate();
                    
                    if (i > 0 && j > 0) {
                        alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Success Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Employee data is added Successfuly");
                        alert.showAndWait();
                    }
                    addEmployeeShowListData();
                    addEmployeeListData();
                    addEmployeeResetField();
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // add employee update data 
    @FXML
    public void addEmployeeUpdate() {

        String uri = getData.path;
        uri = uri.replace("\\", "\\\\");

        connection = Connector.connection();

        try {
            Alert alert;
            if (addEmp_txtId.getText().isEmpty()
                    || addEmp_txtFirstName.getText().isEmpty()
                    || addEmp_txtLastName.getText().isEmpty()
                    || addEmp_comGender.getSelectionModel().getSelectedItem() == null
                    || addEmp_txtPhone.getText().isEmpty()
                    || addEmp_comPosition.getSelectionModel().getSelectedItem() == null
                    || getData.path == null || getData.path == "") {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            } else {
                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Cofirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to UPDATE Employee : " + addEmp_txtFirstName.getText() + " " + addEmp_txtLastName.getText() + "?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {
                    String sql = "UPDATE `employee_data` SET `first_name`=?,`last_name`=?,`gender`=?,`phone`=?,`position`=?,`image`=? WHERE employee_id=?";
                    ps = connection.prepareStatement(sql);
                    ps.setString(1, addEmp_txtFirstName.getText());
                    ps.setString(2, addEmp_txtLastName.getText());
                    ps.setString(3, addEmp_comGender.getSelectionModel().getSelectedItem().toString());
                    ps.setString(4, addEmp_txtPhone.getText());
                    ps.setString(5, addEmp_comPosition.getSelectionModel().getSelectedItem().toString());
                    ps.setString(6, uri);
                    ps.setInt(7, Integer.parseInt(addEmp_txtId.getText()));
                    int i = ps.executeUpdate();
                    
                    String empInfoSql = "UPDATE `employee_salary` SET `first_name`=?,`last_name`=?,`position`=? WHERE id=?";
                    ps = connection.prepareStatement(empInfoSql);
                    ps.setString(1, addEmp_txtFirstName.getText());
                    ps.setString(2, addEmp_txtLastName.getText());
                    ps.setString(3, addEmp_comPosition.getSelectionModel().getSelectedItem().toString());
                    ps.setInt(4, Integer.parseInt(addEmp_txtId.getText()));
                    int j = ps.executeUpdate();
                    if (i > 0 && j > 0) {
                        alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("Information Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Successfully Updated!");
                        alert.showAndWait();
                    }

                    addEmployeeShowListData();
                    salaryEmployeeShowListData();
                    addEmployeeResetField();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // add employee delete data
    @FXML
    public void addEmployeeDelete() {
        String sql = "DELETE FROM `employee_data` WHERE employee_id=?";
        connection = Connector.connection();
        Alert alert;
        try {

            if (addEmp_txtId.getText().isEmpty()
                    || addEmp_txtFirstName.getText().isEmpty()
                    || addEmp_txtLastName.getText().isEmpty()
                    || addEmp_comGender.getSelectionModel().getSelectedItem() == null
                    || addEmp_txtPhone.getText().isEmpty()
                    || addEmp_comPosition.getSelectionModel().getSelectedItem() == null
                    || getData.path == null || getData.path == "") {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            } else {
                alert = new Alert(AlertType.WARNING);
                alert.setTitle("WARNING Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to Delete Employee " + addEmp_txtFirstName.getText() + " " + addEmp_txtLastName.getText() + "?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get().equals(ButtonType.OK)) {
                    ps = connection.prepareStatement(sql);
                    ps.setInt(1, Integer.parseInt(addEmp_txtId.getText()));
                    int i = ps.executeUpdate();
                    
                    String deleteInfo = "DELETE FROM `employee_salary` WHERE id=?";
                    ps = connection.prepareStatement(deleteInfo);
                    ps.setInt(1, Integer.parseInt(addEmp_txtId.getText()));
                    int j = ps.executeUpdate();
                    if (i > 0 && j > 0) {
                        alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("Success Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Employee " + addEmp_txtFirstName.getText() + " " + addEmp_txtLastName.getText() + "is deleted");
                        alert.showAndWait();
                    }
                    addEmployeeShowListData();
                    salaryEmployeeShowListData();
                    addEmployeeResetField();
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // add employee reset data
    @FXML
    public void addEmployeeResetField() {
        addEmp_txtFirstName.setText("");
        addEmp_txtLastName.setText("");
        addEmp_comGender.getSelectionModel().clearSelection();
        addEmp_txtPhone.setText("");
        addEmp_comPosition.getSelectionModel().clearSelection();
        addEmp_image.setImage(null);
        getData.path = "";
    }
    // add employee position list
    private String[] positon = {"Marketer Coordinator", "Web Developer (Back End)", "Web Developer (Front End)", "App Developer", "API Deverlopment", "DevOps", "Team Leader", "IT Manager", "Big Data", "Cloud Computing"};

    public void addEmployeePositionList() {
        List<String> positionList = new ArrayList<>();
        for (String data : positon) {
            positionList.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(positionList);
        addEmp_comPosition.setItems(listData);
    }
    // add employee gender list
    private String[] gender = {"Male", "Female", "Others"};

    public void addEmployeeGenderList() {
        List<String> genderList = new ArrayList<>();

        for (String data : gender) {
            genderList.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(genderList);
        addEmp_comGender.setItems(listData);
    }

    // Choose image
    @FXML
    public void addEmployeeInsertImage() {
        FileChooser open = new FileChooser();
        File file = open.showOpenDialog(mainForm.getScene().getWindow());

        if (file != null) {
            getData.path = file.getAbsolutePath();
            image = new Image(file.toURI().toString(), 147, 167, false, true);
            addEmp_image.setImage(image);
        }
    }

    public ObservableList<EmployeeModel> addEmployeeListData() {
        ObservableList<EmployeeModel> listData = FXCollections.observableArrayList();
        String sql = "SELECT * FROM `employee_data`";

        connection = Connector.connection();
        try {
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                EmployeeModel employeeModel = new EmployeeModel(
                        rs.getInt("employee_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("gender"),
                        rs.getString("phone"),
                        rs.getString("position"),
                        getData.path = rs.getString("image"),
                        rs.getDate("date")
                );

                listData.add(employeeModel);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return listData;
    }

    private ObservableList<EmployeeModel> addEmployeeList;
    // show data on table
    public void addEmployeeShowListData() {
        addEmployeeList = addEmployeeListData();
        
        addEmp_cul_empID.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        addEmp_cul_firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        addEmp_cul_lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        addEmp_cul_gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        addEmp_cul_phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        addEmp_cul_position.setCellValueFactory(new PropertyValueFactory<>("position"));
        addEmp_cul_dateMember.setCellValueFactory(new PropertyValueFactory<>("date"));

        addEmp_tableViews.setItems(addEmployeeList);
    }
    
    
    // salary list data
    public ObservableList<EmployeeModel> salaryEmployeeListData() {
        ObservableList<EmployeeModel> listData = FXCollections.observableArrayList();
        String sql = "SELECT `employee_id`, `first_name`, `last_name`, `position`, `salary` FROM `employee_salary`";

        connection = Connector.connection();
        try {
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                EmployeeModel employeeModel = new EmployeeModel(
                        rs.getInt("employee_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("position"),
                        rs.getFloat("salary")
                );
                listData.add(employeeModel);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return listData;
    }

    
//  show data on table salary
    private ObservableList<EmployeeModel> salaryEmployeeList;
    public void salaryEmployeeShowListData() {
        salaryEmployeeList = salaryEmployeeListData();
        
        salary_cul_employeeID.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        salary_cul_firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        salary_cul_lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        salary_cul_position.setCellValueFactory(new PropertyValueFactory<>("position"));
        salary_cul_salary.setCellValueFactory(new PropertyValueFactory<>("salary"));

        salary_tableViews.setItems(salaryEmployeeList);
    }
//    salary selete
    @FXML
    public void salaryEmployeeSelete(){
         EmployeeModel employee = salary_tableViews.getSelectionModel().getSelectedItem();
        int num = salary_tableViews.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) {
            return;
        }

        salary_employeeID.setText(String.valueOf(employee.getEmployeeId()));
        salary_employeeFirstName.setText(employee.getFirstName());
        salary_employeeLastName.setText(employee.getLastName());
        salary_employeePosition.setText(employee.getPosition());
        salary_employeeSalary.setText(String.valueOf(employee.getSalary()));

    }
//    Salary clear selete
    @FXML
    public void salaryClearSelete(){
        salary_employeeID.setText("");
        salary_employeeFirstName.setText("");
        salary_employeeLastName.setText("");
        salary_employeePosition.setText("");
        salary_employeeSalary.setText("");
    }
    
    public void salaryEmployeeUpdate(){
        String sql = "UPDATE `employee_salary` SET `salary`= ? WHERE employee_id = ?";
        connection = Connector.connection();
        Alert alert;
        try {
            ps = connection.prepareStatement(sql);
            ps.setFloat(1, Float.parseFloat(salary_employeeSalary.getText()));
            ps.setInt(2, Integer.parseInt(salary_employeeID.getText()));
            int i = ps.executeUpdate(); 
            if (i > 0) {
                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("INFORMATION Message");
                alert.setHeaderText(null);
                alert.setContentText("Salary Employee " + addEmp_txtFirstName.getText() + " " + addEmp_txtLastName.getText() + "is Upodated");
            }
            salaryEmployeeShowListData();
            salaryClearSelete();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void addEmployeeSelect() {
        EmployeeModel employeeModel = addEmp_tableViews.getSelectionModel().getSelectedItem();
        int num = addEmp_tableViews.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) {
            return;
        }
        addEmp_txtId.setText(employeeModel.getEmployeeId().toString());
        addEmp_txtFirstName.setText(employeeModel.getFirstName());
        addEmp_txtLastName.setText(employeeModel.getLastName());
        int index = employeeModel.getGender().toLowerCase() == "male" ? 0 : employeeModel.getGender().toLowerCase() == "female" ? 1 : 2;
        addEmp_comGender.getSelectionModel().select(index);
        for (int i = 0; i < positon.length; i++) {
            if (positon[i].equalsIgnoreCase(employeeModel.getPosition())) {
                addEmp_comPosition.getSelectionModel().select(i);
            }
        }
        addEmp_txtPhone.setText(employeeModel.getPhone());

        getData.path = employeeModel.getImage();

        String uri = "file:" + employeeModel.getImage();
        image = new Image(uri, 147, 167, false, true);
        addEmp_image.setImage(image);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        homeChart();
        
        username.setText(getData.name);
        panelHome.setVisible(true);
        panelAddEmployee.setVisible(false);
        panelSalary.setVisible(false);

        joinHomeBtn.setStyle("-fx-background-color:linear-gradient(to bottom right, #E8757E, #7591E8);");
        joinAddEMPBtn.setStyle("-fx-background-color:transparent");
        joinSalaryBtn.setStyle("-fx-background-color:transparent");
        addEmployeeShowListData();
        addEmployeeGenderList();
        addEmployeePositionList();

        homeTotalEmployees();
        homeTotalPresent();
        homeTotalInactive();
    }

    @FXML
    private void gotoSignOut(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Authorization Message");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to sign out ?");
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get().equals(ButtonType.OK)) {
            Parent root;
            try {
                signOutBtn.getScene().getWindow().hide();
                Parent route = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));

                Scene scene = new Scene(route);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.show();
            } catch (IOException ex) {
                Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    @FXML
    private void showHome(ActionEvent event) {
        homeChart();
        
        homeTotalEmployees();
        homeTotalPresent();
        homeTotalInactive();
        
        panelHome.setVisible(true);
        panelAddEmployee.setVisible(false);
        panelSalary.setVisible(false);

        joinHomeBtn.setStyle("-fx-background-color:linear-gradient(to right, #FB907D, #E8757E)");
        joinAddEMPBtn.setStyle("-fx-background-color:transparent");
        joinSalaryBtn.setStyle("-fx-background-color:transparent");
    }

    @FXML
    private void showAddEmployee(ActionEvent event) {
        panelHome.setVisible(false);
        panelAddEmployee.setVisible(true);
        panelSalary.setVisible(false);

        joinAddEMPBtn.setStyle("-fx-background-color:linear-gradient(to right, #FB907D, #E8757E)");
        joinHomeBtn.setStyle("-fx-background-color:transparent");
        joinSalaryBtn.setStyle("-fx-background-color:transparent");

        addEmployeeGenderList();
        addEmployeePositionList();
    }

    @FXML
    private void showEmpSalary(ActionEvent event) {
        
        salaryEmployeeShowListData();
        panelHome.setVisible(false);
        panelAddEmployee.setVisible(false);
        panelSalary.setVisible(true);

        joinSalaryBtn.setStyle("-fx-background-color:linear-gradient(to right, #FB907D, #E8757E)");
        joinHomeBtn.setStyle("-fx-background-color:transparent");
        joinAddEMPBtn.setStyle("-fx-background-color:transparent");
    }

    @FXML
    public void gotoSocialMedia() {
        Hyperlink hyperlink = new Hyperlink(getData.name);
        hyperlink.setOnAction(e -> {
            try {
                Desktop.getDesktop().browse(new URI("https://www.youtube.com/"));
            } catch (IOException | URISyntaxException ex) {
                ex.printStackTrace();
            }
        });
    }

    // add Employee filter
    @FXML
    public void addEmployeeFilter() {
        FilteredList<EmployeeModel> filter = new FilteredList<>(addEmployeeList, e -> true);

        addEmp_search.textProperty().addListener((observable, oldValue, newValue) -> {
            filter.setPredicate(predicateEmployeeData -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String searchKey = newValue.toLowerCase();

                if (predicateEmployeeData.getEmployeeId().toString().contains(searchKey)) {
                    return true;
                } else if (predicateEmployeeData.getFirstName().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateEmployeeData.getLastName().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateEmployeeData.getGender().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateEmployeeData.getPhone().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateEmployeeData.getPosition().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateEmployeeData.getDate().toString().contains(searchKey)) {
                    return true;
                } else {
                    return false;
                }
            });
        });
        SortedList<EmployeeModel> sortList = new SortedList<>(filter);

        sortList.comparatorProperty().bind(addEmp_tableViews.comparatorProperty());
        addEmp_tableViews.setItems(sortList);
    }

    // Home total employee
    public void homeTotalEmployees() {

        String sql = "SELECT `employee_id` FROM `employee_data`";

        connection = Connector.connection();
        int countData = 0;
        try {

            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                countData++;
            }

            home_totalEmp.setText(String.valueOf(countData));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // Home total present
    public void homeTotalPresent() {
        String sql = "SELECT `employee_id`FROM `employee_salary` WHERE salary <> 0.0";

        connection = Connector.connection();
        int countData = 0;
        try {

            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                countData++;
            }

            home_totalPresents.setText(String.valueOf(countData));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Home totalinactive
    public void homeTotalInactive() {
        String sql = "SELECT `employee_id`FROM `employee_salary` WHERE salary = 0.0";

        connection = Connector.connection();
        int countData = 0;
        try {

            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                countData++;
            }

            home_totalInactive.setText(String.valueOf(countData));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Home Chart view
    public void homeChart() {

        home_chart.getData().clear();

        String sql = "SELECT date, COUNT(employee_id) FROM employee_data GROUP BY date ORDER BY TIMESTAMP(date) ASC LIMIT 7";

        connection = Connector.connection();

        try {
            XYChart.Series chart = new XYChart.Series();

            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                chart.getData().add(new XYChart.Data(rs.getString(1), rs.getInt(2)));
            }

            home_chart.getData().add(chart);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
