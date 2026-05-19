/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package pkgfinal.project.oovp2026;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;

public class DBConnection {
    public static Connection connect() {
        try {
            String url = "jdbc:mysql://localhost:3306/final-project-oovp2026";
            String user = "root";
            String password = ""; 
            
            Connection conn = DriverManager.getConnection(url, user, password);
            return conn;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Connection Failed: " + e.getMessage());
            return null;
        }
    }
}
