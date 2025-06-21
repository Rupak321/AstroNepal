import java.awt.*;
import java.sql.Connection;
import javax.swing.*;

public class AstroNepalDashboard extends JFrame {

    public AstroNepalDashboard() {
        setTitle("Astro Nepal Dashboard");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        ImageIcon bgIcon = new ImageIcon("C:\\Users\\rupak43\\Downloads\\NepaliCalendar2082\\AstroNepal\\images\\realbg.jpeg");
        JLabel background = new JLabel(bgIcon);
        background.setBounds(0, 0, bgIcon.getIconWidth(), bgIcon.getIconHeight());
        background.setLayout(null);

        int bgWidth = bgIcon.getIconWidth();
        int bgHeight = bgIcon.getIconHeight();

        int btnWidth = 180;
        int btnHeight = 45;
        int gap = 40;

        int centerX = bgWidth / 2;
        int logoBottomY = 630;

        int totalWidth = btnWidth * 3 + gap * 2;
        int startX = centerX - totalWidth / 2 + 70;
        int y = logoBottomY;

        Color gold1 = new Color(255, 215, 0);
        Color gold2 = new Color(255, 223, 80);
        Color goldBorder = new Color(212, 175, 55);

        class GoldButton extends JButton {
            public GoldButton(String text) {
                super(text);
                setFont(new Font("Segoe UI", Font.BOLD, 18));
                setForeground(new Color(40, 30, 10));
                setFocusPainted(false);
                setContentAreaFilled(false);
                setOpaque(false);
                setBorder(BorderFactory.createLineBorder(goldBorder, 2, true));
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, gold1, getWidth(), getHeight(), gold2);
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 32, 32);
                if (getModel().isRollover()) {
                    g2.setColor(new Color(255, 255, 200, 120));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 32, 32);
                }
                super.paintComponent(g2);
                g2.dispose();
            }
        }

        GoldButton rashifalBtn = new GoldButton("Rashifal");
        rashifalBtn.setBounds(startX, y, btnWidth, btnHeight);

        GoldButton patroBtn = new GoldButton("Patro / Calendar");
        patroBtn.setBounds(startX + btnWidth + gap, y, btnWidth, btnHeight);

        GoldButton newsBtn = new GoldButton("Daily News");
        newsBtn.setBounds(startX + (btnWidth + gap) * 2, y, btnWidth, btnHeight);

        int loginAdminBtnWidth = 200;
        int loginAdminBtnHeight = 48;
        int loginAdminY = y + btnHeight + 40;
        int loginAdminX = patroBtn.getX() + (btnWidth - loginAdminBtnWidth) / 2;

        GoldButton loginAdminBtn = new GoldButton("Login Admin");
        loginAdminBtn.setBounds(loginAdminX, loginAdminY, loginAdminBtnWidth, loginAdminBtnHeight);

        loginAdminBtn.addActionListener(e -> {
            dispose();
            new AstroNepalAdminLogin();
        });

        patroBtn.addActionListener(e -> {
            JFrame calFrame = new JFrame("Calendar");
            calFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            calFrame.setSize(1000, 700);
            calFrame.setLocationRelativeTo(null);
            calFrame.setContentPane(new calender());
            calFrame.setVisible(true);
        });

        rashifalBtn.addActionListener(e -> {
            JFrame viewerFrame = new JFrame("Rashifal Viewer");
            viewerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            viewerFrame.setSize(1200, 700);
            viewerFrame.setLocationRelativeTo(null);
            Connection conn = null;
            try {
                conn = java.sql.DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/AstroNepal", "root", "RUPAK431"
                );
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Database connection failed: " + ex.getMessage());
                return;
            }
            viewerFrame.setContentPane(new RashifalViewerPanel(conn));
            viewerFrame.setVisible(true);
        });

        newsBtn.addActionListener(e -> {
            JFrame newsFrame = new JFrame("Daily News");
            newsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            newsFrame.setSize(1000, 700);
            newsFrame.setLocationRelativeTo(null);
           
            newsFrame.setVisible(true);
        });

        int adminBtnWidth = 140;
        int adminBtnHeight = 38;
        int margin = 40;

        GoldButton adminBtn = new GoldButton("Admin Login") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(30, 30, 30, 220));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 28, 28);
                g2.setColor(goldBorder);
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 28, 28);
                if (getModel().isRollover()) {
                    g2.setColor(new Color(255, 215, 0, 80));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 28, 28);
                }
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        adminBtn.setForeground(gold1);
        adminBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        adminBtn.setBounds(bgWidth - adminBtnWidth - margin, bgHeight - adminBtnHeight - margin, adminBtnWidth, adminBtnHeight);

        background.add(rashifalBtn);
        background.add(patroBtn);
        background.add(newsBtn);
        background.add(loginAdminBtn);
        background.add(adminBtn);

        setContentPane(background);
        setVisible(true);
    }

    public static void main(String[] args) {
        new AstroNepalDashboard();
    }
}
