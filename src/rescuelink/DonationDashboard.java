package rescuelink;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;



public class DonationDashboard extends JFrame {
    private final DonationDAO donationDAO = new DonationDAO();
    private final JTable donationTable;
    private final DefaultTableModel model;

    public DonationDashboard() {
        setTitle("Admin - Donation Dashboard");
        setSize(700, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        model = new DefaultTableModel(new Object[]{"ID", "Donor", "Type", "Amount", "Status"}, 0);
        donationTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(donationTable);

        JButton refreshBtn = new JButton("Refresh");
        JButton markReceivedBtn = new JButton("Mark as Received");
        

        JPanel btnPanel = new JPanel();
        btnPanel.add(refreshBtn);
        btnPanel.add(markReceivedBtn);
        

        add(scrollPane, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);

        refreshBtn.addActionListener(e -> loadDonations());
        markReceivedBtn.addActionListener(e -> updateStatus("Received"));
        

        loadDonations();
    }

    private void loadDonations() {
        model.setRowCount(0);
        List<Donation> list = donationDAO.getAllDonations();
        for (Donation d : list) {
            model.addRow(new Object[]{
                    d.getDonationId(), d.getDonorName(), d.getType(), d.getAmount(), d.getStatus()
            });
        }
    }

    private void updateStatus(String newStatus) {
        int row = donationTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a donation first.");
            return;
        }

        int donationId = (int) model.getValueAt(row, 0);
        if (donationDAO.updateStatus(donationId, newStatus)) {
            JOptionPane.showMessageDialog(this, "Status updated to " + newStatus);
            loadDonations();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update status.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DonationDashboard().setVisible(true));
    }
}
