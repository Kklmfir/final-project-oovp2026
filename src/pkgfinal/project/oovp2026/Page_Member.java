/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package pkgfinal.project.oovp2026;

import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import net.proteanit.sql.DbUtils;

/**
 * Class: Page_Member (JFrame)
 */
public class Page_Member extends javax.swing.JFrame {
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Page_Member.class.getName());
    private static final int FINE_BASE_RP_PER_DAY = 10_000; // Base fine in Rp per day
    private MenuPage parent = null;
    private final SimpleDateFormat uiDateFormat = new SimpleDateFormat("dd/MM/yyyy"); // For displaying dates in the UI
    private final DecimalFormat moneyFormat = new DecimalFormat("#,##0"); // For formatting money values

    /**
     * Constructor: Page_Member()
     */
    public Page_Member() {
        initComponents();
        initRuntimeLogic();
    }
    
    /**
     * Constructor overload: Page_Member(menuPage parent)
     * @param parent The parent menu page to return to when navigating back
     */
    public Page_Member(MenuPage parent) {
        this.parent = parent;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only this window
    }

    /**
     * Method: initRuntimeLogic()
     * Description: Initialize runtime logic, set date default, load table, and auto calc fine
     */
    private void initRuntimeLogic() {
        Calendar cal = Calendar.getInstance();
        Mem_Reg_Date.setText(uiDateFormat.format(cal.getTime()));
        reloadAllData();
        
        Mem_Member_Type.addActionListener(this::MemMemberTypeChanged); // action listener for member type change to recalculate return date
        MemMemberTypeChanged(null); // set initial return date based on default member type

        MenuButton.addActionListener(this::MenuButtonActionPerformed); // action listener for menu button to navigate back to menu
        LogOutButton.addActionListener(this::LogOutButtonActionPerformed); // action listener for log out button to navigate back to login
    }

    /**
     * Method: reloadAllData()
     * Description: Reload all data for members and member types
     */
    private void reloadAllData() {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                loadMemberTypeComboFromDb();
                loadMemberTable();
                loadMemberTypeTable();
                return null;
            }
            @Override
            protected void done() {
                try {
                    get(); // to catch any exceptions from doInBackground
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "Error while loading data: ", e);
                    JOptionPane.showMessageDialog(Page_Member.this, "Error while loading data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }.execute();
    }
    
    /* 
     * Method: MemMemberTypeChanged(ActionEvent evt)
     * Description: Recalculate return date when member type changes
     */
    private void loadMemberTypeComboFromDb() throws SQLException {
        String sql = "SELECT Member_Type_ID, Type_Name FROM Member_Type ORDER BY Member_Type_ID";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            Mem_Member_Type.removeAllItems();
            while (rs.next()) {
                String id = rs.getString("Member_Type_ID");
                String name = rs.getString("Type_Name");
                Mem_Member_Type.addItem(id + "-" + name);
            }
        }
    }

    /**
     * Method: loadMemberTable()
     * Description: Load member data into the member table
     */
    private void loadMemberTable() throws SQLException {
        String sql =
            "SELECT m.Member_ID, m.Member_Name, m.Register_Date, mt.Type_Name, m.Identifier, m.Email, m.Phone " +
            "FROM Member m " +
            "JOIN Member_Type mt ON m.Member_Type_ID = mt.Member_Type_ID " +
            "ORDER BY m.Member_ID";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            Mem_Table.setModel(DbUtils.resultSetToTableModel(rs));
        }
    }

    /**
     * Method: loadMemberTypeTable()
     * Description: Load member type data into the member type table
     */
    private void loadMemberTypeTable() throws SQLException {
        String sql =
            "SELECT Member_Type_ID, Type_Name, Loan_Days, Loan_Limit, Fine_Per_Day " +
            "FROM Member_Type ORDER BY Member_Type_ID";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            MT_Table.setModel(DbUtils.resultSetToTableModel(rs));
        }
    }

    /**
     *  Method: nextMemberId()
     * Description: autoID generater for Member_ID
     */
    private String nextMemberId() throws SQLException {
        String sql = "SELECT MAX(Member_ID) AS MaxID FROM Member";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                String maxId = rs.getString("MaxID");
                if (maxId == null) return "M001";
                int n = Integer.parseInt(maxId.substring(1)) + 1;
                return String.format("M%03d", n);
            }
            return "M001";
        }
    }

    /**
     * Method: nextMemberTypeId()
     * Description: autoID generater for Member_Type_ID
     */
    private String nextMemberTypeId() throws SQLException {
        String sql = "SELECT MAX(Member_Type_ID) AS MaxID FROM Member_Type";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                String maxId = rs.getString("MaxID");
                if (maxId == null) return "MT001";
                int n = Integer.parseInt(maxId.substring(2)) + 1;
                return String.format("MT%03d", n);
            }
            return "MT001";
        }
    }

    /**
     * Method: MemMemberTypeChanged(ActionEvent)
     * Description: Recalculate return date based on member type selection
     */
    private void MemMemberTypeChanged(ActionEvent evt) {
        Object selected = Mem_Member_Type.getSelectedItem();
        if (selected == null) return;

        String item = selected.toString(); // format: ID-Name
        String memberTypeId = item.split("-")[0].trim();

        new SwingWorker<Void, Void>() {
            double finePerDayRatio = 0.0;
            int finePerDayRp = 0;

            @Override
            protected Void doInBackground() throws Exception {
                String sql = "SELECT Fine_Per_Day FROM Member_Type WHERE Member_Type_ID = ?";
                try (Connection conn = DBConnection.getConnection();
                     PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, memberTypeId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            finePerDayRatio = rs.getDouble("Fine_Per_Day");
                            finePerDayRp = (int) Math.round(finePerDayRatio * FINE_BASE_RP_PER_DAY);
                        }
                    }
                }
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    label9.setText("Rp " + moneyFormat.format(finePerDayRp) + "/day (rate=" + finePerDayRatio + ")");
                } catch (Exception ex) {
                    logger.log(Level.SEVERE, null, ex);
                    label9.setText("Rp - /day");
                }
            }
        }.execute();
    }

    /**
     * Method: Add_MemActionPerformed(ActionEvent)
     * Description: Handle add member button click, validate input, and insert new member into database
     */
    private void Add_MemActionPerformed(java.awt.event.ActionEvent evt) {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                String memberId = nextMemberId();
                String name = Mem_Name.getText().trim();
                String regDateStr = Mem_Reg_Date.getText().trim();
                String memberTypeItem = (String) Mem_Member_Type.getSelectedItem();
                String memberTypeId = memberTypeItem != null ? memberTypeItem.split("-")[0].trim() : null;

                String identifier = (String) Mem_Identifier.getSelectedItem();
                String email = Mem_Email.getText().trim();
                String phone = Mem_Phone.getText().trim();

                if (name.isEmpty() || memberTypeId == null || memberTypeId.isEmpty()) {
                    throw new IllegalArgumentException("Name and Member Type must be filled.");
                }

                // parse dd/MM/yyyy -> DATETIME (set time 00:00:00)
                java.util.Date parsed = uiDateFormat.parse(regDateStr);
                java.sql.Timestamp regTs = new java.sql.Timestamp(parsed.getTime());

                String sql =
                    "INSERT INTO Member (Member_ID, Member_Name, Register_Date, Member_Type_ID, Identifier, Email, Phone) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

                try (Connection conn = DBConnection.getConnection();
                     PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, memberId);
                    ps.setString(2, name);
                    ps.setTimestamp(3, regTs);
                    ps.setString(4, memberTypeId);
                    ps.setString(5, identifier);
                    ps.setString(6, email.isEmpty() ? null : email);
                    ps.setString(7, phone.isEmpty() ? null : phone);
                    ps.executeUpdate();
                }

                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    JOptionPane.showMessageDialog(Page_Member.this, "Member successfully added.");
                    clearMemberInputs();
                    reloadAllData();
                } catch (Exception ex) {
                    logger.log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(Page_Member.this, "Failed to add member: " + ex.getMessage());
                }
            }
        }.execute();
    }

    /**
     * Method: clearMemberInputs()
     * Description: Clear input fields after adding a member
     */
    private void clearMemberInputs() {
        Mem_Name.setText("");
        Calendar cal = Calendar.getInstance();
        Mem_Reg_Date.setText(uiDateFormat.format(cal.getTime()));
        if (Mem_Member_Type.getItemCount() > 0) Mem_Member_Type.setSelectedIndex(0);
        if (Mem_Identifier.getItemCount() > 0) Mem_Identifier.setSelectedIndex(0);
        Mem_Email.setText("");
        Mem_Phone.setText("");
    }

    /**
     * Method: Add_MemActionPerformed(ActionEvent evt)
     * Description: Handle add member button click, validate input, and insert new member into database
     */
    private void Add_MTActionPerformed(java.awt.event.ActionEvent evt) {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                String mtId = nextMemberTypeId();
                String typeName = MT_Name.getText().trim();
                if (typeName.isEmpty()) throw new IllegalArgumentException("Type Name must be filled.");

                int loanDays = parseLoanDays((String) MT_Loan_Day.getSelectedItem());
                int loanLimit = parseLoanLimit((String) MT_Loan_Limit.getSelectedItem());
                double fineRatio = parseFineRatio((String) MT_Fine.getSelectedItem());

                String sql =
                    "INSERT INTO Member_Type (Member_Type_ID, Type_Name, Loan_Days, Loan_Limit, Fine_Per_Day) " +
                    "VALUES (?, ?, ?, ?, ?)";

                try (Connection conn = DBConnection.getConnection();
                     PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, mtId);
                    ps.setString(2, typeName);
                    ps.setInt(3, loanDays);
                    ps.setInt(4, loanLimit);
                    ps.setDouble(5, fineRatio);
                    ps.executeUpdate();
                }
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                    JOptionPane.showMessageDialog(Page_Member.this, "Member Type successfully added.");
                    clearMemberTypeInputs();
                    reloadAllData(); // reload combo & table
                } catch (Exception ex) {
                    logger.log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(Page_Member.this, "Failed to add Member Type: " + ex.getMessage());
                }
            }
        }.execute();
    }

    private int parseLoanDays(String s) {
        if (s == null) return 14;
        return Integer.parseInt(s.trim().split(" ")[0]); // from "7 Days" into 7
    }

    private int parseLoanLimit(String s) {
        if (s == null) return 3;
        return Integer.parseInt(s.trim().split(" ")[0]); // from "3 Books" into 3
    }

    private double parseFineRatio(String s) {
        if (s == null) return 0.5;
        String x = s.trim().replace("%", ""); // from "0.50%" into "0.50"
        return Double.parseDouble(x);
    }

    /**
     * Method: clearMemberTypeInputs()
     * Description: Clear input tab Member Type.
     */
    private void clearMemberTypeInputs() {
        MT_Name.setText("");
        if (MT_Loan_Day.getItemCount() > 0) MT_Loan_Day.setSelectedIndex(0);
        if (MT_Loan_Limit.getItemCount() > 0) MT_Loan_Limit.setSelectedIndex(0);
        if (MT_Fine.getItemCount() > 0) MT_Fine.setSelectedIndex(0);
    }

    /**
     * Method: MenuButtonActionPerformed(ActionEvent)
     * Description: Handle menu button click to navigate back to menu page
     */
    private void MenuButtonActionPerformed(ActionEvent evt) {
        if (parent != null) {
            parent.setLocationRelativeTo(null);
            parent.setVisible(true);
        } else {
            MenuPage mp = new MenuPage();
            mp.setLocationRelativeTo(null);
            mp.setVisible(true);
        }
        dispose(); // close current window
    }

    /**
     * Method: LogOutButtonActionPerformed(ActionEvent)
     * Description: Handle log out button click to navigate back to login page
     */
    private void LogOutButtonActionPerformed(ActionEvent evt) {
        LoginPage lp = new LoginPage();
        lp.setLocationRelativeTo(null);
        lp.setVisible(true);

        if (parent != null) parent.dispose(); // close parent menu if exists
        dispose();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        LogOutButton = new java.awt.Button();
        MenuButton = new java.awt.Button();
        TabMenu = new javax.swing.JTabbedPane();
        Member = new javax.swing.JPanel();
        label10 = new java.awt.Label();
        Mem_Name = new javax.swing.JTextField();
        label11 = new java.awt.Label();
        Mem_Reg_Date = new javax.swing.JTextField();
        label1 = new java.awt.Label();
        Mem_Member_Type = new javax.swing.JComboBox<>();
        label2 = new java.awt.Label();
        Mem_Identifier = new javax.swing.JComboBox<>();
        label3 = new java.awt.Label();
        Mem_Email = new javax.swing.JTextField();
        label4 = new java.awt.Label();
        Mem_Phone = new javax.swing.JTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        Mem_Table = new javax.swing.JTable();
        Add_Mem = new javax.swing.JButton();
        Edit_Mem = new javax.swing.JButton();
        Refresh_Mem = new javax.swing.JButton();
        Delete_Mem = new javax.swing.JButton();
        Member_Type = new javax.swing.JPanel();
        label5 = new java.awt.Label();
        MT_Name = new javax.swing.JTextField();
        label6 = new java.awt.Label();
        MT_Loan_Day = new javax.swing.JComboBox<>();
        label7 = new java.awt.Label();
        MT_Loan_Limit = new javax.swing.JComboBox<>();
        label8 = new java.awt.Label();
        MT_Fine = new javax.swing.JComboBox<>();
        label9 = new java.awt.Label();
        jScrollPane5 = new javax.swing.JScrollPane();
        MT_Table = new javax.swing.JTable();
        Add_MT = new javax.swing.JButton();
        Edit_MT = new javax.swing.JButton();
        Refresh_MT = new javax.swing.JButton();
        Delete_MT = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        LogOutButton.setLabel("Log Out");

        MenuButton.setLabel("Back to Menu");

        TabMenu.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        label10.setText("Name");

        label11.setText("Reg Date");

        Mem_Reg_Date.setForeground(new java.awt.Color(204, 204, 204));
        Mem_Reg_Date.setText("dd/mm/yyyy");

        label1.setText("Member Type");

        Mem_Member_Type.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1-Student", "2-Lecturer", "3-Guest" }));

        label2.setText("Identifier");

        Mem_Identifier.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "STD", "LEC", "GST" }));

        label3.setText("Email");

        Mem_Email.setForeground(new java.awt.Color(204, 204, 204));
        Mem_Email.setText("miaw@gmail.com");

        label4.setText("Phone Number");

        Mem_Phone.setForeground(new java.awt.Color(204, 204, 204));
        Mem_Phone.setText("0811-1234-5678");

        Mem_Table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Name", "Reg Date", "Member Type", "Identifier", "Email", "Phone Number"
            }
        ));
        jScrollPane4.setViewportView(Mem_Table);

        Add_Mem.setText("Add");
        Add_Mem.addActionListener(this::Add_MemActionPerformed);

        Edit_Mem.setText("Edit");

        Refresh_Mem.setText("Refresh");
        Refresh_Mem.addActionListener(e -> { clearMemberInputs(); reloadAllData(); });

        Delete_Mem.setForeground(new java.awt.Color(204, 0, 51));
        Delete_Mem.setText("Delete");

        label5.setText("Name");

        label6.setText("Loan Days");

        MT_Loan_Day.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "7 Days", "14 Days" }));

        label7.setText("Loan Limit");

        MT_Loan_Limit.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "3 Books", "5 Books", "10 Books" }));
        MT_Loan_Limit.setToolTipText("");

        label8.setText("Fine per Day");

        MT_Fine.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0.25%", "0.50%", "1.00%" }));
        MT_Fine.setToolTipText("");

        label9.setText("Rp 10.000/day");

        MT_Table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "Name", "Loan Days", "Loan Limit", "Fine per Day"
            }
        ));
        jScrollPane5.setViewportView(MT_Table);

        Add_MT.setText("Add");
        Add_MT.addActionListener(this::Add_MTActionPerformed);

        Edit_MT.setText("Edit");

        Refresh_MT.setText("Refresh");
        Refresh_MT.addActionListener(e -> { clearMemberTypeInputs(); reloadAllData(); });

        Delete_MT.setForeground(new java.awt.Color(204, 0, 51));
        Delete_MT.setText("Delete");

        javax.swing.GroupLayout MemberLayout = new javax.swing.GroupLayout(Member);
        Member.setLayout(MemberLayout);
        MemberLayout.setHorizontalGroup(
            MemberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MemberLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(MemberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
                    .addGroup(MemberLayout.createSequentialGroup()
                        .addGroup(MemberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(label10, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(label11, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(MemberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(Mem_Name)
                            .addComponent(Mem_Reg_Date)
                            .addComponent(Mem_Member_Type, 0, 180, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(MemberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(label2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(label3, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(label4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(MemberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(Mem_Identifier, 0, 180, Short.MAX_VALUE)
                            .addComponent(Mem_Email)
                            .addComponent(Mem_Phone)))
                    .addGroup(MemberLayout.createSequentialGroup()
                        .addComponent(Add_Mem)
                        .addGap(12, 12, 12)
                        .addComponent(Edit_Mem)
                        .addGap(12, 12, 12)
                        .addComponent(Refresh_Mem)
                        .addGap(12, 12, 12)
                        .addComponent(Delete_Mem)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        MemberLayout.setVerticalGroup(
            MemberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MemberLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(MemberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Mem_Name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Mem_Identifier, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(MemberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Mem_Reg_Date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Mem_Email, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(MemberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Mem_Member_Type, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Mem_Phone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(MemberLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Add_Mem)
                    .addComponent(Edit_Mem)
                    .addComponent(Refresh_Mem)
                    .addComponent(Delete_Mem))
                .addGap(14, 14, 14)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                .addContainerGap())
        );

        TabMenu.addTab("Member", Member);

        javax.swing.GroupLayout MemberTypeLayout = new javax.swing.GroupLayout(Member_Type);
        Member_Type.setLayout(MemberTypeLayout);
        MemberTypeLayout.setHorizontalGroup(
            MemberTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MemberTypeLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(MemberTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
                    .addGroup(MemberTypeLayout.createSequentialGroup()
                        .addGroup(MemberTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(label5, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(label6, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(label7, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(label8, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(MemberTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(MT_Name)
                            .addComponent(MT_Loan_Day, 0, 180, Short.MAX_VALUE)
                            .addComponent(MT_Loan_Limit, 0, 180, Short.MAX_VALUE)
                            .addComponent(MT_Fine, 0, 180, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(label9, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE))
                    .addGroup(MemberTypeLayout.createSequentialGroup()
                        .addComponent(Add_MT)
                        .addGap(12, 12, 12)
                        .addComponent(Edit_MT)
                        .addGap(12, 12, 12)
                        .addComponent(Refresh_MT)
                        .addGap(12, 12, 12)
                        .addComponent(Delete_MT)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        MemberTypeLayout.setVerticalGroup(
            MemberTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MemberTypeLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(MemberTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(MT_Name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(MemberTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(MT_Loan_Day, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(MemberTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(MT_Loan_Limit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(MemberTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(MT_Fine, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(MemberTypeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Add_MT)
                    .addComponent(Edit_MT)
                    .addComponent(Refresh_MT)
                    .addComponent(Delete_MT))
                .addGap(14, 14, 14)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                .addContainerGap())
        );

        TabMenu.addTab("Member Type", Member_Type);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(TabMenu)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(MenuButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(LogOutButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(TabMenu)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(MenuButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LogOutButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
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

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new Page_Member().setVisible(true));
    }

    private javax.swing.JButton Add_MT;
    private javax.swing.JButton Add_Mem;
    private javax.swing.JButton Delete_MT;
    private javax.swing.JButton Delete_Mem;
    private javax.swing.JButton Edit_MT;
    private javax.swing.JButton Edit_Mem;
    private java.awt.Button LogOutButton;
    private javax.swing.JComboBox<String> MT_Fine;
    private javax.swing.JComboBox<String> MT_Loan_Day;
    private javax.swing.JComboBox<String> MT_Loan_Limit;
    private javax.swing.JTextField MT_Name;
    private javax.swing.JTable MT_Table;
    private javax.swing.JTextField Mem_Email;
    private javax.swing.JComboBox<String> Mem_Identifier;
    private javax.swing.JComboBox<String> Mem_Member_Type;
    private javax.swing.JTextField Mem_Name;
    private javax.swing.JTextField Mem_Phone;
    private javax.swing.JTextField Mem_Reg_Date;
    private javax.swing.JTable Mem_Table;
    private javax.swing.JPanel Member;
    private javax.swing.JPanel Member_Type;
    private java.awt.Button MenuButton;
    private javax.swing.JButton Refresh_MT;
    private javax.swing.JButton Refresh_Mem;
    private javax.swing.JTabbedPane TabMenu;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private java.awt.Label label1;
    private java.awt.Label label10;
    private java.awt.Label label11;
    private java.awt.Label label2;
    private java.awt.Label label3;
    private java.awt.Label label4;
    private java.awt.Label label5;
    private java.awt.Label label6;
    private java.awt.Label label7;
    private java.awt.Label label8;
    private java.awt.Label label9;
}
