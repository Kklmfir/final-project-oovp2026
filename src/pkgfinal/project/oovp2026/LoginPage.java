/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package pkgfinal.project.oovp2026;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import javax.swing.SwingUtilities;

/**
 * Class: LoginPage
 */
public class LoginPage extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(LoginPage.class.getName());

    /**
     * Constructor: LoginPage()
     * Description: Initialize GUI and call initComponents()
     */
    public LoginPage() {
        initComponents();
    }
    
    /**
     * Method: initComponents()
     * Description: GUI builder generated code
     */
    @SuppressWarnings("unchecked")

    private void initComponents() {

        panel1 = new java.awt.Panel();
        LoginLabel = new java.awt.Label();
        CopyrightLabel = new java.awt.Label();
        label3 = new java.awt.Label();
        UserID = new javax.swing.JTextField();
        label5 = new java.awt.Label();
        Name = new javax.swing.JTextField();
        Login = new javax.swing.JButton();
        Register = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        LoginLabel.setFont(new java.awt.Font("Georgia", 1, 14));
        LoginLabel.setText("Alfairaz Family's Library");

        CopyrightLabel.setText("Made without a coffee :c");

        label3.setText("User ID");

        label5.setText("Name");

        Login.setText("Login");
        Login.addActionListener(this::LoginActionPerformed);

        Register.setText("Register");
        Register.addActionListener(this::RegisterActionPerformed);

        javax.swing.GroupLayout panel1Layout = new javax.swing.GroupLayout(panel1);
        panel1.setLayout(panel1Layout);
        panel1Layout.setHorizontalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel1Layout.createSequentialGroup()
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel1Layout.createSequentialGroup()
                        .addGap(80, 80, 80)
                        .addComponent(LoginLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panel1Layout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(panel1Layout.createSequentialGroup()
                                .addComponent(label3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(UserID, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel1Layout.createSequentialGroup()
                                .addComponent(label5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(CopyrightLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(Name, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(97, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel1Layout.createSequentialGroup()
                .addGap(63, 63, 63)
                .addComponent(Login)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Register)
                .addGap(57, 57, 57))
        );
        panel1Layout.setVerticalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(LoginLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(54, 54, 54)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(UserID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Login)
                    .addComponent(Register))
                .addGap(51, 51, 51)
                .addComponent(CopyrightLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        LoginLabel.getAccessibleContext().setAccessibleName("");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }                      

    /**
     * Method: authenticate(String userId, String name)
     * Member Table. Member_ID and Member_Name
     */
    private boolean authenticate(String userId, String name) {
        String sql = "SELECT 1 FROM Member WHERE Member_ID = ? AND Member_Name = ? LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userId);
            ps.setString(2, name);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, "Connection failed: " + ex.getMessage()));
            return false;
        }
    }

    /**
     * Method: LoginActionPerformed(ActionEvent)
     * Description: Login button handler
     */
    private void LoginActionPerformed(java.awt.event.ActionEvent evt) {                                      
        String userId = UserID.getText().trim();
        String name = Name.getText().trim();

        if (userId.isEmpty() || name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "User ID and Name cannot be empty");
            return;
        }

        Login.setEnabled(false);

        new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() {
                return authenticate(userId, name);
            }

            @Override
            protected void done() {
                try {
                    boolean ok = get();
                    if (ok) {
                        // Open MenuPage
                        MenuPage menu = new MenuPage();
                        menu.setTitle("Welcome, " + name + " (" + userId + ")");
                        menu.setLocationRelativeTo(null);
                        menu.setVisible(true);
                        dispose(); // Close login page
                    } else {
                        JOptionPane.showMessageDialog(LoginPage.this, "Login failed: please check User ID / Name");
                    }
                } catch (Exception ex) {
                    logger.log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(LoginPage.this, "Error occurred during login process: " + ex.getMessage());
                } finally {
                    Login.setEnabled(true);
                }
            }
        }.execute();
    }                                     

    /**
     * Method: RegisterActionPerformed(ActionEvent)
     * Description: Register button handler
     */
    private void RegisterActionPerformed(java.awt.event.ActionEvent evt) {
        MemberForm mf = new MemberForm();
        mf.setLocationRelativeTo(null);
        mf.setVisible(true);
    }

    /**
     * Method: main(String[])
     * Description: Application entry point
     */
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        java.awt.EventQueue.invokeLater(() -> new LoginPage().setVisible(true));
    }
                  
    private java.awt.Label CopyrightLabel;
    private javax.swing.JButton Login;
    private java.awt.Label LoginLabel;
    private javax.swing.JTextField Name;
    private javax.swing.JButton Register;
    private javax.swing.JTextField UserID;
    private java.awt.Label label3;
    private java.awt.Label label5;
    private java.awt.Panel panel1;                  
}