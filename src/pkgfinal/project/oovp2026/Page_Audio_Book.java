/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package pkgfinal.project.oovp2026;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import pkgfinal.project.oovp2026.MenuPage;
public class Page_Audio_Book extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Page_Audio_Book.class.getName());
    private MenuPage parent;

    /**
     * Creates new form Page_Book
     */
    public Page_Audio_Book() {
        initComponents();
        
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat calFormat = new SimpleDateFormat("dd/MM/yyyy");
        Borrow_Date.setText(calFormat.format(cal.getTime()));

    loadAudioBookTable();
    loadCategoryTable();
    loadAuthorTable();
    loadPublisherTable();
    
    loadCategoryCombo();
    loadAuthorCombo();
    loadPublisherCombo();
    loadAudioBookCombo();
    loadMemberCombo();

    wireAudioBookButtons();
    wireCategoryButtons();
    wireAuthorButtons();
    wirePublisherButtons();
    }

    /**
     * Constructor overload: Page_Book(MenuPage Parent)
     * Description: Navigation from MenuPage and keep reference to MenuPage as parent
     */
    public Page_Audio_Book(MenuPage Parent) {
        this();
        this.parent = parent;
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
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

    private void loadAudioBookTable() {
        String sql = "SELECT ab.Audio_Book_ID, ab.Audio_Book_Title, c.Category_Name, " +
                     "a.Author_Audio_Book_Name, p.Publisher_Name, ab.Audio_Book_Year, " +
                     "ab.ISBN, ab.Audio_Book_Duration, ab.Audio_Book_Format, " +
                     "CASE WHEN ab.Audio_Book_Status = 1 THEN 'Available' ELSE 'Borrowed' END " +
                     "FROM audio_book ab " +
                     "LEFT JOIN category c ON ab.Audio_Book_Category_ID = c.Category_ID " +
                     "LEFT JOIN author_audio_book a ON ab.Audio_Book_Author_ID = a.Author_Audio_Book_ID " +
                     "LEFT JOIN publisher p ON ab.Audio_Book_Publisher_ID = p.Publisher_ID";
        fillTable(AB_Table, sql,
                new String[]{"ID", "Title", "Category", "Author", "Publisher", "Year", "ISBN", "Duration (s)", "Format", "Status"});
    }
 
    private void loadCategoryCombo() {
        AB_Category.removeAllItems();
        String sql = "SELECT Category_ID, Category_Name FROM category";
        try (Connection con = getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                AB_Category.addItem(rs.getString("Category_ID") + " - " + rs.getString("Category_Name"));
            }
        } catch (SQLException e) {
            showError("loadCategoryCombo", e);
        }
    }
 
    private Connection getConnection() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getConnection'");
    }

    private void loadAuthorCombo() {
        AB_Author.removeAllItems();
        String sql = "SELECT Author_Audio_Book_ID, Author_Audio_Book_Name FROM author_audio_book";
        try (Connection con = getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                AB_Author.addItem(rs.getString("Author_Audio_Book_ID") + " - " + rs.getString("Author_Audio_Book_Name"));
            }
        } catch (SQLException e) {
            showError("loadAuthorCombo", e);
        }
    }
 
    private void loadPublisherCombo() {
        AB_Publisher.removeAllItems();
        String sql = "SELECT Publisher_ID, Publisher_Name FROM publisher";
        try (Connection con = getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                AB_Publisher.addItem(rs.getString("Publisher_ID") + " - " + rs.getString("Publisher_Name"));
            }
        } catch (SQLException e) {
            // Publisher may not have data yet; silently skip
        }
    }
 
    private void wireAudioBookButtons() {
        Add_AB.addActionListener(e -> addAudioBook());
        Edit_AB.addActionListener(e -> editAudioBook());
        Delete_AB.addActionListener(e -> deleteAudioBook());
        Refresh_AB.addActionListener(e -> {
            loadAudioBookTable();
            clearAudioBookForm();
        });
        AB_Table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                AB_TableMouseClicked(evt);
            }
        });
    }
 
    private void addAudioBook() {
        String title = AB_Title.getText().trim();
        if (title.isEmpty()) { JOptionPane.showMessageDialog(this, "Title tidak boleh kosong!"); return; }
 
        String catId  = extractId(AB_Category.getSelectedItem());
        String authId = extractId(AB_Author.getSelectedItem());
        String pubId  = extractId(AB_Publisher.getSelectedItem());
        String year   = AB_Year.getText().trim();
        String isbn   = AB_ISBN.getText().trim();
        String dur    = AB_Duration.getText().trim();
        String format = AB_Format.getSelectedItem().toString();
        
        String newId = generateNextId("SELECT Audio_Book_ID FROM audio_book ORDER BY Audio_Book_ID DESC LIMIT 1", "ABK");
        String sql = "INSERT INTO audio_book (Audio_Book_ID, Audio_Book_Title, Audio_Book_Author_ID, " +
                     "Audio_Book_Category_ID, Audio_Book_Publisher_ID, Audio_Book_Year, " +
                     "ISBN, Audio_Book_Duration, Audio_Book_Format, Audio_Book_Status) " +
                     "VALUES (?,?,?,?,?,?,?,?,?,1)";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, newId);
            ps.setString(2, title);
            ps.setString(3, authId);
            ps.setString(4, catId);
            ps.setString(5, pubId.isEmpty() ? null : pubId);
            ps.setInt(6, Integer.parseInt(year.isEmpty() ? "0" : year));
            ps.setString(7, isbn);
            ps.setInt(8, Integer.parseInt(dur.isEmpty() ? "0" : dur));
            ps.setString(9, format);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Audio Book berhasil ditambahkan!");
            loadAudioBookTable();
            clearAudioBookForm();
        } catch (SQLException e) {
            showError("addAudioBook", e);
        }
    }
 
    private void editAudioBook() {
        int row = AB_Table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Pilih baris yang ingin diedit!"); return; }
 
        String id     = AB_Table.getValueAt(row, 0).toString();
        String title  = AB_Title.getText().trim();
        String catId  = extractId(AB_Category.getSelectedItem());
        String authId = extractId(AB_Author.getSelectedItem());
        String pubId  = extractId(AB_Publisher.getSelectedItem());
        String year   = AB_Year.getText().trim();
        String isbn   = AB_ISBN.getText().trim();
        String dur    = AB_Duration.getText().trim();
        String format = AB_Format.getSelectedItem().toString();
 
        String sql = "UPDATE audio_book SET Audio_Book_Title=?, Audio_Book_Author_ID=?, " +
                     "Audio_Book_Category_ID=?, Audio_Book_Publisher_ID=?, Audio_Book_Year=?, " +
                     "ISBN=?, Audio_Book_Duration=?, Audio_Book_Format=? WHERE Audio_Book_ID=?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, title);
            ps.setString(2, authId);
            ps.setString(3, catId);
            ps.setString(4, pubId.isEmpty() ? null : pubId);
            ps.setInt(5, Integer.parseInt(year.isEmpty() ? "0" : year));
            ps.setString(6, isbn);
            ps.setInt(7, Integer.parseInt(dur.isEmpty() ? "0" : dur));
            ps.setString(8, format);
            ps.setString(9, id);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Audio Book berhasil diupdate!");
            loadAudioBookTable();
            clearAudioBookForm();
        } catch (SQLException e) {
            showError("editAudioBook", e);
        }
    }
    
    private String generateNextId(String query, String prefix) {
    try (Connection con = getConnection();
         Statement st = con.createStatement();
         ResultSet rs = st.executeQuery(query)) {
        if (rs.next()) {
            String lastId = rs.getString(1);
            int num = Integer.parseInt(lastId.replaceAll("[^0-9]", "")) + 1;
            return prefix + String.format("%03d", num);
        }
    } catch (Exception e) { /* tabel kosong */ }
    return prefix + "001";
}
    
    private void deleteAudioBook() {
        int row = AB_Table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Pilih baris yang ingin dihapus!"); return; }
 
        String id = AB_Table.getValueAt(row, 0).toString();
        int confirm = JOptionPane.showConfirmDialog(this,
                "Hapus Audio Book ID: " + id + "?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
 
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement("DELETE FROM audio_book WHERE Audio_Book_ID=?")) {
            ps.setString(1, id);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Audio Book berhasil dihapus!");
            loadAudioBookTable();
            clearAudioBookForm();
        } catch (SQLException e) {
            showError("deleteAudioBook", e);
        }
    }
 
    private void clearAudioBookForm() {
        AB_Title.setText("");
        AB_Year.setText("");
        AB_ISBN.setText("");
        AB_Duration.setText("");
        if (AB_Category.getItemCount() > 0) AB_Category.setSelectedIndex(0);
        if (AB_Author.getItemCount() > 0) AB_Author.setSelectedIndex(0);
        if (AB_Publisher.getItemCount() > 0) AB_Publisher.setSelectedIndex(0);
        AB_Table.clearSelection();
    }
    
    private void loadCategoryTable() {
        fillTable(Cat_Table, "SELECT Category_ID, Category_Name, Description FROM category",
                new String[]{"ID", "Name", "Description"});
    }
 
    private void wireCategoryButtons() {
        Add_Cat.addActionListener(e -> addCategory());
        Edit_Cat.addActionListener(e -> editCategory());
        Delete_Cat.addActionListener(e -> deleteCategory());
        Refresh_Cat.addActionListener(e -> { loadCategoryTable(); clearCategoryForm(); });
        Cat_Table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = Cat_Table.getSelectedRow();
                if (row < 0) return;
                Cat_Name.setText(nullSafe(Cat_Table.getValueAt(row, 1)));
                Cat_Description.setText(nullSafe(Cat_Table.getValueAt(row, 2)));
            }
        });
    }
 
    private void addCategory() {
        String name = Cat_Name.getText().trim();
        String desc = Cat_Description.getText().trim();
        if (name.isEmpty()) { JOptionPane.showMessageDialog(this, "Category Name tidak boleh kosong!"); return; }
        
        
        String newId = generateNextId("SELECT Category_ID FROM category ORDER BY Category_ID DESC LIMIT 1", "CAT");
        try (Connection con = getConnection();
     PreparedStatement ps = con.prepareStatement(
             "INSERT INTO category (Category_ID, Category_Name, Description) VALUES (?,?,?)")) {
    ps.setString(1, newId);
    ps.setString(2, name);
    ps.setString(3, desc.isEmpty() ? null : desc);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Category berhasil ditambahkan!");
            loadCategoryTable();
            loadCategoryCombo();
            clearCategoryForm();
        } catch (SQLException e) {
            showError("addCategory", e);
        }
    }
 
    private void editCategory() {
        int row = Cat_Table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Pilih baris yang ingin diedit!"); return; }
 
        String id   = Cat_Table.getValueAt(row, 0).toString();
        String name = Cat_Name.getText().trim();
        String desc = Cat_Description.getText().trim();
 
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "UPDATE category SET Category_Name=?, Description=? WHERE Category_ID=?")) {
            ps.setString(1, name);
            ps.setString(2, desc.isEmpty() ? null : desc);
            ps.setString(3, id);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Category berhasil diupdate!");
            loadCategoryTable();
            loadCategoryCombo();
            clearCategoryForm();
        } catch (SQLException e) {
            showError("editCategory", e);
        }
    }
 
    private void deleteCategory() {
        int row = Cat_Table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Pilih baris yang ingin dihapus!"); return; }
 
        String id = Cat_Table.getValueAt(row, 0).toString();
        int confirm = JOptionPane.showConfirmDialog(this,
                "Hapus Category ID: " + id + "?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
 
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement("DELETE FROM category WHERE Category_ID=?")) {
            ps.setString(1, id);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Category berhasil dihapus!");
            loadCategoryTable();
            loadCategoryCombo();
            clearCategoryForm();
        } catch (SQLException e) {
            showError("deleteCategory", e);
        }
    }
 
    private void clearCategoryForm() {
        Cat_Name.setText("");
        Cat_Description.setText("");
        Cat_Table.clearSelection();
    }
    
    private void loadAuthorTable() {
        fillTable(Auth_Table,
                "SELECT Author_Audio_Book_ID, Author_Audio_Book_Name, Author_Audio_Book_Contact FROM author_audio_book",
                new String[]{"ID", "Name", "Contact"});
    }
 
    private void wireAuthorButtons() {
        Auth_Table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = Auth_Table.getSelectedRow();
                if (row < 0) return;
                Auth_Name.setText(nullSafe(Auth_Table.getValueAt(row, 1)));
                Auth_Contact.setText(nullSafe(Auth_Table.getValueAt(row, 2)));
            }
        });
    }
 
    private void addAuthor() {
        String name    = Auth_Name.getText().trim();
        String contact = Auth_Contact.getText().trim();
        if (name.isEmpty()) { JOptionPane.showMessageDialog(this, "Author Name tidak boleh kosong!"); return; }
        
        String newId = generateNextId("SELECT Author_Audio_Book_ID FROM author_audio_book ORDER BY Author_Audio_Book_ID DESC LIMIT 1", "AUTHAB");
        try (Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO author_audio_book (Author_Audio_Book_ID, Author_Audio_Book_Name, Author_Audio_Book_Contact) VALUES (?,?,?)")) {
            ps.setString(1, newId);
            ps.setString(2, name);
            ps.setString(3, contact.isEmpty() ? null : contact);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Author berhasil ditambahkan!");
            loadAuthorTable();
            loadAuthorCombo();
            clearAuthorForm();
        } catch (SQLException e) {
            showError("addAuthor", e);
        }
    }
 
    private void editAuthor() {
        int row = Auth_Table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Pilih baris yang ingin diedit!"); return; }
 
        String id      = Auth_Table.getValueAt(row, 0).toString();
        String name    = Auth_Name.getText().trim();
        String contact = Auth_Contact.getText().trim();
 
        if (name.isEmpty()) { JOptionPane.showMessageDialog(this, "Author Name tidak boleh kosong!"); return; }
        try (Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement(
                "UPDATE author_audio_book SET Author_Audio_Book_Name=?, Author_Audio_Book_Contact=? WHERE Author_Audio_Book_ID=?")) {
            ps.setString(1, name);
            ps.setString(2, contact.isEmpty() ? null : contact);
            ps.setString(3, id);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Author berhasil diupdate!");
            loadAuthorTable();
            loadAuthorCombo();
            clearAuthorForm();
        } catch (SQLException e) {
            showError("editAuthor", e);
        }
    }
 
    private void deleteAuthor() {
        int row = Auth_Table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Pilih baris yang ingin dihapus!"); return; }
 
        String id = Auth_Table.getValueAt(row, 0).toString();
        int confirm = JOptionPane.showConfirmDialog(this,
                "Hapus Author ID: " + id + "?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
 
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement("DELETE FROM author_audio_book WHERE Author_Audio_Book_ID=?")) {
            ps.setString(1, id);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Author berhasil dihapus!");
            loadAuthorTable();
            loadAuthorCombo();
            clearAuthorForm();
        } catch (SQLException e) {
            showError("deleteAuthor", e);
        }
    }
 
    private void clearAuthorForm() {
        Auth_Name.setText("");
        Auth_Contact.setText("");
        Auth_Table.clearSelection();
    }
    
    private void loadPublisherTable() {
        fillTable(Pub_Table,
                "SELECT Publisher_ID, Publisher_Name, Publisher_Contact FROM publisher",
                new String[]{"ID", "Name", "Contact"});
    }
 
    private void wirePublisherButtons() {
        Add_Pub.addActionListener(e -> addPublisher());
        Edit_Pub.addActionListener(e -> editPublisher());
        Delete_Pub.addActionListener(e -> deletePublisher());
        Refresh_Pub.addActionListener(e -> { loadPublisherTable(); clearPublisherForm(); });
        Pub_Table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = Pub_Table.getSelectedRow();
                if (row < 0) return;
                Pub_Name.setText(nullSafe(Pub_Table.getValueAt(row, 1)));
                Pub_Contact.setText(nullSafe(Pub_Table.getValueAt(row, 2)));
            }
        });
    }
 
    private void addPublisher() {
        String name    = Pub_Name.getText().trim();
        String contact = Pub_Contact.getText().trim();
        if (name.isEmpty()) { JOptionPane.showMessageDialog(this, "Publisher Name tidak boleh kosong!"); return; }
 
        String newId = generateNextId("SELECT Publisher_ID FROM publisher ORDER BY Publisher_ID DESC LIMIT 1", "PUB");
        try (Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO publisher (Publisher_ID, Publisher_Name, Publisher_Contact) VALUES (?,?,?)")) {
            ps.setString(1, newId);
            ps.setString(2, name);
            ps.setString(3, contact.isEmpty() ? null : contact);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Publisher berhasil ditambahkan!");
            loadPublisherTable();
            loadPublisherCombo();
            clearPublisherForm();
        } catch (SQLException e) {
            showError("addPublisher", e);
        }
    }
 
    private void editPublisher() {
        int row = Pub_Table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Pilih baris yang ingin diedit!"); return; }
 
        String id      = Pub_Table.getValueAt(row, 0).toString();
        String name    = Pub_Name.getText().trim();
        String contact = Pub_Contact.getText().trim();
 
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "UPDATE publisher SET Publisher_Name=?, Publisher_Contact=? WHERE Publisher_ID=?")) {
            ps.setString(1, name);
            ps.setString(2, contact.isEmpty() ? null : contact);
            ps.setString(3, id);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Publisher berhasil diupdate!");
            loadPublisherTable();
            loadPublisherCombo();
            clearPublisherForm();
        } catch (SQLException e) {
            showError("editPublisher", e);
        }
    }
 
    private void deletePublisher() {
        int row = Pub_Table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Pilih baris yang ingin dihapus!"); return; }
 
        String id = Pub_Table.getValueAt(row, 0).toString();
        int confirm = JOptionPane.showConfirmDialog(this,
                "Hapus Publisher ID: " + id + "?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
 
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement("DELETE FROM publisher WHERE Publisher_ID=?")) {
            ps.setString(1, id);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Publisher berhasil dihapus!");
            loadPublisherTable();
            loadPublisherCombo();
            clearPublisherForm();
        } catch (SQLException e) {
            showError("deletePublisher", e);
        }
    }
 
    private void clearPublisherForm() {
        Pub_Name.setText("");
        Pub_Contact.setText("");
        Pub_Table.clearSelection();
    }
    
    private void loadBorrowTable() {
    ((javax.swing.table.DefaultTableModel) Borrow_Table.getModel()).setRowCount(0);
    
        if (true) return; // skip query
        String sql = "SELECT Issued_Audio_Book_ID, Audio_Book_ID, Member_ID, " +
                     "DATE_FORMAT(Date_issued,'%d/%m/%Y %H:%i'), DATE_FORMAT(Date_returned,'%d/%m/%Y %H:%i') " +
                     "FROM issued_audio_book";
        fillTable(Borrow_Table, sql,
                new String[]{"ID", "Audio Book ID", "Member ID", "Date Issued", "Date Returned"});
    }
 
    private void loadAudioBookCombo() {
        Borrow_AB.removeAllItems();
        String sql = "SELECT Audio_Book_ID, Audio_Book_Title FROM audio_book WHERE Audio_Book_Status=1";
        try (Connection con = getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Borrow_AB.addItem(rs.getString("Audio_Book_ID") + " - " + rs.getString("Audio_Book_Title"));
            }
        } catch (SQLException e) {
            showError("loadAudioBookCombo", e);
        }
    }
 
    private void loadMemberCombo() {
        Borrow_Member.removeAllItems();
        String sql = "SELECT m.Member_ID, m.Member_Name, mt.Type_Name " +
                     "FROM member m LEFT JOIN member_type mt ON m.Member_Type_ID = mt.Member_Type_ID";
        try (Connection con = getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Borrow_Member.addItem(rs.getString("Member_ID") + " - " +
                        rs.getString("Member_Name") + " (" + rs.getString("Type_Name") + ")");
            }
        } catch (SQLException e) {
            showError("loadMemberCombo", e);
        }
    }
 
    private void wireBorrowButtons() {
        Add_Borrow.addActionListener(e -> addBorrow());
        Edit_Borrow.addActionListener(e -> editBorrow());
        Delete_Borrow.addActionListener(e -> deleteBorrow());
        Refresh_Borrow.addActionListener(e -> {
            loadBorrowTable();
            loadAudioBookCombo();
            loadMemberCombo();
            clearBorrowForm();
        });
        Borrow_Table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = Borrow_Table.getSelectedRow();
                if (row < 0) return;
                // Show borrow date & return date in text fields
                Borrow_Date.setText(nullSafe(Borrow_Table.getValueAt(row, 3)));
                Return_Date.setText(nullSafe(Borrow_Table.getValueAt(row, 4)));
            }
        });
        Borrow_Member.addItemListener(this::countDate);
    }
 
    private void addBorrow() {
        JOptionPane.showMessageDialog(this, "Fitur borrow akan tersedia versi online.");
        
        if (true) return;
        if (Borrow_AB.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this, "Tidak ada Audio Book tersedia!"); return;
        }
        String abId     = extractId(Borrow_AB.getSelectedItem());
        String memberId = extractId(Borrow_Member.getSelectedItem());
        String issued   = Borrow_Date.getText().trim();
        String returned = Return_Date.getText().trim();
 
        // Convert dd/MM/yyyy HH:mm or dd/MM/yyyy to yyyy-MM-dd HH:mm:ss for MySQL
        String issuedSql   = convertDateToSql(issued);
        String returnedSql = convertDateToSql(returned);
 
        String sql = "INSERT INTO issued_audio_book (Audio_Book_ID, Member_ID, Date_issued, Date_returned) VALUES (?,?,?,?)";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, abId);
            ps.setString(2, memberId);
            ps.setString(3, issuedSql);
            ps.setString(4, returnedSql);
            ps.executeUpdate();
 
            // Mark audio book as borrowed (status = 0)
            try (PreparedStatement ps2 = con.prepareStatement(
                    "UPDATE audio_book SET Audio_Book_Status=0 WHERE Audio_Book_ID=?")) {
                ps2.setString(1, abId);
                ps2.executeUpdate();
            }
 
            JOptionPane.showMessageDialog(this, "Peminjaman berhasil ditambahkan!");
            loadBorrowTable();
            loadAudioBookCombo();
            clearBorrowForm();
        } catch (SQLException e) {
            showError("addBorrow", e);
        }
    }
 
    private void editBorrow() {
        JOptionPane.showMessageDialog(this, "Fitur borrow akan tersedia versi online.");
        if (true) return;
        int row = Borrow_Table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Pilih baris yang ingin diedit!"); return; }
 
        String id       = Borrow_Table.getValueAt(row, 0).toString();
        String abId     = extractId(Borrow_AB.getSelectedItem());
        String memberId = extractId(Borrow_Member.getSelectedItem());
        String issued   = Borrow_Date.getText().trim();
        String returned = Return_Date.getText().trim();
 
        String issuedSql   = convertDateToSql(issued);
        String returnedSql = convertDateToSql(returned);
 
        String sql = "UPDATE issued_audio_book SET Audio_Book_ID=?, Member_ID=?, Date_issued=?, Date_returned=? " +
                     "WHERE Issued_Audio_Book_ID=?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, abId);
            ps.setString(2, memberId);
            ps.setString(3, issuedSql);
            ps.setString(4, returnedSql);
            ps.setString(5, id);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data peminjaman berhasil diupdate!");
            loadBorrowTable();
            clearBorrowForm();
        } catch (SQLException e) {
            showError("editBorrow", e);
        }
    }
 
    private void deleteBorrow() {
        JOptionPane.showMessageDialog(this, "Fitur borrow akan tersedia versi online.");
        if (true) return;
        int row = Borrow_Table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Pilih baris yang ingin dihapus!"); return; }
 
        String id   = Borrow_Table.getValueAt(row, 0).toString();
        String abId = Borrow_Table.getValueAt(row, 1).toString();
 
        int confirm = JOptionPane.showConfirmDialog(this,
                "Hapus data peminjaman ID: " + id + "?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
 
        try (Connection con = getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(
                    "DELETE FROM issued_audio_book WHERE Issued_Audio_Book_ID=?")) {
                ps.setString(1, id);
                ps.executeUpdate();
            }
            // Restore audio book status to available
            try (PreparedStatement ps2 = con.prepareStatement(
                    "UPDATE audio_book SET Audio_Book_Status=1 WHERE Audio_Book_ID=?")) {
                ps2.setString(1, abId);
                ps2.executeUpdate();
            }
            JOptionPane.showMessageDialog(this, "Data peminjaman berhasil dihapus!");
            loadBorrowTable();
            loadAudioBookCombo();
            clearBorrowForm();
        } catch (SQLException e) {
            showError("deleteBorrow", e);
        }
    }
 
    private void clearBorrowForm() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Borrow_Date.setText(sdf.format(cal.getTime()));
        Return_Date.setText("");
        Borrow_Table.clearSelection();
    }
    
    private void fillTable(JTable table, String sql, String[] columns) {
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };
        try (Connection con = getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            int colCount = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                Object[] row = new Object[colCount];
                for (int i = 0; i < colCount; i++) row[i] = rs.getString(i + 1);
                model.addRow(row);
            }
        } catch (SQLException e) {
            showError("fillTable", e);
        }
        table.setModel(model);
    }
 
    /** Extract the ID part before " - " from a combo item string. */
    private String extractId(Object item) {
        if (item == null) return "";
        String s = item.toString();
        int idx = s.indexOf(" - ");
        return idx >= 0 ? s.substring(0, idx).trim() : s.trim();
    }
 
    /** Safe null-to-empty conversion for table cell values. */
    private String nullSafe(Object val) {
        return val == null ? "" : val.toString();
    }
 
    /**
     * Convert date string "dd/MM/yyyy" or "dd/MM/yyyy HH:mm" to MySQL "yyyy-MM-dd HH:mm:ss".
     */
    private String convertDateToSql(String input) {
        if (input == null || input.isEmpty()) return null;
        try {
            if (input.contains(" ")) {
                SimpleDateFormat in  = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                SimpleDateFormat out = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                return out.format(in.parse(input));
            } else {
                SimpleDateFormat in  = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat out = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                return out.format(in.parse(input));
            }
        } catch (Exception ex) {
            return input; // fallback: return as-is
        }
    }
 
    private void showError(String context, Exception e) {
        logger.log(java.util.logging.Level.SEVERE, context, e);
        JOptionPane.showMessageDialog(this, "Error [" + context + "]: " + e.getMessage(),
                "Database Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private void countDate() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Calendar cal = Calendar.getInstance();

            // Get the date from jTextField1
            cal.setTime(sdf.parse(Borrow_Date.getText()));

            // Logic duration from Borrow_Member (JComboBox)
            String selectedMember = Borrow_Member.getSelectedItem().toString();
            int duration = 7; // Default
            
            if (selectedMember.contains("Lect") || selectedMember.contains("Std") || selectedMember.contains("Gst")) {
                duration = 14;
            }

            cal.add(Calendar.DAY_OF_MONTH, duration);

            String returnDate = sdf.format(cal.getTime());
            label17.setText("Return Date: " + returnDate);

        } catch (Exception e) {
            System.err.println("Error while counting days: " + e.getMessage());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        LogOutButton = new java.awt.Button();
        MenuButton = new java.awt.Button();
        TabMenu = new javax.swing.JTabbedPane();
        AB = new javax.swing.JPanel();
        label1 = new java.awt.Label();
        AB_Title = new javax.swing.JTextField();
        label2 = new java.awt.Label();
        AB_Category = new javax.swing.JComboBox<>();
        label3 = new java.awt.Label();
        AB_Author = new javax.swing.JComboBox<>();
        label4 = new java.awt.Label();
        AB_Publisher = new javax.swing.JComboBox<>();
        label5 = new java.awt.Label();
        AB_Year = new javax.swing.JTextField();
        label6 = new java.awt.Label();
        AB_ISBN = new javax.swing.JTextField();
        label7 = new java.awt.Label();
        AB_Duration = new javax.swing.JTextField();
        label18 = new java.awt.Label();
        AB_Format = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        AB_Table = new javax.swing.JTable();
        Add_AB = new javax.swing.JButton();
        Edit_AB = new javax.swing.JButton();
        Refresh_AB = new javax.swing.JButton();
        Delete_AB = new javax.swing.JButton();
        Category = new javax.swing.JPanel();
        label8 = new java.awt.Label();
        Cat_Name = new javax.swing.JTextField();
        label9 = new java.awt.Label();
        jScrollPane2 = new javax.swing.JScrollPane();
        Cat_Description = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        Cat_Table = new javax.swing.JTable();
        Add_Cat = new javax.swing.JButton();
        Edit_Cat = new javax.swing.JButton();
        Refresh_Cat = new javax.swing.JButton();
        Delete_Cat = new javax.swing.JButton();
        Author = new javax.swing.JPanel();
        label10 = new java.awt.Label();
        Auth_Name = new javax.swing.JTextField();
        label11 = new java.awt.Label();
        Auth_Contact = new javax.swing.JTextField();
        jScrollPane4 = new javax.swing.JScrollPane();
        Auth_Table = new javax.swing.JTable();
        Add_Auth = new javax.swing.JButton();
        Edit_Auth = new javax.swing.JButton();
        Refresh_Auth = new javax.swing.JButton();
        Delete_Auth = new javax.swing.JButton();
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
        Borrow = new javax.swing.JPanel();
        label14 = new java.awt.Label();
        Borrow_AB = new javax.swing.JComboBox<>();
        label15 = new java.awt.Label();
        Borrow_Member = new javax.swing.JComboBox<>();
        label16 = new java.awt.Label();
        Borrow_Date = new javax.swing.JTextField();
        label17 = new java.awt.Label();
        Return_Date = new javax.swing.JTextField();
        jScrollPane6 = new javax.swing.JScrollPane();
        Borrow_Table = new javax.swing.JTable();
        Add_Borrow = new javax.swing.JButton();
        Edit_Borrow = new javax.swing.JButton();
        Refresh_Borrow = new javax.swing.JButton();
        Delete_Borrow = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        LogOutButton.setLabel("Log Out");
        LogOutButton.addActionListener(this::LogOutButtonActionPerformed);

        MenuButton.setLabel("Back to Menu");

        TabMenu.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        label1.setText("Title");

        AB_Title.addActionListener(this::AB_TitleActionPerformed);

        label2.setText("Category");

        AB_Category.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1- Cat1", "2- Cat2", "3- Cat3" }));

        label3.setText("Author");

        AB_Author.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1- Author1", "2- Author2", "3 Author3" }));

        label4.setText("Publisher");

        AB_Publisher.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1- Pub1", "2- Pub2", "3- Pub3" }));

        label5.setText("Year");

        AB_Year.addActionListener(this::AB_YearActionPerformed);

        label6.setText("ISBN");

        AB_ISBN.addActionListener(this::AB_ISBNActionPerformed);

        label7.setText("Duration");

        AB_Duration.setText("in the second");
        AB_Duration.addActionListener(this::AB_DurationActionPerformed);

        label18.setText("Format");

        AB_Format.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "MP3", "AAC", "WAV" }));

        AB_Table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Title", "Category", "Author", "Publisher", "Year", "ISBN", "Duration", "Format", "Status"
            }
        ));
        AB_Table.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        AB_Table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                AB_TableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(AB_Table);

        Add_AB.setText("Add");
        Add_AB.addActionListener(this::Add_ABActionPerformed);

        Edit_AB.setText("Edit");
        Edit_AB.addActionListener(this::Edit_ABActionPerformed);

        Refresh_AB.setText("Refresh");
        Refresh_AB.addActionListener(this::Refresh_ABActionPerformed);

        Delete_AB.setForeground(new java.awt.Color(204, 0, 51));
        Delete_AB.setText("Delete");
        Delete_AB.addActionListener(this::Delete_ABActionPerformed);

        javax.swing.GroupLayout ABLayout = new javax.swing.GroupLayout(AB);
        AB.setLayout(ABLayout);
        ABLayout.setHorizontalGroup(
            ABLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ABLayout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addGroup(ABLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(ABLayout.createSequentialGroup()
                        .addGroup(ABLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(label3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(label2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(label4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(ABLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(AB_Category, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(AB_Title)
                            .addComponent(AB_Author, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(AB_Publisher, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(ABLayout.createSequentialGroup()
                        .addComponent(Add_AB)
                        .addGap(42, 42, 42)
                        .addComponent(Edit_AB)))
                .addGroup(ABLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ABLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(Refresh_AB)
                        .addGap(42, 42, 42)
                        .addComponent(Delete_AB))
                    .addGroup(ABLayout.createSequentialGroup()
                        .addGap(67, 67, 67)
                        .addGroup(ABLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(ABLayout.createSequentialGroup()
                                .addGroup(ABLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(label6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(label5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(28, 28, 28)
                                .addGroup(ABLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(AB_Year, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
                                    .addComponent(AB_ISBN)))
                            .addGroup(ABLayout.createSequentialGroup()
                                .addComponent(label7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(AB_Duration, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(ABLayout.createSequentialGroup()
                                .addComponent(label18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(AB_Format, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(86, 86, 86))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ABLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 548, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );
        ABLayout.setVerticalGroup(
            ABLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ABLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(ABLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ABLayout.createSequentialGroup()
                        .addGroup(ABLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(AB_Title, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(ABLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(ABLayout.createSequentialGroup()
                                .addComponent(label2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(label3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(ABLayout.createSequentialGroup()
                                .addComponent(AB_Category, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(AB_Author, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(ABLayout.createSequentialGroup()
                        .addGroup(ABLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(AB_Year, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(label5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(ABLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(AB_ISBN, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(label6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(ABLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(label7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(AB_Duration, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ABLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(AB_Publisher, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(ABLayout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(label18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(AB_Format, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(ABLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Add_AB)
                    .addComponent(Edit_AB)
                    .addComponent(Refresh_AB)
                    .addComponent(Delete_AB))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );

        TabMenu.addTab("Audio Book", AB);

        label8.setText("Name");

        Cat_Name.addActionListener(this::Cat_NameActionPerformed);

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

        Edit_Cat.setText("Edit");

        Refresh_Cat.setText("Refresh");

        Delete_Cat.setForeground(new java.awt.Color(204, 0, 51));
        Delete_Cat.setText("Delete");

        javax.swing.GroupLayout CategoryLayout = new javax.swing.GroupLayout(Category);
        Category.setLayout(CategoryLayout);
        CategoryLayout.setHorizontalGroup(
            CategoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CategoryLayout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addGroup(CategoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(CategoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(CategoryLayout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(CategoryLayout.createSequentialGroup()
                        .addComponent(Cat_Name, javax.swing.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE)
                        .addGap(296, 296, 296))))
            .addGroup(CategoryLayout.createSequentialGroup()
                .addGap(73, 73, 73)
                .addComponent(Add_Cat)
                .addGap(42, 42, 42)
                .addComponent(Edit_Cat)
                .addGap(49, 49, 49)
                .addComponent(Refresh_Cat)
                .addGap(42, 42, 42)
                .addComponent(Delete_Cat)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(CategoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(CategoryLayout.createSequentialGroup()
                    .addGap(14, 14, 14)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 548, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(14, Short.MAX_VALUE)))
        );
        CategoryLayout.setVerticalGroup(
            CategoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CategoryLayout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(CategoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Cat_Name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(CategoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(44, 44, 44)
                .addGroup(CategoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Add_Cat)
                    .addComponent(Edit_Cat)
                    .addComponent(Refresh_Cat)
                    .addComponent(Delete_Cat))
                .addContainerGap(372, Short.MAX_VALUE))
            .addGroup(CategoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(CategoryLayout.createSequentialGroup()
                    .addGap(253, 253, 253)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(31, Short.MAX_VALUE)))
        );

        TabMenu.addTab("Category", Category);

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
                            .addComponent(label11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(21, 21, 21)
                        .addGroup(AuthorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Auth_Contact, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                .addGap(24, 24, 24)
                .addGroup(AuthorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Auth_Name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(AuthorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(label11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Auth_Contact, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addGroup(AuthorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Add_Auth)
                    .addComponent(Edit_Auth)
                    .addComponent(Refresh_Auth)
                    .addComponent(Delete_Auth))
                .addGap(26, 26, 26)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 416, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
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
        jScrollPane5.setViewportView(Pub_Table);

        Add_Pub.setText("Add");

        Edit_Pub.setText("Edit");

        Refresh_Pub.setText("Refresh");

        Delete_Pub.setForeground(new java.awt.Color(204, 0, 51));
        Delete_Pub.setText("Delete");

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
                            .addComponent(label13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(21, 21, 21)
                        .addGroup(PublisherLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Pub_Contact, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                .addGap(24, 24, 24)
                .addGroup(PublisherLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Pub_Name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(PublisherLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(label13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Pub_Contact, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addGroup(PublisherLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Add_Pub)
                    .addComponent(Edit_Pub)
                    .addComponent(Refresh_Pub)
                    .addComponent(Delete_Pub))
                .addGap(26, 26, 26)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 416, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        TabMenu.addTab("Publisher", Publisher);

        label14.setText("Audio Book ID");

        Borrow_AB.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1- Book1", "2- Book2", "3- Book3" }));

        label15.setText("Member ID");

        Borrow_Member.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1- Member1", "2- Member2", "3- Member3" }));
        Borrow_Member.addItemListener(this::countDate);

        label16.setText("Borrow");

        Borrow_Date.setForeground(new java.awt.Color(204, 204, 204));
        Borrow_Date.setText("dd/mm/yyyy");
        Borrow_Date.addActionListener(this::Borrow_DateActionPerformed);

        label17.setText("Return");

        Return_Date.setForeground(new java.awt.Color(204, 204, 204));
        Return_Date.setText("dd/mm/yyyy");
        Return_Date.addActionListener(this::Return_DateActionPerformed);

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
        jScrollPane6.setViewportView(Borrow_Table);

        Add_Borrow.setText("Add");

        Edit_Borrow.setText("Edit");

        Refresh_Borrow.setText("Refresh");

        Delete_Borrow.setForeground(new java.awt.Color(204, 0, 51));
        Delete_Borrow.setText("Delete");

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
                            .addComponent(label17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(21, 21, 21)
                        .addGroup(BorrowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(Borrow_Member, 0, 120, Short.MAX_VALUE)
                            .addComponent(Borrow_AB, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Borrow_Date)
                            .addComponent(Return_Date)))
                    .addGroup(BorrowLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 540, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(BorrowLayout.createSequentialGroup()
                        .addGap(66, 66, 66)
                        .addComponent(Add_Borrow)
                        .addGap(42, 42, 42)
                        .addComponent(Edit_Borrow)
                        .addGap(47, 47, 47)
                        .addComponent(Refresh_Borrow)
                        .addGap(42, 42, 42)
                        .addComponent(Delete_Borrow)))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        BorrowLayout.setVerticalGroup(
            BorrowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BorrowLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(BorrowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Borrow_AB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(BorrowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Borrow_Member, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(BorrowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Borrow_Date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(BorrowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Return_Date, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                .addGroup(BorrowLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Add_Borrow)
                    .addComponent(Edit_Borrow)
                    .addComponent(Refresh_Borrow)
                    .addComponent(Delete_Borrow))
                .addGap(27, 27, 27)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );

        TabMenu.addTab("Borrow Audio Book", Borrow);

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
                .addComponent(TabMenu)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(MenuButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LogOutButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void AB_TitleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AB_TitleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_AB_TitleActionPerformed

    private void AB_YearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AB_YearActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_AB_YearActionPerformed

    private void AB_ISBNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AB_ISBNActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_AB_ISBNActionPerformed

    private void AB_DurationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AB_DurationActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_AB_DurationActionPerformed

    private void AB_TableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_AB_TableMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_AB_TableMouseClicked

    private void Cat_NameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Cat_NameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Cat_NameActionPerformed

    private void Cat_TableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Cat_TableMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_Cat_TableMouseClicked

    private void Borrow_DateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Borrow_DateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Borrow_DateActionPerformed

    private void Return_DateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Return_DateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_Return_DateActionPerformed

    private void countDate(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_countDate
        // TODO add your handling code here:
    }//GEN-LAST:event_countDate

    private void LogOutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LogOutButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_LogOutButtonActionPerformed

    private void Edit_ABActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Edit_ABActionPerformed
    editAudioBook();
    }//GEN-LAST:event_Edit_ABActionPerformed

    private void Add_ABActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Add_ABActionPerformed
    addAudioBook();
    }//GEN-LAST:event_Add_ABActionPerformed

    private void Delete_ABActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Delete_ABActionPerformed
    deleteAudioBook();
    }//GEN-LAST:event_Delete_ABActionPerformed

    private void Refresh_ABActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Refresh_ABActionPerformed
    loadAudioBookTable();
    loadCategoryCombo();
    loadAuthorCombo();
    loadPublisherCombo();
    clearAudioBookForm();
    }//GEN-LAST:event_Refresh_ABActionPerformed

    private void Edit_AuthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Edit_AuthActionPerformed
    editAuthor();
    }//GEN-LAST:event_Edit_AuthActionPerformed

    private void Add_AuthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Add_AuthActionPerformed
    addAuthor();
    }//GEN-LAST:event_Add_AuthActionPerformed

    private void Refresh_AuthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Refresh_AuthActionPerformed
    loadAuthorTable();
    clearAuthorForm();
    }//GEN-LAST:event_Refresh_AuthActionPerformed

    private void Delete_AuthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Delete_AuthActionPerformed
    deleteAuthor();
    }//GEN-LAST:event_Delete_AuthActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new Page_Audio_Book().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel AB;
    private javax.swing.JComboBox<String> AB_Author;
    private javax.swing.JComboBox<String> AB_Category;
    private javax.swing.JTextField AB_Duration;
    private javax.swing.JComboBox<String> AB_Format;
    private javax.swing.JTextField AB_ISBN;
    private javax.swing.JComboBox<String> AB_Publisher;
    private javax.swing.JTable AB_Table;
    private javax.swing.JTextField AB_Title;
    private javax.swing.JTextField AB_Year;
    private javax.swing.JButton Add_AB;
    private javax.swing.JButton Add_Auth;
    private javax.swing.JButton Add_Borrow;
    private javax.swing.JButton Add_Cat;
    private javax.swing.JButton Add_Pub;
    private javax.swing.JTextField Auth_Contact;
    private javax.swing.JTextField Auth_Name;
    private javax.swing.JTable Auth_Table;
    private javax.swing.JPanel Author;
    private javax.swing.JPanel Borrow;
    private javax.swing.JComboBox<String> Borrow_AB;
    private javax.swing.JTextField Borrow_Date;
    private javax.swing.JComboBox<String> Borrow_Member;
    private javax.swing.JTable Borrow_Table;
    private javax.swing.JTextArea Cat_Description;
    private javax.swing.JTextField Cat_Name;
    private javax.swing.JTable Cat_Table;
    private javax.swing.JPanel Category;
    private javax.swing.JButton Delete_AB;
    private javax.swing.JButton Delete_Auth;
    private javax.swing.JButton Delete_Borrow;
    private javax.swing.JButton Delete_Cat;
    private javax.swing.JButton Delete_Pub;
    private javax.swing.JButton Edit_AB;
    private javax.swing.JButton Edit_Auth;
    private javax.swing.JButton Edit_Borrow;
    private javax.swing.JButton Edit_Cat;
    private javax.swing.JButton Edit_Pub;
    private java.awt.Button LogOutButton;
    private java.awt.Button MenuButton;
    private javax.swing.JTextField Pub_Contact;
    private javax.swing.JTextField Pub_Name;
    private javax.swing.JTable Pub_Table;
    private javax.swing.JPanel Publisher;
    private javax.swing.JButton Refresh_AB;
    private javax.swing.JButton Refresh_Auth;
    private javax.swing.JButton Refresh_Borrow;
    private javax.swing.JButton Refresh_Cat;
    private javax.swing.JButton Refresh_Pub;
    private javax.swing.JTextField Return_Date;
    private javax.swing.JTabbedPane TabMenu;
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
    private java.awt.Label label2;
    private java.awt.Label label3;
    private java.awt.Label label4;
    private java.awt.Label label5;
    private java.awt.Label label6;
    private java.awt.Label label7;
    private java.awt.Label label8;
    private java.awt.Label label9;
    // End of variables declaration//GEN-END:variables
}
