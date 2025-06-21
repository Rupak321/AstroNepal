import java.awt.*;
import java.sql.ResultSet;
import javax.swing.*;

public class AstroNepalAdminLogin extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel messageLabel;

    public AstroNepalAdminLogin() {
        setTitle("Astro Nepal - Admin Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        JLabel bg = new JLabel(new ImageIcon("C:\\Users\\rupak43\\Downloads\\NepaliCalendar2082\\AstroNepal\\images\\realbg.jpeg"));
        bg.setBounds(0, 0, getWidth(), getHeight());
        setContentPane(bg);
        bg.setLayout(null);

        int panelWidth = 440;
        int panelHeight = 300;
        int x = (Toolkit.getDefaultToolkit().getScreenSize().width - panelWidth) / 2 - 10;
        int y = (Toolkit.getDefaultToolkit().getScreenSize().height - panelHeight) / 2 + 70;

        JPanel formPanel = new JPanel();
        formPanel.setOpaque(false);
        formPanel.setLayout(null);
        formPanel.setBounds(x, y, panelWidth, panelHeight);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        userLabel.setForeground(Color.WHITE);
        userLabel.setBounds(10, 10, 120, 35);
        formPanel.add(userLabel);

        usernameField = new JTextField();
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        usernameField.setBounds(140, 10, 280, 35);
        formPanel.add(usernameField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        passLabel.setForeground(Color.WHITE);
        passLabel.setBounds(10, 60, 120, 35);
        formPanel.add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        passwordField.setBounds(140, 60, 280, 35);
        formPanel.add(passwordField);

        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        messageLabel.setForeground(Color.RED);
        messageLabel.setBounds(10, 105, 420, 30);
        formPanel.add(messageLabel);

        JPanel buttonPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                setOpaque(false);
                super.paintComponent(g);
            }
        };
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 40, 0));
        buttonPanel.setBounds(0, 150, 440, 60);

        JButton loginBtn = new JButton("Login") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 102, 204));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 20));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        loginBtn.setContentAreaFilled(false);
        loginBtn.setBorder(BorderFactory.createEmptyBorder(12, 40, 12, 40));
        loginBtn.addActionListener(e -> handleLogin());

        JButton backBtn = new JButton("Back") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(220, 220, 220, 220));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 20));
        backBtn.setForeground(new Color(80, 80, 80));
        backBtn.setFocusPainted(false);
        backBtn.setContentAreaFilled(false);
        backBtn.setBorder(BorderFactory.createEmptyBorder(12, 40, 12, 40));
        backBtn.addActionListener(e -> {
            UIManager.put("OptionPane.background", new Color(30, 30, 30));
            UIManager.put("Panel.background", new Color(30, 30, 30));
            UIManager.put("OptionPane.messageForeground", new Color(212, 175, 55));
            UIManager.put("OptionPane.messageFont", new Font("Segoe UI", Font.BOLD, 18));
            JOptionPane.showMessageDialog(this, "Going back to dashboard...");
            new AstroNepalDashboard();
            dispose();
        });

        buttonPanel.add(loginBtn);
        buttonPanel.add(backBtn);
        formPanel.add(buttonPanel);

        bg.add(formPanel);

        setVisible(true);
    }

    private void handleLogin() {
        String user = usernameField.getText();
        String pass = new String(passwordField.getPassword());

        try {
            conn c = new conn();
            String query = "SELECT * FROM login WHERE username='" + user + "' AND password='" + pass + "'";
            ResultSet rs = c.statement.executeQuery(query);

            if (rs.next()) {
                messageLabel.setText("");
                JOptionPane.showMessageDialog(this, "Login Successful!", "Welcome", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                new AdminDash();
            } else {
                messageLabel.setText("Invalid credentials. Try again.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            messageLabel.setText("Database error. Please try again.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AstroNepalAdminLogin::new);
    }
}
