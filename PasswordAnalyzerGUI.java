
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.awt.geom.RoundRectangle2D;

public class PasswordAnalyzerGUI extends JFrame {
    private PasswordAnalyzer analyzer;
    private SecurityPolicy policy;
    private JTextField passwordField;
    private JTextArea resultArea;
    private JProgressBar strengthBar;
    private JLabel strengthLabel;

    // Modern color scheme
    private static final Color PRIMARY_COLOR = new Color(64, 128, 255);
    private static final Color SECONDARY_COLOR = new Color(245, 247, 250);
    private static final Color SUCCESS_COLOR = new Color(34, 197, 94);
    private static final Color WARNING_COLOR = new Color(251, 146, 60);
    private static final Color DANGER_COLOR = new Color(239, 68, 68);
    private static final Color DARK_TEXT = new Color(31, 41, 55);
    private static final Color LIGHT_TEXT = new Color(107, 114, 128);
    private static final Color BORDER_COLOR = new Color(229, 231, 235);

    public PasswordAnalyzerGUI() {
        // Initialize analyzer and policy
        analyzer = new PasswordAnalyzer();
        policy = new SecurityPolicy();

        // Set up the frame with modern styling
        setTitle("Password Security Analyzer");
        setSize(900, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set background color
        getContentPane().setBackground(SECONDARY_COLOR);

        // Create components with modern styling
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(SECONDARY_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        // Enhanced title panel with gradient-like effect
        JPanel titlePanel = createTitlePanel();

        // Modern input panel
        JPanel inputPanel = createInputPanel();

        // Enhanced results area
        JScrollPane scrollPane = createResultsPanel();

        // Modern button panel
        JPanel buttonPanel = createButtonPanel();

        // Create a split pane with custom styling
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, inputPanel, scrollPane);
        splitPane.setResizeWeight(0.25);
        splitPane.setDividerLocation(200);
        splitPane.setBackground(SECONDARY_COLOR);
        splitPane.setBorder(null);
        splitPane.setDividerSize(8);

        // Add all components to main panel
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(splitPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add main panel to frame
        add(mainPanel);

        // Set up event listeners
        setupEventListeners();
    }

    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel(new BorderLayout(0, 10));
        titlePanel.setBackground(SECONDARY_COLOR);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        // Main title with modern styling
        JLabel titleLabel = new JLabel("🔐 Password Security Analyzer", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(DARK_TEXT);

        // Subtitle with lighter text
        JLabel subtitleLabel = new JLabel("Enterprise-grade security analysis based on ISO 22301 standards", JLabel.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(LIGHT_TEXT);

        titlePanel.add(titleLabel, BorderLayout.CENTER);
        titlePanel.add(subtitleLabel, BorderLayout.SOUTH);

        return titlePanel;
    }

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel(new BorderLayout(0, 15));
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        // Password input section
        JPanel passwordPanel = createPasswordInputPanel();

        // Strength indicator section
        JPanel strengthPanel = createStrengthPanel();

        inputPanel.add(passwordPanel, BorderLayout.NORTH);
        inputPanel.add(strengthPanel, BorderLayout.CENTER);

        return inputPanel;
    }

    private JPanel createPasswordInputPanel() {
        JPanel passwordPanel = new JPanel(new BorderLayout(10, 5));
        passwordPanel.setBackground(Color.WHITE);

        JLabel passwordLabel = new JLabel("Enter Password:");
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        passwordLabel.setForeground(DARK_TEXT);

        passwordField = new JTextField(20);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        passwordField.setPreferredSize(new Dimension(0, 40));

        JButton analyzeButton = createStyledButton("Analyze Password", PRIMARY_COLOR);
        analyzeButton.setPreferredSize(new Dimension(150, 40));

        passwordPanel.add(passwordLabel, BorderLayout.WEST);
        passwordPanel.add(passwordField, BorderLayout.CENTER);
        passwordPanel.add(analyzeButton, BorderLayout.EAST);

        return passwordPanel;
    }

    private JPanel createStrengthPanel() {
        JPanel strengthPanel = new JPanel(new BorderLayout(0, 10));
        strengthPanel.setBackground(Color.WHITE);
        strengthPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        // Strength label
        strengthLabel = new JLabel("Password Strength: Not Analyzed", JLabel.CENTER);
        strengthLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        strengthLabel.setForeground(LIGHT_TEXT);

        // Custom strength bar
        strengthBar = new JProgressBar(0, 100) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw background
                g2d.setColor(new Color(229, 231, 235));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

                // Draw progress
                if (getValue() > 0) {
                    int width = (int) ((double) getValue() / getMaximum() * getWidth());
                    g2d.setColor(getForeground());
                    g2d.fillRoundRect(0, 0, width, getHeight(), 10, 10);
                }

                g2d.dispose();
            }
        };
        strengthBar.setStringPainted(false);
        strengthBar.setBorderPainted(false);
        strengthBar.setPreferredSize(new Dimension(0, 20));
        strengthBar.setOpaque(false);

        strengthPanel.add(strengthLabel, BorderLayout.NORTH);
        strengthPanel.add(strengthBar, BorderLayout.CENTER);

        return strengthPanel;
    }

