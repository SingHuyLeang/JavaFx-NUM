package DataBase;

import java.sql.*;
import javax.swing.JOptionPane;

public class Connector {
    public static Connection connection(){
        Connection con = null;        
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/employee", "root", "");
        } catch (ClassNotFoundException|SQLException ex) {
            JOptionPane.showConfirmDialog(null, "Connection fail");
        }
        return con;
    }
}
