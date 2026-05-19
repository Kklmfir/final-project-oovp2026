/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package pkgfinal.project.oovp2026;
import java.sql.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import javax.swing.table.TableModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import net.proteanit.sql.DbUtils;
import com.toedter.calendar.JDateChooser;

import pkgfinal.project.oovp2026.MenuPage;
import pkgfinal.project.oovp2026.LoginPage;

public class Page_Journal extends javax.swing.JFrame {
    Connection conn = DBConnection.connect();
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Page_Journal.class.getName());
    private MenuPage parent = null;

    /**
     * Creates new form Page_Book
     */
    public Page_Journal() {
        initComponents();
        conn = DBConnection.connect();
        autoIDPublisher();
        loadPubTable();
        autoIDAuthor();
        loadAuthTable();
        autoIDInstitute();
        loadInstTable();
        autoIDJournal();
        loadJournalComboBoxes();
        loadJournalTable();
    }
    
    private void autoIDPublisher() {
        try {
            String sql = "SELECT MAX(Publisher_ID) FROM Publisher"; 
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
        
            if (rs.next()) {
                String maxID = rs.getString(1);
                if (maxID != null) {
                    int id = Integer.parseInt(maxID.substring(2)) + 1; 
                    Pub_ID.setText(String.format("PB%03d", id));
                } else {
                    Pub_ID.setText("PB001");
            }
        }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error AutoID Publisher: " + e.getMessage());
            }
    }
    
    private void autoIDJournal() {
        try {
            String sql = "SELECT MAX(Journal_ID) FROM journal";
            java.sql.PreparedStatement pst = conn.prepareStatement(sql);
            java.sql.ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            String maxID = rs.getString(1);
            if (maxID == null) {
                Journal_ID.setText("J001"); // Prefix J untuk Journal
            } else {
                int id = Integer.parseInt(maxID.substring(1)) + 1;
                Journal_ID.setText(String.format("J%03d", id));
            }
        }
        } catch (Exception e) {
            System.out.println("Error AutoID Journal: " + e.getMessage());
            }
    }

    private void loadJournalComboBoxes() {
        try {
            Journal_Institute.removeAllItems();
            String sqlInst = "SELECT Institute_ID, Institute_Name FROM institute";
            java.sql.PreparedStatement pstInst = conn.prepareStatement(sqlInst);
            java.sql.ResultSet rsInst = pstInst.executeQuery();
            while(rsInst.next()) {
                String item = rsInst.getString("Institute_ID") + "-" + rsInst.getString("Institute_Name");
                Journal_Institute.addItem(item);
        }
            Journal_Author.removeAllItems();
            String sqlAuth = "SELECT Author_Journal_ID, Author_Journal_Name FROM author_journal"; 
            java.sql.PreparedStatement pstAuth = conn.prepareStatement(sqlAuth);
            java.sql.ResultSet rsAuth = pstAuth.executeQuery();
            while(rsAuth.next()) {
                String item = rsAuth.getString("Author_Journal_ID") + "-" + rsAuth.getString("Author_Journal_Name");
                Journal_Author.addItem(item);
            }
            Journal_Publisher.removeAllItems();
            String sqlPub = "SELECT Publisher_ID, Publisher_Name FROM publisher";
            java.sql.PreparedStatement pstPub = conn.prepareStatement(sqlPub);
            java.sql.ResultSet rsPub = pstPub.executeQuery();
            while(rsPub.next()) {
                String item = rsPub.getString("Publisher_ID") + "-" + rsPub.getString("Publisher_Name");
                Journal_Publisher.addItem(item);
        }
        
        } catch (Exception e) {
            System.out.println("Error Load ComboBox Jurnal: " + e.getMessage());
            }
    }

    private void loadJournalTable() {
        try {
            String sql = "SELECT * FROM journal";
            java.sql.PreparedStatement pst = conn.prepareStatement(sql);
            java.sql.ResultSet rs = pst.executeQuery();
            Journal_Table.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {},
                new String [] {
                    "Journal ID", "Title", "Institute", "Author", "Publisher", "Year", "Link", "City", "Index"
                }
            ));
        } catch (Exception e) {
            System.out.println("Error Load Table Journal: " + e.getMessage());
            }
    }
    


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        LogOutButton = new java.awt.Button();
        MenuButton = new java.awt.Button();
        TabMenu = new javax.swing.JTabbedPane();
        Journal = new javax.swing.JPanel();
        label1 = new java.awt.Label();
        Journal_Title = new javax.swing.JTextField();
        label2 = new java.awt.Label();
        Journal_Institute = new javax.swing.JComboBox<>();
        label3 = new java.awt.Label();
        Journal_Author = new javax.swing.JComboBox<>();
        label4 = new java.awt.Label();
        Journal_Publisher = new javax.swing.JComboBox<>();
        label5 = new java.awt.Label();
        Journal_Year = new javax.swing.JTextField();
        label6 = new java.awt.Label();
        Journal_Link = new javax.swing.JTextField();
        label7 = new java.awt.Label();
        jScrollPane1 = new javax.swing.JScrollPane();
        Journal_Table = new javax.swing.JTable();
        Add_Journal = new javax.swing.JButton();
        Edit_Journal = new javax.swing.JButton();
        Refresh_Journal = new javax.swing.JButton();
        Delete_Journal = new javax.swing.JButton();
        label14 = new java.awt.Label();
        Journal_ID = new javax.swing.JTextField();
        label15 = new java.awt.Label();
        Journal_City = new javax.swing.JTextField();
        Journal_Index = new javax.swing.JTextField();
        Institute = new javax.swing.JPanel();
        label8 = new java.awt.Label();
        Institute_Name = new javax.swing.JTextField();
        label9 = new java.awt.Label();
        Institute_Contact = new javax.swing.JTextField();
        label18 = new java.awt.Label();
        Institute_City = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        Institute_Table = new javax.swing.JTable();
        Add_Institute = new javax.swing.JButton();
        Edit_Institute = new javax.swing.JButton();
        Refresh_Institute = new javax.swing.JButton();
        Delete_Institute = new javax.swing.JButton();
        label21 = new java.awt.Label();
        Institute_ID = new javax.swing.JTextField();
        Author = new javax.swing.JPanel();
        label10 = new java.awt.Label();
        Auth_ID = new javax.swing.JTextField();
        label11 = new java.awt.Label();
        Auth_Contact = new javax.swing.JTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        Auth_Table = new javax.swing.JTable();
        Add_Auth = new javax.swing.JButton();
        Edit_Auth = new javax.swing.JButton();
        Refresh_Auth = new javax.swing.JButton();
        Delete_Auth = new javax.swing.JButton();
        label20 = new java.awt.Label();
        Auth_Name = new javax.swing.JTextField();
        Publisher = new javax.swing.JPanel();
        label12 = new java.awt.Label();
        Pub_Name = new javax.swing.JTextField();
        label13 = new java.awt.Label();
        Pub_Contact = new javax.swing.JTextField();
        jScrollPane5 = new javax.swing.JScrollPane();
        Pub_Table = new javax.swing.JTable();
        Add_Pub = new javax.swing.JButton();
        Edit_Pub = new javax.swing.JButton();
        Refresh_Pub = new javax.swing.JButton();
        Delete_Pub = new javax.swing.JButton();
        label19 = new java.awt.Label();
        Pub_ID = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        LogOutButton.setLabel("Log Out");
        LogOutButton.addActionListener(this::LogOutButtonActionPerformed);

        MenuButton.setLabel("Back to Menu");

        TabMenu.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        label1.setText("Title");

        Journal_Title.addActionListener(this::Journal_TitleActionPerformed);

        label2.setText("Institute");

        Journal_Institute.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1- uni1", "2- uni2", "3- uni3" }));

        label3.setText("Author");

        Journal_Author.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1- Author1", "2- Author2", "3 Author3" }));

        label4.setText("Publisher");

        Journal_Publisher.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1- Pub1", "2- Pub2", "3- Pub3" }));

        label5.setText("Year");

        Journal_Year.addActionListener(this::Journal_YearActionPerformed);

        label6.setText("City");

        Journal_Link.addActionListener(this::Journal_LinkActionPerformed);

        label7.setText("Index");

        Journal_Table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Title", "Institute", "Author", "Publisher", "Year", "City", "Index", "Status"
            }
        ));
        Journal_Table.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        Journal_Table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Journal_TableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(Journal_Table);

        Add_Journal.setText("Add");
        Add_Journal.addActionListener(this::Add_JournalActionPerformed);

        Edit_Journal.setText("Edit");
        Edit_Journal.addActionListener(this::Edit_JournalActionPerformed);

        Refresh_Journal.setText("Refresh");
        Refresh_Journal.addActionListener(this::Refresh_JournalActionPerformed);

        Delete_Journal.setForeground(new java.awt.Color(204, 0, 51));
        Delete_Journal.setText("Delete");
        Delete_Journal.addActionListener(this::Delete_JournalActionPerformed);

        label14.setText("ID");

        Journal_ID.addActionListener(this::Journal_IDActionPerformed);

        label15.setText("Link");

        Journal_City.addActionListener(this::Journal_CityActionPerformed);

        Journal_Index.addActionListener(this::Journal_IndexActionPerformed);

        javax.swing.GroupLayout JournalLayout = new javax.swing.GroupLayout(Journal);
        Journal.setLayout(JournalLayout);
        JournalLayout.setHorizontalGroup(
            JournalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, JournalLayout.createSequentialGroup()
                .addGap(0, 17, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 548, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
            .addGroup(JournalLayout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addGroup(JournalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(JournalLayout.createSequentialGroup()
                        .addGroup(JournalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(JournalLayout.createSequentialGroup()
                                .addGroup(JournalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(JournalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(JournalLayout.createSequentialGroup()
                                            .addGroup(JournalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addGroup(JournalLayout.createSequentialGroup()
                                                    .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGap(37, 37, 37))
                                                .addGroup(JournalLayout.createSequentialGroup()
                                                    .addComponent(label3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGap(25, 25, 25)))
                                            .addGroup(JournalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(Journal_Institute, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(Journal_Title)
                                                .addComponent(Journal_Author, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(Journal_ID)))
                                        .addGroup(JournalLayout.createSequentialGroup()
                                            .addComponent(Add_Journal)
                                            .addGap(42, 42, 42)
                                            .addComponent(Edit_Journal)))
                                    .addComponent(label2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(67, 67, 67))
                            .addGroup(JournalLayout.createSequentialGroup()
                                .addComponent(label4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Journal_Publisher, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(JournalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(JournalLayout.createSequentialGroup()
                                .addComponent(Refresh_Journal)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(Delete_Journal))
                            .addGroup(JournalLayout.createSequentialGroup()
                                .addComponent(label6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(Journal_City, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(JournalLayout.createSequentialGroup()
                                .addComponent(label5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(Journal_Year, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(JournalLayout.createSequentialGroup()
                                .addComponent(label15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(17, 17, 17)
                                .addComponent(Journal_Link, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(JournalLayout.createSequentialGroup()
                                .addComponent(label7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Journal_Index, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        JournalLayout.setVerticalGroup(
            JournalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(JournalLayout.createSequentialGroup()
                .addGroup(JournalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(JournalLayout.createSequentialGroup()
                        .addGroup(JournalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(JournalLayout.createSequentialGroup()
                                .addGap(13, 13, 13)
                                .addComponent(label14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, JournalLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(Journal_ID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(JournalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Journal_Title, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(JournalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(label2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Journal_Institute, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(JournalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(label3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Journal_Author, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(JournalLayout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addGroup(JournalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(label5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Journal_Year, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)
                        .addGroup(JournalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(label6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Journal_City, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(JournalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(Journal_Link, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(label15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(JournalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Journal_Publisher, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Journal_Index, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                .addGroup(JournalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Add_Journal)
                    .addComponent(Edit_Journal)
                    .addComponent(Refresh_Journal)
                    .addComponent(Delete_Journal))
                .addGap(28, 28, 28)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );

        TabMenu.addTab("Journal", Journal);

        label8.setText("Name");

        Institute_Name.addActionListener(this::Institute_NameActionPerformed);

        label9.setText("Contact");

        label18.setText("City");

        Institute_Table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "Name", "Contact", "City"
            }
        ));
        Institute_Table.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        Institute_Table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Institute_TableMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(Institute_Table);

        Add_Institute.setText("Add");
        Add_Institute.addActionListener(this::Add_InstituteActionPerformed);

        Edit_Institute.setText("Edit");
        Edit_Institute.addActionListener(this::Edit_InstituteActionPerformed);

        Refresh_Institute.setText("Refresh");
        Refresh_Institute.addActionListener(this::Refresh_InstituteActionPerformed);

        Delete_Institute.setForeground(new java.awt.Color(204, 0, 51));
        Delete_Institute.setText("Delete");
        Delete_Institute.addActionListener(this::Delete_InstituteActionPerformed);

        label21.setText("ID");

        Institute_ID.addActionListener(this::Institute_IDActionPerformed);

        javax.swing.GroupLayout InstituteLayout = new javax.swing.GroupLayout(Institute);
        Institute.setLayout(InstituteLayout);
        InstituteLayout.setHorizontalGroup(
            InstituteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(InstituteLayout.createSequentialGroup()
                .addGap(73, 73, 73)
                .addComponent(Add_Institute)
                .addGap(42, 42, 42)
                .addComponent(Edit_Institute)
                .addGap(49, 49, 49)
                .addComponent(Refresh_Institute)
                .addGap(42, 42, 42)
                .addComponent(Delete_Institute)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(InstituteLayout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addGroup(InstituteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(InstituteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Institute_ID, javax.swing.GroupLayout.DEFAULT_SIZE, 163, Short.MAX_VALUE)
                    .addComponent(Institute_City)
                    .addComponent(Institute_Name, javax.swing.GroupLayout.DEFAULT_SIZE, 163, Short.MAX_VALUE)
                    .addComponent(Institute_Contact))
                .addGap(296, 296, 296))
            .addGroup(InstituteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(InstituteLayout.createSequentialGroup()
                    .addGap(14, 14, 14)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 548, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(17, Short.MAX_VALUE)))
        );
        InstituteLayout.setVerticalGroup(
            InstituteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(InstituteLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(InstituteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(Institute_ID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(InstituteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(InstituteLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(Institute_Name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, InstituteLayout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(label8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(InstituteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(Institute_Contact, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(InstituteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Institute_City, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(61, 61, 61)
                .addGroup(InstituteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Add_Institute)
                    .addComponent(Edit_Institute)
                    .addComponent(Refresh_Institute)
                    .addComponent(Delete_Institute))
                .addContainerGap(393, Short.MAX_VALUE))
            .addGroup(InstituteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(InstituteLayout.createSequentialGroup()
                    .addGap(253, 253, 253)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(52, Short.MAX_VALUE)))
        );

        TabMenu.addTab("Institute", Institute);

        label10.setText("Name");

        label11.setText("Contact");

        Auth_Table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "ID", "Name", "Contact"
            }
        ));
        Auth_Table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Auth_TableMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(Auth_Table);

        Add_Auth.setText("Add");
        Add_Auth.addActionListener(this::Add_AuthActionPerformed);

        Edit_Auth.setText("Edit");
        Edit_Auth.addActionListener(this::Edit_AuthActionPerformed);

        Refresh_Auth.setText("Refresh");
        Refresh_Auth.addActionListener(this::Refresh_AuthActionPerformed);

        Delete_Auth.setForeground(new java.awt.Color(204, 0, 51));
        Delete_Auth.setText("Delete");
        Delete_Auth.addActionListener(this::Delete_AuthActionPerformed);

        label20.setText("ID");

        javax.swing.GroupLayout AuthorLayout = new javax.swing.GroupLayout(Author);
        Author.setLayout(AuthorLayout);
        AuthorLayout.setHorizontalGroup(
            AuthorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AuthorLayout.createSequentialGroup()
                .addGroup(AuthorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(AuthorLayout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addGroup(AuthorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(label10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(label11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(label20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(21, 21, 21)
                        .addGroup(AuthorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Auth_Contact, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Auth_ID, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Auth_Name, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(AuthorLayout.createSequentialGroup()
                        .addGap(72, 72, 72)
                        .addComponent(Add_Auth)
                        .addGap(42, 42, 42)
                        .addComponent(Edit_Auth)
                        .addGap(47, 47, 47)
                        .addComponent(Refresh_Auth)
                        .addGap(42, 42, 42)
                        .addComponent(Delete_Auth))
                    .addGroup(AuthorLayout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 540, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        AuthorLayout.setVerticalGroup(
            AuthorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AuthorLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(AuthorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Auth_ID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(AuthorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Auth_Name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(AuthorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Auth_Contact, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(AuthorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Add_Auth)
                    .addComponent(Edit_Auth)
                    .addComponent(Refresh_Auth)
                    .addComponent(Delete_Auth))
                .addGap(26, 26, 26)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 416, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(37, Short.MAX_VALUE))
        );

        TabMenu.addTab("Author", Author);

        label12.setText("Name");

        label13.setText("Contact");

        Pub_Table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "ID", "Name", "Contact"
            }
        ));
        Pub_Table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Pub_TableMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(Pub_Table);

        Add_Pub.setText("Add");
        Add_Pub.addActionListener(this::Add_PubActionPerformed);

        Edit_Pub.setText("Edit");
        Edit_Pub.addActionListener(this::Edit_PubActionPerformed);

        Refresh_Pub.setText("Refresh");
        Refresh_Pub.addActionListener(this::Refresh_PubActionPerformed);

        Delete_Pub.setForeground(new java.awt.Color(204, 0, 51));
        Delete_Pub.setText("Delete");
        Delete_Pub.addActionListener(this::Delete_PubActionPerformed);

        label19.setText("ID");

        javax.swing.GroupLayout PublisherLayout = new javax.swing.GroupLayout(Publisher);
        Publisher.setLayout(PublisherLayout);
        PublisherLayout.setHorizontalGroup(
            PublisherLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PublisherLayout.createSequentialGroup()
                .addGroup(PublisherLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PublisherLayout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addGroup(PublisherLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(label12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(label13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(label19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(21, 21, 21)
                        .addGroup(PublisherLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Pub_Contact, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Pub_Name, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Pub_ID, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(PublisherLayout.createSequentialGroup()
                        .addGap(72, 72, 72)
                        .addComponent(Add_Pub)
                        .addGap(42, 42, 42)
                        .addComponent(Edit_Pub)
                        .addGap(47, 47, 47)
                        .addComponent(Refresh_Pub)
                        .addGap(42, 42, 42)
                        .addComponent(Delete_Pub))
                    .addGroup(PublisherLayout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 540, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(22, Short.MAX_VALUE))
        );
        PublisherLayout.setVerticalGroup(
            PublisherLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PublisherLayout.createSequentialGroup()
                .addGroup(PublisherLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PublisherLayout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(label19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PublisherLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(Pub_ID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PublisherLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Pub_Name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(PublisherLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(label13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Pub_Contact, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PublisherLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Add_Pub)
                    .addComponent(Edit_Pub)
                    .addComponent(Refresh_Pub)
                    .addComponent(Delete_Pub))
                .addGap(26, 26, 26)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 416, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(43, Short.MAX_VALUE))
        );

        TabMenu.addTab("Publisher", Publisher);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(TabMenu)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(MenuButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(LogOutButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(TabMenu, javax.swing.GroupLayout.DEFAULT_SIZE, 650, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(MenuButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LogOutButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void Delete_PubActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Delete_PubActionPerformed
        try {
            int confirm = JOptionPane.showConfirmDialog(null, "Yakin mau hapus publisher ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (confirm == 0) {
                String sql = "DELETE FROM publisher WHERE Publisher_ID=?";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, Pub_ID.getText());
                pst.executeUpdate();

                JOptionPane.showMessageDialog(null, "Publisher Dihapus!");
                loadPubTable();
                loadJournalComboBoxes();
                autoIDPublisher();
                
                Pub_Name.setText("");
                Pub_Contact.setText("");
                
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal Delete: " + e.getMessage());
        }
    }//GEN-LAST:event_Delete_PubActionPerformed

    private void Refresh_PubActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Refresh_PubActionPerformed
        loadPubTable();
        autoIDPublisher();
        Pub_Name.setText("");
        Pub_Contact.setText("");
    }//GEN-LAST:event_Refresh_PubActionPerformed

    private void Edit_PubActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Edit_PubActionPerformed
        try {
            String sql = "UPDATE publisher SET Publisher_Name=?, Publisher_Contact=? WHERE Publisher_ID=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, Pub_Name.getText());
            pst.setString(2, Pub_Contact.getText());
            pst.setString(3, Pub_ID.getText());

            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data Publisher Diupdate!");
            loadPubTable();
            loadJournalComboBoxes();
            
        }catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal Edit: " + e.getMessage());
        }
    }//GEN-LAST:event_Edit_PubActionPerformed

    private void Add_PubActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Add_PubActionPerformed
        try {
            String sql = "INSERT INTO publisher (Publisher_ID, Publisher_Name, Publisher_Contact) VALUES (?,?,?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, Pub_ID.getText());
            pst.setString(2, Pub_Name.getText());
            pst.setString(3, Pub_Contact.getText());

            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Publisher Baru Berhasil Ditambahkan");

            loadPubTable();
            autoIDPublisher();
            loadJournalComboBoxes();
            
            Pub_Name.setText("");
            Pub_Contact.setText("");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal Add: " + e.getMessage());
        }
    }//GEN-LAST:event_Add_PubActionPerformed

    private void Pub_TableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Pub_TableMouseClicked
        int row = Pub_Table.getSelectedRow();
        String id = Pub_Table.getValueAt(row, 0).toString(); // Ambil ID
        String name = Pub_Table.getValueAt(row, 1).toString();
        String contact = Pub_Table.getValueAt(row, 2).toString();

        Pub_ID.setText(id);
        Pub_Name.setText(name);
        Pub_Contact.setText(contact);
    }//GEN-LAST:event_Pub_TableMouseClicked

    private void Delete_AuthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Delete_AuthActionPerformed
        int confirm = JOptionPane.showConfirmDialog(null, "Hapus Author ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String sql = "DELETE FROM author_journal WHERE Author_Journal_ID = ?";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, Auth_ID.getText());

                pst.executeUpdate();
                loadAuthTable();
                autoIDAuthor();
                loadJournalComboBoxes();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Gagal Hapus: " + e.getMessage());
            }
        }
    }//GEN-LAST:event_Delete_AuthActionPerformed

    private void Refresh_AuthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Refresh_AuthActionPerformed
        Auth_Name.setText("");
        Auth_Contact.setText("");
        loadAuthTable();
        autoIDAuthor();
    }//GEN-LAST:event_Refresh_AuthActionPerformed

    private void Edit_AuthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Edit_AuthActionPerformed
        try {
            String sql = "UPDATE author_journal SET Author_Journal_Name = ?, Author_Journal_Contact = ? WHERE Author_Journal_ID = ?";
            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setString(1, Auth_Name.getText());
            pst.setString(2, Auth_Contact.getText());
            pst.setString(3, Auth_ID.getText());

            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data Publisher Diperbarui!");

            loadAuthTable();
            loadJournalComboBoxes();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }//GEN-LAST:event_Edit_AuthActionPerformed

    private void Add_AuthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Add_AuthActionPerformed
        try {
            String sql = "INSERT INTO author_journal (Author_Journal_ID, Author_Journal_Name, Author_Journal_Contact) VALUES (?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, Auth_ID.getText());
            pst.setString(2, Auth_Name.getText());
            pst.setString(3, Auth_Contact.getText());

            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Author Berhasil Ditambahkan!");

            loadAuthTable();
            autoIDAuthor();
            loadJournalComboBoxes();

            Auth_Name.setText("");
            Auth_Contact.setText("");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal Add Author: " + e.getMessage());
        }
    }//GEN-LAST:event_Add_AuthActionPerformed

    private void Auth_TableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Auth_TableMouseClicked
        int row = Auth_Table.getSelectedRow();
        Auth_ID.setText(Auth_Table.getValueAt(row, 0).toString());
        Auth_Name.setText(Auth_Table.getValueAt(row, 1).toString());
        Auth_Contact.setText(Auth_Table.getValueAt(row, 2).toString());
    }//GEN-LAST:event_Auth_TableMouseClicked

    private void Institute_IDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Institute_IDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Institute_IDActionPerformed

    private void Delete_InstituteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Delete_InstituteActionPerformed
        int confirm = JOptionPane.showConfirmDialog(null, "Hapus Institute ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String sql = "DELETE FROM institute WHERE Institute_ID = ?";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, Institute_ID.getText());

                pst.executeUpdate();
                loadInstTable();
                autoIDInstitute();
                loadJournalComboBoxes();

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Gagal Hapus: " + e.getMessage());
            }
        }
    }//GEN-LAST:event_Delete_InstituteActionPerformed

    private void Refresh_InstituteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Refresh_InstituteActionPerformed
        Institute_Name.setText("");
        Institute_Contact.setText("");
        Institute_City.setText("");
        loadInstTable();
        autoIDInstitute();
    }//GEN-LAST:event_Refresh_InstituteActionPerformed

    private void Edit_InstituteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Edit_InstituteActionPerformed
        try {
            String sql = "UPDATE institute SET Institute_Name = ?, Institute_Contact = ?, Institute_City = ? WHERE Institute_ID = ?";
            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setString(1, Institute_Name.getText());
            pst.setString(2, Institute_Contact.getText());
            pst.setString(3, Institute_City.getText());
            pst.setString(4, Institute_ID.getText());

            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data Institute Diperbarui!");

            loadInstTable();
            loadJournalComboBoxes();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }//GEN-LAST:event_Edit_InstituteActionPerformed

    private void Add_InstituteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Add_InstituteActionPerformed
        try {
            String sql = "INSERT INTO institute (Institute_ID, Institute_Name, Institute_Contact, Institute_City) VALUES (?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, Institute_ID.getText());
            pst.setString(2, Institute_Name.getText());
            pst.setString(3, Institute_Contact.getText());
            pst.setString(4, Institute_City.getText());

            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Author Berhasil Ditambahkan!");

            loadJournalComboBoxes();
            autoIDInstitute();
            loadInstTable();

            Auth_Name.setText("");
            Auth_Contact.setText("");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal Add Author: " + e.getMessage());
        }
    }//GEN-LAST:event_Add_InstituteActionPerformed

    private void Institute_TableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Institute_TableMouseClicked
        int row = Institute_Table.getSelectedRow();
        Institute_ID.setText(Institute_Table.getValueAt(row, 0).toString());
        Institute_Name.setText(Institute_Table.getValueAt(row, 1).toString());
        Institute_Contact.setText(Institute_Table.getValueAt(row, 2).toString());
        Institute_City.setText(Institute_Table.getValueAt(row, 3).toString());
    }//GEN-LAST:event_Institute_TableMouseClicked

    private void Institute_NameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Institute_NameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Institute_NameActionPerformed

    private void Journal_TableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Journal_TableMouseClicked
        int row = Journal_Table.getSelectedRow();
        Journal_ID.setText(Journal_Table.getValueAt(row, 0).toString());
        Journal_Title.setText(Journal_Table.getValueAt(row, 1).toString());
        Journal_Year.setText(Journal_Table.getValueAt(row, 2).toString());
        Journal_Author.setSelectedItem(Journal_Table.getValueAt(row, 3).toString());
        Journal_Publisher.setSelectedItem(Journal_Table.getValueAt(row, 4).toString());
        Journal_Institute.setSelectedItem(Journal_Table.getValueAt(row, 5).toString());
        Journal_City.setText(Journal_Table.getValueAt(row, 6).toString());
        Journal_Index.setText(Journal_Table.getValueAt(row, 7).toString());
        Journal_Link.setText(Journal_Table.getValueAt(row, 8).toString());
    }//GEN-LAST:event_Journal_TableMouseClicked

    private void Journal_LinkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Journal_LinkActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Journal_LinkActionPerformed

    private void Journal_YearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Journal_YearActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Journal_YearActionPerformed

    private void Journal_TitleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Journal_TitleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Journal_TitleActionPerformed

    private void Journal_IDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Journal_IDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Journal_IDActionPerformed

    private void Journal_CityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Journal_CityActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Journal_CityActionPerformed

    private void Add_JournalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Add_JournalActionPerformed
        try {
            
            String authorID = Journal_Author.getSelectedItem().toString().split("-")[0];
            String instituteID = Journal_Institute.getSelectedItem().toString().split("-")[0];
            String publisherID = Journal_Publisher.getSelectedItem().toString().split("-")[0];
    
            String sql = "INSERT INTO journal (Journal_ID, Journal_Title, Journal_Year, Journal_Author_ID, Journal_Publisher_ID, Journal_Institute_ID, Journal_City, Journal_Index, Link) VALUES (?,?,?,?,?,?,?,?,?)";
            java.sql.PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, Journal_ID.getText());
            pst.setString(2, Journal_Title.getText());
            pst.setString(3, Journal_Year.getText());
            pst.setString(4, authorID);
            pst.setString(5, publisherID);
            pst.setString(6, instituteID);
            pst.setString(7, Journal_City.getText());
            pst.setString(8, Journal_Index.getText());
            pst.setString(9, Journal_Link.getText());
    
            pst.executeUpdate();
            javax.swing.JOptionPane.showMessageDialog(null, "Jurnal Online Berhasil Ditambahkan!");
    

            loadJournalTable();
            autoIDJournal();
            Journal_Title.setText("");
            Journal_Year.setText("");
            Journal_City.setText("");
            Journal_Index.setText("");
            Journal_Link.setText("");
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(null, "Gagal Add Jurnal: " + e.getMessage());
            }
    }//GEN-LAST:event_Add_JournalActionPerformed

    private void Edit_JournalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Edit_JournalActionPerformed
        try {
            String sql = "UPDATE journal SET Journal_Title=?, Journal_Institute_ID=?, Journal_Author_ID=?, Journal_Publisher_ID=?, Journal_Year=?, Journal_City=?, Journal_Index=?, Link=? WHERE Journal_ID=?";
            java.sql.PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, Journal_Title.getText());
            pst.setString(2, Journal_Institute.getSelectedItem().toString().split("-")[0]);
            pst.setString(3, Journal_Author.getSelectedItem().toString().split("-")[0]);
            pst.setString(4, Journal_Publisher.getSelectedItem().toString().split("-")[0]);
            pst.setString(5, Journal_Year.getText());
            pst.setString(6, Journal_City.getText());
            pst.setString(7, Journal_Index.getText());
            pst.setString(8, Journal_Link.getText());
            pst.setString(9, Journal_ID.getText());
    
            pst.executeUpdate();
            javax.swing.JOptionPane.showMessageDialog(null, "Data Jurnal Diupdate!");
            loadJournalTable();
            } catch (Exception e) {
                javax.swing.JOptionPane.showMessageDialog(null, "Gagal Edit Jurnal: " + e.getMessage());
            }
    }//GEN-LAST:event_Edit_JournalActionPerformed

    private void Delete_JournalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Delete_JournalActionPerformed
        try {
            int confirm = javax.swing.JOptionPane.showConfirmDialog(null, "Hapus jurnal ini?", "Konfirmasi", javax.swing.JOptionPane.YES_NO_OPTION);
            if (confirm == 0) {
                String sql = "DELETE FROM journal WHERE Journal_ID=?";
                java.sql.PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, Journal_ID.getText());
                pst.executeUpdate();
                javax.swing.JOptionPane.showMessageDialog(null, "Jurnal Berhasil Dihapus!");
                loadJournalTable();
                autoIDJournal();
                Journal_Title.setText("");
                Journal_Year.setText("");
                Journal_City.setText("");
                Journal_Index.setText("");
                Journal_Link.setText("");
            }
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(null, "Gagal Delete Jurnal: " + e.getMessage());
        }
    }//GEN-LAST:event_Delete_JournalActionPerformed

    private void Refresh_JournalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Refresh_JournalActionPerformed
        loadJournalTable();
        autoIDJournal();
        loadJournalComboBoxes();
        Journal_Title.setText("");
        Journal_Year.setText("");
        Journal_City.setText("");
        Journal_Index.setText("");
        Journal_Link.setText("");
    }//GEN-LAST:event_Refresh_JournalActionPerformed

    private void Journal_IndexActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Journal_IndexActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Journal_IndexActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
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
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new Page_Journal().setVisible(true));
    }

    /**
     * Class: DbUtils
     * @param rs
     * @return
     */
    public class DbUtils {
    public static TableModel resultSetToTableModel(ResultSet rs) {
            throw new UnsupportedOperationException("Unimplemented method 'resultSetToTableModel'");
        }
    }

    private void loadPubTable() {
        try {
            String sql = "SELECT * FROM publisher";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            Pub_Table.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            System.out.println("Error Load Table Pub: " + e.getMessage());
            }
    }
    
    private void loadAuthTable() {
        try {
            String sql = "SELECT * FROM author_journal"; 
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            Auth_Table.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error Load Author: " + e.getMessage());
            }
    }
    
    private void autoIDAuthor() {
        try {
            String sql = "SELECT MAX(Author_Journal_ID) FROM author_journal";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                String maxID = rs.getString(1);
                if (maxID != null) {
                    int id = Integer.parseInt(maxID.substring(2)) + 1;
                    Auth_ID.setText(String.format("AU%03d", id));
                } else { Auth_ID.setText("AU001"); }
            }
            Auth_ID.setEditable(false);
            } catch (Exception e) { System.out.println(e.getMessage()); }
    }
    
    private void autoIDInstitute() {
        try {
            String sql = "SELECT MAX(Institute_ID) FROM institute";
            java.sql.PreparedStatement pst = conn.prepareStatement(sql);
            java.sql.ResultSet rs = pst.executeQuery();
        
        if (rs.next()) {
            String maxID = rs.getString(1);
            if (maxID == null) {
                Institute_ID.setText("I001"); // Prefix I untuk Institute
            } else {
                int id = Integer.parseInt(maxID.substring(1)) + 1;
                Institute_ID.setText(String.format("I%03d", id));
            }
        }
        Institute_ID.setEditable(false); 
        } catch (Exception e) {
            System.out.println("Error AutoID Institute: " + e.getMessage());
        }
    }

    private void loadInstTable() {
        try {
            String sql = "SELECT * FROM institute";
            java.sql.PreparedStatement pst = conn.prepareStatement(sql);
            java.sql.ResultSet rs = pst.executeQuery();
            Institute_Table.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            System.out.println("Error Load Table Institute: " + e.getMessage());
        }
    }

    private void MenuButtonMouseClicked(java.awt.event.MouseEvent evt) {
        if (parent != null) {
            parent.setLocationRelativeTo(null);
            parent.setVisible(true);
        } else {
            MenuPage menu = new MenuPage();
            menu.setLocationRelativeTo(null);
            menu.setVisible(true);
        }
        this.dispose();
    }

    private void LogOutButtonActionPerformed(java.awt.event.ActionEvent evt) {
        LoginPage lp = new LoginPage();
        lp.setLocationRelativeTo(null);
        lp.setVisible(true);

        if (parent != null) {
            parent.dispose(); // close hidden MenuPage if available
        }
        this.dispose(); // close Page_Book
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Add_Auth;
    private javax.swing.JButton Add_Institute;
    private javax.swing.JButton Add_Journal;
    private javax.swing.JButton Add_Pub;
    private javax.swing.JTextField Auth_Contact;
    private javax.swing.JTextField Auth_ID;
    private javax.swing.JTextField Auth_Name;
    private javax.swing.JTable Auth_Table;
    private javax.swing.JPanel Author;
    private javax.swing.JButton Delete_Auth;
    private javax.swing.JButton Delete_Institute;
    private javax.swing.JButton Delete_Journal;
    private javax.swing.JButton Delete_Pub;
    private javax.swing.JButton Edit_Auth;
    private javax.swing.JButton Edit_Institute;
    private javax.swing.JButton Edit_Journal;
    private javax.swing.JButton Edit_Pub;
    private javax.swing.JPanel Institute;
    private javax.swing.JTextField Institute_City;
    private javax.swing.JTextField Institute_Contact;
    private javax.swing.JTextField Institute_ID;
    private javax.swing.JTextField Institute_Name;
    private javax.swing.JTable Institute_Table;
    private javax.swing.JPanel Journal;
    private javax.swing.JComboBox<String> Journal_Author;
    private javax.swing.JTextField Journal_City;
    private javax.swing.JTextField Journal_ID;
    private javax.swing.JTextField Journal_Index;
    private javax.swing.JComboBox<String> Journal_Institute;
    private javax.swing.JTextField Journal_Link;
    private javax.swing.JComboBox<String> Journal_Publisher;
    private javax.swing.JTable Journal_Table;
    private javax.swing.JTextField Journal_Title;
    private javax.swing.JTextField Journal_Year;
    private java.awt.Button LogOutButton;
    private java.awt.Button MenuButton;
    private javax.swing.JTextField Pub_Contact;
    private javax.swing.JTextField Pub_ID;
    private javax.swing.JTextField Pub_Name;
    private javax.swing.JTable Pub_Table;
    private javax.swing.JPanel Publisher;
    private javax.swing.JButton Refresh_Auth;
    private javax.swing.JButton Refresh_Institute;
    private javax.swing.JButton Refresh_Journal;
    private javax.swing.JButton Refresh_Pub;
    private javax.swing.JTabbedPane TabMenu;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private java.awt.Label label1;
    private java.awt.Label label10;
    private java.awt.Label label11;
    private java.awt.Label label12;
    private java.awt.Label label13;
    private java.awt.Label label14;
    private java.awt.Label label15;
    private java.awt.Label label18;
    private java.awt.Label label19;
    private java.awt.Label label2;
    private java.awt.Label label20;
    private java.awt.Label label21;
    private java.awt.Label label3;
    private java.awt.Label label4;
    private java.awt.Label label5;
    private java.awt.Label label6;
    private java.awt.Label label7;
    private java.awt.Label label8;
    private java.awt.Label label9;
    // End of variables declaration//GEN-END:variables
}
