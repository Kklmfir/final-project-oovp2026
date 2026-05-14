/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author faira
 */
package librarymanagementsystem;

import java.sql.Connection;
import java.sql.DriverManager;
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
            JOptionPane.showMessageDialog(null, "Koneksi Database Gagal: " + e.getMessage());
            return null;
        }
    }
}
