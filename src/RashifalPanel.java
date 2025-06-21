import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;

public class RashifalPanel extends JPanel {
    private BufferedImage bgImage;
    private Connection conn;
    private JTextArea[] rashifalAreas = new JTextArea[12];

    private static final String[] ENGLISH_RASHIS = {
        "Aries", "Taurus", "Gemini", "Cancer", "Leo", "Virgo",
        "Libra", "Scorpio", "Sagittarius", "Capricorn", "Aquarius", "Pisces"
    };
    private static final String[] DB_RASHIS = {
        "Mesh", "Vrish", "Mithun", "Kark", "Singh", "Kanya",
        "Tula", "Vrischik", "Dhanu", "Makar", "Kumbh", "Meen"
    };

    public RashifalPanel(Connection conn) {
        this.conn = conn;
        setLayout(new BorderLayout());
        setOpaque(false);

        // Load background image
        try {
            bgImage = ImageIO.read(new File("C:\\Users\\rupak43\\Downloads\\NepaliCalendar2082\\AstroNepal\\images\\bgtwo.jpg"));
        } catch (IOException e) {
            bgImage = null;
        }

        JPanel gridPanel = new JPanel(new GridLayout(3, 4, 24, 24));
        gridPanel.setOpaque(false);
        gridPanel.setBorder(new EmptyBorder(24, 24, 24, 24));

        for (int i = 0; i < ENGLISH_RASHIS.length; i++) {
            gridPanel.add(createRashiEditCard(i, ENGLISH_RASHIS[i], DB_RASHIS[i]));
        }

        JLabel title = new JLabel("✦ Edit Daily Rashifal", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        title.setBorder(new EmptyBorder(18, 0, 10, 0));

        JButton saveAllButton = new JButton("Save All");
        saveAllButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        saveAllButton.setForeground(Color.WHITE);
        saveAllButton.setBackground(new Color(0x0099FF));
        saveAllButton.setFocusPainted(false);
        saveAllButton.setBorder(new EmptyBorder(10, 32, 10, 32));
        saveAllButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        saveAllButton.addActionListener(e -> saveAllRashifals());

        JButton backButton = new JButton("Back to Dashboard");
        backButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(new Color(0x1976D2));
        backButton.setFocusPainted(false);
        backButton.setBorder(new EmptyBorder(10, 32, 10, 32));
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> {

            SwingUtilities.getWindowAncestor(this).dispose();
           
            new AdminDash().setVisible(true);
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.add(saveAllButton);
        bottomPanel.add(Box.createHorizontalStrut(16)); // spacing between buttons
        bottomPanel.add(backButton);

        add(title, BorderLayout.NORTH);
        add(gridPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        // Load all rashifals initially
        loadAllRashifals();
    }

    private JPanel createRashiEditCard(int idx, String englishName, String dbName) {
        JPanel card = new JPanel(new BorderLayout(10, 10)) {
            protected void paintComponent(Graphics g) {
                g.setColor(new Color(255,255,255,220));
                g.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                super.paintComponent(g);
            }
        };
        card.setOpaque(false);
        card.setBorder(new CompoundBorder(
            new EmptyBorder(16, 16, 16, 16),
            new RoundedBorder(16)
        ));

        JLabel nameLabel = new JLabel(englishName, JLabel.LEFT);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        nameLabel.setForeground(new Color(0x1976D2));
        card.add(nameLabel, BorderLayout.NORTH);

        JTextArea rashifalArea = new JTextArea();
        rashifalArea.setLineWrap(true);
        rashifalArea.setWrapStyleWord(true);
        rashifalArea.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        rashifalArea.setForeground(new Color(0x222222));
        rashifalArea.setOpaque(false);
        rashifalArea.setBorder(null);

        rashifalAreas[idx] = rashifalArea;

        JScrollPane scrollPane = new JScrollPane(rashifalArea);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        card.add(scrollPane, BorderLayout.CENTER);

        return card;
    }

    private void loadAllRashifals() {
        if (conn == null) return;
        java.sql.Date today = new java.sql.Date(System.currentTimeMillis());
        for (int i = 0; i < DB_RASHIS.length; i++) {
            String sql = "SELECT content FROM rashifal WHERE rashi_name = ? AND date = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, DB_RASHIS[i]);
                ps.setDate(2, today);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        rashifalAreas[i].setText(rs.getString("content"));
                    } else {
                        rashifalAreas[i].setText("");
                    }
                }
            } catch (SQLException ex) {
                rashifalAreas[i].setText("Could not load: " + ex.getMessage());
            }
        }
    }

    private void saveAllRashifals() {
        if (conn == null) return;
        java.sql.Date today = new java.sql.Date(System.currentTimeMillis());
        for (int i = 0; i < DB_RASHIS.length; i++) {
            String content = rashifalAreas[i].getText().trim();
            if (content.isEmpty()) continue;
            try {
                // Check if entry exists
                String checkSql = "SELECT id FROM rashifal WHERE rashi_name = ? AND date = ?";
                PreparedStatement checkStmt = conn.prepareStatement(checkSql);
                checkStmt.setString(1, DB_RASHIS[i]);
                checkStmt.setDate(2, today);
                ResultSet rs = checkStmt.executeQuery();

                if (rs.next()) {
                    // Update
                    int id = rs.getInt("id");
                    String updateSql = "UPDATE rashifal SET content = ? WHERE id = ?";
                    PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                    updateStmt.setString(1, content);
                    updateStmt.setInt(2, id);
                    updateStmt.executeUpdate();
                    updateStmt.close();
                } else {
                    // Insert
                    String insertSql = "INSERT INTO rashifal (rashi_name, date, content) VALUES (?, ?, ?)";
                    PreparedStatement insertStmt = conn.prepareStatement(insertSql);
                    insertStmt.setString(1, DB_RASHIS[i]);
                    insertStmt.setDate(2, today);
                    insertStmt.setString(3, content);
                    insertStmt.executeUpdate();
                    insertStmt.close();
                }
                rs.close();
                checkStmt.close();
            } catch (SQLException ex) {
                rashifalAreas[i].setText("Save error: " + ex.getMessage());
            }
        }
        JOptionPane.showMessageDialog(this, "All rashifals saved/updated!");
    }

    static class RoundedBorder extends AbstractBorder {
        private int radius;
        RoundedBorder(int radius) { this.radius = radius; }
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(0x0099FF));
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(x+1, y+1, width-3, height-3, radius, radius);
        }
        public Insets getBorderInsets(Component c) { return new Insets(8, 12, 8, 12); }
        public Insets getBorderInsets(Component c, Insets insets) { return getBorderInsets(c); }
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (bgImage != null) {
            g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setColor(new Color(0,0,0,60));
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.dispose();
        } else {
            g.setColor(new Color(0x222244));
            g.fillRect(0, 0, getWidth(), getHeight());
        }
        super.paintComponent(g);
    }

    public static void main(String[] args) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/AstroNepal", "root", "RUPAK431");
        } catch (Exception e) {
            e.printStackTrace();
        }
        JFrame frame = new JFrame("Rashifal Editor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 700);
        frame.setContentPane(new RashifalPanel(conn));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
