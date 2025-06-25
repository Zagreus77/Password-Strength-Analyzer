import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Create instances of our classes
        PasswordAnalyzer analyzer = new PasswordAnalyzer();
        SecurityPolicy policy = new SecurityPolicy();
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("=== CYBERSECURITY PASSWORD ANALYZER ===");
        System.out.println("This tool demonstrates GCS policy enforcement and ICS technical analysis");
        System.out.println("Based on ISO 22301 security standards\n");
        
        while (true) {
            System.out.println("\nChoose an option:");
            System.out.println("1. Analyze a single password");
            System.out.println("2. Test multiple passwords");
            System.out.println("3. View security policy");
            System.out.println("4. Exit");
            System.out.print("Enter your choice (1-4): ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            switch (choice) {
                case 1:
                    analyzeSinglePassword(analyzer, policy, scanner);
                    break;
                case 2:
                    testMultiplePasswords(analyzer, policy, scanner);
                    break;
                case 3:
                    displaySecurityPolicy();
                    break;
                case 4:
                    System.out.println("Thank you for using the Password Analyzer!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    // Method to analyze a single password
    private static void analyzeSinglePassword(PasswordAnalyzer analyzer, SecurityPolicy policy, Scanner scanner) {
        System.out.print("Enter password to analyze: ");
        String password = scanner.nextLine();
        
        // Generate and display the compliance report
        String report = policy.generateComplianceReport(password, analyzer);
        System.out.println("\n" + report);
        
        // Provide recommendations if password is not compliant
        if (!policy.isCompliant(password, analyzer)) {
            System.out.println("RECOMMENDATIONS FOR IMPROVEMENT:");
            if (!analyzer.checkMinimumLength(password, SecurityPolicy.MINIMUM_LENGTH)) {
                System.out.println("- Increase password length to at least " + SecurityPolicy.MINIMUM_LENGTH + " characters");
            }
            if (!analyzer.hasUppercase(password)) {
                System.out.println("- Add uppercase letters (A-Z)");
            }
            if (!analyzer.hasLowercase(password)) {
                System.out.println("- Add lowercase letters (a-z)");
            }
            if (!analyzer.hasNumbers(password)) {
                System.out.println("- Add numbers (0-9)");
            }
            if (!analyzer.hasSpecialCharacters(password)) {
                System.out.println("- Add special characters (!@#$%^&*...)");
            }
            if (analyzer.isCommonPassword(password)) {
                System.out.println("- Avoid common passwords - use something unique");
            }
        }
    }
    
    // Method to test multiple passwords at once
    private static void testMultiplePasswords(PasswordAnalyzer analyzer, SecurityPolicy policy, Scanner scanner) {
        String[] testPasswords = {
            "password",           // Very weak
            "Password123",        // Moderate
            "MyStr0ng!Pass",     // Strong
            "admin",             // Very weak
            "ComplexP@ssw0rd2024!" // Very strong
        };
        
        System.out.println("\n=== TESTING MULTIPLE PASSWORDS ===");
        for (String pwd : testPasswords) {
            int score = analyzer.calculateStrengthScore(pwd);
            String level = analyzer.getStrengthLevel(score);
            boolean compliant = policy.isCompliant(pwd, analyzer);
            
            System.out.printf("Password: %-20s | Score: %3d | Level: %-10s | Compliant: %s%n", 
                            pwd, score, level, compliant ? "YES" : "NO");
        }
    }
    
    // Method to display the current security policy
    private static void displaySecurityPolicy() {
        System.out.println("\n=== ORGANIZATIONAL SECURITY POLICY ===");
        System.out.println("Based on ISO 22301 Standards");
        System.out.println("Defined by GCS (Governance Cyber Security) Team\n");
        
        System.out.println("Password Requirements:");
        System.out.println("• Minimum length: " + SecurityPolicy.MINIMUM_LENGTH + " characters");
        System.out.println("• Must contain uppercase letters: " + (SecurityPolicy.REQUIRE_UPPERCASE ? "YES" : "NO"));
        System.out.println("• Must contain lowercase letters: " + (SecurityPolicy.REQUIRE_LOWERCASE ? "YES" : "NO"));
        System.out.println("• Must contain numbers: " + (SecurityPolicy.REQUIRE_NUMBERS ? "YES" : "NO"));
        System.out.println("• Must contain special characters: " + (SecurityPolicy.REQUIRE_SPECIAL_CHARS ? "YES" : "NO"));
        System.out.println("• Minimum security score required: " + SecurityPolicy.MINIMUM_SCORE_REQUIRED + "/100");
        System.out.println("• Cannot be a common/weak password");
    }
}
