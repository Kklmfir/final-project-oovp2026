/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package pkgfinal.project.oovp2026;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import net.proteanit.sql.DbUtils;
import com.toedter.calendar.JDateChooser;

// To input MenuPage and LoginPage
import pkgfinal.project.oovp2026.MenuPage;
import pkgfinal.project.oovp2026.LoginPage;

/*
 * Class: Page_Book (JFrame)
 */
public class Page_Book extends javax.swing.JFrame {
    Connection conn = DBConnection.getConnection();
    private MenuPage parent = null;

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Page_Book.class.getName());

    /**
     * Method: loadComboBox()
     * Description: Fill menu with Category, Author, Publisher, Borrow_Book, and Borrow_Member data from database
     */
    private void loadComboBox() {
        try {
            // Fill ComboBox Category
            Book_Category.removeAllItems();
            String sqlCat = "SELECT Category_ID, Category_Name FROM Category";
            PreparedStatement pstCat = conn.prepareStatement(sqlCat);
            ResultSet rsCat = pstCat.executeQuery();
            while (rsCat.next()) {
                Book_Category.addItem(rsCat.getString("Category_ID") + "-" + rsCat.getString("Category_Name"));
            }
            
            // Fill ComboBox Author
            Book_Author.removeAllItems();
            String sqlAuth = "SELECT Author_Book_ID, Author_Book_Name FROM Author_Book";
            PreparedStatement pstAuth = conn.prepareStatement(sqlAuth);
            ResultSet rsAuth = pstAuth.executeQuery();
            while (rsAuth.next()) {
                Book_Author.addItem(rsAuth.getString("Author_Book_ID") + "-" + rsAuth.getString("Author_Book_Name"));
            }

            // Fill ComboBox Publisher
            Book_Publisher.removeAllItems();
            String sqlPub = "SELECT Publisher_ID, Publisher_Name FROM Publisher";
            PreparedStatement pstPub = conn.prepareStatement(sqlPub);
            ResultSet rsPub = pstPub.executeQuery();
            while (rsPub.next()) {
                Book_Publisher.addItem(rsPub.getString("Publisher_ID") + "-" + rsPub.getString("Publisher_Name"));
            }
        } catch (Exception e) {
            System.out.println("Error load combo box: " + e.getMessage());
        }
        
        // Inside ComboBox
        try {
            // Load book with status 'Available' for Borrow_Book ComboBox
            String sqlBook = "SELECT Book_ID, Book_Title FROM book WHERE Book_Status = 'Available'";
            PreparedStatement pstBook = conn.prepareStatement(sqlBook);
            ResultSet rs = pstBook.executeQuery();
            Borrow_Book.removeAllItems();
            while(rs.next()){
                String id = rs.getString("Book_ID");
                    String title = rs.getString("Book_Title");
                    Borrow_Book.addItem(id + " - " + title);
            } 

            // Load members for Borrow_Member ComboBox
            String sqlMem = "SELECT Member_ID, Member_Name FROM member";
            PreparedStatement pstMem = conn.prepareStatement(sqlMem);
            ResultSet rsMem = pstMem.executeQuery();
            Borrow_Member.removeAllItems();
            while(rsMem.next()){
                Borrow_Member.addItem(rsMem.getString("Member_ID") + " - " + rsMem.getString("Member_Name"));
            }
        } catch (Exception e) { System.out.println(e.getMessage()); }
    }

    /**
     * Method: loadBookTable()
     * Description: Showing book data into JTable Book_Table
     */
    private void loadBookTable() {
        try {
            String sql = "SELECT b.Book_ID, b.Book_Title, c.Category_Name, a.Author_Book_Name, " +
                        "p.Publisher_Name, b.Book_Year, b.ISBN, b.Book_Status " +
                        "FROM Book b " +
                        "JOIN Category c ON b.Book_Category_ID = c.Category_ID " +
                        "JOIN Author_Book a ON b.Book_Author_ID = a.Author_Book_ID " +
                        "JOIN Publisher p ON b.Book_Publisher_ID = p.Publisher_ID";
                        
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            Book_Table.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            System.out.println("Error load book table: " + e.getMessage());
        }
    }

    /**
     * Constructor: Page_Book()
     * Description: To opening Page_Book without a parent
     */
    public Page_Book() {
        initComponents();
        autoIDCategory();
        loadComboBox();
        autoIDBook();
        loadBookTable();
        loadCatTable();
        loadPubTable();
        autoIDPublisher();
        loadAuthTable();
        autoIDAuthor();
        loadBorrowTable();
        autoIDIssued();
    }
    
    /**
     * Constructor overload: Page_Book(MenuPage parent)
     * Description: Navigation from MenuPage and keep reference to MenuPage as parent
     */
    public Page_Book(MenuPage parent) {
        this(); // using default constructor
        this.parent = parent;
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    }

    /**
     * Method: autoIDBook()
     * Description: Generate Book_ID automatically based on MAX(Book_ID).
     */
    private void autoIDBook() {
        try {
            // Put logic to get MAX(Book_ID) from database
            String sql = "SELECT MAX(Book_ID) FROM Book";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                String maxID = rs.getString(1);
                if (maxID == null) {
                    Book_ID.setText("B001");
                } else {
                    int id = Integer.parseInt(maxID.substring(1));
                    id++;
                    Book_ID.setText(String.format("B%03d", id)); // use %03d for automatically 3 digits
                }
            }
            Book_ID.setEditable(false);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error Auto ID Book: " + e.getMessage());
        }
    }

    /**
     * Method: countDate()
     * Description: Counting Return_Date based on Borrow_Date and member type
     */
    private void countDate() {
        try {
            if (Borrow_Date.getDate() != null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(Borrow_Date.getDate());

                String selectedMember = Borrow_Member.getSelectedItem().toString();
                int duration = 7; // default (also for guest)
                
                if (selectedMember.contains("Std")) { // for student
                    duration = 7;
                } else if (selectedMember.contains("Lect")) { // for lecturer
                    duration = 14;
                }

                cal.add(Calendar.DAY_OF_MONTH, duration);
                Return_Date.setDate(cal.getTime());
            }
        } catch (Exception e) {
            System.err.println("Error while counting days: " + e.getMessage());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {

        TabMenu = new javax.swing.JTabbedPane();
        Book = new javax.swing.JPanel();
        label1 = new java.awt.Label();
        Book_Title = new javax.swing.JTextField();
        label2 = new java.awt.Label();
        Book_Category = new javax.swing.JComboBox<>();
        label3 = new java.awt.Label();
        Book_Author = new javax.swing.JComboBox<>();
        label4 = new java.awt.Label();
        Book_Publisher = new javax.swing.JComboBox<>();
        label5 = new java.awt.Label();
        Book_Year = new javax.swing.JTextField();
        label6 = new java.awt.Label();
        Book_ISBN = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        Book_Table = new javax.swing.JTable();
        Add_Book = new javax.swing.JButton();
        Edit_Book = new javax.swing.JButton();
        Refresh_Book = new javax.swing.JButton();
        Delete_Book = new javax.swing.JButton();
        label18 = new java.awt.Label();
        Book_ID = new javax.swing.JTextField();
        Category = new javax.swing.JPanel();
        label8 = new java.awt.Label();
        Cat_ID = new javax.swing.JTextField();
        label9 = new java.awt.Label();
        jScrollPane2 = new javax.swing.JScrollPane();
        Cat_Description = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        Cat_Table = new javax.swing.JTable();
        Add_Cat = new javax.swing.JButton();
        Edit_Cat = new javax.swing.JButton();
        Refresh_Cat = new javax.swing.JButton();
        Delete_Cat = new javax.swing.JButton();
        ID = new java.awt.Label();
        Cat_Name = new javax.swing.JTextField();
        Publisher = new javax.swing.JPanel();
        label12 = new java.awt.Label();
        Pub_ID = new javax.swing.JTextField();
        label13 = new java.awt.Label();
        Pub_Contact = new javax.swing.JTextField();
        jScrollPane5 = new javax.swing.JScrollPane();
        Pub_Table = new javax.swing.JTable();
        Add_Pub = new javax.swing.JButton();
        Edit_Pub = new javax.swing.JButton();
        Refresh_Pub = new javax.swing.JButton();
        Delete_Pub = new javax.swing.JButton();
        label19 = new java.awt.Label();
        Pub_Name = new javax.swing.JTextField();
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
        jLabel1 = new javax.swing.JLabel();
        Auth_Name = new javax.swing.JTextField();
        Borrow = new javax.swing.JPanel();
        label14 = new java.awt.Label();
        Borrow_Book = new javax.swing.JComboBox<>();
        label15 = new java.awt.Label();
        Borrow_Member = new javax.swing.JComboBox<>();
        label16 = new java.awt.Label();
        label17 = new java.awt.Label();
        jScrollPane6 = new javax.swing.JScrollPane();
        Borrow_Table = new javax.swing.JTable();
        Add_Borrow = new javax.swing.JButton();
        Edit_Borrow = new javax.swing.JButton();
        Refresh_Borrow = new javax.swing.JButton();
        Delete_Borrow = new javax.swing.JButton();
        Borrow_Date = new com.toedter.calendar.JDateChooser();
        Return_Date = new com.toedter.calendar.JDateChooser();
        label20 = new java.awt.Label();
        Issued_ID_Field = new javax.swing.JTextField();
        MenuButton = new java.awt.Button();
        LogOutButton = new java.awt.Button();
        LogOutButton1 = new java.awt.Button();
        MenuButton1 = new java.awt.Button();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        TabMenu.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        label1.setText("Title");

        Book_Title.addActionListener(this::Book_TitleActionPerformed);

        label2.setText("Category");

        Book_Category.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1- Cat1", "2- Cat2", "3- Cat3" }));

        label3.setText("Author");

        Book_Author.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1- Author1", "2- Author2", "3 Author3" }));

        label4.setText("Publisher");

        Book_Publisher.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1- Pub1", "2- Pub2", "3- Pub3" }));

        label5.setText("Year");

        Book_Year.addActionListener(this::Book_YearActionPerformed);

        label6.setText("ISBN");

        Book_ISBN.addActionListener(this::Book_ISBNActionPerformed);

        Book_Table.setModel(new javax.swing.table.DefaultTableModel(
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
                "ID", "Title", "Category", "Author", "Publisher", "Year", "ISBN", "Stock", "Status"
            }
        ));
        Book_Table.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        Book_Table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Book_TableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(Book_Table);

        Add_Book.setText("Add");
        Add_Book.addActionListener(this::Add_BookActionPerformed);

        Edit_Book.setText("Edit");
        Edit_Book.addActionListener(this::Edit_BookActionPerformed);

        Refresh_Book.setText("Refresh");
        Refresh_Book.addActionListener(this::Refresh_BookActionPerformed);

        Delete_Book.setForeground(new java.awt.Color(204, 0, 51));
        Delete_Book.setText("Delete");
        Delete_Book.addActionListener(this::Delete_BookActionPerformed);

        label18.setText("ID");

        Book_ID.addActionListener(this::Book_IDActionPerformed);

        javax.swing.GroupLayout BookLayout = new javax.swing.GroupLayout(Book);
        Book.setLayout(BookLayout);
        BookLayout.setHorizontalGroup(
            BookLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BookLayout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addGroup(BookLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(BookLayout.createSequentialGroup()
                        .addGroup(BookLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(label3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(label4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BookLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(label18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(label2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(BookLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(Book_Category, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Book_Author, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Book_Publisher, 0, 122, Short.MAX_VALUE)
                            .addComponent(Book_ID)))
                    .addGroup(BookLayout.createSequentialGroup()
                        .addComponent(Add_Book)
                        .addGap(42, 42, 42)
                        .addComponent(Edit_Book)))
                .addGroup(BookLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(BookLayout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addComponent(Refresh_Book)
                        .addGap(42, 42, 42)
                        .addComponent(Delete_Book))
                    .addGroup(BookLayout.createSequentialGroup()
                        .addGap(99, 99, 99)
                        .addGroup(BookLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(BookLayout.createSequentialGroup()
                                .addComponent(label6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(21, 21, 21)
                                .addComponent(Book_ISBN, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(BookLayout.createSequentialGroup()
                                .addGroup(BookLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(label5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(23, 23, 23)
                                .addGroup(BookLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(Book_Title)
                                    .addComponent(Book_Year))))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BookLayout.createSequentialGroup()
                .addGap(0, 14, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 548, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );
        BookLayout.setVerticalGroup(
            BookLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BookLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(BookLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(BookLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(Book_Title, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(BookLayout.createSequentialGroup()
                            .addComponent(label18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(2, 2, 2)))
                    .addComponent(Book_ID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(BookLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(BookLayout.createSequentialGroup()
                        .addComponent(label2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(label3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(BookLayout.createSequentialGroup()
                        .addGroup(BookLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(Book_Category, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(label5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Book_Year, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(BookLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(BookLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(Book_Author, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(BookLayout.createSequentialGroup()
                                    .addGap(2, 2, 2)
                                    .addComponent(label6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(Book_ISBN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(2, 2, 2)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(BookLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Book_Publisher, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(BookLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Add_Book)
                    .addGroup(BookLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(Edit_Book)
                        .addComponent(Refresh_Book)
                        .addComponent(Delete_Book)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );

        TabMenu.addTab("Book", Book);

        label8.setText("Name");

        Cat_ID.addActionListener(this::Cat_IDActionPerformed);

        label9.setText("Description");

        Cat_Description.setColumns(20);
        Cat_Description.setRows(5);
        jScrollPane2.setViewportView(Cat_Description);

        Cat_Table.setModel(new javax.swing.table.DefaultTableModel(
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
                "ID", "Name", "Description"
            }
        ));
        Cat_Table.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        Cat_Table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Cat_TableMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(Cat_Table);

        Add_Cat.setText("Add");
        Add_Cat.addActionListener(this::Add_CatActionPerformed);

        Edit_Cat.setText("Edit");
        Edit_Cat.addActionListener(this::Edit_CatActionPerformed);

        Refresh_Cat.setText("Refresh");
        Refresh_Cat.addActionListener(this::Refresh_CatActionPerformed);

        Delete_Cat.setForeground(new java.awt.Color(204, 0, 51));
        Delete_Cat.setText("Delete");
        Delete_Cat.addActionListener(this::Delete_CatActionPerformed);

        ID.setText("ID");

        Cat_Name.addActionListener(this::Cat_NameActionPerformed);

        javax.swing.GroupLayout CategoryLayout = new javax.swing.GroupLayout(Category);
        Category.setLayout(CategoryLayout);
        CategoryLayout.setHorizontalGroup(
            CategoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CategoryLayout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addGroup(CategoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(CategoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(CategoryLayout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, CategoryLayout.createSequentialGroup()
                        .addGroup(CategoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(Cat_Name, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Cat_ID, javax.swing.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE))
                        .addGap(296, 296, 296))))
            .addGroup(CategoryLayout.createSequentialGroup()
                .addGroup(CategoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(CategoryLayout.createSequentialGroup()
                        .addGap(73, 73, 73)
                        .addComponent(Add_Cat)
                        .addGap(42, 42, 42)
                        .addComponent(Edit_Cat)
                        .addGap(49, 49, 49)
                        .addComponent(Refresh_Cat)
                        .addGap(42, 42, 42)
                        .addComponent(Delete_Cat))
                    .addGroup(CategoryLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 548, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        CategoryLayout.setVerticalGroup(
            CategoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CategoryLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(CategoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Cat_ID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(CategoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(CategoryLayout.createSequentialGroup()
                        .addComponent(label8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, CategoryLayout.createSequentialGroup()
                        .addComponent(Cat_Name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(CategoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(44, 44, 44)
                .addGroup(CategoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Add_Cat)
                    .addComponent(Edit_Cat)
                    .addComponent(Refresh_Cat)
                    .addComponent(Delete_Cat))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 56, Short.MAX_VALUE)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        TabMenu.addTab("Category", Category);

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
                            .addComponent(Pub_ID, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Pub_Name, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                .addContainerGap(19, Short.MAX_VALUE))
        );
        PublisherLayout.setVerticalGroup(
            PublisherLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PublisherLayout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(PublisherLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Pub_ID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PublisherLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Pub_Name, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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
                .addContainerGap(29, Short.MAX_VALUE))
        );

        TabMenu.addTab("Publisher", Publisher);

        label10.setText("Name");

        Auth_ID.addActionListener(this::Auth_IDActionPerformed);

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

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 12)); 
        jLabel1.setText("ID");

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
                            .addComponent(jLabel1))
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
                .addContainerGap(19, Short.MAX_VALUE))
        );
        AuthorLayout.setVerticalGroup(
            AuthorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(AuthorLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(AuthorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(Auth_ID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(AuthorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Auth_Name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(AuthorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(label11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Auth_Contact, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(AuthorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Add_Auth)
                    .addComponent(Edit_Auth)
                    .addComponent(Refresh_Auth)
                    .addComponent(Delete_Auth))
                .addGap(26, 26, 26)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 416, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        TabMenu.addTab("Author", Author);

        label14.setText("Book ID");

        Borrow_Book.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1- Book1", "2- Book2", "3- Book3" }));

        label15.setText("Member ID");

        Borrow_Member.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1- Member1", "2- Member2", "3- Member3" }));
        Borrow_Member.addItemListener(this::countDate);

        label16.setText("Borrow");

        label17.setText("Return");

        Borrow_Table.setModel(new javax.swing.table.DefaultTableModel(
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
                "ID", "Book ID", "Member ID", "Borrow Date", "Return Date"
            }
        ));
        Borrow_Table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Borrow_TableMouseClicked(evt);
            }
        });
        jScrollPane6.setViewportView(Borrow_Table);

        Add_Borrow.setText("Add");
        Add_Borrow.addActionListener(this::Add_BorrowActionPerformed);

        Edit_Borrow.setText("Edit");
        Edit_Borrow.addActionListener(this::Edit_BorrowActionPerformed);

        Refresh_Borrow.setText("Refresh");
        Refresh_Borrow.addActionListener(this::Refresh_BorrowActionPerformed);

        Delete_Borrow.setForeground(new java.awt.Color(204, 0, 51));
        Delete_Borrow.setText("Delete");
        Delete_Borrow.addActionListener(this::Delete_BorrowActionPerformed);

        label20.setText("ID");

        MenuButton.setLabel("Back to Menu");
        MenuButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                MenuButtonMouseClicked(evt);
            }
        });

        LogOutButton.setLabel("Log Out");
        LogOutButton.addActionListener(this::LogOutButtonActionPerformed);

        pack();

        javax.swing.GroupLayout BorrowLayout = new javax.swing.GroupLayout(Borrow);
        Borrow.setLayout(BorrowLayout);
        BorrowLayout.setHorizontalGroup(
            BorrowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BorrowLayout.createSequentialGroup()
                .addGroup(BorrowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(BorrowLayout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addGroup(BorrowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(label14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(label15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(label16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(label17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(label20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(21, 21, 21)
                        .addGroup(BorrowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(Borrow_Member, 0, 120, Short.MAX_VALUE)
                            .addComponent(Borrow_Book, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Borrow_Date, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Return_Date, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Issued_ID_Field)))
                    .addGroup(BorrowLayout.createSequentialGroup()
                        .addGap(66, 66, 66)
                        .addComponent(Add_Borrow)
                        .addGap(42, 42, 42)
                        .addComponent(Edit_Borrow)
                        .addGap(47, 47, 47)
                        .addComponent(Refresh_Borrow)
                        .addGap(42, 42, 42)
                        .addComponent(Delete_Borrow))
                    .addGroup(BorrowLayout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addGroup(BorrowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(BorrowLayout.createSequentialGroup()
                                .addComponent(MenuButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(LogOutButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 540, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        BorrowLayout.setVerticalGroup(
            BorrowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BorrowLayout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addGroup(BorrowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BorrowLayout.createSequentialGroup()
                        .addGroup(BorrowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(label20, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Issued_ID_Field, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(BorrowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(label14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Borrow_Book, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(BorrowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(label15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Borrow_Member, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(BorrowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(label16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Borrow_Date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(BorrowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(label17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(Return_Date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(15, 15, 15)
                        .addGroup(BorrowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Add_Borrow)
                            .addComponent(Edit_Borrow)
                            .addComponent(Refresh_Borrow)
                            .addComponent(Delete_Borrow))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(9, 9, 9)
                        .addComponent(MenuButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(LogOutButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        TabMenu.addTab("Borrow Book", Borrow);

        LogOutButton1.setLabel("Log Out");

        MenuButton1.setLabel("Back to Menu");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(MenuButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(LogOutButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(TabMenu, javax.swing.GroupLayout.DEFAULT_SIZE, 580, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(TabMenu)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(MenuButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LogOutButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }

    private void Book_TitleActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }//GEN-LAST:event_Book_TitleActionPerformed

    private void Book_YearActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }//GEN-LAST:event_Book_YearActionPerformed

    private void Book_ISBNActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }//GEN-LAST:event_Book_ISBNActionPerformed

    private void Book_TableMouseClicked(java.awt.event.MouseEvent evt) {
        int row = Book_Table.getSelectedRow();
        String id = Book_Table.getValueAt(row, 0).toString();

        try {
            String sql = "SELECT * FROM Book WHERE Book_ID = '" + id + "'";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                Book_ID.setText(rs.getString("Book_ID"));
                Book_Title.setText(rs.getString("Book_Title"));
                Book_Year.setText(rs.getString("Book_Year"));
                Book_ISBN.setText(rs.getString("ISBN"));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }//GEN-LAST:event_Book_TableMouseClicked

    private void Cat_IDActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void Cat_TableMouseClicked(java.awt.event.MouseEvent evt) {
        int row = Cat_Table.getSelectedRow();
        Cat_ID.setText(Cat_Table.getValueAt(row, 0).toString());
        Cat_Name.setText(Cat_Table.getValueAt(row, 1).toString());
        Cat_Description.setText(Cat_Table.getValueAt(row, 2).toString());
    }//GEN-LAST:event_Cat_TableMouseClicked

    private void countDate(java.awt.event.ItemEvent evt) {
        // TODO add your handling code here:
    }//GEN-LAST:event_countDate

    private void Book_IDActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }//GEN-LAST:event_Book_IDActionPerformed

    private void Add_BookActionPerformed(java.awt.event.ActionEvent evt) {
        try {
        String sql = "INSERT INTO Book (Book_ID, Book_Title, Book_Category_ID, Book_Author_ID, Book_Publisher_ID, Book_Year, ISBN, Book_Status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement pst = conn.prepareStatement(sql);

        pst.setString(1, Book_ID.getText());
        pst.setString(2, Book_Title.getText());

        String catItem = Book_Category.getSelectedItem().toString();
        pst.setString(3, catItem.split("-")[0]);

        String authItem = Book_Author.getSelectedItem().toString();
        pst.setString(4, authItem.split("-")[0]);

        String pubItem = Book_Publisher.getSelectedItem().toString();
        pst.setString(5, pubItem.split("-")[0]);

        pst.setString(6, Book_Year.getText());
        pst.setString(7, Book_ISBN.getText());
        
        pst.setString(8, "Available"); 

        pst.executeUpdate();
        JOptionPane.showMessageDialog(null, "Book Successfully Added!");
        
        loadBookTable();
        
        Book_Title.setText("");
        Book_Year.setText("");
        Book_ISBN.setText("");
        Book_Category.setSelectedIndex(0);
        Book_Author.setSelectedIndex(0);
        Book_Publisher.setSelectedIndex(0);
        
        autoIDBook();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to Add Book Data: " + e.getMessage());
        }
        
    }//GEN-LAST:event_Add_BookActionPerformed

    private void Cat_NameActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }//GEN-LAST:event_Cat_NameActionPerformed

    private void Add_CatActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            String sql = "INSERT INTO Category (Category_ID, Category_Name, Description) VALUES (?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, Cat_ID.getText());
            pst.setString(2, Cat_Name.getText());
            pst.setString(3, Cat_Description.getText());
            
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Category Successfully Added!");
            
            loadComboBox();
            autoIDCategory();
            loadCatTable();
            Cat_Name.setText("");
            Cat_Description.setText("");
        } catch (Exception e) { JOptionPane.showMessageDialog(null, "Failed to Add Category Data: " + e.getMessage()); }
    }//GEN-LAST:event_Add_CatActionPerformed

    private void Edit_BookActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            String sql = "UPDATE Book SET Book_Title=?, Book_Category_ID=?, Book_Author_ID=?, Book_Publisher_ID=?, Book_Year=?, ISBN=? WHERE Book_ID=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            
            pst.setString(1, Book_Title.getText());
            pst.setString(2, Book_Category.getSelectedItem().toString().split("-")[0]);
            pst.setString(3, Book_Author.getSelectedItem().toString().split("-")[0]);
            pst.setString(4, Book_Publisher.getSelectedItem().toString().split("-")[0]);
            pst.setString(5, Book_Year.getText());
            pst.setString(6, Book_ISBN.getText());
            pst.setString(7, Book_ID.getText()); 

            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Book Data Updated Successfully!");
            loadBookTable();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to Update Book Data: " + e.getMessage());
        }
    }//GEN-LAST:event_Edit_BookActionPerformed

    private void Refresh_BookActionPerformed(java.awt.event.ActionEvent evt) {
        Book_ID.setText("");
        Book_Title.setText("");
        Book_Year.setText("");
        Book_ISBN.setText("");
        Book_Category.setSelectedIndex(0);
        Book_Author.setSelectedIndex(0);
        Book_Publisher.setSelectedIndex(0);
    
        loadBookTable();
        autoIDBook();
    }//GEN-LAST:event_Refresh_BookActionPerformed

    private void Delete_BookActionPerformed(java.awt.event.ActionEvent evt) {
        int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this book?", "Confirm", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String sql = "DELETE FROM Book WHERE Book_ID = ?";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, Book_ID.getText());
                
                int row = pst.executeUpdate();
                if (row > 0) {
                    JOptionPane.showMessageDialog(null, "Book Successfully Deleted!");
                    loadBookTable();
                    autoIDBook();
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a book from the table first!");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        }
    }//GEN-LAST:event_Delete_BookActionPerformed

    private void Delete_CatActionPerformed(java.awt.event.ActionEvent evt) {
        int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this category?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String sql = "DELETE FROM Category WHERE Category_ID = ?";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, Cat_ID.getText());
                
                pst.executeUpdate();
                loadCatTable();
                autoIDCategory();
                loadComboBox();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Failed to Delete Category: " + e.getMessage());
            }
        }
    }//GEN-LAST:event_Delete_CatActionPerformed

    private void Edit_CatActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            String sql = "UPDATE Category SET Category_Name = ?, Description = ? WHERE Category_ID = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            
            pst.setString(1, Cat_Name.getText());
            pst.setString(2, Cat_Description.getText());
            pst.setString(3, Cat_ID.getText()); 
            
            int updated = pst.executeUpdate();
            
            if (updated > 0) {
                JOptionPane.showMessageDialog(null, "Category Data Updated Successfully!");
                
                loadCatTable();
                loadComboBox();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to Update Category Data: " + e.getMessage());
        }
    }//GEN-LAST:event_Edit_CatActionPerformed

    private void Refresh_CatActionPerformed(java.awt.event.ActionEvent evt) {
        Cat_Name.setText("");
        Cat_Description.setText("");

        loadCatTable();
        autoIDCategory();
    }//GEN-LAST:event_Refresh_CatActionPerformed

    private void Add_PubActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            String sql = "INSERT INTO Publisher (Publisher_ID, Publisher_Name, Publisher_Contact) VALUES (?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, Pub_ID.getText());
            pst.setString(2, Pub_Name.getText());
            pst.setString(3, Pub_Contact.getText());
            
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Publisher Successfully Added!");
            
            loadPubTable();
            autoIDPublisher();
            loadComboBox();
            
            Pub_Name.setText("");
            Pub_Contact.setText("");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to Add Publisher: " + e.getMessage());
        }
    }//GEN-LAST:event_Add_PubActionPerformed

    private void Edit_PubActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            String sql = "UPDATE Publisher SET Publisher_Name = ?, Publisher_Contact = ? WHERE Publisher_ID = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            
            pst.setString(1, Pub_Name.getText());
            pst.setString(2, Pub_Contact.getText());
            pst.setString(3, Pub_ID.getText());
            
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Publisher Data Updated Successfully!");
            
            loadPubTable();
            loadComboBox();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to Update Publisher Data: " + e.getMessage());
        }
    }//GEN-LAST:event_Edit_PubActionPerformed

    private void Delete_PubActionPerformed(java.awt.event.ActionEvent evt) {
         int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this publisher?", "Confirmation Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String sql = "DELETE FROM publisher WHERE Publisher_ID = ?";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, Pub_ID.getText());
                
                pst.executeUpdate();
                loadPubTable();
                autoIDPublisher();
                loadComboBox();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Failed to Delete Publisher: " + e.getMessage());
            }
        }
    }//GEN-LAST:event_Delete_PubActionPerformed

    private void Refresh_PubActionPerformed(java.awt.event.ActionEvent evt) {
        Pub_Name.setText("");
        Pub_Contact.setText("");

        loadPubTable();
        autoIDPublisher();
    }//GEN-LAST:event_Refresh_PubActionPerformed

    private void Pub_TableMouseClicked(java.awt.event.MouseEvent evt) {
        int row = Pub_Table.getSelectedRow();
        Pub_ID.setText(Pub_Table.getValueAt(row, 0).toString());
        Pub_Name.setText(Pub_Table.getValueAt(row, 1).toString());
        Pub_Contact.setText(Pub_Table.getValueAt(row, 2).toString());
    }//GEN-LAST:event_Pub_TableMouseClicked

    private void Add_AuthActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            String sql = "INSERT INTO author_book (Author_Book_ID, Author_Book_Name, Author_Book_Contact) VALUES (?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, Auth_ID.getText());
            pst.setString(2, Auth_Name.getText());
            pst.setString(3, Auth_Contact.getText());
            
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Author Successfully Added!");
            
            loadAuthTable();
            autoIDAuthor();
            loadComboBox();
            
            Auth_Name.setText("");
            Auth_Contact.setText("");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to Add Author: " + e.getMessage());
        }
    }//GEN-LAST:event_Add_AuthActionPerformed

    private void Auth_IDActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }//GEN-LAST:event_Auth_IDActionPerformed

    private void Refresh_AuthActionPerformed(java.awt.event.ActionEvent evt) {
        Auth_Name.setText("");
        Auth_Contact.setText("");
        loadAuthTable();
        autoIDAuthor();
    }//GEN-LAST:event_Refresh_AuthActionPerformed

    private void Auth_TableMouseClicked(java.awt.event.MouseEvent evt) {
        int row = Auth_Table.getSelectedRow();
        Auth_ID.setText(Auth_Table.getValueAt(row, 0).toString());
        Auth_Name.setText(Auth_Table.getValueAt(row, 1).toString());
        Auth_Contact.setText(Auth_Table.getValueAt(row, 2).toString());
    }//GEN-LAST:event_Auth_TableMouseClicked

    private void Delete_AuthActionPerformed(java.awt.event.ActionEvent evt) {
        int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this author?", "Confirmation Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    String sql = "DELETE FROM author_book WHERE Author_Book_ID = ?";
                    PreparedStatement pst = conn.prepareStatement(sql);
                    pst.setString(1, Auth_ID.getText());
                    
                    pst.executeUpdate();
                    loadAuthTable();
                    autoIDAuthor();
                    loadComboBox();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Failed to Delete Author: " + e.getMessage());
                }
        }
    }//GEN-LAST:event_Delete_AuthActionPerformed

    private void Edit_AuthActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            String sql = "UPDATE author_book SET Author_Book_Name = ?, Author_Book_Contact = ? WHERE Author_Book_ID = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            
            pst.setString(1, Auth_Name.getText());
            pst.setString(2, Auth_Contact.getText());
            pst.setString(3, Auth_ID.getText());
            
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Author Data Updated Successfully!");
            
            loadAuthTable();
            loadComboBox();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to Update Author Data: " + e.getMessage());
        }
    }//GEN-LAST:event_Edit_AuthActionPerformed

    private void loadBorrowTable() {
        try {
            String sql = "SELECT Issued_ID, Book_ID, Member_ID, Date_issued, Date_returned FROM issued_book";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            
            Borrow_Table.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            System.out.println("Error Load Tabel: " + e.getMessage());
        }
    }

    private void Add_BorrowActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            String issuedID = Issued_ID_Field.getText();
            String bookID = Borrow_Book.getSelectedItem().toString().split(" - ")[0];
            String memberID = Borrow_Member.getSelectedItem().toString().split(" - ")[0];
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String iDate = sdf.format(Borrow_Date.getDate());
            String rDate = sdf.format(Return_Date.getDate());

            String sql = "INSERT INTO issued_book (Issued_ID, Book_ID, Member_ID, Date_issued, Date_returned) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, issuedID);
            pst.setString(2, bookID);
            pst.setString(3, memberID);
            pst.setString(4, iDate);
            pst.setString(5, rDate);
            pst.executeUpdate();

            String sqlUpdate = "UPDATE Book SET Book_Status = 'Borrowed' WHERE Book_ID = ?";
            PreparedStatement pstUpdate = conn.prepareStatement(sqlUpdate);
            pstUpdate.setString(1, bookID);
            pstUpdate.executeUpdate();

            JOptionPane.showMessageDialog(null, "Borrowing Successful! Book status has been updated.");

            autoIDIssued();
            loadBookTable();
            loadComboBox();
            loadBorrowTable();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to Borrow Book: " + e.getMessage());
        }
    }//GEN-LAST:event_Add_BorrowActionPerformed

    private void Borrow_TableMouseClicked(java.awt.event.MouseEvent evt) {
        int row = Borrow_Table.getSelectedRow();
        String id = Borrow_Table.getValueAt(row, 0).toString();
        Issued_ID_Field.setText(id);

        try {
            String dateIssueStr = Borrow_Table.getValueAt(row, 3).toString();
            String dateReturnStr = Borrow_Table.getValueAt(row, 4).toString();
            
            java.util.Date d1 = new SimpleDateFormat("yyyy-MM-dd").parse(dateIssueStr);
            java.util.Date d2 = new SimpleDateFormat("yyyy-MM-dd").parse(dateReturnStr);
            
            Borrow_Date.setDate(d1);
            Return_Date.setDate(d2);
        } catch (Exception e) {
            System.out.println("Error clicking table: " + e.getMessage());
        }
    }//GEN-LAST:event_Borrow_TableMouseClicked

    private void autoIDIssued() {
        try {
            String sql = "SELECT MAX(Issued_ID) FROM issued_book";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                String maxID = rs.getString(1);
                if (maxID == null) {
                    Issued_ID_Field.setText("IS001");
                } else {
                    int id = Integer.parseInt(maxID.substring(2)) + 1;
                    Issued_ID_Field.setText(String.format("IS%03d", id));
                }
            }
            Issued_ID_Field.setEditable(false);
            
        } catch (Exception e) {
            System.out.println("Error AutoID Issued: " + e.getMessage());
        }
    }

    private void Delete_BorrowActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            String bID = Borrow_Book.getSelectedItem().toString().split(" - ")[0];
            String sqlDel = "DELETE FROM issued_book WHERE Book_ID = ?";
            PreparedStatement pstDel = conn.prepareStatement(sqlDel);
            pstDel.setString(1, bID);
            pstDel.executeUpdate();

            String sqlUp = "UPDATE Book SET Book_Status = 'Available' WHERE Book_ID = ?";
            PreparedStatement pstUp = conn.prepareStatement(sqlUp);
            pstUp.setString(1, bID);
            pstUp.executeUpdate();

            JOptionPane.showMessageDialog(null, "Book Has Been Returned!");
            
            loadBorrowTable();
            loadBookTable();
            loadComboBox(); 
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to Return Book: " + e.getMessage());
        }
    }//GEN-LAST:event_Delete_BorrowActionPerformed

    private void Edit_BorrowActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            String idIssued = Issued_ID_Field.getText();
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String iDate = sdf.format(Borrow_Date.getDate());
            String rDate = sdf.format(Return_Date.getDate());

            String sql = "UPDATE issued_book SET Date_issued = ?, Date_returned = ? WHERE Issued_ID = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, iDate);
            pst.setString(2, rDate);
            pst.setString(3, idIssued);
            
            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Borrowing Data Updated Successfully!");
            
            loadBorrowTable();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to Edit Borrowing Data: " + e.getMessage());
        }
    }//GEN-LAST:event_Edit_BorrowActionPerformed

    private void Refresh_BorrowActionPerformed(java.awt.event.ActionEvent evt) {
        loadBorrowTable();
        autoIDIssued();
        loadComboBox();

        Borrow_Date.setDate(null);
        Return_Date.setDate(null);

        Borrow_Book.setSelectedIndex(0);
        Borrow_Member.setSelectedIndex(0);
    }//GEN-LAST:event_Refresh_BorrowActionPerformed

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
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                /* Set the Nimbus look and feel */
                /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
                * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
                */
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> {
            Page_Book pb = new Page_Book();
            pb.setLocationRelativeTo(null);
            pb.setVisible(true);
        });
    }
    
    private void autoIDCategory() {
        try {
            String sql = "SELECT MAX(Category_ID) FROM category";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                String maxID = rs.getString(1);
                if (maxID != null) {
                    int id = Integer.parseInt(maxID.substring(2)) + 1;
                    Cat_ID.setText(String.format("CT%03d", id));
                } else { Cat_ID.setText("CT001"); }
            }
            Cat_ID.setEditable(false);
        } catch (Exception e) { System.out.println(e.getMessage()); }
    }

    private void loadAuthTable() {
        try {
            String sql = "SELECT * FROM author_book"; 
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            Auth_Table.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error Load Author: " + e.getMessage());
        }
    }

    private void autoIDAuthor() {
        try {
            String sql = "SELECT MAX(Author_Book_ID) FROM author_Book";
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

    private void loadCatTable() {
        try {
            String sql = "SELECT * FROM Category";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            Cat_Table.setModel(DbUtils.resultSetToTableModel(rs)); // Using rs2xml
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error Load Category: " + e.getMessage());
        }
    }

    private void loadPubTable() {
        try {
            String sql = "SELECT * FROM Publisher"; 
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            Pub_Table.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error Load Publisher: " + e.getMessage());
        }
    }

    private javax.swing.JButton Add_Auth;
    private javax.swing.JButton Add_Book;
    private javax.swing.JButton Add_Borrow;
    private javax.swing.JButton Add_Cat;
    private javax.swing.JButton Add_Pub;
    private javax.swing.JTextField Auth_Contact;
    private javax.swing.JTextField Auth_ID;
    private javax.swing.JTextField Auth_Name;
    private javax.swing.JTable Auth_Table;
    private javax.swing.JPanel Author;
    private javax.swing.JPanel Book;
    private javax.swing.JComboBox<String> Book_Author;
    private javax.swing.JComboBox<String> Book_Category;
    private javax.swing.JTextField Book_ID;
    private javax.swing.JTextField Book_ISBN;
    private javax.swing.JComboBox<String> Book_Publisher;
    private javax.swing.JTable Book_Table;
    private javax.swing.JTextField Book_Title;
    private javax.swing.JTextField Book_Year;
    private javax.swing.JPanel Borrow;
    private javax.swing.JComboBox<String> Borrow_Book;
    private com.toedter.calendar.JDateChooser Borrow_Date;
    private javax.swing.JComboBox<String> Borrow_Member;
    private javax.swing.JTable Borrow_Table;
    private javax.swing.JTextArea Cat_Description;
    private javax.swing.JTextField Cat_ID;
    private javax.swing.JTextField Cat_Name;
    private javax.swing.JTable Cat_Table;
    private javax.swing.JPanel Category;
    private javax.swing.JButton Delete_Auth;
    private javax.swing.JButton Delete_Book;
    private javax.swing.JButton Delete_Borrow;
    private javax.swing.JButton Delete_Cat;
    private javax.swing.JButton Delete_Pub;
    private javax.swing.JButton Edit_Auth;
    private javax.swing.JButton Edit_Book;
    private javax.swing.JButton Edit_Borrow;
    private javax.swing.JButton Edit_Cat;
    private javax.swing.JButton Edit_Pub;
    private java.awt.Label ID;
    private javax.swing.JTextField Issued_ID_Field;
    private java.awt.Button LogOutButton;
    private java.awt.Button LogOutButton1;
    private java.awt.Button MenuButton;
    private java.awt.Button MenuButton1;
    private javax.swing.JTextField Pub_Contact;
    private javax.swing.JTextField Pub_ID;
    private javax.swing.JTextField Pub_Name;
    private javax.swing.JTable Pub_Table;
    private javax.swing.JPanel Publisher;
    private javax.swing.JButton Refresh_Auth;
    private javax.swing.JButton Refresh_Book;
    private javax.swing.JButton Refresh_Borrow;
    private javax.swing.JButton Refresh_Cat;
    private javax.swing.JButton Refresh_Pub;
    private com.toedter.calendar.JDateChooser Return_Date;
    private javax.swing.JTabbedPane TabMenu;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private java.awt.Label label1;
    private java.awt.Label label10;
    private java.awt.Label label11;
    private java.awt.Label label12;
    private java.awt.Label label13;
    private java.awt.Label label14;
    private java.awt.Label label15;
    private java.awt.Label label16;
    private java.awt.Label label17;
    private java.awt.Label label18;
    private java.awt.Label label19;
    private java.awt.Label label2;
    private java.awt.Label label20;
    private java.awt.Label label3;
    private java.awt.Label label4;
    private java.awt.Label label5;
    private java.awt.Label label6;
    private java.awt.Label label8;
    private java.awt.Label label9;
}
