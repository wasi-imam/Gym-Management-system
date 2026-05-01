import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;

public class PaymentPanel extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;

    public PaymentPanel() {
        setLayout(new BorderLayout());

        // ═══ TOP - Title ═══
        JLabel title = new JLabel("Payment Status", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));

        // ═══ CENTER - Table ═══
        String[] columns = {"ID", "Name", "Membership Type", "Fee Paid"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);

        // ═══ BOTTOM - Buttons ═══
        JPanel btnPanel = new JPanel();
        JButton markPaidBtn = new JButton("Mark as Paid");
        JButton markUnpaidBtn = new JButton("Mark as Unpaid");
        JButton refreshBtn = new JButton("Refresh");

        btnPanel.add(markPaidBtn);
        btnPanel.add(markUnpaidBtn);
        btnPanel.add(refreshBtn);

        // ═══ Sab Add Karo ═══
        add(title, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);

        // ═══ Button Actions ═══
        markPaidBtn.addActionListener(e -> updateFee("Yes"));
        markUnpaidBtn.addActionListener(e -> updateFee("No"));
        refreshBtn.addActionListener(e -> loadPayments());

        // ═══ Data Load Karo ═══
        loadPayments();
    }

    // Database Se Members Load Karo
    private void loadPayments() {
        tableModel.setRowCount(0);

        try {
            Connection conn = DBConnection.getConnection();
            String sql = "SELECT id, name, mem_type, fee_paid FROM members";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("mem_type"),
                    rs.getString("fee_paid")
                });
            }
            conn.close();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    // Fee Status Update Karo
    private void updateFee(String status) {
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a member first!");
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);

        try {
            Connection conn = DBConnection.getConnection();
            String sql = "UPDATE members SET fee_paid = ? WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, status);
            ps.setInt(2, id);
            ps.executeUpdate();
            conn.close();

            JOptionPane.showMessageDialog(this, "Payment Status Updated!");
            loadPayments();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}