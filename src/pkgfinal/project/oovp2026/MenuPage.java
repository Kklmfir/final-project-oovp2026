/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package pkgfinal.project.oovp2026;

import javax.swing.SwingUtilities;

/**
 * Class: MenuPage
 */
public class MenuPage extends javax.swing.JFrame {

    /**
     * Constructor: MenuPage()
     * Description: Initialise GUI components
     */
    public MenuPage() {
        initComponents();
    }

    /**
     * Method: initComponents()
     * Description: Add actionListener for each button
     */
    @SuppressWarnings("unchecked")

    private void initComponents() {

        panel1 = new java.awt.Panel();
        MenuLabel = new java.awt.Label();
        ShowBooksButton = new javax.swing.JButton();
        ShowJournalsButton = new javax.swing.JButton();
        ShowAudioBooksButton = new javax.swing.JButton();
        ShowMembersButton = new javax.swing.JButton();
        LogoutButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        MenuLabel.setFont(new java.awt.Font("Georgia", 1, 14));
        MenuLabel.setText("Menu");

        ShowBooksButton.setText("Show the Books");
        ShowBooksButton.addActionListener(this::ShowBooksButtonActionPerformed);

        ShowJournalsButton.setText("Show the Journals");
        ShowJournalsButton.addActionListener(this::ShowJournalsButtonActionPerformed);

        ShowAudioBooksButton.setText("Show the Audio Books");
        ShowAudioBooksButton.addActionListener(this::ShowAudioBooksButtonActionPerformed);

        ShowMembersButton.setText("Show the Members");
        ShowMembersButton.addActionListener(this::ShowMembersButtonActionPerformed);

        LogoutButton.setText("Log Out");
        LogoutButton.addActionListener(this::LogoutButtonActionPerformed);

        javax.swing.GroupLayout panel1Layout = new javax.swing.GroupLayout(panel1);
        panel1.setLayout(panel1Layout);
        panel1Layout.setHorizontalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel1Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(MenuLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ShowBooksButton)
                    .addComponent(ShowJournalsButton)
                    .addComponent(ShowAudioBooksButton)
                    .addComponent(ShowMembersButton)
                    .addComponent(LogoutButton))
                .addContainerGap(40, Short.MAX_VALUE))
        );
        panel1Layout.setVerticalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(MenuLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(ShowBooksButton)
                .addGap(12, 12, 12)
                .addComponent(ShowJournalsButton)
                .addGap(12, 12, 12)
                .addComponent(ShowAudioBooksButton)
                .addGap(12, 12, 12)
                .addComponent(ShowMembersButton)
                .addGap(24, 24, 24)
                .addComponent(LogoutButton)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }
    
    /**
     * Method: ShowBooksButtonActionPerformed(ActionEvent)
     * Description: Handler "Show the Books" button -> open the Page_Book
     */
    private void ShowBooksButtonActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            Page_Book pb = new Page_Book();
            pb.setLocationRelativeTo(null);
            pb.setVisible(true);
            this.setVisible(false); // hide MenuPage
        } catch (Exception ex) {
            SwingUtilities.invokeLater(() -> javax.swing.JOptionPane.showMessageDialog(this, "Failed to load Page_Book: " + ex.getMessage()));
        }
    }

    /**
     * Method: ShowJournalsButtonActionPerformed(ActionEvent)
     * Description: Handler "Show the Journals" button -> open the Page_Journal
     */
    private void ShowJournalsButtonActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            Page_Journal pj = new Page_Journal();
            pj.setLocationRelativeTo(null);
            pj.setVisible(true);
            this.setVisible(false); // hide MenuPage
        } catch (Exception ex) {
            SwingUtilities.invokeLater(() -> javax.swing.JOptionPane.showMessageDialog(this, "Failed to load Page_Journal: " + ex.getMessage()));
        }
    }

    /**
     * Method: ShowAudioBooksButtonActionPerformed(ActionEvent)
     * Description: Handler "Show the Audio Books" button -> open the Page_Audio_Book
     */
    private void ShowAudioBooksButtonActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            Page_Audio_Book pa = new Page_Audio_Book();
            pa.setLocationRelativeTo(null);
            pa.setVisible(true);
            this.setVisible(false); // hide MenuPage
        } catch (Exception ex) {
            SwingUtilities.invokeLater(() -> javax.swing.JOptionPane.showMessageDialog(this, "Failed to load Page_Audio_Book: " + ex.getMessage()));
        }
    }

    /**
     * Method: ShowMembersButtonActionPerformed(ActionEvent)
     * Description: Handler "Show the Members" button -> open the Page_Member
     */
    private void ShowMembersButtonActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            Page_Member pm = new Page_Member();
            pm.setLocationRelativeTo(null);
            pm.setVisible(true);
            this.setVisible(false); // hide MenuPage
        } catch (Exception ex) {
            SwingUtilities.invokeLater(() -> javax.swing.JOptionPane.showMessageDialog(this, "Failed to load Page_Member: " + ex.getMessage()));
        }
    }

    /**
     * Method: LogoutButtonActionPerformed(ActionEvent)
     * Description: Handler "Log Out" button -> go back to LoginPage and close MenuPage
     */
    private void LogoutButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // go back to LoginPage
        LoginPage lp = new LoginPage();
        lp.setLocationRelativeTo(null);
        lp.setVisible(true);
        dispose(); // close MenuPage
    }

    /**
     * Method: main(String[])
     * Description: Application entry point
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            MenuPage mp = new MenuPage();
            mp.setLocationRelativeTo(null);
            mp.setVisible(true);
        });
    }
               
    private java.awt.Label MenuLabel;
    private javax.swing.JButton ShowAudioBooksButton;
    private javax.swing.JButton ShowBooksButton;
    private javax.swing.JButton ShowJournalsButton;
    private javax.swing.JButton ShowMembersButton;
    private javax.swing.JButton LogoutButton;
    private java.awt.Panel panel1;             
}