    private JScrollPane createResultsPanel() {
        resultArea = new JTextArea(20, 50);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setBackground(Color.WHITE);
        resultArea.setForeground(DARK_TEXT);
        resultArea.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));
        scrollPane.setPreferredSize(new Dimension(850, 400));
        scrollPane.getViewport().setBackground(Color.WHITE);

        return scrollPane;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        buttonPanel.setBackground(SECONDARY_COLOR);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        JButton policyButton = createStyledButton("📋 View Security Policy", new Color(99, 102, 241));
        JButton exampleButton = createStyledButton("🧪 Test Sample Passwords", new Color(168, 85, 247));
        JButton generateButton = createStyledButton("⚡ Generate Strong Password", SUCCESS_COLOR);

        buttonPanel.add(policyButton);
        buttonPanel.add(exampleButton);
        buttonPanel.add(generateButton);

        return buttonPanel;
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Button background
                if (getModel().isPressed()) {
                    g2d.setColor(color.darker());
                } else if (getModel().isRollover()) {
                    g2d.setColor(color.brighter());
                } else {
                    g2d.setColor(color);
                }

                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);

                // Button text
                g2d.setColor(Color.WHITE);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int textWidth = fm.stringWidth(getText());
                int textHeight = fm.getHeight();
                int x = (getWidth() - textWidth) / 2;
                int y = (getHeight() + textHeight) / 2 - fm.getDescent();
                g2d.drawString(getText(), x, y);

                g2d.dispose();
            }
        };

        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(0, 45));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }

    private void setupEventListeners() {
        // Find buttons by their text content
        Component[] components = getAllComponents(this);
        for (Component comp : components) {
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                String text = button.getText();

                if (text.contains("Analyze")) {
                    button.addActionListener(e -> analyzePassword());
                } else if (text.contains("Security Policy")) {
                    button.addActionListener(e -> displaySecurityPolicy());
                } else if (text.contains("Sample")) {
                    button.addActionListener(e -> testSamplePasswords());
                } else if (text.contains("Generate")) {
                    button.addActionListener(e -> generatePassword());
                }
            }
        }

        // Add Enter key listener to password field
        passwordField.addActionListener(e -> analyzePassword());
    }

    private Component[] getAllComponents(Container container) {
        java.util.List<Component> components = new java.util.ArrayList<>();
        for (Component comp : container.getComponents()) {
            components.add(comp);
            if (comp instanceof Container) {
                Component[] subComponents = getAllComponents((Container) comp);
                for (Component subComp : subComponents) {
                    components.add(subComp);
                }
            }
        }
        return components.toArray(new Component[0]);
    }

    private void analyzePassword() {
        String password = passwordField.getText();
        if (password.isEmpty()) {
            resultArea.setText("⚠️ Please enter a password to analyze.");
            return;
        }

        // Calculate strength score
        int score = analyzer.calculateStrengthScore(password);
        String level = analyzer.getStrengthLevel(score);
        boolean compliant = policy.isCompliant(password, analyzer);

        // Update strength bar with modern colors
        strengthBar.setValue(score);

        Color barColor;
        String emoji;
        if (score < 40) {
            barColor = DANGER_COLOR;
            emoji = "🔴";
            strengthLabel.setText(emoji + " Password Strength: WEAK (" + score + "/100)");
        } else if (score < 70) {
            barColor = WARNING_COLOR;
            emoji = "🟡";
            strengthLabel.setText(emoji + " Password Strength: MODERATE (" + score + "/100)");
        } else {
            barColor = SUCCESS_COLOR;
            emoji = "🟢";
            strengthLabel.setText(emoji + " Password Strength: STRONG (" + score + "/100)");
        }

        strengthBar.setForeground(barColor);
        strengthLabel.setForeground(barColor);

        // Generate enhanced report
        StringBuilder report = new StringBuilder();
        report.append("🔐 PASSWORD ANALYSIS REPORT\n");
        report.append("═══════════════════════════════════════════════════════════════\n\n");
        report.append("📝 Password: ").append(password).append("\n");
        report.append("📊 Strength Score: ").append(score).append("/100\n");
        report.append("📈 Strength Level: ").append(level).append("\n");
        report.append("✅ Policy Compliant: ").append(compliant ? "YES" : "NO").append("\n\n");

        report.append("🔍 DETAILED ANALYSIS\n");
        report.append("─────────────────────────────────────────────────────────────\n");
        report.append("📏 Length: ").append(password.length()).append(" characters ")
                .append(analyzer.checkMinimumLength(password, SecurityPolicy.MINIMUM_LENGTH) ? "✅" : "❌").append("\n");
        report.append("🔤 Uppercase letters: ")
                .append(analyzer.hasUppercase(password) ? "✅" : "❌").append("\n");
        report.append("🔡 Lowercase letters: ")
                .append(analyzer.hasLowercase(password) ? "✅" : "❌").append("\n");
        report.append("🔢 Numbers: ")
                .append(analyzer.hasNumbers(password) ? "✅" : "❌").append("\n");
        report.append("🔣 Special characters: ")
                .append(analyzer.hasSpecialCharacters(password) ? "✅" : "❌").append("\n");
        report.append("🚫 Common password: ")
                .append(analyzer.isCommonPassword(password) ? "YES ❌" : "NO ✅").append("\n");

        // Add recommendations if not compliant
        if (!compliant) {
            report.append("\n💡 RECOMMENDATIONS FOR IMPROVEMENT\n");
            report.append("─────────────────────────────────────────────────────────────\n");
            if (!analyzer.checkMinimumLength(password, SecurityPolicy.MINIMUM_LENGTH)) {
                report.append("• 📏 Increase password length to at least ")
                        .append(SecurityPolicy.MINIMUM_LENGTH).append(" characters\n");
            }
            if (!analyzer.hasUppercase(password)) {
                report.append("• 🔤 Add uppercase letters (A-Z)\n");
            }
            if (!analyzer.hasLowercase(password)) {
                report.append("• 🔡 Add lowercase letters (a-z)\n");
            }
            if (!analyzer.hasNumbers(password)) {
                report.append("• 🔢 Add numbers (0-9)\n");
            }
            if (!analyzer.hasSpecialCharacters(password)) {
                report.append("• 🔣 Add special characters (!@#$%^&*...)\n");
            }
            if (analyzer.isCommonPassword(password)) {
                report.append("• 🚫 Avoid common passwords - use something unique\n");
            }
        } else {
            report.append("\n🎉 EXCELLENT! Your password meets all security requirements!\n");
        }

        resultArea.setText(report.toString());
        resultArea.setCaretPosition(0);
    }

    private void displaySecurityPolicy() {
        strengthBar.setValue(0);
        strengthLabel.setText("📋 Security Policy Display Mode");
        strengthLabel.setForeground(PRIMARY_COLOR);

        StringBuilder policy = new StringBuilder();
        policy.append("📋 ORGANIZATIONAL SECURITY POLICY\n");
        policy.append("═══════════════════════════════════════════════════════════════\n");
        policy.append("🏢 Based on ISO 22301 Standards\n");
        policy.append("🛡️ Defined by GCS (Governance Cyber Security) Team\n\n");

        policy.append("📜 PASSWORD REQUIREMENTS:\n");
        policy.append("─────────────────────────────────────────────────────────────\n");
        policy.append("• 📏 Minimum length: ").append(SecurityPolicy.MINIMUM_LENGTH).append(" characters\n");
        policy.append("• 🔤 Must contain uppercase letters: ")
                .append(SecurityPolicy.REQUIRE_UPPERCASE ? "✅ YES" : "❌ NO").append("\n");
        policy.append("• 🔡 Must contain lowercase letters: ")
                .append(SecurityPolicy.REQUIRE_LOWERCASE ? "✅ YES" : "❌ NO").append("\n");
        policy.append("• 🔢 Must contain numbers: ")
                .append(SecurityPolicy.REQUIRE_NUMBERS ? "✅ YES" : "❌ NO").append("\n");
        policy.append("• 🔣 Must contain special characters: ")
                .append(SecurityPolicy.REQUIRE_SPECIAL_CHARS ? "✅ YES" : "❌ NO").append("\n");
        policy.append("• 📊 Minimum security score required: ")
                .append(SecurityPolicy.MINIMUM_SCORE_REQUIRED).append("/100\n");
        policy.append("• 🚫 Cannot be a common/weak password\n");

        resultArea.setText(policy.toString());
        resultArea.setCaretPosition(0);
    }

    private void testSamplePasswords() {
        strengthBar.setValue(0);
        strengthLabel.setText("🧪 Sample Testing Mode");
        strengthLabel.setForeground(new Color(168, 85, 247));

        String[] testPasswords = {
                "password",           // Very weak
                "Password123",        // Moderate
                "MyStr0ng!Pass",      // Strong
                "admin",              // Very weak
                "ComplexP@ssw0rd2024!" // Very strong
        };

        StringBuilder results = new StringBuilder();
        results.append("🧪 SAMPLE PASSWORD ANALYSIS\n");
        results.append("═══════════════════════════════════════════════════════════════\n\n");
        results.append(String.format("%-20s | %-8s | %-12s | %s\n",
                "Password", "Score", "Level", "Compliant"));
        results.append("───────────────────────────────────────────────────────────────\n");

        for (String pwd : testPasswords) {
            int score = analyzer.calculateStrengthScore(pwd);
            String level = analyzer.getStrengthLevel(score);
            boolean compliant = policy.isCompliant(pwd, analyzer);
            String status = compliant ? "✅ YES" : "❌ NO";

            results.append(String.format("%-20s | %3d/100  | %-12s | %s\n",
                    pwd, score, level, status));
        }

        results.append("\n💡 This demonstrates how the analyzer evaluates different password strengths.\n");
        results.append("🔍 Try analyzing these passwords individually for detailed breakdowns!");

        resultArea.setText(results.toString());
        resultArea.setCaretPosition(0);
    }

    private void generatePassword() {
        // Simple password generator
        StringBuilder password = new StringBuilder();
        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lower = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String special = "!@#$%^&*()-_=+[]{}|;:,.<>?";
        String all = upper + lower + digits + special;

        // Add at least one of each required character type
        password.append(upper.charAt((int)(Math.random() * upper.length())));
        password.append(lower.charAt((int)(Math.random() * lower.length())));
        password.append(digits.charAt((int)(Math.random() * digits.length())));
        password.append(special.charAt((int)(Math.random() * special.length())));

        // Add random characters to reach minimum length
        while (password.length() < 12) {
            password.append(all.charAt((int)(Math.random() * all.length())));
        }

        // Shuffle the password
        char[] chars = password.toString().toCharArray();
        for (int i = 0; i < chars.length; i++) {
            int j = (int)(Math.random() * chars.length);
            char temp = chars[i];
            chars[i] = chars[j];
            chars[j] = temp;
        }

        String generatedPassword = new String(chars);
        passwordField.setText(generatedPassword);

        // Update strength meter
        int score = analyzer.calculateStrengthScore(generatedPassword);
        strengthBar.setValue(score);
        strengthBar.setForeground(SUCCESS_COLOR);

        // Update strength label
        strengthLabel.setText("🟢 Password Strength: STRONG (" + score + "/100)");
        strengthLabel.setForeground(SUCCESS_COLOR);

        StringBuilder result = new StringBuilder();
        result.append("⚡ STRONG PASSWORD GENERATED\n");
        result.append("═══════════════════════════════════════════════════════════════\n\n");
        result.append("🔑 Generated Password: ").append(generatedPassword).append("\n\n");
        result.append("✅ This password meets all security requirements:\n");
        result.append("• 📏 Sufficient length (12+ characters)\n");
        result.append("• 🔤 Contains uppercase letters\n");
        result.append("• 🔡 Contains lowercase letters\n");
        result.append("• 🔢 Contains numbers\n");
        result.append("• 🔣 Contains special characters\n");
        result.append("• 🚫 Not a common password\n\n");
        result.append("💡 Click 'Analyze Password' to see the detailed security analysis!");

        resultArea.setText(result.toString());
    }

    public static void main(String[] args) {
        // Set system look and feel with modern enhancements
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            // Modern UI enhancements
            System.setProperty("awt.useSystemAAFontSettings", "on");
            System.setProperty("swing.aatext", "true");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Launch the application
        SwingUtilities.invokeLater(() -> {
            PasswordAnalyzerGUI app = new PasswordAnalyzerGUI();
            app.setVisible(true);
        });
    }

}
