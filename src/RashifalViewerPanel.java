import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.sql.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;

public class RashifalViewerPanel extends JPanel {
    private BufferedImage bgImage;
    private Connection conn;

    private static final String[] ENGLISH_RASHIS = {
        "Aries", "Taurus", "Gemini", "Cancer", "Leo", "Virgo",
        "Libra", "Scorpio", "Sagittarius", "Capricorn", "Aquarius", "Pisces"
    };
    private static final String[] DB_RASHIS = {
        "Mesh", "Vrish", "Mithun", "Kark", "Singh", "Kanya",
        "Tula", "Vrischik", "Dhanu", "Makar", "Kumbh", "Meen"
    };

    public RashifalViewerPanel(Connection conn) {
        this.conn = conn;
        setLayout(new BorderLayout());
        setOpaque(false);

        try {
            bgImage = ImageIO.read(new File("C:\\Users\\rupak43\\Downloads\\NepaliCalendar2082\\AstroNepal\\images\\bgtwo.jpg"));
        } catch (IOException e) {
            bgImage = null;
        }

        JPanel gridPanel = new JPanel(new GridLayout(3, 4, 24, 24));
        gridPanel.setOpaque(false);
        gridPanel.setBorder(new EmptyBorder(24, 24, 24, 24));

        for (int i = 0; i < ENGLISH_RASHIS.length; i++) {
            gridPanel.add(createRashiCard(ENGLISH_RASHIS[i], DB_RASHIS[i]));
        }

        JLabel title = new JLabel("Today's Rashifal", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        title.setBorder(new EmptyBorder(18, 0, 10, 0));

        add(title, BorderLayout.NORTH);
        add(gridPanel, BorderLayout.CENTER);
    }

    private JPanel createRashiCard(String englishName, String dbName) {
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

        
        JTextArea rashifalText = new JTextArea(loadRashifal(dbName));
        rashifalText.setLineWrap(true);
        rashifalText.setWrapStyleWord(true);
        rashifalText.setEditable(false);
        rashifalText.setOpaque(false);
        rashifalText.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        rashifalText.setForeground(new Color(0x222222));
        rashifalText.setBorder(null);

        card.add(rashifalText, BorderLayout.CENTER);

        return card;
    }

    private String loadRashifal(String rashiName) {
        if (conn == null) return "Database connection unavailable.";
        java.sql.Date today = new java.sql.Date(System.currentTimeMillis());
        String sql = "SELECT content FROM rashifal WHERE rashi_name = ? AND date = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, rashiName);
            ps.setDate(2, today);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString("content");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return "Could not load rashifal: " + ex.getMessage();
        }
        return "No rashifal available for today.";
    }

    static class RoundedBorder extends AbstractBorder {
        private int radius;
        RoundedBorder(int radius) { this.radius = radius; }
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(0x90CAF9));
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
        JFrame frame = new JFrame("Rashifal Viewer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 700);
        frame.setContentPane(new RashifalViewerPanel(conn));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
