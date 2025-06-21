import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import javax.imageio.ImageIO;
import javax.swing.*;

public class AdminDash extends JFrame {

    public AdminDash() {
        setTitle("Astro Nepal - Admin Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        JPanel bgPanel = new JPanel() {
            BufferedImage bgImg;
            {
                try {
                    bgImg = ImageIO.read(new File("C:\\Users\\rupak43\\Downloads\\NepaliCalendar2082\\AstroNepal\\images\\admindash.png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                setLayout(null);
            }
            @Override
            protected void 
            paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bgImg != null) {
                    g.drawImage(bgImg, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        setContentPane(bgPanel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new GridLayout(4, 1, 0, 20));

        JButton rashifalBtn = new JButton("Edit Rashifal") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(10, 20, 60), getWidth(), getHeight(), new Color(30, 40, 90));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
                for (int i = 0; i < 15; i++) {
                    int x = (int) (Math.random() * getWidth());
                    int y = (int) (Math.random() * getHeight());
                    g2.setColor(new Color(255, 255, 255, 180));
                    g2.fillOval(x, y, 3, 3);
                }
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        JButton newsBtn = new JButton("Daily news post") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(20, 30, 70), getWidth(), getHeight(), new Color(60, 60, 120));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
                for (int i = 0; i < 10; i++) {
                    int x = (int) (Math.random() * getWidth());
                    int y = (int) (Math.random() * getHeight());
                    g2.setColor(new Color(255, 255, 200, 180));
                    g2.fillOval(x, y, 2, 2);
                }
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        JButton calendarBtn = new JButton("Calendar") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(30, 30, 80), getWidth(), getHeight(), new Color(80, 80, 160));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
                g2.setColor(new Color(220, 220, 255, 120));
                g2.fillOval(getWidth() - 40, 10, 30, 30);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        JButton logoutBtn = new JButton("Logout") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(60, 0, 30), getWidth(), getHeight(), new Color(120, 30, 60));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
                g2.setColor(new Color(255, 255, 180, 180));
                g2.drawLine(20, getHeight() - 10, getWidth() - 10, 10);
                super.paintComponent(g2);
                g2.dispose();
            }
        };

        Font btnFont = new Font("Segoe UI", Font.BOLD, 22);
        Color fgColor = new Color(255, 215, 100);

        JButton[] buttons = {rashifalBtn, newsBtn, calendarBtn, logoutBtn};
        for (JButton btn : buttons) {
            btn.setFont(btnFont);
            btn.setForeground(fgColor);
            btn.setFocusPainted(false);
            btn.setContentAreaFilled(false);
            btn.setOpaque(false);
            btn.setBorder(BorderFactory.createEmptyBorder(16, 40, 16, 40));
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        buttonPanel.add(rashifalBtn);
        buttonPanel.add(newsBtn);
        buttonPanel.add(calendarBtn);
        buttonPanel.add(logoutBtn);

        buttonPanel.setBounds(160, 260, 300, 260);

        bgPanel.add(buttonPanel);

        logoutBtn.addActionListener(e -> {
            dispose();
            new AstroNepalDashboard();
        });

        rashifalBtn.addActionListener(e -> {
            Connection conn = null;
            try {
                conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/AstroNepal", "root", "RUPAK431"
                );
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Database connection failed: " + ex.getMessage());
                return;
            }
            JFrame rashifalFrame = new JFrame("Edit Rashifal");
            rashifalFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            rashifalFrame.setSize(1200, 700);
            rashifalFrame.setLocationRelativeTo(this);
            rashifalFrame.setContentPane(new RashifalPanel(conn));
            rashifalFrame.setVisible(true);
        });

        calendarBtn.addActionListener(e -> {
            JFrame calFrame = new JFrame("Nepali Calendar");
            calFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            calFrame.setSize(1000, 700);
            calFrame.setLocationRelativeTo(this);
            calFrame.setContentPane(new calender());
            calFrame.setVisible(true);
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AdminDash::new);
    }
}
