import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class MemberPanel extends JPanel {

    // Table ke liye
    private JTable table;
    private DefaultTableModel tableModel;

    // Form fields
    private JTextField nameField, phoneField;
    private JComboBox<String> memTypeBox;

    public MemberPanel() {
        setLayout(new BorderLayout());

        // ═══ TOP - Form Area ═══
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Add Member"));

        formPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);

        formPanel.add(new JLabel("Phone:"));
        phoneField = new JTextField();
        formPanel.add(phoneField);

        formPanel.add(new JLabel("Membership Type:"));
        memTypeBox = new JComboBox<>(new String[]{"Monthly", "Quarterly", "Yearly"});
        formPanel.add(memTypeBox);

        JButton addBtn = new JButton("Add Member");
        formPanel.add(addBtn);

        // ═══ CENTER - Table Area ═══
        String[] columns = {"ID", "Name", "Phone", "Type", "Fee Paid"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);

        // ═══ BOTTOM - Delete Button ═══
        JButton deleteBtn = new JButton("Delete Selected");

        // ═══ Sab Add Karo Panel Mein ═══
        add(formPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(deleteBtn, BorderLayout.SOUTH);

        // ═══ Button Actions ═══
        addBtn.addActionListener(e -> addMember());
        deleteBtn.addActionListener(e -> deleteMember());

        // ═══ Table Data Load Karo ═══
        loadMembers();
    }

    // Member Add Karo Database Mein
    private void addMember() {
        String name = nameField.getText();
        String phone = phoneField.getText();
        String type = (String) memTypeBox.getSelectedItem();

        if (name.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter Name and Phone!");
            return;
        }

        try {
            Connection conn = DBConnection.getConnection();
            String sql = "INSERT INTO members (id, name, phone, mem_type, fee_paid) VALUES (members_seq.NEXTVAL, ?, ?, ?, 'No')";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, phone);
            ps.setString(3, type);
            ps.executeUpdate();
            conn.close();

            JOptionPane.showMessageDialog(this,"Member Added Successfully!");
            nameField.setText("");
            phoneField.setText("");
            loadMembers();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    // Database Se Members Load Karo Table Mein
    private void loadMembers() {
        tableModel.setRowCount(0); // pehle table clear karo

        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT * FROM members";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("phone"),
                    rs.getString("mem_type"),
                    rs.getString("fee_paid")
                });
            }
            conn.close();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    // Selected Member Delete Karo
    private void deleteMember() {
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a member first!");
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);

        try {
            Connection conn = DBConnection.getConnection();
            String sql = "DELETE FROM members WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            conn.close();

            JOptionPane.showMessageDialog(this, "Member Deleted Successfully!");
            loadMembers();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}