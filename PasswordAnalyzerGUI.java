import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;

public class PasswordAnalyzerGUI extends JFrame {
    private PasswordAnalyzer analyzer;
    private SecurityPolicy policy;
    private JTextField passwordField;
    private JTextArea resultArea;
    private JProgressBar strengthBar;
    private JLabel strengthLabel;

    public PasswordAnalyzerGUI() {
        // Initialize analyzer and policy
        analyzer = new PasswordAnalyzer();
        policy = new SecurityPolicy();

        // Set up the frame
        setTitle("Password Analyzer");
        setSize(800, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create components
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("PASSWORD ANALYZER", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        JLabel subtitleLabel = new JLabel("Based on ISO 22301 Security Standards", JLabel.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.ITALIC, 12));

        titlePanel.add(titleLabel, BorderLayout.CENTER);
        titlePanel.add(subtitleLabel, BorderLayout.SOUTH);

        // Input panel
        JPanel inputPanel = new JPanel(new BorderLayout(5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Password Analysis"));

        JPanel passwordPanel = new JPanel(new BorderLayout(5, 5));
        passwordPanel.add(new JLabel("Enter password: "), BorderLayout.WEST);
        passwordField = new JTextField(20);
        passwordPanel.add(passwordField, BorderLayout.CENTER);

        JButton analyzeButton = new JButton("Analyze");
        passwordPanel.add(analyzeButton, BorderLayout.EAST);

        // Strength indicator panel
        JPanel strengthPanel = new JPanel(new BorderLayout(5, 5));

        // Strength meter with improved visibility
        strengthBar = new JProgressBar(0, 100);
        strengthBar.setStringPainted(false); // Don't paint string on the bar itself
        strengthBar.setBorderPainted(true);
        strengthBar.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

        // Separate label to show strength level
        strengthLabel = new JLabel("Password Strength: Not Analyzed", JLabel.CENTER);
        strengthLabel.setFont(new Font("Arial", Font.BOLD, 14));
        strengthLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        strengthPanel.add(strengthLabel, BorderLayout.NORTH);
        strengthPanel.add(strengthBar, BorderLayout.CENTER);

        inputPanel.add(passwordPanel, BorderLayout.NORTH);
        inputPanel.add(strengthPanel, BorderLayout.CENTER);

        // Results area
        resultArea = new JTextArea(20, 50);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Analysis Results"));
        scrollPane.setPreferredSize(new Dimension(750, 400));

        // Button panel for additional options
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        JButton policyButton = new JButton("View Security Policy");
        JButton exampleButton = new JButton("Test Sample Passwords");
        JButton generateButton = new JButton("Generate Strong Password");

        buttonPanel.add(policyButton);
        buttonPanel.add(exampleButton);
        buttonPanel.add(generateButton);

        // Create a split pane to allow resizing the results area
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                inputPanel, scrollPane);
        splitPane.setResizeWeight(0.2);
        splitPane.setDividerLocation(120); // Increased to accommodate strength label

        // Add all components to main panel
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(splitPane, BorderLayout.CENTER);

        // Add action listeners
        analyzeButton.addActionListener(e -> analyzePassword());
        policyButton.addActionListener(e -> displaySecurityPolicy());
        exampleButton.addActionListener(e -> testSamplePasswords());
        generateButton.addActionListener(e -> generatePassword());

        // Add main panel to frame
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void analyzePassword() {
        String password = passwordField.getText();
        if (password.isEmpty()) {
            resultArea.setText("Please enter a password to analyze.");
            return;
        }

        // Calculate strength score
        int score = analyzer.calculateStrengthScore(password);
        String level = analyzer.getStrengthLevel(score);
        boolean compliant = policy.isCompliant(password, analyzer);

        // Update strength bar with better visibility
        strengthBar.setValue(score);

        // Set colors based on strength
        Color barColor;
        Color textColor;
        if (score < 40) {
            barColor = new Color(255, 102, 102); // Lighter red
            textColor = new Color(153, 0, 0);    // Darker red
            strengthLabel.setText("Password Strength: WEAK (" + score + "/100)");
        } else if (score < 70) {
            barColor = new Color(255, 204, 0);   // Lighter yellow
            textColor = new Color(153, 102, 0);  // Darker yellow/orange
            strengthLabel.setText("Password Strength: MODERATE (" + score + "/100)");
        } else {
            barColor = new Color(102, 204, 0);   // Lighter green
            textColor = new Color(0, 102, 0);    // Darker green
            strengthLabel.setText("Password Strength: STRONG (" + score + "/100)");
        }

        strengthBar.setForeground(barColor);
        strengthLabel.setForeground(textColor);

        // Add a border with the same color to emphasize the strength level
        strengthLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, textColor),
                BorderFactory.createEmptyBorder(5, 0, 5, 0)
        ));

        // Generate report
        StringBuilder report = new StringBuilder();
        report.append("=== PASSWORD ANALYSIS REPORT ===\n\n");
        report.append(String.format("Password: %s\n", password));
        report.append(String.format("Strength Score: %d/100\n", score));
        report.append(String.format("Strength Level: %s\n", level));
        report.append(String.format("Policy Compliant: %s\n\n", compliant ? "YES" : "NO"));

        report.append("=== DETAILED ANALYSIS ===\n");
        report.append(String.format("Length: %d characters %s\n",
                password.length(),
                analyzer.checkMinimumLength(password, SecurityPolicy.MINIMUM_LENGTH) ? "✓" : "✗"));
        report.append(String.format("Contains uppercase: %s\n",
                analyzer.hasUppercase(password) ? "✓" : "✗"));
        report.append(String.format("Contains lowercase: %s\n",
                analyzer.hasLowercase(password) ? "✓" : "✗"));
        report.append(String.format("Contains numbers: %s\n",
                analyzer.hasNumbers(password) ? "✓" : "✗"));
        report.append(String.format("Contains special chars: %s\n",
                analyzer.hasSpecialCharacters(password) ? "✓" : "✗"));
        report.append(String.format("Common password: %s\n",
                analyzer.isCommonPassword(password) ? "YES ✗" : "NO ✓"));

        // Add recommendations if not compliant
        if (!compliant) {
            report.append("\n=== RECOMMENDATIONS ===\n");
            if (!analyzer.checkMinimumLength(password, SecurityPolicy.MINIMUM_LENGTH)) {
                report.append("- Increase password length to at least " +
                        SecurityPolicy.MINIMUM_LENGTH + " characters\n");
            }
            if (!analyzer.hasUppercase(password)) {
                report.append("- Add uppercase letters (A-Z)\n");
            }
            if (!analyzer.hasLowercase(password)) {
                report.append("- Add lowercase letters (a-z)\n");
            }
            if (!analyzer.hasNumbers(password)) {
                report.append("- Add numbers (0-9)\n");
            }
            if (!analyzer.hasSpecialCharacters(password)) {
                report.append("- Add special characters (!@#$%^&*...)\n");
            }
            if (analyzer.isCommonPassword(password)) {
                report.append("- Avoid common passwords - use something unique\n");
            }
        }

        resultArea.setText(report.toString());
        resultArea.setCaretPosition(0); // Scroll to top
    }

