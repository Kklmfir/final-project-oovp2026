/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package pkgfinal.project.oovp2026;
import java.sql.*;
import javax.xml.crypto.Data;
import net.proteanit.sql.DbUtils;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class FinalProjectOovp2026 {
    private void connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/final-project-oovp2026";
            String username = "root";
            String password = "";
            conn = DriverManager.getConnection(url, username, password);
            statement = conn.createStatement(res.TYPE_SCROLL_SENSITIVE, res.CONCUR_UPDATABLE);
            res = statement.executeQuery("SELECT name, major, id FROM students");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
            System.exit(0);
        }
    }
    
    public static void main(String[] args) {
        // TODO code application logic here
    }
    
}
