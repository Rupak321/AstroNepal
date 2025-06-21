import java.awt.*;
import javax.swing.*;

public class calender extends JFrame {
    String[] nepaliMonths = {
        "Baisakh", "Jestha", "Ashar", "Shrawan", "Bhadra", "Ashwin",
        "Kartik", "Mangsir", "Poush", "Magh", "Falgun", "Chaitra"
    }; 

    int[] daysInMonth = {
        31, 32, 31, 32, 31, 30, 30, 29, 29, 30, 29, 30
    };

    String[] nepaliWeekDays = {"Aait", "Som", "Mangal", "Budh", "Bihib", "Shukra", "Shani"};

    JPanel calendarPanel;
    JComboBox<String> monthBox;
    Font calendarFont;

    public calender() {
        setTitle("Nepali Calendar 2082");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        calendarFont = new Font("Segoe UI", Font.PLAIN, 18);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(245, 236, 225));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setOpaque(false);

        JButton backBtn = new JButton("Back");
        backBtn.setFont(calendarFont.deriveFont(Font.BOLD, 16f));
        backBtn.setFocusPainted(false);
        backBtn.setBackground(new Color(205, 133, 63));
        backBtn.setForeground(Color.WHITE);
        backBtn.setBorder(BorderFactory.createEmptyBorder(8, 24, 8, 24));
        backBtn.addActionListener(e -> {
            dispose();
            new AstroNepalDashboard();
        });
        leftPanel.add(backBtn);

        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerPanel.setOpaque(false);

        JLabel monthLabel = new JLabel("Select Month: ");
        monthLabel.setFont(calendarFont.deriveFont(Font.BOLD, 20f));
        monthBox = new JComboBox<>(nepaliMonths);
        monthBox.setFont(calendarFont);
        monthBox.addActionListener(e -> updateCalendar(monthBox.getSelectedIndex()));
        centerPanel.add(monthLabel);
        centerPanel.add(monthBox);

        topPanel.add(leftPanel, BorderLayout.WEST);
        topPanel.add(centerPanel, BorderLayout.CENTER);

        calendarPanel = new JPanel();
        calendarPanel.setLayout(new GridLayout(0, 7, 12, 12));
        calendarPanel.setBackground(new Color(245, 236, 225));
        calendarPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(topPanel, BorderLayout.NORTH);
        add(calendarPanel, BorderLayout.CENTER);

        updateCalendar(0);
        setVisible(true);
    }

    void updateCalendar(int monthIndex) {
        calendarPanel.removeAll();

        Color brown = new Color(139, 69, 19);
        Color lightBrown = new Color(205, 133, 63);
        Color white = Color.WHITE;
        Color headerGradientStart = new Color(180, 120, 60);
        Color headerGradientEnd = new Color(139, 69, 19);

        for (int i = 0; i < nepaliWeekDays.length; i++) {
            final int idx = i;
            JLabel lbl = new JLabel(nepaliWeekDays[idx], SwingConstants.CENTER) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    GradientPaint gp;
                    if (idx == 6) {
                        gp = new GradientPaint(0, 0, lightBrown, getWidth(), getHeight(), white);
                    } else {
                        gp = new GradientPaint(0, 0, headerGradientStart, getWidth(), getHeight(), headerGradientEnd);
                    }
                    g2.setPaint(gp);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                    super.paintComponent(g);
                }
            };
            lbl.setFont(calendarFont.deriveFont(Font.BOLD, 18f));
            lbl.setOpaque(false);
            lbl.setForeground(idx == 6 ? brown : white);
            lbl.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
            lbl.setPreferredSize(new Dimension(80, 40));
            lbl.setHorizontalAlignment(SwingConstants.CENTER);
            calendarPanel.add(lbl);
        }

        int days = daysInMonth[monthIndex];
        int firstDayOfWeek = 0;

        for (int i = 0; i < firstDayOfWeek; i++) {
            JPanel emptyPanel = new JPanel();
            emptyPanel.setOpaque(false);
            calendarPanel.add(emptyPanel);
        }

        for (int i = 1; i <= days; i++) {
            int dayOfWeek = (firstDayOfWeek + i - 1) % 7;
            JLabel dayLabel = new JLabel(String.valueOf(i), SwingConstants.CENTER) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    if (dayOfWeek == 6) {
                        g2.setColor(new Color(255, 239, 213));
                    } else {
                        g2.setColor(white);
                    }
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                    g2.setColor(new Color(0, 0, 0, 30));
                    g2.fillRoundRect(4, getHeight() - 8, getWidth() - 8, 8, 8, 8);
                    super.paintComponent(g);
                }
            };
            if (dayOfWeek == 6) {
                dayLabel.setFont(calendarFont.deriveFont(Font.BOLD, 18f));
                dayLabel.setForeground(Color.RED);
                dayLabel.setToolTipText("Holiday");
            } else {
                dayLabel.setFont(calendarFont.deriveFont(17f));
                dayLabel.setForeground(brown);
            }
            dayLabel.setOpaque(false);
            dayLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
            dayLabel.setPreferredSize(new Dimension(80, 50));
            dayLabel.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    dayLabel.setForeground(new Color(255, 87, 34));
                    dayLabel.setFont(dayLabel.getFont().deriveFont(Font.BOLD, 20f));
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    if (dayOfWeek == 6) {
                        dayLabel.setForeground(Color.RED);
                        dayLabel.setFont(calendarFont.deriveFont(Font.BOLD, 18f));
                    } else {
                        dayLabel.setForeground(brown);
                        dayLabel.setFont(calendarFont.deriveFont(17f));
                    }
                }
            });
            calendarPanel.add(dayLabel);
        }

        calendarPanel.revalidate();
        calendarPanel.repaint();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
        SwingUtilities.invokeLater(calender::new);
    }
}
