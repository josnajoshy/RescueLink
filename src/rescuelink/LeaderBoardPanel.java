package rescuelink;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Comparator;
import java.util.List;

public class LeaderBoardPanel extends JFrame {
    private final JTable leaderboardTable;

    public LeaderBoardPanel() {
        setTitle("Volunteer Leaderboard");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        String[] columns = {"Name", "Location", "Skill", "Reward Points", "Attended Requests"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        leaderboardTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(leaderboardTable);

        add(scrollPane, BorderLayout.CENTER);
        loadLeaderboard(model);
    }

    private void loadLeaderboard(DefaultTableModel model) {
        VolunteerDAO dao = new VolunteerDAO();
        List<Volunteer> volunteers = dao.getVolunteerList();

        // Sort by reward points descending
        volunteers.sort(Comparator.comparingInt(Volunteer::getRewardPoints).reversed());

        for (Volunteer v : volunteers) {
            model.addRow(new Object[]{
                v.getName(),
                v.getLocation(),
                v.getSkill(),
                v.getRewardPoints(),
                v.getAttendedRequests()
            });
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LeaderBoardPanel().setVisible(true);
        });
    }
}