    private void displaySecurityPolicy() {
        // Reset strength indicator to default
        strengthBar.setValue(0);
        strengthLabel.setText("Password Strength: Not Analyzed");
        strengthLabel.setForeground(Color.BLACK);
        strengthLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        StringBuilder policy = new StringBuilder();
        policy.append("=== ORGANIZATIONAL SECURITY POLICY ===\n");
        policy.append("Based on ISO 22301 Standards\n");
        policy.append("Defined by GCS (Governance Cyber Security) Team\n\n");

        policy.append("Password Requirements:\n");
        policy.append("• Minimum length: " + SecurityPolicy.MINIMUM_LENGTH + " characters\n");
        policy.append("• Must contain uppercase letters: " +
                (SecurityPolicy.REQUIRE_UPPERCASE ? "YES" : "NO") + "\n");
        policy.append("• Must contain lowercase letters: " +
                (SecurityPolicy.REQUIRE_LOWERCASE ? "YES" : "NO") + "\n");
        policy.append("• Must contain numbers: " +
                (SecurityPolicy.REQUIRE_NUMBERS ? "YES" : "NO") + "\n");
        policy.append("• Must contain special characters: " +
                (SecurityPolicy.REQUIRE_SPECIAL_CHARS ? "YES" : "NO") + "\n");
        policy.append("• Minimum security score required: " +
                SecurityPolicy.MINIMUM_SCORE_REQUIRED + "/100\n");
        policy.append("• Cannot be a common/weak password\n");

        resultArea.setText(policy.toString());
        resultArea.setCaretPosition(0);
    }

    private void testSamplePasswords() {
        // Reset strength indicator to default
        strengthBar.setValue(0);
        strengthLabel.setText("Password Strength: Sample Testing Mode");
        strengthLabel.setForeground(Color.BLUE);
        strengthLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        String[] testPasswords = {
                "password",           // Very weak
                "Password123",        // Moderate
                "MyStr0ng!Pass",      // Strong
                "admin",              // Very weak
                "ComplexP@ssw0rd2024!" // Very strong
        };

        StringBuilder results = new StringBuilder();
        results.append("=== SAMPLE PASSWORD ANALYSIS ===\n\n");
        results.append(String.format("%-20s | %-8s | %-12s | %s\n",
                "Password", "Score", "Level", "Compliant"));
        results.append("--------------------------------------------------------------\n");

        for (String pwd : testPasswords) {
            int score = analyzer.calculateStrengthScore(pwd);
            String level = analyzer.getStrengthLevel(score);
            boolean compliant = policy.isCompliant(pwd, analyzer);

            results.append(String.format("%-20s | %3d/100  | %-12s | %s\n",
                    pwd, score, level, compliant ? "YES" : "NO"));
        }

        results.append("\nNote: This demonstrates how the analyzer evaluates different passwords.");

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
        strengthBar.setForeground(new Color(102, 204, 0)); // Light green

        // Update strength label
        strengthLabel.setText("Password Strength: STRONG (" + score + "/100)");
        strengthLabel.setForeground(new Color(0, 102, 0)); // Dark green
        strengthLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(0, 102, 0)),
                BorderFactory.createEmptyBorder(5, 0, 5, 0)
        ));

        resultArea.setText("Generated strong password: " + generatedPassword +
                "\n\nThis password meets all security requirements.\n" +
                "Click 'Analyze' to see the detailed analysis.");
    }

    public static void main(String[] args) {
        // Use the system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
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