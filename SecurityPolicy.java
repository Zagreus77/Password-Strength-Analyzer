public class SecurityPolicy {
    // This class represents GCS policies - the rules that must be followed
    
    // ISO 22301 inspired password policy requirements
    public static final int MINIMUM_LENGTH = 8;
    public static final boolean REQUIRE_UPPERCASE = true;
    public static final boolean REQUIRE_LOWERCASE = true;
    public static final boolean REQUIRE_NUMBERS = true;
    public static final boolean REQUIRE_SPECIAL_CHARS = true;
    public static final int MINIMUM_SCORE_REQUIRED = 60;
    
    // Method to check if password complies with organizational policy
    public boolean isCompliant(String password, PasswordAnalyzer analyzer) {
        // Check all policy requirements
        boolean lengthOk = analyzer.checkMinimumLength(password, MINIMUM_LENGTH);
        boolean uppercaseOk = !REQUIRE_UPPERCASE || analyzer.hasUppercase(password);
        boolean lowercaseOk = !REQUIRE_LOWERCASE || analyzer.hasLowercase(password);
        boolean numbersOk = !REQUIRE_NUMBERS || analyzer.hasNumbers(password);
        boolean specialOk = !REQUIRE_SPECIAL_CHARS || analyzer.hasSpecialCharacters(password);
        boolean notCommon = !analyzer.isCommonPassword(password);
        boolean scoreOk = analyzer.calculateStrengthScore(password) >= MINIMUM_SCORE_REQUIRED;
        
        return lengthOk && uppercaseOk && lowercaseOk && numbersOk && specialOk && notCommon && scoreOk;
    }
    
    // Method to generate policy compliance report
    public String generateComplianceReport(String password, PasswordAnalyzer analyzer) {
        StringBuilder report = new StringBuilder();
        report.append("=== SECURITY POLICY COMPLIANCE REPORT ===\n");
        report.append("Password: ").append("*".repeat(password.length())).append("\n\n");
        
        // Check each requirement
        report.append("Policy Requirements:\n");
        report.append("✓ Minimum length (").append(MINIMUM_LENGTH).append("): ")
               .append(analyzer.checkMinimumLength(password, MINIMUM_LENGTH) ? "PASS" : "FAIL").append("\n");
        report.append("✓ Uppercase letters: ")
               .append(analyzer.hasUppercase(password) ? "PASS" : "FAIL").append("\n");
        report.append("✓ Lowercase letters: ")
               .append(analyzer.hasLowercase(password) ? "PASS" : "FAIL").append("\n");
        report.append("✓ Numbers: ")
               .append(analyzer.hasNumbers(password) ? "PASS" : "FAIL").append("\n");
        report.append("✓ Special characters: ")
               .append(analyzer.hasSpecialCharacters(password) ? "PASS" : "FAIL").append("\n");
        report.append("✓ Not common password: ")
               .append(!analyzer.isCommonPassword(password) ? "PASS" : "FAIL").append("\n");
        
        int score = analyzer.calculateStrengthScore(password);
        report.append("\nStrength Score: ").append(score).append("/100\n");
        report.append("Strength Level: ").append(analyzer.getStrengthLevel(score)).append("\n");
        report.append("Policy Compliance: ").append(isCompliant(password, analyzer) ? "COMPLIANT" : "NON-COMPLIANT").append("\n");
        
        return report.toString();
    }
}